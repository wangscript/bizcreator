/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bizcreator.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Administrator
 */
public abstract class ResourceManager extends AbstractBean {
    //private static final Log log = LogFactory.getLog(ResourceManager.class);
    protected final Map<String, ResourceMap> resourceMaps;
    private List<String> applicationBundleNames = null;
    private ResourceMap appResourceMap = null;
    private Class appClass = null;
    
    private static ResourceManager instance;
    
    
    public ResourceManager() {
        resourceMaps = new ConcurrentHashMap<String, ResourceMap>();
    }
    
    public static void init(Class appClass) {
        instance().setAppClass(appClass);
    }
    
    public static ResourceManager instance() {
        if (instance == null) {
            instance = new InheritResourceManager();
        }
        return instance;
    }
    
    public void setLocale(Locale locale) {
        ResourceMap.setLocale(locale);
        appResourceMap  = null;
        resourceMaps.clear();
    }
    
    public void setAppClass(Class clazz) {
        this.appClass = clazz;
    }
    
    /**
     * The names of the ResourceBundles to be shared by the entire
     * application.  The list is in priority order: resources defined
     * by the first ResourceBundle shadow resources with the the same
     * name that come later.
     * <p>
     * The default value for this property is a list of {@link
     * #getClassBundleNames per-class} ResourceBundle names, beginning
     * with the {@code Application's} class and of each of its
     * superclasses, up to {@code Application.class}.
     * For example, if the Application's class was 
     * {@code com.foo.bar.MyApp}, and MyApp was a subclass
     * of {@code SingleFrameApplication.class}, then the 
     * ResourceBundle names would be:
     * <code><ol>
     * <li>com.foo.bar.resources.MyApp</li>
     * <li>javax.swing.application.resources.SingleFrameApplication</li>
     * <li>javax.swing.application.resources.Application</li>
     * </code></ol>
     * <p> 
     * The default value of this property is computed lazily and
     * cached.  If it's reset, then all ResourceMaps cached by
     * {@code getResourceMap} will be updated.
     * 
     * @see #setApplicationBundleNames
     * @see #getResourceMap
     * @see #getClassBundleNames
     * @see ApplicationContext#getApplication
     */
    public List<String> getApplicationBundleNames() {
        /* Lazily compute an initial value for this property, unless the
         * application's class hasn't been specified yet.  In that case
         * we just return a placeholder based on Application.class.
         */
        if (applicationBundleNames == null) {
            if (appClass == null) {
                //return allBundleNames(Application.class, Application.class); // placeholder
                return null;
            }
            else {
                applicationBundleNames = getClassBundleNames(appClass);
            }
        }
        return applicationBundleNames;
    }

    /**
     * Specify the names of the ResourceBundles to be shared by the entire
     * application.  More information about the property is provided
     * by the {@link #getApplicationBundleNames} method.  
     * 
     * @see #setApplicationBundleNames
     */
    public void setApplicationBundleNames(List<String> bundleNames) {
        if (bundleNames != null) {
            for(String bundleName : bundleNames) {
            if ((bundleName == null) || (bundleNames.size() == 0)) {
                throw new IllegalArgumentException("invalid bundle name \"" + bundleName + "\"");
            }
            }
        }
        Object oldValue = applicationBundleNames;
        if (bundleNames != null) {
            applicationBundleNames = Collections.unmodifiableList(new ArrayList(bundleNames));
        }
        else {
            applicationBundleNames = null;
        }
        resourceMaps.clear();
        firePropertyChange("applicationBundleNames", oldValue, applicationBundleNames);
    }
    
    
    
