package com.bizcreator.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.KeyStroke;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 字符串操作实用类：
 * 
 * @author <a href="mailto:rhino03142000@yahoo.com">luoguanhua</a>
 * @version $Revision: 1.3 $
 */
public class StringUtil {
    
    static Log log = LogFactory.getLog(StringUtil.class);
    
	/**
	 * 将字符串中指定的子串用新的子串替换，区分字符大小写
	 * @param string 被替换的字符串
	 * @param oldString 旧子串
	 * @param newString 新子串
	 * @return
	 */
	public static final String replace(String string, String oldString,
			String newString) {
		if (string == null)
			return null;
		if (newString == null)
			return string;
		int i = 0;
		if ((i = string.indexOf(oldString, i)) >= 0) {
			char string2[] = string.toCharArray();
			char newString2[] = newString.toCharArray();
			int oLength = oldString.length();
			StringBuffer buf = new StringBuffer(string2.length);
			buf.append(string2, 0, i).append(newString2);
			i += oLength;
			int j;
			for (j = i; (i = string.indexOf(oldString, i)) > 0; j = i) {
				buf.append(string2, j, i - j).append(newString2);
				i += oLength;
			}

			buf.append(string2, j, string2.length - j);
			return buf.toString();
		} else {
			return string;
		}
	}
	
	/**
	 * 将字符串中指定的子串用新的子串替换，不区分字符大小写
	 * @param string 被替换的字符串
	 * @param oldString 旧子串
	 * @param newString 新子串
	 * @return
	 */
	public static final String replaceIgnoreCase(String line, String oldString,
			String newString) {
		if (line == null)
			return null;
		String lcLine = line.toLowerCase();
		String lcOldString = oldString.toLowerCase();
		int i = 0;
		if ((i = lcLine.indexOf(lcOldString, i)) >= 0) {
			char line2[] = line.toCharArray();
			char newString2[] = newString.toCharArray();
			int oLength = oldString.length();
			StringBuffer buf = new StringBuffer(line2.length);
			buf.append(line2, 0, i).append(newString2);
			i += oLength;
			int j;
			for (j = i; (i = lcLine.indexOf(lcOldString, i)) > 0; j = i) {
				buf.append(line2, j, i - j).append(newString2);
				i += oLength;
			}

			buf.append(line2, j, line2.length - j);
			return buf.toString();
		} else {
			return line;
		}
	}

	public static final String replaceIgnoreCase(String line, String oldString,
			String newString, int count[]) {
		if (line == null)
			return null;
		String lcLine = line.toLowerCase();
		String lcOldString = oldString.toLowerCase();
		int i = 0;
		if ((i = lcLine.indexOf(lcOldString, i)) >= 0) {
			int counter = 1;
			char line2[] = line.toCharArray();
			char newString2[] = newString.toCharArray();
			int oLength = oldString.length();
			StringBuffer buf = new StringBuffer(line2.length);
			buf.append(line2, 0, i).append(newString2);
			i += oLength;
			int j;
			for (j = i; (i = lcLine.indexOf(lcOldString, i)) > 0; j = i) {
				counter++;
				buf.append(line2, j, i - j).append(newString2);
				i += oLength;
			}

			buf.append(line2, j, line2.length - j);
			count[0] = counter;
			return buf.toString();
		} else {
			return line;
		}
	}

	public static final String replace(String line, String oldString,
			String newString, int count[]) {
		if (line == null)
			return null;
		int i = 0;
		if ((i = line.indexOf(oldString, i)) >= 0) {
			int counter = 1;
			char line2[] = line.toCharArray();
			char newString2[] = newString.toCharArray();
			int oLength = oldString.length();
			StringBuffer buf = new StringBuffer(line2.length);
			buf.append(line2, 0, i).append(newString2);
			i += oLength;
			int j;
			for (j = i; (i = line.indexOf(oldString, i)) > 0; j = i) {
				counter++;
				buf.append(line2, j, i - j).append(newString2);
				i += oLength;
			}

			buf.append(line2, j, line2.length - j);
			count[0] = counter;
			return buf.toString();
		} else {
			return line;
		}
	}
	
	/**
	 * 剥离xml标记，取得其中的字符串，如: stripTags("<html><title>front page</title><body> detail content </body></html>")
	 * 结果为：front page detail content
	 * @param in
	 * @return
	 */
	public static final String stripTags(String in) {
		if (in == null)
			return null;
		else
			return stripTags(in, false);
	}

