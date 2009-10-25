/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bizcreator.core.validator;

import com.bizcreator.core.entity.MenuModel;
import java.beans.Introspector;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.annotations.common.reflection.Filter;
import org.hibernate.annotations.common.reflection.ReflectionManager;
import org.hibernate.annotations.common.reflection.XClass;
import org.hibernate.annotations.common.reflection.XMember;
import org.hibernate.annotations.common.reflection.XMethod;
import org.hibernate.annotations.common.reflection.XProperty;
import org.hibernate.annotations.common.reflection.java.JavaReflectionManager;
import org.hibernate.validator.InvalidValue;
import org.hibernate.validator.Valid;
import org.hibernate.validator.Validator;
import org.hibernate.validator.ValidatorClass;
import org.hibernate.AssertionFailure;

/**
 *
 * @author Administrator
 */
public class BizClassValidator<T> implements Serializable {

    static Log log = LogFactory.getLog(BizClassValidator.class);

    
    
    private final Class<T> beanClass;
    private transient ReflectionManager reflectionManager;
    private final transient Map<XClass, BizClassValidator> childClassValidators;
    private transient List<Validator> beanValidators;
    private transient List<Validator> memberValidators;
    private transient List<XMember> memberGetters;
    private transient List<XMember> childGetters;
    
    private static final Filter GET_ALL_FILTER = new Filter() {
        public boolean returnStatic() {
            return true;
        }

        public boolean returnTransient() {
            return true;
        }
    };

    //
    private transient Map<String, List<Validator>> validatorMap = new HashMap<String, List<Validator>>();
    
    public BizClassValidator(Class<T> beanClass) {
        this(beanClass, new HashMap<XClass, BizClassValidator>(), null );
    }
    
    public BizClassValidator(Class<T> beanClass, Map<XClass, BizClassValidator> childClassValidators,
            ReflectionManager reflectionManager) {
        this.reflectionManager = reflectionManager != null ? reflectionManager : new JavaReflectionManager();
        XClass beanXClass = this.reflectionManager.toXClass(beanClass);
        this.beanClass = beanClass;
        /*
        this.messageBundle = resourceBundle == null ?
        getDefaultResourceBundle() :
        resourceBundle;
        this.defaultMessageBundle = ResourceBundle.getBundle( DEFAULT_VALIDATOR_MESSAGE );
        this.userInterpolator = interpolator;
         */
        this.childClassValidators = childClassValidators != null ? childClassValidators : new HashMap<XClass, BizClassValidator>();
        initValidator(beanXClass, this.childClassValidators);
    }

    @SuppressWarnings("unchecked")
    protected BizClassValidator(XClass beanXClass, Map<XClass, BizClassValidator> childClassValidators,
            ReflectionManager reflectionManager) {
        this.reflectionManager = reflectionManager;
        this.beanClass = reflectionManager.toClass(beanXClass);
        /*
        this.messageBundle = resourceBundle == null ?
        getDefaultResourceBundle() :
        resourceBundle;
        this.defaultMessageBundle = ResourceBundle.getBundle( DEFAULT_VALIDATOR_MESSAGE );
        this.userInterpolator = userInterpolator;
         */
        this.childClassValidators = childClassValidators;
        initValidator(beanXClass, childClassValidators);
    }

    private void initValidator(XClass xClass, Map<XClass, BizClassValidator> childClassValidators) {
        beanValidators = new ArrayList<Validator>();
        memberValidators = new ArrayList<Validator>();
        memberGetters = new ArrayList<XMember>();
        childGetters = new ArrayList<XMember>();

        //defaultInterpolator = new DefaultMessageInterpolatorAggerator();
        //defaultInterpolator.initialize( messageBundle, defaultMessageBundle );

        //build the class hierarchy to look for members in
        childClassValidators.put(xClass, this);
        Collection<XClass> classes = new HashSet<XClass>();
        addSuperClassesAndInterfaces(xClass, classes);
        for (XClass currentClass : classes) {
            Annotation[] classAnnotations = currentClass.getAnnotations();
            for (int i = 0; i < classAnnotations.length; i++) {
                Annotation classAnnotation = classAnnotations[i];
                Validator beanValidator = createValidator(classAnnotation);
                if (beanValidator != null) {
                    beanValidators.add(beanValidator);
                }
                handleAggregateAnnotations(classAnnotation, null);
            }
        }

        //Check on all selected classes
        for (XClass currClass : classes) {
            List<XMethod> methods = currClass.getDeclaredMethods();
            for (XMethod method : methods) {
                createMemberValidator(method);
                createChildValidator(method);
            }

            List<XProperty> fields = currClass.getDeclaredProperties("field", GET_ALL_FILTER);
            for (XProperty field : fields) {
                createMemberValidator(field);
                createChildValidator(field);
            }
        }
    }

    private void addSuperClassesAndInterfaces(XClass clazz, Collection<XClass> classes) {
        for (XClass currClass = clazz; currClass != null; currClass = currClass.getSuperclass()) {
            if (!classes.add(currClass)) {
                return;
            }
            XClass[] interfaces = currClass.getInterfaces();
            for (XClass interf : interfaces) {
                addSuperClassesAndInterfaces(interf, classes);
            }
        }
    }

