package com.bizcreator.core;

import com.bizcreator.core.ResourceMap;
import com.bizcreator.core.ResourceManager;

public class BaseException extends RuntimeException {

    public BaseException() {////////////
        super();
    }

    public BaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public BaseException(String message) {
        super(message);
    }

    public BaseException(Throwable cause) {
        super(cause);
    }

    @Override
    public String getLocalizedMessage() {
        String msg = getMessage();
        if (msg != null && msg.contains("@{")) {
            //通过resource map 取得本地字符串
            msg = evaluateStringExpression(msg);
        }
        return msg;
    }

    private String evaluateStringExpression(String expr) {

        if (expr.trim().equals("@{null}")) {
            return null;
        }

        ResourceMap res = ResourceManager.instance().getResourceMap(this.getClass());
        StringBuffer value = new StringBuffer();
        int i0 = 0, i1 = 0;
        while((i1 = expr.indexOf("@{", i0)) != -1) {
            if ((i1 == 0) || ((i1 > 0) && (expr.charAt(i1-1) != '\\'))) {

                int i2 = expr.indexOf("}", i1);
                if ((i2 != -1) && (i2 > i1+2)) {
                    String k = expr.substring(i1+2, i2);
                    String v = res.getString(k);
                    value.append(expr.substring(i0, i1));
                    if (v != null) {
                        value.append(v);
                    }
                    else {
                        /*
                        String msg = String.format("no value for \"%s\" in \"%s\"", k, expr);
                        throw new LookupException(msg, k, String.class);
                         */
                        //如果没有该key对应的串, 则原样返回
                        value.append(expr.substring(i1, i2 + 1));
                    }
                    i0 = i2 + 1;  // skip trailing "}"
                } else {
                    /*
                    String msg = String.format("no closing brace in \"%s\"", expr);
                    throw new LookupException(msg, "<not found>", String.class);
                     */
                    //没有对应的'}', 则原样返回
                    value.append(expr.substring(i1));
                }
            }
            else {  // we've found an escaped variable - "\@{"
                value.append(expr.substring(i0, i1-1));
                value.append("${");
                i0 = i1 + 2; // skip past "${"
            }
        }
        value.append(expr.substring(i0));
        return value.toString();
    }

}