	public static final String stripTags(String in, boolean stripBRTag) {
		if (in == null)
			return null;
		int i = 0;
		int last = 0;
		char input[] = in.toCharArray();
		int len = input.length;
		StringBuffer out = new StringBuffer((int) ((double) len * 1.3D));
		for (; i < len; i++) {
			char ch = input[i];
			if (ch > '>')
				continue;
			if (ch == '<') {
				if (!stripBRTag && i + 3 < len && input[i + 1] == 'b'
						&& input[i + 2] == 'r' && input[i + 3] == '>') {
					i += 3;
					continue;
				}
				if (i > last) {
					if (last > 0)
						out.append(" ");
					out.append(input, last, i - last);
				}
				last = i + 1;
				continue;
			}
			if (ch == '>')
				last = i + 1;
		}

		if (last == 0)
			return in;
		if (i > last)
			out.append(input, last, i - last);
		return out.toString();
	}

	public static final String escapeHTMLTags(String in) {
		if (in == null)
			return null;
		int i = 0;
		int last = 0;
		char input[] = in.toCharArray();
		int len = input.length;
		StringBuffer out = new StringBuffer((int) ((double) len * 1.3D));
		for (; i < len; i++) {
			char ch = input[i];
			if (ch > '>')
				continue;
			if (ch == '<') {
				if (i > last)
					out.append(input, last, i - last);
				last = i + 1;
				out.append(LT_ENCODE);
				continue;
			}
			if (ch == '>') {
				if (i > last)
					out.append(input, last, i - last);
				last = i + 1;
				out.append(GT_ENCODE);
				continue;
			}
			if (ch != '"')
				continue;
			if (i > last)
				out.append(input, last, i - last);
			last = i + 1;
			out.append(QUOTE_ENCODE);
		}

		if (last == 0)
			return in;
		if (i > last)
			out.append(input, last, i - last);
		return out.toString();
	}
	
	public static final synchronized String hash(String data) {
		if (digest == null)
			try {
				digest = MessageDigest.getInstance("MD5");
			} catch (NoSuchAlgorithmException nsae) {
			}
		try {
			digest.update(data.getBytes("utf-8"));
		} catch (UnsupportedEncodingException e) {
		}
		return encodeHex(digest.digest());
	}

	public static final synchronized String hash(byte data[]) {
		if (digest == null)
			try {
				digest = MessageDigest.getInstance("MD5");
			} catch (NoSuchAlgorithmException nsae) {
			}
		digest.update(data);
		return encodeHex(digest.digest());
	}

	public static final String encodeHex(byte bytes[]) {
		StringBuffer buf = new StringBuffer(bytes.length * 2);
		for (int i = 0; i < bytes.length; i++) {
			if ((bytes[i] & 0xff) < 16)
				buf.append("0");
			buf.append(Long.toString(bytes[i] & 0xff, 16));
		}

		return buf.toString();
	}

	public static final byte[] decodeHex(String hex) {
		char chars[] = hex.toCharArray();
		byte bytes[] = new byte[chars.length / 2];
		int byteCount = 0;
		for (int i = 0; i < chars.length; i += 2) {
			int newByte = 0;
			newByte |= hexCharToByte(chars[i]);
			newByte <<= 4;
			newByte |= hexCharToByte(chars[i + 1]);
			bytes[byteCount] = (byte) newByte;
			byteCount++;
		}

		return bytes;
	}

