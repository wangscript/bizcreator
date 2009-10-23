package com.bizcreator.core.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NestedModelUtil {

    @SuppressWarnings("unchecked")
    public static <X> X getNestedValue(ModelData model, String property) {
        List<String> paths = Arrays.asList(property.split("\\."));
        return (X) getNestedValue(model, paths);
    }

    public static Object convertIfNecessary(Object obj) {
        /*
        if (obj instanceof BeanModelTag) {
        BeanModelFactory factory = BeanModelLookup.get().getFactory(obj.getClass());
        obj = factory.createModel(obj);
        }*/
        return obj;
    }

    @SuppressWarnings("unchecked")
    public static <X> X getNestedValue(ModelData model, List<String> paths) {
        if (paths.size() == 1) {
            Object obj = model.get(paths.get(0));
            obj = convertIfNecessary(obj);
            return (X) obj;
        } else {
            Object obj = model.get(paths.get(0));

            obj = convertIfNecessary(obj);

            if (obj != null && obj instanceof ModelData) {
                ArrayList<String> tmp = new ArrayList<String>(paths);
                tmp.remove(0);
                return (X) getNestedValue((ModelData) obj, tmp);
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static <X> X setNestedValue(ModelData model, String property, Object value) {
        List<String> paths = Arrays.asList(property.split("\\."));
        int i = property.lastIndexOf('.');
        String prefix = property.substring(0, i);
        return (X) setNestedValue(model, paths, value, prefix, model);
    }

    @SuppressWarnings("unchecked")
    public static <X> X setNestedValue(ModelData model, List<String> paths, 
            Object value, String prefix, ModelData nestedModel) {
        if (paths.size() == 1) {
            model.setNestedPrefix(prefix);
            model.setNestedModel(nestedModel);
            model.setSrcField(paths.get(0));
            return (X) model.set(paths.get(0), value);
        } else {
            Object data = model.get(paths.get(0));
            if (data != null && data instanceof ModelData) {
                ArrayList<String> tmp = new ArrayList<String>(paths);
                tmp.remove(0);
                return (X) setNestedValue((ModelData) data, tmp, value, prefix, nestedModel);
            }
        }
        return null;
    }

    public static boolean isNestedProperty(String property) {
        return property.indexOf(".") != -1;
    }
}

