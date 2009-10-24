/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bizcreator.core;

import com.bizcreator.core.ResourceMap;
import com.bizcreator.core.ResourceManager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

/**
 * 将类层次对应的资源全部放到一个resource map中, 但appClass的resource map作为root
 * @author Administrator
 */
public class FlatResourceManager extends ResourceManager {

    /* Returns a read-only list of the ResourceBundle names for all of
     * the classes from startClass to (including) stopClass.  The
     * bundle names for each class are #getClassBundleNames(Class).
     * The list is in priority order: resources defined in bundles
     * earlier in the list shadow resources with the same name that
     * appear bundles that come later.
     */
    private List<String> allBundleNames(Class startClass, Class stopClass) {
	List<String> bundleNames = new ArrayList<String>();
        Class limitClass = stopClass.getSuperclass(); // could be null
        for(Class c = startClass; c != limitClass; c = c.getSuperclass()) {
            bundleNames.addAll(getClassBundleNames(c));
        }
        return Collections.unmodifiableList(bundleNames);
    }

    private String bundlePackageName(String bundleName) {
        int i = bundleName.lastIndexOf(".");
        return (i == -1) ? "" : bundleName.substring(0, i);
    }

    /* Creates a parent chain of ResourceMaps for the specfied
     * ResourceBundle names.  One ResourceMap is created for each
     * subsequence of ResourceBundle names with a common bundle
     * package name, i.e. with a common resourcesDir.  The parent
     * of the final ResourceMap in the chain is root.
     */
    private ResourceMap createResourceMapChain(ClassLoader cl, ResourceMap root, ListIterator<String> names) {
        if (!names.hasNext()) {
            return root;
        }
        else {

            String bundleName0 = names.next();
            String rmBundlePackage = bundlePackageName(bundleName0);
            List<String> rmNames = new ArrayList<String>();
            rmNames.add(bundleName0);
            while(names.hasNext()) {
                String bundleName = names.next();
                if (rmBundlePackage.equals(bundlePackageName(bundleName))) {
                    rmNames.add(bundleName);
                }
                else {
                    names.previous();
                    break;
                }
            }
            ResourceMap parent = createResourceMapChain(cl, root, names);
            //前面的代码将names的bundleName按包名的顺序排列, 得到rmNames

            return createResourceMap(cl, parent, rmNames);
        }
    }

    /**
     * Called by {@link #getResourceMap} to construct {@code ResourceMaps}.
     * By default this method is effectively just:
     * <pre>
     * return new ResourceMap(parent, classLoader, bundleNames);
     * </pre>
     * Custom ResourceManagers might override this method to construct their
     * own ResourceMap subclasses.
     */
    protected ResourceMap createResourceMap(ClassLoader classLoader, ResourceMap parent, List<String> bundleNames) {
        return new ResourceMap(parent, classLoader, bundleNames);
    }

    /* Lazily creates the ResourceMap chain for the the class from
     * startClass to stopClass.
     */
    private ResourceMap getClassResourceMap(Class startClass, Class stopClass) {
        String classResourceMapKey = startClass.getName() + stopClass.getName();
        ResourceMap classResourceMap = resourceMaps.get(classResourceMapKey);
        if (classResourceMap == null) {
            List<String> classBundleNames = allBundleNames(startClass, stopClass);
            ClassLoader classLoader = startClass.getClassLoader();
            ResourceMap appRM = getResourceMap();
            classResourceMap = createResourceMapChain(classLoader, appRM, classBundleNames.listIterator());
            resourceMaps.put(classResourceMapKey, classResourceMap);
        }
        return classResourceMap;
    }

    /**
     * Returns a {@link ResourceMap#getParent chain} of {@code ResourceMaps}
     * that encapsulate the {@code ResourceBundles} for each class
     * from {@code startClass} to (including) {@code stopClass}.  The
     * final link in the chain is Application ResourceMap chain, i.e.
     * the value of {@link #getResourceMap() getResourceMap()}.
     * <p>
     * The ResourceBundle names for the chain of ResourceMaps
     * are defined by  {@link #getClassBundleNames} and
     * {@link #getApplicationBundleNames}.  Collectively they define the
     * standard location for {@code ResourceBundles} for a particular
     * class as the {@code resources} subpackage.  For example, the
     * ResourceBundle for the single class {@code com.myco.MyScreen}, would
     * be named {@code com.myco.resources.MyScreen}.  Typical
     * ResourceBundles are ".properties" files, so: {@code
     * com/foo/bar/resources/MyScreen.properties}.  The following table
     * is a list of the ResourceMaps and their constituent
     * ResourceBundles for the same example:
     * <p>
     * <table border="1" cellpadding="4%">
     *   <caption><em>ResourceMap chain for class MyScreen in MyApp</em></caption>
     *     <tr>
     *       <th></th>
     *       <th>ResourceMap</th>
     *       <th>ResourceBundle names</th>
     *       <th>Typical ResourceBundle files</th>
     *     </tr>
     *     <tr>
     *       <td>1</td>
     *       <td>class: com.myco.MyScreen</td>
     *       <td>com.myco.resources.MyScreen</td>
     *       <td>com/myco/resources/MyScreen.properties</td>
     *     </tr>
     *     <tr>
     *       <td>2/td>
     *       <td>application: com.myco.MyApp</td>
     *       <td>com.myco.resources.MyApp</td>
     *       <td>com/myco/resources/MyApp.properties</td>
     *     </tr>
     *     <tr>
     *       <td>3</td>
     *       <td>application: javax.swing.application.Application</td>
     *       <td>javax.swing.application.resources.Application</td>
     *       <td>javax.swing.application.resources.Application.properties</td>
     *     </tr>
     * </table>
     *
     * <p>
     * None of the ResourceBundles are required to exist.  If more than one
     * ResourceBundle contains a resource with the same name then
     * the one earlier in the list has precedence
     * <p>
     * ResourceMaps are constructed lazily and cached.  One ResourceMap
     * is constructed for each sequence of classes in the same package.
     *
     * @param startClass the first class whose ResourceBundles will be included
     * @param stopClass the last class whose ResourceBundles will be included
     * @return a {@code ResourceMap} chain that contains resources loaded from
     *   {@code ResourceBundles}  found in the resources subpackage for
     *   each class.
     * @see #getClassBundleNames
     * @see #getApplicationBundleNames
     * @see ResourceMap#getParent
     * @see ResourceMap#getBundleNames
     */
    public ResourceMap getResourceMap(Class startClass, Class stopClass) {
        if (startClass == null) {
            throw new IllegalArgumentException("null startClass");
        }
        if (stopClass == null) {
            throw new IllegalArgumentException("null stopClass");
        }
        if (!stopClass.isAssignableFrom(startClass)) {
            throw new IllegalArgumentException("startClass is not a subclass, or the same as, stopClass");
        }
        return getClassResourceMap(startClass, stopClass);

    }

    /**
     * Return the ResourcedMap chain for the specified class. This is
     * just a convenince method, it's the same as:
     * <code>getResourceMap(cls, cls)</code>.
     *
     * @param cls the class that defines the location of ResourceBundles
     * @return a {@code ResourceMap} that contains resources loaded from
     *   {@code ResourceBundles}  found in the resources subpackage of the
     *   specified class's package.
     * @see #getResourceMap(Class, Class)
     */
    public ResourceMap getResourceMap(Class cls) {
        if (cls == null) {
            throw new IllegalArgumentException("null class");
        }
        return getResourceMap(cls, cls);
    }
}
