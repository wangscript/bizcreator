/*
 *$Id: UniTransform.java,v 1.1 2008/02/12 01:48:24 lgh Exp $
 */

package com.bizcreator.core.encoding;

import java.io.UnsupportedEncodingException;

/**
 * Title:        www.365tt.com
 * Description:
 * Copyright:    Copyright (c)
 * Company:      apusic
 * @author lgh
 * @version 1.0
 */

public final class UniTransform {

    public static String GBToUnicode(String strIn){
        String strOut=null;
        if(strIn==null || strIn.length()==0)
			return strIn;
        try{
            strOut = new String(strIn.getBytes("8859_1"),"GB2312");
        }catch(UnsupportedEncodingException ue){
        		System.err.println(ue.getMessage());
            //Debug.println(ue.getMessage());
        }
        strOut = HzTransform.gbToBig5(strOut);
        return strOut;
    }

    public static String UnicodeToGB(String strIn){
	   	String strOut = null;
		if(strIn == null || (strIn.trim()).equals(""))
			return strIn;
        try{
            String str1 = HzTransform.big5ToGb(strIn);
            strOut = new String(str1.getBytes("GB2312"),"8859_1");
        }catch(UnsupportedEncodingException ue){
            System.err.println(ue.getMessage());
        }
        return strOut;
    }
}