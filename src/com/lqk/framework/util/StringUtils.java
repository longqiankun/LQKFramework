package com.lqk.framework.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;
import android.widget.TextView;

/**
 * 字符串帮助类
 * 
 * @author gdpancheng@gmail.com 2013-10-22 下午1:09:14
 */
public class StringUtils {
	public static final int TYPE_NUMBER = 0;
	public static final int TYPE_ENGLISH = 1;
	public static final int TYPE_FUHAO = 2;
	public static final int TYPE_CHINA = 3;

	// --------------------------------------------------------------------------------------------
	// 对于字符串 添加0或者空格的处理
	public static String addPrefix(int num, String prefix) {
		return num < 10 ? prefix + num : String.valueOf(num);
	}

	public static String addPrefix(String numStr, String prefix) {
		int num = Integer.parseInt(numStr);
		return addPrefix(num, prefix);
	}

	public static String addPrefixZero(int num) {
		return addPrefix(num, "0");
	}

	public static String addPrefixZero(String numStr) {
		return addPrefix(numStr, "0");
	}

	public static String addPrefixHtmlSpace(int num) {
		return addPrefix(num, "&nbsp;");
	}

	public static String addPrefixHtmlSpace(String numStr) {
		return addPrefix(numStr, "&nbsp;");
	}

	/**
	 * 数组拼接成字符串 中间以逗号连接
	 * 
	 * @author gdpancheng@gmail.com 2014-2-21 下午5:14:52
	 * @param data
	 *            需要连接的数据
	 * @param symbol
	 *            连接符
	 * @return String
	 */
	public static String commaInt(Object[] data, String symbol) {
		StringBuffer sbf = new StringBuffer();
		for (int i = 0; i < data.length; i++) {
			sbf.append(data[i]);
			if (i < data.length - 1) {
				sbf.append(symbol);
			}
		}
		return sbf.toString();
	}

	/**
	 * 数组拼接成字符串 以逗号隔开
	 * 
	 * @author gdpancheng@gmail.com 2014-2-21 下午5:15:20
	 * @param data
	 * @return String
	 */
	public static String commaInt(Object[] data) {
		return commaInt(data, ",");
	}

	// --------------------------------------------------------------------------------------------
	/**
	 * 判断是否为空
	 * 
	 * @author gdpancheng@gmail.com 2013-10-15 下午10:24:22
	 * @param text
	 * @return boolean
	 */
	public static boolean isNull(String text) {
		if (text == null || text.length() == 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * bytes[]转换成Hex字符串,可用于URL转换，IP地址转换.
	 * */
	public static String bytesToHexString(byte[] bytes) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < bytes.length; i++) {
			String hex = Integer.toHexString(0xFF & bytes[i]);
			if (hex.length() == 1) {
				sb.append('0');
			}
			sb.append(hex);
		}
		return sb.toString();
	}

	public static String prettyBytes(long value) {
		String args[] = { "B", "KB", "MB", "GB", "TB" };
		StringBuilder sb = new StringBuilder();
		int i;
		if (value < 1024L) {
			sb.append(String.valueOf(value));
			i = 0;
		} else if (value < 1048576L) {
			sb.append(String.format("%.1f", value / 1024.0));
			i = 1;
		} else if (value < 1073741824L) {
			sb.append(String.format("%.2f", value / 1048576.0));
			i = 2;
		} else if (value < 1099511627776L) {
			sb.append(String.format("%.3f", value / 1073741824.0));
			i = 3;
		} else {
			sb.append(String.format("%.4f", value / 1099511627776.0));
			i = 4;
		}
		sb.append(' ');
		sb.append(args[i]);
		return sb.toString();
	}

	// --------------------------------------------------------------------------------------------
	/**
	 * 字符串重复多少遍
	 * 
	 * @author gdpancheng@gmail.com 2013-10-16 下午10:43:14
	 * @param str
	 * @param times
	 * @return String
	 */
	public static String repeat(String str, int times) {
		StringBuffer sbf = new StringBuffer();
		for (int i = 0; i < times; i++) {
			sbf.append(str);
		}
		return sbf.toString();
	}

	/**
	 * 获得数组中最长的字符串的长度
	 * 
	 * @author gdpancheng@gmail.com 2013-10-16 下午10:43:02
	 * @param keys
	 * @return int
	 */
	public static int getLargestLengthInArray(String[] keys) {
		int length = 0;
		for (int i = 0; i < keys.length; i++) {
			String key = keys[i];
			if (key.length() > length) {
				length = key.length();
			}
		}
		return length;
	}

