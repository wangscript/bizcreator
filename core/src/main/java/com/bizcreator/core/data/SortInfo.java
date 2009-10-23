/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bizcreator.core.data;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Aggregates sort field and sort direction.
 */
public class SortInfo implements Serializable {

    private String sortField;
    private SortDir sortDir = SortDir.NONE;

    /**
     * Creates a new sort field instance.
     */
    public SortInfo() {
    }

    /**
     * Creates a new sort info instance.
     *
     * @param field the sort field
     * @param sortDir the sort direction
     */
    public SortInfo(String field, SortDir sortDir) {
        this.sortField = field;
        this.sortDir = sortDir;
    }

    /**
     * Returns the sort field.
     *
     * @return the sort field
     */
    public String getSortField() {
        return sortField;
    }

    /**
     * Sets the sort field.
     *
     * @param sortField the sort field
     */
    public void setSortField(String sortField) {
        this.sortField = sortField;
    }

    /**
     * Returns the sort direction.
     *
     * @return the sort direction
     */
    public SortDir getSortDir() {
        return sortDir;
    }

    /**
     * Sets the sort direction.
     *
     * @param sortDir the sort direction
     */
    public void setSortDir(SortDir sortDir) {
        this.sortDir = sortDir;
    }

/**
   * Sort direction enum.
   */
  public enum SortDir {

    NONE {
      @SuppressWarnings("unchecked")
      @Override
      public Comparator comparator(Comparator c) {
        return c;
      }
    },

    ASC {
      @Override
      public <X> Comparator<X> comparator(final Comparator<X> c) {
        return new Comparator<X>() {
          public int compare(X o1, X o2) {
            return c.compare(o1, o2);
          }
        };
      }
    },

    DESC {
      @Override
      public <X> Comparator<X> comparator(final Comparator<X> c) {
        return new Comparator<X>() {
          public int compare(X o1, X o2) {
            return c.compare(o2, o1);
          }
        };
      }
    };

    public static SortDir findDir(String sortDir) {
      if ("ASC".equals(sortDir)) {
        return SortDir.ASC;
      } else if ("DESC".equals(sortDir)) {
        return SortDir.DESC;
      }
      return null;
    }

    public static SortDir toggle(SortDir sortDir) {
      return (sortDir == ASC) ? DESC : ASC;
    }

    /**
     * An example of how to use this :
     *
     * List<Something> list = ...
     *
     * Collections.sort(list, SortDir.ASC.comparator(new Comparator() { public
     * int compare(Object o1, Object o2) { return ... } });
     *
     *
     * @return a Comparator that wraps the specific comparator that orders the
     *         results according to the sort direction
     */
    public abstract <X> Comparator<X> comparator(Comparator<X> c);
  }

}
