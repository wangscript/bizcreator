/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bizcreator.core.entity;

import com.bizcreator.core.data.BeanModel;
import com.bizcreator.core.annotation.FieldInfo;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * 长宽高
 * @author lgh
 */
@Embeddable
public class LWH extends BeanModel implements Serializable {

    @FieldInfo(name="长度")
    protected double dimL;

    @FieldInfo(name="宽度")
    protected double dimW;

    @FieldInfo(name="高度")
    protected double dimH;

    public LWH() {}

    public LWH(double dimL, double dimW, double dimH) {
        this.dimL = dimL;
        this.dimW = dimW;
        this.dimH = dimH;
    }

    @Column(name="dim_l")
    public double getDimL() {
        return dimL;
    }

    public void setDimL(double dimL) {
        this.dimL = dimL;
    }

    @Column(name="dim_w")
    public double getDimW() {
        return dimW;
    }

    public void setDimW(double dimW) {
        this.dimW = dimW;
    }

    @Column(name="dim_h")
    public double getDimH() {
        return dimH;
    }

    public void setDimH(double dimH) {
        this.dimH = dimH;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj instanceof LWH) {
            LWH lwh = (LWH) obj;
            if (this.dimL == lwh.dimL && this.dimW == lwh.dimW && this.dimH == lwh.dimH) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + (int) (Double.doubleToLongBits(this.dimL) ^ (Double.doubleToLongBits(this.dimL) >>> 32));
        hash = 67 * hash + (int) (Double.doubleToLongBits(this.dimW) ^ (Double.doubleToLongBits(this.dimW) >>> 32));
        hash = 67 * hash + (int) (Double.doubleToLongBits(this.dimH) ^ (Double.doubleToLongBits(this.dimH) >>> 32));
        return hash;
    }

    @Override
    public String toString() {
        return this.dimL + " * " + this.dimW + " * " + this.dimH;
    }

    public final static LWH ZERO = new LWH(0, 0, 0);
    
}