	/**
	 * 替换string中的指定字符串
	 * 
	 * @author gdpancheng@gmail.com 2013-10-16 下午10:42:22
	 * @param strSearch
	 * @param replacement
	 * @param body
	 * @return String
	 */
	public static String replaceAllByStringBuffer(String strSearch, String replacement, String body) {
		StringBuffer sbf = new StringBuffer(body);
		int index = 0;
		int offset = 0;
		do {
			index = sbf.indexOf(strSearch, offset);
			if (index > -1) {
				sbf.replace(index, index + strSearch.length(), replacement);
				/**
				 * 下一次开始的点是index加上置换后的字符串的长度
				 */
				offset = index + replacement.length();
			}
		} while (index > -1);

		return sbf.toString();
	}

	/**
	 * 判断 char c 是汉字还是数字 还是字母
	 * 
	 * @author gdpancheng@gmail.com 2013-10-16 下午10:41:15
	 * @param c
	 * @return int
	 */
	public static int sepMarkNot(char c) {
		// 数字 48-57
		if (c > 47 && c < 58) {
			return TYPE_NUMBER;
		}
		// 大写字母 65-90
		if (c > 64 && c < 91) {
			return TYPE_ENGLISH;
		}
		// 小写字母 97-122
		if (c > 96 && c < 122) {
			return TYPE_ENGLISH;
		}
		// 汉字（简体）
		if (c >= 0x4e00 && c <= 0x9fbb) {
			return TYPE_CHINA;
		}
		return TYPE_FUHAO;
	}

	/**
	 * 判断字节数 汉字2个字节英文1个字节
	 * 
	 * @author gdpancheng@gmail.com 2013-10-16 下午10:43:53
	 * @param content
	 * @return int
	 */
	public static int getLengths(String content) {
		int count = 0;
		for (int i = 0; i < content.length(); i++) {
			if (sepMarkNot(content.charAt(i)) == TYPE_CHINA) {
				count = count + 2;
			} else {
				count = count + 1;
			}
		}
		return count;
	}

	 /**
     * is null or its length is 0 or it is made by space
     * 
     * <pre>
     * isBlank(null) = true;
     * isBlank(&quot;&quot;) = true;
     * isBlank(&quot;  &quot;) = true;
     * isBlank(&quot;a&quot;) = false;
     * isBlank(&quot;a &quot;) = false;
     * isBlank(&quot; a&quot;) = false;
     * isBlank(&quot;a b&quot;) = false;
     * </pre>
     * 
     * @param str
     * @return if string is null or its size is 0 or it is made by space, return true, else return false.
     */
    public static boolean isBlank(String str) {
        return (str == null || str.trim().length() == 0);
    }

    /**
     * is null or its length is 0
     * 
     * <pre>
     * isEmpty(null) = true;
     * isEmpty(&quot;&quot;) = true;
     * isEmpty(&quot;  &quot;) = false;
     * </pre>
     * 
     * @param str
     * @return if string is null or its size is 0, return true, else return false.
     */
    public static boolean isEmpty(String str) {
        return (str == null || str.length() == 0);
    }

    /**
     * compare two string
     * 
     * @param actual
     * @param expected
     * @return
     * @see ObjectUtils#isEquals(Object, Object)
     */
    public static boolean isEquals(String actual, String expected) {
        return ObjectUtils.isEquals(actual, expected);
    }

    /**
     * null string to empty string
     * 
     * <pre>
     * nullStrToEmpty(null) = &quot;&quot;;
     * nullStrToEmpty(&quot;&quot;) = &quot;&quot;;
     * nullStrToEmpty(&quot;aa&quot;) = &quot;aa&quot;;
     * </pre>
     * 
     * @param str
     * @return
     */
    public static String nullStrToEmpty(String str) {
        return (str == null ? "" : str);
    }

    /**
     * capitalize first letter
     * 
     * <pre>
     * capitalizeFirstLetter(null)     =   null;
     * capitalizeFirstLetter("")       =   "";
     * capitalizeFirstLetter("2ab")    =   "2ab"
     * capitalizeFirstLetter("a")      =   "A"
     * capitalizeFirstLetter("ab")     =   "Ab"
     * capitalizeFirstLetter("Abc")    =   "Abc"
     * </pre>
     * 
     * @param str
     * @return
     */
    public static String capitalizeFirstLetter(String str) {
        if (isEmpty(str)) {
            return str;
        }

        char c = str.charAt(0);
        return (!Character.isLetter(c) || Character.isUpperCase(c)) ? str : new StringBuilder(str.length())
                .append(Character.toUpperCase(c)).append(str.substring(1)).toString();
    }