	private static final byte hexCharToByte(char ch) {
		switch (ch) {
		case 48: // '0'
			return 0;

		case 49: // '1'
			return 1;

		case 50: // '2'
			return 2;

		case 51: // '3'
			return 3;

		case 52: // '4'
			return 4;

		case 53: // '5'
			return 5;

		case 54: // '6'
			return 6;

		case 55: // '7'
			return 7;

		case 56: // '8'
			return 8;

		case 57: // '9'
			return 9;

		case 97: // 'a'
			return 10;

		case 98: // 'b'
			return 11;

		case 99: // 'c'
			return 12;

		case 100: // 'd'
			return 13;

		case 101: // 'e'
			return 14;

		case 102: // 'f'
			return 15;

		case 58: // ':'
		case 59: // ';'
		case 60: // '<'
		case 61: // '='
		case 62: // '>'
		case 63: // '?'
		case 64: // '@'
		case 65: // 'A'
		case 66: // 'B'
		case 67: // 'C'
		case 68: // 'D'
		case 69: // 'E'
		case 70: // 'F'
		case 71: // 'G'
		case 72: // 'H'
		case 73: // 'I'
		case 74: // 'J'
		case 75: // 'K'
		case 76: // 'L'
		case 77: // 'M'
		case 78: // 'N'
		case 79: // 'O'
		case 80: // 'P'
		case 81: // 'Q'
		case 82: // 'R'
		case 83: // 'S'
		case 84: // 'T'
		case 85: // 'U'
		case 86: // 'V'
		case 87: // 'W'
		case 88: // 'X'
		case 89: // 'Y'
		case 90: // 'Z'
		case 91: // '['
		case 92: // '\\'
		case 93: // ']'
		case 94: // '^'
		case 95: // '_'
		case 96: // '`'
		default:
			return 0;
		}
	}

	public static String encodeBase64(String data) {
		byte bytes[] = null;
		try {
			bytes = data.getBytes("ISO-8859-1");
		} catch (UnsupportedEncodingException uee) {
		}
		return encodeBase64(bytes);
	}

	public static String encodeBase64(byte data[]) {
		int len = data.length;
		StringBuffer ret = new StringBuffer((len / 3 + 1) * 4);
		for (int i = 0; i < len; i++) {
			int c = data[i] >> 2 & 0x3f;
			ret
					.append("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"
							.charAt(c));
			c = data[i] << 4 & 0x3f;
			if (++i < len)
				c |= data[i] >> 4 & 0xf;
			ret
					.append("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"
							.charAt(c));
			if (i < len) {
				c = data[i] << 2 & 0x3f;
				if (++i < len)
					c |= data[i] >> 6 & 3;
				ret
						.append("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"
								.charAt(c));
			} else {
				i++;
				ret.append('=');
			}
			if (i < len) {
				c = data[i] & 0x3f;
				ret
						.append("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"
								.charAt(c));
			} else {
				ret.append('=');
			}
		}

		return ret.toString();
	}

	public static String decodeBase64(String data) {
		byte bytes[] = null;
		try {
			bytes = data.getBytes("ISO-8859-1");
		} catch (UnsupportedEncodingException uee) {
		}
		return decodeBase64(bytes);
	}

	public static String decodeBase64(byte data[]) {
		int len = data.length;
		StringBuffer ret = new StringBuffer((len * 3) / 4);
		for (int i = 0; i < len; i++) {
			int c = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"
					.indexOf(data[i]);
			i++;
			int c1 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"
					.indexOf(data[i]);
			c = c << 2 | c1 >> 4 & 3;
			ret.append((char) c);
			if (++i < len) {
				c = data[i];
				if (61 == c)
					break;
				c = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"
						.indexOf(c);
				c1 = c1 << 4 & 0xf0 | c >> 2 & 0xf;
				ret.append((char) c1);
			}
			if (++i >= len)
				continue;
			c1 = data[i];
			if (61 == c1)
				break;
			c1 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"
					.indexOf(c1);
			c = c << 6 & 0xc0 | c1;
			ret.append((char) c);
		}

		return ret.toString();
	}

	public static String URLEncode(String original, String charset)
			throws UnsupportedEncodingException {
		if (original == null)
			return null;
		byte octets[];
		try {
			octets = original.getBytes(charset);
		} catch (UnsupportedEncodingException error) {
			throw new UnsupportedEncodingException();
		}
		StringBuffer buf = new StringBuffer(octets.length);
		for (int i = 0; i < octets.length; i++) {
			char c = (char) octets[i];
			if (allowed_query.get(c)) {
				buf.append(c);
			} else {
				buf.append('%');
				byte b = octets[i];
				char hexadecimal = Character.forDigit(b >> 4 & 0xf, 16);
				buf.append(Character.toUpperCase(hexadecimal));
				hexadecimal = Character.forDigit(b & 0xf, 16);
				buf.append(Character.toUpperCase(hexadecimal));
			}
		}

		return buf.toString();
	}
	