    /**
     * Map from a class to a list of the names of the 
     * {@code ResourceBundles} specific to the class.
     * The list is in priority order: resources defined 
     * by the first ResourceBundle shadow resources with the
     * the same name that come later.
     * <p>
     * By default this method returns one ResourceBundle 
     * whose name is the same as the class's name, but in the
     * {@code "resources"} subpackage.
     * <p>
     * For example, given a class named
     * {@code com.foo.bar.MyClass}, the ResourceBundle name would
     * be {@code "com.foo.bar.resources.MyClass"}. If MyClass is
     * an inner class, only its "simple name" is used.  For example,
     * given an inner class named {@code com.foo.bar.OuterClass$InnerClass},
     * the ResourceBundle name would be
     * {@code "com.foo.bar.resources.InnerClass"}.
     * <p>
     * This method is used by the {@code getResourceMap} methods
     * to compute the list of ResourceBundle names
     * for a new {@code ResourceMap}.  ResourceManager subclasses
     * can override this method to add additional class-specific
     * ResourceBundle names to the list.
     * 
     * @param cls the named ResourceBundles are specific to {@code cls}.
     * @return the names of the ResourceBundles to be loaded for {@code cls}
     * @see #getResourceMap
     * @see #getApplicationBundleNames
     */
    protected List<String> getClassBundleNames(Class cls) {
        String bundleName = classBundleBaseName(cls);
        return Collections.singletonList(bundleName);
    }
    
    /* Convert a class name to an eponymous resource bundle in the 
     * resources subpackage.  For example, given a class named
     * com.foo.bar.MyClass, the ResourceBundle name would be
     * "com.foo.bar.resources.MyClass"  If MyClass is an inner class,
     * only its "simple name" is used.  For example, given an
     * inner class named com.foo.bar.OuterClass$InnerClass, the
     * ResourceBundle name would be "com.foo.bar.resources.InnerClass".
     * Although this could result in a collision, creating more
     * complex rules for inner classes would be a burden for
     * developers.
     */
    private String classBundleBaseName(Class cls) {
        String className = cls.getName();
        StringBuffer sb = new StringBuffer();
        int i = className.lastIndexOf('.');
        if (i > 0) {
            sb.append(className.substring(0, i));
            sb.append(".resources.");
            sb.append(cls.getSimpleName());
        }
        else {
            sb.append("resources.");
            sb.append(cls.getSimpleName());
        }
        return sb.toString();
    }
    
    /**
     * Returns the chain of ResourceMaps that's shared by the entire application,
     * beginning with the resources defined for the application's class, i.e.
     * the value of the ApplicationContext 
     * {@link ApplicationContext#getApplicationClass applicationClass} property.
     * If the {@code applicationClass} property has not been set, e.g. because
     * the application has not been {@link Application#launch launched} yet,
     * then a ResourceMap for just {@code Application.class} is returned.
     * 
     * @return the Application's ResourceMap
     * @see ApplicationContext#getResourceMap()
     * @see ApplicationContext#getApplicationClass
     */
    public ResourceMap getResourceMap() {
        return getApplicationResourceMap();
    }
    
    /* Lazily creates the Application ResourceMap chain,
     * appResourceMap.  If the Application hasn't been launched yet,
     * i.e. if the ApplicationContext applicationClass property hasn't
     * been set yet, then the ResourceMap just corresponds to
     * Application.class.
     */
    public ResourceMap getApplicationResourceMap() {
        if (appResourceMap == null) {
            List<String> appBundleNames = getApplicationBundleNames();
            /*
            Class appClass = getContext().getApplicationClass();
            if (appClass == null) {
                logger.warning("getApplicationResourceMap(): no Application class");
                appClass = Application.class;
            }
             */
            if (appClass != null) {
                ClassLoader classLoader = appClass.getClassLoader();
                //appResourceMap = createResourceMapChain(classLoader, null, appBundleNames.listIterator());
                appResourceMap = new ResourceMap(null, classLoader, appBundleNames);
            }
        }
        return appResourceMap;
    }
    
    public abstract ResourceMap getResourceMap(Class startClass, Class stopClass);
    
    public abstract ResourceMap getResourceMap(Class cls);
}
