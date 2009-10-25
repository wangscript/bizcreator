/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bizcreator.core.jasper;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: RhBeanArrayDataSource.java,v 1.1 2008/04/21 14:27:08 lgh Exp $
 */
public class RhBeanArrayDataSource extends RhAbstractBeanDataSource {

    /**
     *
     */
    private Object[] data = null;
    private int index = -1;

    /**
     *
     */
    public RhBeanArrayDataSource(Object[] beanArray) {
        this(beanArray, true);
    }

    /**
     *
     */
    public RhBeanArrayDataSource(Object[] beanArray, boolean isUseFieldDescription) {
        super(isUseFieldDescription);

        this.data = beanArray;
    }

    /**
     *
     */
    public boolean next() {
        this.index++;

        if (this.data != null) {
            return (this.index < this.data.length);
        }

        return false;
    }

    /**
     *
     */
    public Object getFieldValue(JRField field) throws JRException {
        return getFieldValue(data[this.index], field);
    }

    /**
     *
     */
    public void moveFirst() {
        this.index = -1;
    }
}

