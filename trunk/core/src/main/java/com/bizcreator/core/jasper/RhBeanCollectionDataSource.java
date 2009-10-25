/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bizcreator.core.jasper;

import java.util.Collection;
import java.util.Iterator;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: RhBeanCollectionDataSource.java,v 1.1 2008/04/21 14:27:08 lgh Exp $
 */
public class RhBeanCollectionDataSource extends RhAbstractBeanDataSource {

    /**
     *
     */
    private Collection data = null;
    private Iterator iterator = null;
    private Object currentBean = null;

    /**
     *
     */
    public RhBeanCollectionDataSource(Collection beanCollection) {
        this(beanCollection, true);
    }

    /**
     *
     */
    public RhBeanCollectionDataSource(Collection beanCollection, boolean isUseFieldDescription) {
        super(isUseFieldDescription);

        this.data = beanCollection;

        if (this.data != null) {
            this.iterator = this.data.iterator();
        }
    }

    /**
     *
     */
    public boolean next() {
        boolean hasNext = false;

        if (this.iterator != null) {
            hasNext = this.iterator.hasNext();

            if (hasNext) {
                this.currentBean = this.iterator.next();
            }
        }

        return hasNext;
    }

    /**
     *
     */
    public Object getFieldValue(JRField field) throws JRException {
        return getFieldValue(currentBean, field);
    }

    /**
     *
     */
    public void moveFirst() {
        if (this.data != null) {
            this.iterator = this.data.iterator();
        }
    }
}

