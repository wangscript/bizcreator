package com.bizcreator.core.encoding;

import java.io.*;
import java.util.*;

public final class HzTransform {

    /**
     * ----------------------------------------------------------------------
     * 字节流处理
     * ----------------------------------------------------------------------
     */
    public static boolean isBIG5(int i1, int i2) {
        boolean rt = ( (i2 >= 64) && (i2 <= 126)) ||
            ( (i2 >= 161) && (i2 <= 254));
        return rt;
    }

    public static boolean isGB(int i1, int i2) {
        boolean rt = false;
        if ( (i1 <= 161) && (i1 >= 247)) {
            rt = false;
        }
        else if ( (i2 <= 161) && (i2 >= 254)) {
            rt = false;
        }
        else {
            rt = true;
        }
        return rt;
    }

    public static int gbOffset(int i1, int i2) {
        return ( (i1 - 161) * 94 + (i2 - 161));
    }

    public static int big5Offset(int i1, int i2) {
        int of = -1;
        if ( (i2 >= 64) && (i2 <= 126)) {
            of = (i1 - 161) * 157 + (i2 - 64);
        }
        if ( (i2 >= 161) && (i2 <= 254)) {
            of = (i1 - 161) * 157 + 63 + (i2 - 161);
        }
        return of;
    }

    public static byte[] getBig5(int offset) {
        byte[] b = {
            0, 0};
        if ( (offset >= 0) && (offset <= 8177)) {
            if ( (offset >= 0) && (offset <= 1999)) {
                b[0] = Big1.b[ (offset) * 2];
                b[1] = Big1.b[ (offset) * 2 + 1];
            }
            else if ( (offset >= 2000) && (offset <= 3999)) {
                b[0] = Big2.b[ (offset - 2000) * 2];
                b[1] = Big2.b[ (offset - 2000) * 2 + 1];
            }
            else if ( (offset >= 4000) && (offset <= 5999)) {
                b[0] = Big3.b[ (offset - 4000) * 2];
                b[1] = Big3.b[ (offset - 4000) * 2 + 1];
            }
            else if ( (offset >= 6000) && (offset <= 8177)) {
                b[0] = Big4.b[ (offset - 6000) * 2];
                b[1] = Big4.b[ (offset - 6000) * 2 + 1];
            }
        }
        return b;
    }

    public static byte[] getGb(int offset) {
        byte[] b = {
            0, 0};
        if ( (offset >= 0) && (offset <= 14757)) {
            if ( (offset >= 0) && (offset <= 1999)) {
                b[0] = GB1.b[ (offset) * 2];
                b[1] = GB1.b[ (offset) * 2 + 1];
            }
            else if ( (offset >= 2000) && (offset <= 3999)) {
                b[0] = GB2.b[ (offset - 2000) * 2];
                b[1] = GB2.b[ (offset - 2000) * 2 + 1];
            }
            else if ( (offset >= 4000) && (offset <= 5999)) {
                b[0] = GB3.b[ (offset - 4000) * 2];
                b[1] = GB3.b[ (offset - 4000) * 2 + 1];
            }
            else if ( (offset >= 6000) && (offset <= 7999)) {
                b[0] = GB4.b[ (offset - 6000) * 2];
                b[1] = GB4.b[ (offset - 6000) * 2 + 1];
            }
            else if ( (offset >= 8000) && (offset <= 9999)) {
                b[0] = GB5.b[ (offset - 8000) * 2];
                b[1] = GB5.b[ (offset - 8000) * 2 + 1];
            }
            else if ( (offset >= 10000) && (offset <= 11999)) {
                b[0] = GB6.b[ (offset - 10000) * 2];
                b[1] = GB6.b[ (offset - 10000) * 2 + 1];
            }
            else if ( (offset >= 12000) && (offset <= 14757)) {
                b[0] = GB7.b[ (offset - 12000) * 2];
                b[1] = GB7.b[ (offset - 12000) * 2 + 1];
            }
        }
        return b;
    }

    /**
     * -----------------------------------------------------------------------
     * 字符串转换
     * -----------------------------------------------------------------------
     */
    public static String gbToBig5(String value) {
        String rt = "";
        byte[] b = {
            0, 0};
        try {
            b = value.getBytes("GB2312");
        }
        catch (Exception e) {}
        int len = b.length;
        int i = 0;
        while (i < len - 1) {
            int i1 = 0x00ff & b[i];
            int i2 = 0x00ff & b[i + 1];
            if (isGB(i1, i2)) {
                int offset = gbOffset(i1, i2);
                if ( (offset >= 0) && (offset <= 8177)) {
                    byte[] bb = getBig5(offset);
                    b[i] = bb[0];
                    b[i + 1] = bb[1];
                    i++;
                }
            }
            i++;
        }
        try {
            rt = new String(b, "BIG5");
        }
        catch (Exception e) {}
        ;
        return rt;
    }

