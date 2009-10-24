/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bizcreator.core.session;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ResultList
    implements Serializable {

    private boolean isLast;
    private boolean isFirst;
    private boolean isBeforeFirst;
    private boolean isAlreadyLast;
    private int size = 0;
    private List liResults;
    private List liRowIndex;

    /**
     * Unique constructor
     */
    public ResultList() {
        liResults = new ArrayList();
        liRowIndex = new ArrayList();
    }

    public ResultList(int size) {
        liResults = new ArrayList(size);
        liRowIndex = new ArrayList(size);
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public boolean isFirst() {
        return isFirst;
    }

    public void setFirst(boolean first) {
        this.isFirst = first;
    }

    public boolean isLast() {
        return isLast;
    }

    public void setLast(boolean last) {
        this.isLast = last;
    }

    public boolean isAlreadyLast() {
        return isAlreadyLast;
    }

    public void setAlreadyLast(boolean alreadyLast) {
        this.isAlreadyLast = alreadyLast;
    }

    public boolean isBeforeFirst() {
        return isBeforeFirst;
    }

    public void setBeforeFirst(boolean beforeFirst) {
        this.isBeforeFirst = beforeFirst;
    }

    public List getRows() {
        return liResults;
    }

    public List getRowIndexes() {
        return liRowIndex;
    }

    /**
     * adds the row (an arrayList) in the list of result.
     * Used by the RI.
     * @param p_liRow
     */
    public void addRow(List liRow, int rowPosition) {
        liResults.add(liRow);
        liRowIndex.add(rowPosition);
    }

    public void addRow(Object[] rowArray, int rowPosition) {
        liResults.add(rowArray);
        liRowIndex.add(rowPosition);
    }
    
    public void addRow(Object rowObj, int rowPosition) {
        liResults.add(rowObj);
        liRowIndex.add(rowPosition);
    }
}