	/**
	 * 返回一段文字中的单词的数组
	 * @param text
	 * @return
	 */
	public static final String[] toLowerCaseWordArray(String text) {
		if (text == null || text.length() == 0)
			return new String[0];
		ArrayList wordList = new ArrayList();
		BreakIterator boundary = BreakIterator.getWordInstance();
		boundary.setText(text);
		int start = 0;
		for (int end = boundary.next(); end != -1; end = boundary.next()) {
			String tmp = text.substring(start, end).trim();
			tmp = replace(tmp, "+", "");
			tmp = replace(tmp, "/", "");
			tmp = replace(tmp, "\\", "");
			tmp = replace(tmp, "#", "");
			tmp = replace(tmp, "*", "");
			tmp = replace(tmp, ")", "");
			tmp = replace(tmp, "(", "");
			tmp = replace(tmp, "&", "");
			if (tmp.length() > 0)
				wordList.add(tmp);
			start = end;
		}

		return (String[]) wordList.toArray(new String[wordList.size()]);
	}
	
	/**
	 * 取得指定长度的随机字符串
	 * @param length
	 * @return
	 */
	public static final String randomString(int length) {
		if (length < 1)
			return null;
		char randBuffer[] = new char[length];
		for (int i = 0; i < randBuffer.length; i++)
			randBuffer[i] = numbersAndLetters[randGen.nextInt(71)];

		return new String(randBuffer);
	}

	public static final String chopAtWord(String string, int length,
			int minLength) {
		if (length < 2)
			throw new IllegalArgumentException("Length specified (" + length
					+ ") must be > 2");
		if (minLength >= length)
			throw new IllegalArgumentException(
					"minLength must be smaller than length");
		int sLength = string != null ? string.length() : -1;
		if (sLength < 1)
			return string;
		if (minLength != -1 && sLength < minLength)
			return string;
		if (minLength == -1 && sLength < length)
			return string;
		char charArray[] = string.toCharArray();
		if (sLength > length) {
			sLength = length;
			for (int i = 0; i < sLength - 1; i++) {
				if (charArray[i] == '\r' && charArray[i + 1] == '\n')
					return string.substring(0, i + 1);
				if (charArray[i] == '\n')
					return string.substring(0, i);
			}

			if (charArray[sLength - 1] == '\n')
				return string.substring(0, sLength - 1);
			for (int i = sLength - 1; i > 0; i--)
				if (charArray[i] == ' ')
					return string.substring(0, i).trim();

		} else if (minLength != -1 && sLength > minLength) {
			for (int i = 0; i < minLength; i++)
				if (charArray[i] == ' ')
					return string;

		}
		if (minLength > -1 && minLength <= string.length())
			return string.substring(0, minLength);
		else
			return string.substring(0, length);
	}

	public static final String chopAtWord(String string, int length) {
		return chopAtWord(string, length, -1);
	}

	public static String chopAtWordsAround(String input, String wordList[],
			int numChars) {
		if (input == null || "".equals(input.trim()) || wordList == null
				|| wordList.length == 0 || numChars == 0)
			return "";
		String lc = input.toLowerCase();
		for (int i = 0; i < wordList.length; i++) {
			int pos = lc.indexOf(wordList[i]);
			if (pos > -1) {
				int beginIdx = pos - numChars;
				if (beginIdx < 0)
					beginIdx = 0;
				int endIdx = pos + numChars;
				if (endIdx > input.length() - 1)
					endIdx = input.length() - 1;
				char chars[];
				for (chars = input.toCharArray(); beginIdx > 0
						&& chars[beginIdx] != ' ' && chars[beginIdx] != '\n'
						&& chars[beginIdx] != '\r'; beginIdx--)
					;
				for (; endIdx < input.length() && chars[endIdx] != ' '
						&& chars[endIdx] != '\n' && chars[endIdx] != '\r'; endIdx++)
					;
				return input.substring(beginIdx, endIdx);
			}
		}

		return input.substring(0, input.length() < 200 ? input.length() : 200);
	}

