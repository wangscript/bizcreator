/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bizcreator.core.session.impl;

import com.bizcreator.core.BaseException;
import com.bizcreator.core.QueryFactory;
import com.bizcreator.core.session.BatchQuery;
import com.bizcreator.core.session.ResultList;
import org.hibernate.ScrollableResults;
import org.springframework.transaction.annotation.Transactional;

/**
 * @todo 目前仅仅测试了next方法，其他方法待测
 * lgh 2008-06-04 9:45
 * @author lgh
 */
@Transactional
public class BatchQuerySession extends StatefulSession implements BatchQuery {

    protected String[] fields = null;
    protected Class qeClass;
    protected Class fromClass;
    protected String extraFrom;
    protected String filter;
    protected Object[] paramPairs;

    protected transient ScrollableResults results;
    
    //position information.Keeps last read block size
    private int lastReadBlockSize = 0;

    /**
     * curent position into the resultset. Needed only for
     * Passivation - restauration
     */
    private int position = 0;
    
    // default block size for read operations
    private int defaultBlockSize = DEFAULT_BLOCK_SIZE;
    
    private final static int DEFAULT_BLOCK_SIZE = 150;

    //BlockSize must be a multiple of this value
    private final static int BLOCK_SIZE_MUST_BE_MULTIPLE_OF = 10;
    private final static int FETCH_SIZE = 2000; // fetch size
    
    //
    private boolean beforeFirst = true;
    private boolean afterLast = false;
    
    public void create(Class qeClass, Class fromClass, String extraFrom, 
            String filter, Object[] paramPairs) {
        this.qeClass = qeClass;
        this.fromClass = fromClass;
        this.extraFrom = extraFrom;
        this.filter = filter;
        this.paramPairs = paramPairs;
        this.setFetchSize(FETCH_SIZE);
    }
    
    public void create(Class qeClass, Class fromClass, String filter, Object[] paramPairs) {
        create(qeClass, fromClass, null, filter, paramPairs);
    }
    
    public void create(Class qeClass, Class fromClass, Object[] paramPairs) {
        create(qeClass, fromClass, null, null, paramPairs);
    }
    
    public void create(Class qeClass, Object[] paramPairs) {
        create(qeClass, qeClass, null, null, paramPairs);
    }
    
    public void setFields(String[] fields) {
        this.fields = fields;
    }
    
    public void execute() {
        String ql = null;
        if (fields != null && fields.length > 0) {
             ql = QueryFactory.getQL(fields, qeClass, fromClass, extraFrom, filter, paramPairs);
        }
        else {
            ql = QueryFactory.getQL(qeClass, fromClass, extraFrom, filter, paramPairs);
        }
        results = this.scrollNp(ql, paramPairs);
    }
    
    //---------------------lifecycle methods --------------------
    @Override
    public void create() {
        super.create();
    }
    
    @Override
    public void activate() {
        super.activate();
    }
    
    @Override
    public void passivate() {
        super.passivate();
    }
    
    @Override
    public void destroy() {
        super.destroy();
    }
    
    
    //--------------------- navigate methods --------------------
    //------------------------------current methods !!!-------------------
    public ResultList current(int blockSize) {
        ResultList result = new ResultList(blockSize);

        // @todo use movePrevious() instead when "cannot move to row 0 Exception" solved
        log.debug("Begin : current(p_blocksize)");

        if (!isBeforeFirst()) {
            // if after last record move to a valid record (last record)
            if (isAfterLast()) {
                results.last();
            }

            // go back to the begining of the previous record
            if ((getPosition() - lastReadBlockSize) > 0) {
                results.scroll(-lastReadBlockSize);
            } else {
                lastReadBlockSize = getPosition() + 1;
                results.beforeFirst();
            }
        }

        result = next(blockSize);
        return result;
    }

    public ResultList current() {
        int blockSize = lastReadBlockSize;
        return current(blockSize);
    }
    
    //------------------------------first methods !!!-------------------
    public ResultList first(int blockSize) {
        // before first
        results.beforeFirst(); //调用该方法后, rownum=-1, isFirst()返回false
        lastReadBlockSize = 0;
        return next(blockSize);
    }

    public ResultList first() {
        return first(DEFAULT_BLOCK_SIZE);
    }

    /**
     * ------------------------------previous methods !!!-------------------
     */
    public ResultList previous(int blockSize) {
        log.debug("Begin : previous");
        ResultList result = null;

        // test if we are already on the first record
        if (isFirstBlock() || isBeforeFirst()) {
            result = new ResultList(blockSize);
            // add R_RI_IS_BEFORE_FIRST property in the result table
            result.setBeforeFirst((true));
            return result;
        }

        // move to begining of last read block (which is the current block)
        movePrevious(lastReadBlockSize);

        // test if blockSize is to big for this previous
        int currentPos = getPosition();
        if (currentPos - lastReadBlockSize < 0) {
            blockSize = currentPos;
        }

        // move to the begining of the previous block (the block to be returned )
        movePrevious(blockSize);

        // reading a data block. Cursor is located at the end of the block
        result = next(blockSize);

        // if on the first record
        if (isFirstBlock()) {
            result.setFirst((true));
        }

        return result;
    }