    public static String big5ToGb(String value) {
        String rt = "";
        byte[] b = {
            0, 0};
        try {
            b = value.getBytes("BIG5");
        }
        catch (Exception e) {}
        int len = b.length;
        int i = 0;
        while (i < len - 1) {
            int i1 = 0x00ff & b[i];
            int i2 = 0x00ff & b[i + 1];
            if (isBIG5(i1, i2)) {
                int offset = big5Offset(i1, i2);
                if ( (offset >= 0) && (offset <= 14757)) {
                    byte[] bb = getGb(offset);
                    b[i] = bb[0];
                    b[i + 1] = bb[1];
                    i++;
                }
            }
            i++;
        }
        try {
            rt = new String(b, "GB2312");
        }
        catch (Exception e) {}
        ;
        return rt;
    }

    /**
     * -----------------------------------------------------------------------
     * 整个文件转换
     * -----------------------------------------------------------------------
     */

    static void writeOutput(String filename,
                            String str, String enc) {
        try {
            FileOutputStream fos = new FileOutputStream(filename);
            Writer out = new OutputStreamWriter(fos, enc);
            out.write(str);
            out.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    static String readInput(String filename, String enc) {
        StringBuffer buffer = new StringBuffer();
        try {
            FileInputStream fis = new FileInputStream(filename);
            InputStreamReader isr = new InputStreamReader(fis, enc);
            Reader in = new BufferedReader(isr);

            int ch;
            while ( (ch = in.read()) > -1) {
                buffer.append( (char) ch);
            }
            in.close();
            return buffer.toString();
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void toBig5(String gbFile, String big5File) {
        if (gbFile != null && big5File != null) {
            String in = readInput(gbFile, "GB2312");
            String out = gbToBig5(in);
            writeOutput(big5File, out, "BIG5");
        }
    }

    public static void toGb(String big5File, String gbFile) {
        if (big5File != null && gbFile != null) {
            String in = readInput(big5File, "BIG5");
            String out = big5ToGb(in);
            writeOutput(gbFile, out, "GB2312");
        }
    }

    public static String getFilePrefix(String filename) {
        int dotIndex = filename.lastIndexOf(".");
        if (dotIndex != -1) {
            return filename.substring(0, dotIndex);
        }
        else {
            return filename;
        }
    }

    public static String getFileExt(String filename) {
        int dotIndex = filename.lastIndexOf(".");
        int len = filename.length();
        if (dotIndex != -1) {
            return filename.substring(dotIndex + 1, len);
        }
        else {
            return "";
        }
    }

    public static void convert(String inputFile, String ct) {
        String filePrefix = getFilePrefix(inputFile);
        String outputFile = "";
        final String oldExt = getFileExt(inputFile);
        String newExt = "big5";
        if (filePrefix.equals("*")) {
            try {
                File path = new File(".");
                String[] files;
                if (oldExt.equals("*")) {
                    files = path.list();
                }
                else {
                    files = path.list(
                        new FilenameFilter() {
                        public boolean accept(File dir, String name) {
                            String f = new File(name).getName();
                            String ext = getFileExt(f);
                            return (ext.equals(oldExt));
                        }
                    }
                    );
                    //begin converting
                }
                for (int i = 0; i < files.length; i++) {
                    if (ct.equals("1")) {
                        outputFile = getFilePrefix(files[i]) + ".big5";
                        toBig5(inputFile, outputFile);
                    }
                    if (ct.equals("2")) {
                        outputFile = getFilePrefix(files[i]) + ".gb";
                        toGb(inputFile, outputFile);
                    }
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            if (ct.equals("1")) {
                outputFile = filePrefix + ".big5";
                toBig5(inputFile, outputFile);
            }
            if (ct.equals("2")) {
                outputFile = filePrefix + ".gb";
                toGb(inputFile, outputFile);
            }
        }
    }

    public static void main(String[] args) {
        //String t1 = gbToBig5("b中abcdef华c人rr[]民;共a和国曾经沧桑a");
        String t1 = gbToBig5("在写字板中可以创建和编辑简单文本文档，或者有复杂格式和图形的文档。可以将信息从其");
        String t2 = big5ToGb(t1);
        System.out.println(t1);
        System.out.println(t2);

        //文件转换
        String inputFile = null;
        String outputFile = null;
        String ct = "1";
        String in = null;
        String out = null;
        int len = args.length;
        if (args.length >= 2) {
            ct = args[len - 1];
            for (int i = 0; i < len - 1; i++) {
                inputFile = args[i];
                String filePrefix = getFilePrefix(inputFile);
                if (ct.equals("1")) {
                    outputFile = filePrefix + ".big5";
                    toBig5(inputFile, outputFile);
                }
                if (ct.equals("2")) {
                    outputFile = filePrefix + ".gb";
                    toGb(inputFile, outputFile);
                }
            }
        }
        else {
            System.out.println("Usage: StreamConverter inputfile 1|2");
            return;
        }
    }
}