	public static String wordWrap(String input, int width, Locale locale) {
		if (input == null)
			return "";
		if (width < 5)
			return input;
		if (width >= input.length())
			return input;
		StringBuffer buf = new StringBuffer(input);
		boolean endOfLine = false;
		int lineStart = 0;
		for (int i = 0; i < buf.length(); i++) {
			if (buf.charAt(i) == '\n') {
				lineStart = i + 1;
				endOfLine = true;
			}
			if (i <= (lineStart + width) - 1)
				continue;
			if (!endOfLine) {
				int limit = i - lineStart - 1;
				BreakIterator breaks = BreakIterator.getLineInstance(locale);
				breaks.setText(buf.substring(lineStart, i));
				int end = breaks.last();
				if (end == limit + 1
						&& !Character.isWhitespace(buf.charAt(lineStart + end)))
					end = breaks.preceding(end - 1);
				if (end != -1 && end == limit + 1) {
					buf.replace(lineStart + end, lineStart + end + 1, "\n");
					lineStart += end;
					continue;
				}
				if (end != -1 && end != 0) {
					buf.insert(lineStart + end, '\n');
					lineStart = lineStart + end + 1;
				} else {
					buf.insert(i, '\n');
					lineStart = i + 1;
				}
			} else {
				buf.insert(i, '\n');
				lineStart = i + 1;
				endOfLine = false;
			}
		}

		return buf.toString();
	}

	public static final String highlightWords(String string, String words[],
			String startHighlight, String endHighlight) {
		if (string == null || words == null || startHighlight == null
				|| endHighlight == null)
			return null;
		StringBuffer regexp = new StringBuffer();
		regexp.append("(?i)\\b(");
		for (int x = 0; x < words.length; x++) {
			words[x] = words[x].replaceAll("([\\$\\?\\|\\/\\.])", "\\\\$1");
			regexp.append(words[x]);
			if (x != words.length - 1)
				regexp.append("|");
		}

		regexp.append(")");
		return string.replaceAll(regexp.toString(), startHighlight + "$1"
				+ endHighlight);
	}

	public static final String escapeForXML(String string) {
		if (string == null)
			return null;
		int i = 0;
		int last = 0;
		char input[] = string.toCharArray();
		int len = input.length;
		StringBuffer out = new StringBuffer((int) ((double) len * 1.3D));
		for (; i < len; i++) {
			char ch = input[i];
			if (ch > '>')
				continue;
			if (ch == '<') {
				if (i > last)
					out.append(input, last, i - last);
				last = i + 1;
				out.append(LT_ENCODE);
				continue;
			}
			if (ch == '&') {
				if (i > last)
					out.append(input, last, i - last);
				last = i + 1;
				out.append(AMP_ENCODE);
				continue;
			}
			if (ch == '"') {
				if (i > last)
					out.append(input, last, i - last);
				last = i + 1;
				out.append(QUOTE_ENCODE);
				continue;
			}
			if (ch == '\n' || ch == '\r' || ch == '\t' || ch >= ' ')
				continue;
			if (i > last)
				out.append(input, last, i - last);
			last = i + 1;
		}

		if (last == 0)
			return string;
		if (i > last)
			out.append(input, last, i - last);
		return out.toString();
	}

	public static final String unescapeFromXML(String string) {
		string = replace(string, "&lt;", "<");
		string = replace(string, "&gt;", ">");
		string = replace(string, "&quot;", "\"");
		return replace(string, "&amp;", "&");
	}

	public static final String zeroPadString(String string, int length) {
		if (string == null || string.length() > length) {
			return string;
		} else {
			StringBuffer buf = new StringBuffer(length);
			buf.append(zeroArray, 0, length - string.length()).append(string);
			return buf.toString();
		}
	}

	public static final String dateToMillis(Date date) {
		return Long.toString(date.getTime());
	}
	
	/**
	 * 检验是否有效的邮件地址
	 * @param addr
	 * @return
	 */
	public static boolean isValidEmailAddress(String addr) {
		if (addr == null)
			return false;
		addr = addr.trim();
		if (addr.length() == 0)
			return false;
		Matcher matcher = basicAddressPattern.matcher(addr);
		if (!matcher.matches())
			return false;
		String userPart = matcher.group(1);
		String domainPart = matcher.group(2);
		matcher = validUserPattern.matcher(userPart);
		if (!matcher.matches())
			return false;
		matcher = ipDomainPattern.matcher(domainPart);
		if (matcher.matches()) {
			for (int i = 1; i < 5; i++) {
				String num = matcher.group(i);
				if (num == null)
					return false;
				if (Integer.parseInt(num) > 254)
					return false;
			}

			return true;
		}
		matcher = domainPattern.matcher(domainPart);
		if (matcher.matches()) {
			String tld = matcher.group(matcher.groupCount());
			matcher = tldPattern.matcher(tld);
			return tld.length() == 3 || matcher.matches();
		} else {
			return false;
		}
	}

