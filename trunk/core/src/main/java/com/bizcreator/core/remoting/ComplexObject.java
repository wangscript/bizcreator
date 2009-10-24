package com.bizcreator.core.remoting;

import java.io.Serializable;

/**
 * @author <a href="mailto:tom.elrod@jboss.com">Tom Elrod</a>
 */
public class ComplexObject implements Serializable
{
   public int i = 42;
   public String s = "test";
   public boolean b = true;
   public byte[] bytes = new byte[0];

   public ComplexObject()
   {

   }

   public ComplexObject(int i, String s, boolean b)
   {
      this.i = i;
      this.s = s;
      this.b = b;
   }

   public ComplexObject(int i, String s, boolean b, int byteSize)
   {
      this(i, s, b);
      bytes = new byte[byteSize];
   }

   public void setBytes(byte[] bytes)
   {
      this.bytes = bytes;
   }

   public int getSize()
   {
      return bytes.length;
   }

   public boolean equals(Object o)
   {
      if(o instanceof ComplexObject)
      {
         ComplexObject co = (ComplexObject) o;
         if(co.i == i && co.s.equals(s) && co.b == b)
         {
            return true;
         }
         else
         {
            return false;
         }
      }
      else
      {
         return false;
      }
   }
}