    /**
     * encoded in utf-8
     * 
     * <pre>
     * utf8Encode(null)        =   null
     * utf8Encode("")          =   "";
     * utf8Encode("aa")        =   "aa";
     * utf8Encode("啊啊啊啊")   = "%E5%95%8A%E5%95%8A%E5%95%8A%E5%95%8A";
     * </pre>
     * 
     * @param str
     * @return
     * @throws UnsupportedEncodingException if an error occurs
     */
    public static String utf8Encode(String str) {
        if (!isEmpty(str) && str.getBytes().length != str.length()) {
            try {
                return URLEncoder.encode(str, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("UnsupportedEncodingException occurred. ", e);
            }
        }
        return str;
    }

    /**
     * encoded in utf-8, if exception, return defultReturn
     * 
     * @param str
     * @param defultReturn
     * @return
     */
    public static String utf8Encode(String str, String defultReturn) {
        if (!isEmpty(str) && str.getBytes().length != str.length()) {
            try {
                return URLEncoder.encode(str, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                return defultReturn;
            }
        }
        return str;
    }

    /**
     * get innerHtml from href
     * 
     * <pre>
     * getHrefInnerHtml(null)                                  = ""
     * getHrefInnerHtml("")                                    = ""
     * getHrefInnerHtml("mp3")                                 = "mp3";
     * getHrefInnerHtml("&lt;a innerHtml&lt;/a&gt;")                    = "&lt;a innerHtml&lt;/a&gt;";
     * getHrefInnerHtml("&lt;a&gt;innerHtml&lt;/a&gt;")                    = "innerHtml";
     * getHrefInnerHtml("&lt;a&lt;a&gt;innerHtml&lt;/a&gt;")                    = "innerHtml";
     * getHrefInnerHtml("&lt;a href="baidu.com"&gt;innerHtml&lt;/a&gt;")               = "innerHtml";
     * getHrefInnerHtml("&lt;a href="baidu.com" title="baidu"&gt;innerHtml&lt;/a&gt;") = "innerHtml";
     * getHrefInnerHtml("   &lt;a&gt;innerHtml&lt;/a&gt;  ")                           = "innerHtml";
     * getHrefInnerHtml("&lt;a&gt;innerHtml&lt;/a&gt;&lt;/a&gt;")                      = "innerHtml";
     * getHrefInnerHtml("jack&lt;a&gt;innerHtml&lt;/a&gt;&lt;/a&gt;")                  = "innerHtml";
     * getHrefInnerHtml("&lt;a&gt;innerHtml1&lt;/a&gt;&lt;a&gt;innerHtml2&lt;/a&gt;")        = "innerHtml2";
     * </pre>
     * 
     * @param href
     * @return <ul>
     *         <li>if href is null, return ""</li>
     *         <li>if not match regx, return source</li>
     *         <li>return the last string that match regx</li>
     *         </ul>
     */
    public static String getHrefInnerHtml(String href) {
        if (isEmpty(href)) {
            return "";
        }

        String hrefReg = ".*<[\\s]*a[\\s]*.*>(.+?)<[\\s]*/a[\\s]*>.*";
        Pattern hrefPattern = Pattern.compile(hrefReg, Pattern.CASE_INSENSITIVE);
        Matcher hrefMatcher = hrefPattern.matcher(href);
        if (hrefMatcher.matches()) {
            return hrefMatcher.group(1);
        }
        return href;
    }

/**
     * process special char in html
     * 
     * <pre>
     * htmlEscapeCharsToString(null) = null;
     * htmlEscapeCharsToString("") = "";
     * htmlEscapeCharsToString("mp3") = "mp3";
     * htmlEscapeCharsToString("mp3&lt;") = "mp3<";
     * htmlEscapeCharsToString("mp3&gt;") = "mp3\>";
     * htmlEscapeCharsToString("mp3&amp;mp4") = "mp3&mp4";
     * htmlEscapeCharsToString("mp3&quot;mp4") = "mp3\"mp4";
     * htmlEscapeCharsToString("mp3&lt;&gt;&amp;&quot;mp4") = "mp3\<\>&\"mp4";
     * </pre>
     * 
     * @param source
     * @return
     */
    public static String htmlEscapeCharsToString(String source) {
        return StringUtils.isEmpty(source) ? source : source.replaceAll("&lt;", "<").replaceAll("&gt;", ">")
                .replaceAll("&amp;", "&").replaceAll("&quot;", "\"");
    }

    /**
     * transform half width char to full width char
     * 
     * <pre>
     * fullWidthToHalfWidth(null) = null;
     * fullWidthToHalfWidth("") = "";
     * fullWidthToHalfWidth(new String(new char[] {12288})) = " ";
     * fullWidthToHalfWidth("！＂＃＄％＆) = "!\"#$%&";
     * </pre>
     * 
     * @param s
     * @return
     */
    public static String fullWidthToHalfWidth(String s) {
        if (isEmpty(s)) {
            return s;
        }

        char[] source = s.toCharArray();
        for (int i = 0; i < source.length; i++) {
            if (source[i] == 12288) {
                source[i] = ' ';
                // } else if (source[i] == 12290) {
                // source[i] = '.';
            } else if (source[i] >= 65281 && source[i] <= 65374) {
                source[i] = (char)(source[i] - 65248);
            } else {
                source[i] = source[i];
            }
        }
        return new String(source);
    }

    /**
     * transform full width char to half width char
     * 
     * <pre>
     * halfWidthToFullWidth(null) = null;
     * halfWidthToFullWidth("") = "";
     * halfWidthToFullWidth(" ") = new String(new char[] {12288});
     * halfWidthToFullWidth("!\"#$%&) = "！＂＃＄％＆";
     * </pre>
     * 
     * @param s
     * @return
     */
    public static String halfWidthToFullWidth(String s) {
        if (isEmpty(s)) {
            return s;
        }

        char[] source = s.toCharArray();
        for (int i = 0; i < source.length; i++) {
            if (source[i] == ' ') {
                source[i] = (char)12288;
                // } else if (source[i] == '.') {
                // source[i] = (char)12290;
            } else if (source[i] >= 33 && source[i] <= 126) {
                source[i] = (char)(source[i] + 65248);
            } else {
                source[i] = source[i];
            }
        }
        return new String(source);
    }
	/**
	 * 
	 * 描述: 单位和联系人在一个字符串中，已","分割 针对客户信息中单位和联系人来获取单位名称。
	 * @param name
	 * @return
	 */
public static String getCompanyName(String name){
	if(name.contains(",")){
	return name.substring(0, name.indexOf(","));
	}else{
		return name;
	}
}
/**
 * 
 * 描述: 针对客户信息中单位和联系人来获取联系人。
 * @param name
 * @return
 */
public static String getContactName(String name){
	if(name.contains(",")){
		return name.substring( name.indexOf(",")+1,name.length());
	}else{
		return name;
	}

}
/**
 * 
 * 描述: 获取从0到9的字符串数组
 * @return
 */
public static String[] getArr0To9(){
	 String qus[] = new String[10];  
     for (int i = 0; i < 10; i++) {
     	qus[i]=i+"";
		}
     return qus;
}
/**
 * 
 * 描述:将字符串转换成字符数组
 * @param str
 * @return
 */
public static String[] getStringToSArr(String s){
	if(!TextUtils.isEmpty(s)){
	String str=Integer.valueOf(s)+"";
	String[] sArr=new String[str.length()];
	for (int i = 0; i < str.length(); i++) {
		sArr[i]=String.valueOf(str.charAt(i));
	}
	return sArr;
	}
	return new String[0];
}
/**
 * 从资源文件中获取资源
 * @param context
 * @return
 */
public static String getStringFromResource(Context context,int id){
	Resources rs=context.getResources();
	String str=rs.getString(id);
	return str;
}
/**
 * 拼接url地址，在url中添加_s
 * @param url
 * @return
 */
public static String mergeUrl(String url){
	if(!TextUtils.isEmpty(url)&&url.contains(".")){
	String pre=url.substring(0, url.lastIndexOf("."));
	String after=url.substring(url.lastIndexOf("."), url.length());
	String resultUrl=pre+"_s"+after;
	return resultUrl;
	}else{
		return "";
	}
}
/**
 * @description 判断文本view中是否为空
 * @param context
 * @param tv
 * @return ture:不为空  false : 为空
 */
public static boolean isStrNull(Context context,TextView tv){
	if(tv!=null){
		String text=tv.getText().toString();
		if(!TextUtils.isEmpty(text)){
			return true;
		}else{
			ToastUtils.showToast(context, "输入内容不能为空！");
		}
	}
	return false;
}
}