	public static String keyStroke2String(KeyStroke key) {
		StringBuffer s = new StringBuffer(50);
		int m = key.getModifiers();
		if ((m & 0x41) != 0)
			s.append("shift ");
		if ((m & 0x82) != 0)
			s.append("ctrl ");
		if ((m & 0x104) != 0)
			s.append("meta ");
		if ((m & 0x208) != 0)
			s.append("alt ");
		if ((m & 0x410) != 0)
			s.append("button1 ");
		if ((m & 0x808) != 0)
			s.append("button2 ");
		if ((m & 0x1004) != 0)
			s.append("button3 ");
		switch (key.getKeyEventType()) {
		case 400:
			s.append("typed ");
			s.append(key.getKeyChar() + " ");
			break;

		case 401:
			s.append("pressed ");
			s.append(getKeyText(key.getKeyCode()) + " ");
			break;

		case 402:
			s.append("released ");
			s.append(getKeyText(key.getKeyCode()) + " ");
			break;

		default:
			s.append("unknown-event-type");
			break;
		}
		return s.toString();
	}

	public static String getKeyText(int keyCode) {
		if (keyCode >= 48 && keyCode <= 57 || keyCode >= 65 && keyCode <= 90)
			return String.valueOf((char) keyCode);
		switch (keyCode) {
		case 44: // ','
			return "COMMA";

		case 46: // '.'
			return "PERIOD";

		case 47: // '/'
			return "SLASH";

		case 59: // ';'
			return "SEMICOLON";

		case 61: // '='
			return "EQUALS";

		case 91: // '['
			return "OPEN_BRACKET";

		case 92: // '\\'
			return "BACK_SLASH";

		case 93: // ']'
			return "CLOSE_BRACKET";

		case 10: // '\n'
			return "ENTER";

		case 8: // '\b'
			return "BACK_SPACE";

		case 9: // '\t'
			return "TAB";

		case 3: // '\003'
			return "CANCEL";

		case 12: // '\f'
			return "CLEAR";

		case 16: // '\020'
			return "SHIFT";

		case 17: // '\021'
			return "CONTROL";

		case 18: // '\022'
			return "ALT";

		case 19: // '\023'
			return "PAUSE";

		case 20: // '\024'
			return "CAPS_LOCK";

		case 27: // '\033'
			return "ESCAPE";

		case 32: // ' '
			return "SPACE";

		case 33: // '!'
			return "PAGE_UP";

		case 34: // '"'
			return "PAGE_DOWN";

		case 35: // '#'
			return "END";

		case 36: // '$'
			return "HOME";

		case 37: // '%'
			return "LEFT";

		case 38: // '&'
			return "UP";

		case 39: // '\''
			return "RIGHT";

		case 40: // '('
			return "DOWN";

		case 106: // 'j'
			return "MULTIPLY";

		case 107: // 'k'
			return "ADD";

		case 108: // 'l'
			return "SEPARATOR";

		case 109: // 'm'
			return "SUBTRACT";

		case 110: // 'n'
			return "DECIMAL";

		case 111: // 'o'
			return "DIVIDE";

		case 127: // '\177'
			return "DELETE";

		case 144:
			return "NUM_LOCK";

		case 145:
			return "SCROLL_LOCK";

		case 112: // 'p'
			return "F1";

		case 113: // 'q'
			return "F2";

		case 114: // 'r'
			return "F3";

		case 115: // 's'
			return "F4";

		case 116: // 't'
			return "F5";

		case 117: // 'u'
			return "F6";

		case 118: // 'v'
			return "F7";

		case 119: // 'w'
			return "F8";

		case 120: // 'x'
			return "F9";

		case 121: // 'y'
			return "F10";

		case 122: // 'z'
			return "F11";

		case 123: // '{'
			return "F12";

		case 61440:
			return "F13";

		case 61441:
			return "F14";

		case 61442:
			return "F15";

		case 61443:
			return "F16";

		case 61444:
			return "F17";

		case 61445:
			return "F18";

		case 61446:
			return "F19";

		case 61447:
			return "F20";

		case 61448:
			return "F21";

		case 61449:
			return "F22";

		case 61450:
			return "F23";

		case 61451:
			return "F24";

		case 154:
			return "PRINTSCREEN";

		case 155:
			return "INSERT";

		case 156:
			return "HELP";

		case 157:
			return "META";

		case 192:
			return "BACK_QUOTE";

		case 222:
			return "QUOTE";

		case 224:
			return "KP_UP";

		case 225:
			return "KP_DOWN";

		case 226:
			return "KP_LEFT";

		case 227:
			return "KP_RIGHT";

		case 128:
			return "DEAD_GRAVE";

		case 129:
			return "DEAD_ACUTE";

		case 130:
			return "DEAD_CIRCUMFLEX";

		case 131:
			return "DEAD_TILDE";

		case 132:
			return "DEAD_MACRON";

		case 133:
			return "DEAD_BREVE";

		case 134:
			return "DEAD_ABOVEDOT";

		case 135:
			return "DEAD_DIAERESIS";

		case 136:
			return "DEAD_ABOVERING";

		case 137:
			return "DEAD_DOUBLEACUTE";

		case 138:
			return "DEAD_CARON";

		case 139:
			return "DEAD_CEDILLA";

		case 140:
			return "DEAD_OGONEK";

		case 141:
			return "DEAD_IOTA";

		case 142:
			return "DEAD_VOICED_SOUND";

		case 143:
			return "DEAD_SEMIVOICED_SOUND";

		case 150:
			return "AMPERSAND";

		case 151:
			return "ASTERISK";

		case 152:
			return "QUOTEDBL";

		case 153:
			return "LESS";

		case 160:
			return "GREATER";

		case 161:
			return "BRACELEFT";

		case 162:
			return "BRACERIGHT";

		case 512:
			return "AT";

		case 513:
			return "COLON";

		case 514:
			return "CIRCUMFLEX";

		case 515:
			return "DOLLAR";

		case 516:
			return "EURO_SIGN";

		case 517:
			return "EXCLAMATION_MARK";

		case 518:
			return "INVERTED_EXCLAMATION_MARK";

		case 519:
			return "LEFT_PARENTHESIS";

		case 520:
			return "NUMBER_SIGN";

		case 45: // '-'
			return "MINUS";

		case 521:
			return "PLUS";

		case 522:
			return "RIGHT_PARENTHESIS";

		case 523:
			return "UNDERSCORE";

		case 24: // '\030'
			return "FINAL";

		case 28: // '\034'
			return "CONVERT";

		case 29: // '\035'
			return "NONCONVERT";

		case 30: // '\036'
			return "ACCEPT";

		case 31: // '\037'
			return "MODECHANGE";

		case 21: // '\025'
			return "KANA";

		case 25: // '\031'
			return "KANJI";

		case 240:
			return "ALPHANUMERIC";

		case 241:
			return "KATAKANA";

		case 242:
			return "HIRAGANA";

		case 243:
			return "FULL_WIDTH";

		case 244:
			return "HALF_WIDTH";

		case 245:
			return "ROMAN_CHARACTERS";

		case 256:
			return "ALL_CANDIDATES";

		case 257:
			return "PREVIOUS_CANDIDATE";

		case 258:
			return "CODE_INPUT";

		case 259:
			return "JAPANESE_KATAKANA";

		case 260:
			return "JAPANESE_HIRAGANA";

		case 261:
			return "JAPANESE_ROMAN";

		case 262:
			return "KANA_LOCK";

		case 263:
			return "INPUT_METHOD_ON_OFF";

		case 65481:
			return "AGAIN";

		case 65483:
			return "UNDO";

		case 65485:
			return "COPY";

		case 65487:
			return "PASTE";

		case 65489:
			return "CUT";

		case 65488:
			return "FIND";

		case 65482:
			return "PROPS";

		case 65480:
			return "STOP";

		case 65312:
			return "COMPOSE";

		case 65406:
			return "ALT_GRAPH";
		}
		if (keyCode >= 96 && keyCode <= 105) {
			char c = (char) ((keyCode - 96) + 48);
			return "NUMPAD" + c;
		} else {
			return "unknown(0x" + Integer.toString(keyCode, 16) + ")";
		}
	}