    public ResultList previous() {
        return previous(DEFAULT_BLOCK_SIZE);
    }

    
    //------------------------------next methods !!!-------------------
    public ResultList next(int blockSize) {
        ResultList result = new ResultList(blockSize);
        int iReadBlockPos = 0;
        int endPos = -1;

        if (beforeFirst) {
            result.setFirst(true);
        }
        else if (afterLast) {
            result.setAlreadyLast(true);
            result.setLast(true);
            return result;
        }
        
        //reads rows
        while ((iReadBlockPos < blockSize) && results.next()) {
            addCurrentRow(result);
            // increment position in current block
            iReadBlockPos++;
            if (results.isLast()) {
                endPos = getPosition();
                result.setLast((true));
                if (endPos != -1) {
                    result.setSize(endPos);
                }
            }
            if (beforeFirst) beforeFirst = false;
        }
        lastReadBlockSize = iReadBlockPos;
        
        //System.out.println(">>>is last: " + results.isLast());
        //System.out.println(">>>pos: " + results.getRowNumber());
        
        if (getPosition() == -1) {
            afterLast = true;
        }
        return result;
    }
    
    public ResultList next() {
        return next(DEFAULT_BLOCK_SIZE);
    }
    
    
   
    //------------------------------last methods !!!-------------------
    public ResultList last(int blockSize) {
        results.afterLast();
        afterLast = true;
        
        // after last
        lastReadBlockSize = 0;
        return previous(blockSize);

    }

    public ResultList last() {
        return last(DEFAULT_BLOCK_SIZE);
    }
    
    //--------------------------- private methods ---------------------------

    /**
     *  adding current record data
     *@exception  SQLException  Description of Exception
     */
    private void addCurrentRow(ResultList resultList) {

        // get current row
        Object[] objs = results.get();
        if (objs.length == 1) {
            resultList.addRow(objs[0], results.getRowNumber());
        }
        else {
            resultList.addRow(objs, results.getRowNumber());
        }
    }

    private boolean isBeforeFirst() {
        int rowNum = results.getRowNumber();
        return (results.isFirst() && rowNum == -1);
    }
    
    private boolean isAfterLast() {
        int rowNum = results.getRowNumber();
        return (results.isLast() && rowNum == -1);
    }
    
    /**
     *  Returns if the cursor is on the first record
     */
    private boolean isFirstBlock() {
        if (getPosition() - lastReadBlockSize <= 0) {
            return true;
        }
        return false;
    }

    
    /**
     *  Gets the Position attribute of the RemoteIteratorBean object
     *@return    The Position value
     */
    public int getPosition() {
        if (results == null) {
            return 0;
        }
        else {
            return results.getRowNumber();
        }
    }

    /**
     *  Allows to move to another bloc. _currentBlockNumber is set with the
     *  value of the block to be read.
     */
    private void restorePosition(int position, int lastReadBlockSize) {
        if (position >= 0) {
            results.setRowNumber(position);
        }
        this.lastReadBlockSize = lastReadBlockSize;
    }

    /**
     *  move to next block
     */
    private void moveNext(int blockSize) {
        int iPos = 0;
        while ((iPos < blockSize) && results.next()) {
            iPos++;
        }
        // incr?ente le bloc courant
        lastReadBlockSize = iPos;
    }

    /**
     *  move from the end of the current block to the end of the previous block
     */
    private void movePrevious(int blockSize)  {
        if (isBeforeFirst()) {
            return;
        }
        if (isAfterLast()) {
            results.last();
        }
        
        //results.scroll(-blockSize);
        
        int oldRow = results.getRowNumber();
        int newRow = oldRow - blockSize;
        if (newRow < 0) {
            results.beforeFirst();
            lastReadBlockSize = oldRow;
        }
        else {
            results.setRowNumber(newRow);
            lastReadBlockSize = blockSize;
        }
    }

    public int getCurrentBlockPosition() {
        int result = 0;
        if (results != null) {
            result = results.getRowNumber() - lastReadBlockSize;
        }
        return result;
    }

    public int getTotalSize() {
        log.debug("Begin : getTotalSize");
        // maybe we need to test if we are after the last record - need a test
        // get current position
        int currentPosition = getPosition();

        // move to last record
        results.last();

        // get last record position;
        int size = getPosition();

        if (currentPosition == 0) {
            results.beforeFirst();
        } else {
            results.setRowNumber(currentPosition);
        }
        return size;
    }
    
    public void setDefaultBlockSize(int defaultBlockSize) {
        this.defaultBlockSize = defaultBlockSize;
        checkBlockSize();
    }
    
    private void checkBlockSize() {
        String msage =
            "RemoteQueryBean.ejbCreate : Block size must be a multiple of " +
            BLOCK_SIZE_MUST_BE_MULTIPLE_OF;
        // verify if bock size is a multiple of RI_BLOCK_SIZE_MUST_BE_MULTIPLE_OF
        if (defaultBlockSize % BLOCK_SIZE_MUST_BE_MULTIPLE_OF != 0) {
            log.error(msage);
            throw new BaseException(msage);
        }
    }
}
