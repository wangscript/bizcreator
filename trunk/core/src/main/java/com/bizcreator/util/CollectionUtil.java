package com.bizcreator.util;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class CollectionUtil {
//	--------- tool methods
	   public static List toList(final Iterator iterator)
	   {
	      List list = new ArrayList();
	      while (iterator.hasNext())
	      {
	         list.add(iterator.next());
	      }
	      return list;
	   }
	   
	   public static List toList(final Object[] objects)
	   {
	      List list = new ArrayList();
	      for (int i = 0; i < objects.length; i++)
	      {
	         Object object = objects[i];
	         list.add(object);
	      }
	      return list;
	   }
	   
	   public static List toList(Enumeration e)
	   {
	      List list = new ArrayList();
	      while (e.hasMoreElements())
	      {
	         list.add(e.nextElement());
	      }
	      return list;
	   }
	   
	   public static Set toSet(Enumeration e)
	   {
	      HashSet set = new HashSet();
	      while (e.hasMoreElements())
	      {
	         set.add(e.nextElement());
	      }
	      return set;
	   }
	   
	   public static Set toSet(final Iterator iterator)
	   {
	      HashSet set = new HashSet();
	      while (iterator.hasNext())
	      {
	         set.add(iterator.next());
	      }
	      return set;
	   }
	   
	   public static Set toSet(Object[] objects)
	   {
	      HashSet set = new HashSet();
	      for (int i = 0; i < objects.length; i++)
	      {
	         set.add(objects[i]);
	      }
	      return set;
	   }
}