	private static final char QUOTE_ENCODE[] = "&quot;".toCharArray();

	private static final char AMP_ENCODE[] = "&amp;".toCharArray();

	private static final char LT_ENCODE[] = "&lt;".toCharArray();

	private static final char GT_ENCODE[] = "&gt;".toCharArray();

	private static Pattern basicAddressPattern;

	private static Pattern validUserPattern;

	private static Pattern domainPattern;

	private static Pattern ipDomainPattern;

	private static Pattern tldPattern;

	private static MessageDigest digest = null;

	private static final BitSet allowed_query;

	private static final int fillchar = 61;

	private static final String cvt = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";

	private static Random randGen = new Random();

	private static char numbersAndLetters[] = "0123456789abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ"
			.toCharArray();

	private static final char zeroArray[] = "0000000000000000000000000000000000000000000000000000000000000000"
			.toCharArray();

	static {
		String basicAddress = "^([\\w\\.-]+)@([\\w\\.-]+)$";
		String specialChars = "\\(\\)><@,;:\\\\\\\"\\.\\[\\]";
		String validChars = "[^ \f\n\r\t" + specialChars + "]";
		String atom = validChars + "+";
		String quotedUser = "(\"[^\"]+\")";
		String word = "(" + atom + "|" + quotedUser + ")";
		String validUser = "^" + word + "(\\." + word + ")*$";
		String domain = "^" + atom + "(\\." + atom + ")+$";
		String ipDomain = "^(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})$";
		String knownTLDs = "^\\.(com|net|org|edu|int|mil|gov|arpa|biz|aero|name|coop|info|pro|museum)$";
		basicAddressPattern = Pattern.compile(basicAddress, 2);
		validUserPattern = Pattern.compile(validUser, 2);
		domainPattern = Pattern.compile(domain, 2);
		ipDomainPattern = Pattern.compile(ipDomain, 2);
		tldPattern = Pattern.compile(knownTLDs, 2);
		allowed_query = new BitSet(256);
		for (int i = 48; i <= 57; i++)
			allowed_query.set(i);

		for (int i = 97; i <= 122; i++)
			allowed_query.set(i);

		for (int i = 65; i <= 90; i++)
			allowed_query.set(i);

		allowed_query.set(45);
		allowed_query.set(95);
		allowed_query.set(46);
		allowed_query.set(33);
		allowed_query.set(126);
		allowed_query.set(42);
		allowed_query.set(39);
		allowed_query.set(40);
		allowed_query.set(41);
	}
	