    private boolean handleAggregateAnnotations(Annotation annotation, XMember member) {
        Object[] values;
        try {
            Method valueMethod = annotation.getClass().getMethod("value");
            if (valueMethod.getReturnType().isArray()) {
                values = (Object[]) valueMethod.invoke(annotation);
            } else {
                return false;
            }
        } catch (NoSuchMethodException e) {
            return false;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }

        boolean validatorPresent = false;
        for (Object value : values) {
            if (value instanceof Annotation) {
                annotation = (Annotation) value;
                Validator validator = createValidator(annotation);
                if (validator != null) {
                    if (member != null) {
                        //member
                        memberValidators.add(validator);
                        setAccessible(member);
                        memberGetters.add(member);
                    } else {
                        //bean
                        beanValidators.add(validator);
                    }
                    validatorPresent = true;
                }
            }
        }
        return validatorPresent;
    }

    @SuppressWarnings("unchecked")
    private void createChildValidator(XMember member) {
        if (member.isAnnotationPresent(Valid.class)) {
            setAccessible(member);
            childGetters.add(member);
            XClass clazz;
            if (member.isCollection() || member.isArray()) {
                clazz = member.getElementClass();
            } else {
                clazz = member.getType();
            }
            if (!childClassValidators.containsKey(clazz)) {
                //ClassValidator added by side effect (added to childClassValidators during CV construction)
                new BizClassValidator(clazz, childClassValidators, reflectionManager);
            }
        }
    }

    private void createMemberValidator(XMember member) {
        boolean validatorPresent = false;
        String propName = this.getPropertyName(member);
        List<Validator> list = validatorMap.get(propName);
        if (list == null) {
            list = new ArrayList<Validator>();
            validatorMap.put(propName, list);
        }
        
        Annotation[] memberAnnotations = member.getAnnotations();
        for (Annotation methodAnnotation : memberAnnotations) {
            Validator propertyValidator = createValidator(methodAnnotation);
            if (propertyValidator != null) {
                memberValidators.add(propertyValidator);
                setAccessible(member);
                memberGetters.add(member);
                validatorPresent = true;
                list.add(propertyValidator);
            }
            boolean agrValidPresent = handleAggregateAnnotations(methodAnnotation, member);
            validatorPresent = validatorPresent || agrValidPresent;
        }
        if (validatorPresent && !member.isTypeResolved()) {
            log.warn("Original type of property " + member + " is unbound and has been approximated.");
        }
    }

    private static void setAccessible(XMember member) {
        if (!Modifier.isPublic(member.getModifiers())) {
            member.setAccessible(true);
        }
    }

    @SuppressWarnings("unchecked")
    private Validator createValidator(Annotation annotation) {
        try {
            ValidatorClass validatorClass = annotation.annotationType().getAnnotation(ValidatorClass.class);
            if (validatorClass == null) {
                return null;
            }
            Validator beanValidator = validatorClass.value().newInstance();
            beanValidator.initialize(annotation);
            //defaultInterpolator.addInterpolator( annotation, beanValidator );
            return beanValidator;
        } catch (Exception e) {
            throw new IllegalArgumentException("could not instantiate ClassValidator", e);
        }
    }

    public boolean hasValidationRules() {
        return beanValidators.size() != 0 || memberValidators.size() != 0;
    }

    /**
     * Apply constraints of a particular property on a bean instance and return all the failures.
     * Note this is not recursive.
     */
    //TODO should it be recursive?
    public InvalidValue[] getInvalidValues(T bean, String propertyName) {
        List<InvalidValue> results = new ArrayList<InvalidValue>();

        for (int i = 0; i < memberValidators.size(); i++) {
            XMember getter = memberGetters.get(i);
            if (getPropertyName(getter).equals(propertyName)) {
                Object value = getMemberValue(bean, getter);
                Validator validator = memberValidators.get(i);
                if (!validator.isValid(value)) {
                //results.add( new InvalidValue( interpolate(validator), beanClass, propertyName, value, bean ) );
                }
            }
        }

        return results.toArray(new InvalidValue[results.size()]);
    }

    private Object getMemberValue(T bean, XMember getter) {
        Object value;
        try {
            value = getter.invoke(bean);
        } catch (Exception e) {
            throw new IllegalStateException("Could not get property value", e);
        }
        return value;
    }

    public String getPropertyName(XMember member) {
        //Do no try to cache the result in a map, it's actually much slower (2.x time)
        String propertyName;
        if (XProperty.class.isAssignableFrom(member.getClass())) {
            propertyName = member.getName();
        } else if (XMethod.class.isAssignableFrom(member.getClass())) {
            propertyName = member.getName();
            if (propertyName.startsWith("is")) {
                propertyName = Introspector.decapitalize(propertyName.substring(2));
            } else if (propertyName.startsWith("get")) {
                propertyName = Introspector.decapitalize(propertyName.substring(3));
            }
        //do nothing for non getter method, in case someone want to validate a PO Method
        } else {
            throw new AssertionFailure("Unexpected member: " + member.getClass().getName());
        }
        return propertyName;
    }
    
    public List<Validator> getValidators(String propName) {
        return validatorMap.get(propName);
    }
    
    
    private final static Map<Class, BizClassValidator> entityValidators = new HashMap<Class, BizClassValidator>();
    /**
     * 直接通过entity class获得class validator, 并具有缓存功能
     * @param clazz
     * @return
     */
    public static BizClassValidator get(Class clazz) {
        BizClassValidator rcv = entityValidators.get(clazz);
        if (rcv == null) {
            rcv = new BizClassValidator(clazz);
            entityValidators.put(clazz, rcv);
        }
        return rcv;
    }
    
    public static void main(String[] args) {
        BizClassValidator rcv = new BizClassValidator(MenuModel.class);
        System.out.println(rcv.getValidators("name"));
        System.out.println(rcv.getValidators("code"));
    }
}
