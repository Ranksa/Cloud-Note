package com.zxl.tsc.util;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class Pinyin4jUtil {
	private static Logger logger = LoggerFactory.getLogger(Pinyin4jUtil.class);

	/**
	 * 字符串集合转换字符串(逗号分隔)
	 * 
	 * @param stringSet
	 * @return
	 */
	public static String makeStringByStringSet(Set<String> stringSet) {
		StringBuilder str = null;
		try {
			logger.debug("set to string start!");
			str = new StringBuilder();
			int i = 0;
			for (String s : stringSet) {
				if (i == stringSet.size() - 1) {
					str.append(s);
				} else {
					str.append(s + ",");
				}
				i++;
			}
		} catch (Exception e) {
			logger.error("set to string error!" + stringSet.toString(), e);
		}
		return str.toString().toLowerCase();
	}

	/**
	 * 获取拼音集合
	 * 
	 * @author wyh
	 * @param src
	 * @return Set<String>
	 */
	public static Set<String> getPinyin(String src) {
		if (src != null && !src.trim().equalsIgnoreCase("")) {
			try {
				logger.debug("getPinyin start!");
				char[] srcChar;
				srcChar = src.toCharArray();
				// 汉语拼音格式输出类
				HanyuPinyinOutputFormat hanYuPinOutputFormat = new HanyuPinyinOutputFormat();
				// 输出设置，大小写，音标方式等
				hanYuPinOutputFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
				hanYuPinOutputFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
				hanYuPinOutputFormat.setVCharType(HanyuPinyinVCharType.WITH_V);
				String[][] temp = new String[src.length()][];
				for (int i = 0; i < srcChar.length; i++) {
					char c = srcChar[i];
					// 是中文或者a-z或者A-Z转换拼音(我的需求，是保留中文或者a-z或者A-Z)
					if (String.valueOf(c).matches("[\\u4E00-\\u9FA5]+")) {
						try {
							temp[i] = PinyinHelper.toHanyuPinyinStringArray(
									srcChar[i], hanYuPinOutputFormat);
						} catch (BadHanyuPinyinOutputFormatCombination e) {
							e.printStackTrace();
						}
					} else if (((int) c >= 65 && (int) c <= 90)
							|| ((int) c >= 97 && (int) c <= 122)) {
						temp[i] = new String[] { String.valueOf(srcChar[i]) };
					} else {
						temp[i] = new String[] { "" };
					}
				}
				String[] pingyinArray = Exchange(temp);
				Set<String> pinyinSet = new HashSet<String>();
				for (int i = 0; i < pingyinArray.length; i++) {
					pinyinSet.add(pingyinArray[i]);
				}
				return pinyinSet;
			} catch (Exception e) {
				logger.error("getPinyin error!"+src,e);
			}
		}
		return null;
	}

	/**
	 * 获取拼音数组第一个
	 * 
	 * @author wyh
	 * @param src
	 * @return Set<String>
	 */
	public static String getPinyinFirst(String src) {
		if (src != null && !src.trim().equalsIgnoreCase("")) {
			try {
				logger.debug("getPinyinFirst start!");
				char[] srcChar;
				srcChar = src.toCharArray();
				// 汉语拼音格式输出类
				HanyuPinyinOutputFormat hanYuPinOutputFormat = new HanyuPinyinOutputFormat();

				// 输出设置，大小写，音标方式等
				hanYuPinOutputFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
				hanYuPinOutputFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
				hanYuPinOutputFormat.setVCharType(HanyuPinyinVCharType.WITH_V);

				String[][] temp = new String[src.length()][];
				for (int i = 0; i < srcChar.length; i++) {
					char c = srcChar[i];
					// 是中文或者a-z或者A-Z转换拼音(我的需求，是保留中文或者a-z或者A-Z)
					if (String.valueOf(c).matches("[\\u4E00-\\u9FA5]+")) {
						try {
							temp[i] = PinyinHelper.toHanyuPinyinStringArray(
									srcChar[i], hanYuPinOutputFormat);
						} catch (BadHanyuPinyinOutputFormatCombination e) {
							e.printStackTrace();
						}
					} else if (((int) c >= 65 && (int) c <= 90)
							|| ((int) c >= 97 && (int) c <= 122)) {
						temp[i] = new String[] { String.valueOf(srcChar[i]) };
					} else {
						temp[i] = new String[] { "" };
					}
				}
				String[] pingyinArray = Exchange(temp);
				return pingyinArray[0];
			} catch (Exception e) {
				logger.error("getPinyinFirst error!"+src,e);
			}
		}
		return null;
	}

	/**
	 * 递归
	 * 
	 * @author wyh
	 * @param strJaggedArray
	 * @return
	 */
	public static String[] Exchange(String[][] strJaggedArray) {
		String[][] temp = DoExchange(strJaggedArray);
		return temp[0];
	}

	/**
	 * 递归
	 * 
	 * @author wyh
	 * @param strJaggedArray
	 * @return
	 */
	private static String[][] DoExchange(String[][] strJaggedArray) {
			int len = strJaggedArray.length;
			if (len >= 2) {
				int len1 = strJaggedArray[0].length;
				int len2 = strJaggedArray[1].length;
				int newlen = len1 * len2;
				String[] temp = new String[newlen];
				int Index = 0;
				for (int i = 0; i < len1; i++) {
					for (int j = 0; j < len2; j++) {
						temp[Index] = strJaggedArray[0][i] + strJaggedArray[1][j];
						Index++;
					}
				}
				String[][] newArray = new String[len - 1][];
				for (int i = 2; i < len; i++) {
					newArray[i - 1] = strJaggedArray[i];
				}
				newArray[0] = temp;
				return DoExchange(newArray);
			} else {
				return strJaggedArray;
			}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String str = "单田芳";
		System.out.println(makeStringByStringSet(getPinyin(str)));

	}

}