	/**
	 * 在字符串的前面用指定的字符填充，使之达到固定长度
	 * @param mainStr 主字符串
	 * @param padStr 填充字符
	 * @param len 要求填充后的长度
	 * @return
	 */
	public static String padPrefix(String mainStr, String padStr, int len) {
		if (mainStr == null) return null;
		if (padStr == null) padStr = "0";
		
		int mainLen =  mainStr.length();
		int padLen = padStr.length();
		
		if (mainLen >= len) return mainStr;
		
		int padCount = (len - mainLen)/padLen;
		if ((len - mainLen) % padLen >0) {
			padCount++;
		}
		
		String prefix = "";
		for (int i=0; i<padCount; i++) {
			prefix = padStr + prefix;
		}
		
		String result = prefix + mainStr;
		if (result.length() > len) {
			result = result.substring(result.length()-len);
		}
		return result;
	}
	
	/**
     * Splits a String on a delimiter into a List of Strings.
     * @param str the String to split
     * @param delim the delimiter character(s) to join on (null will split on whitespace)
     * @return a list of Strings
     */
    public static List<String> split(String str, String delim) {
        List<String> splitList = null;
        StringTokenizer st = null;

        if (str == null)
            return splitList;

        if (delim != null)
            st = new StringTokenizer(str, delim);
        else
            st = new StringTokenizer(str);

        if (st != null && st.hasMoreTokens()) {
            splitList = new ArrayList<String>();

            while (st.hasMoreTokens())
                splitList.add(st.nextToken());
        }
        
        return splitList;
    }
    
	public static void main(String[] args) {
		String result = padPrefix("1234", "0", 12);
		System.out.println("result str: " + result);
        
        String fld = "partyLocation.location.region";
        List<String> flds = split(fld, ".");
        log.info("size: " + flds.size());
        for (String s : flds) {
            log.info(s);
        }
	}
}
