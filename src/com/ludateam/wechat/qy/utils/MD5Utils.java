package com.ludateam.wechat.qy.utils;

import java.security.MessageDigest;

public class MD5Utils {

	public static String encodeString(String origin) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return byteArrayToString(md.digest(origin.getBytes()));
	}

	/**
	 * 转换字节数组为16进制字符串或者全数组字符串
	 * 
	 * @param b
	 *            字节数组
	 * @return 16进制字串
	 */
	private static String byteArrayToString(byte[] b) {
		StringBuffer resultSb = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			byte aB = b[i];
			resultSb.append(byteToHexString(aB));
		}
		return resultSb.toString();
	}

	/**
	 * 十六进制字符
	 */
	private final static String[] hexDigits = { "0", "1", "2", "3", "4", "5",
			"6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

	/**
	 * 转换字节数组为十六进制字符串
	 * 
	 * @param b
	 *            byte
	 * @return String
	 */
	private static String byteToHexString(byte b) {
		int n = b;
		if (n < 0) {
			n = 256 + n;
		}
		int d1 = n / 16;
		int d2 = n % 16;
		return hexDigits[d1] + hexDigits[d2];
	}

	public String encryptPWD(String sPassword) {
		byte cResult[] = new byte[16];
		String sResult = "";
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(("chenlilin.nhdrtpj" + sPassword).getBytes());
			cResult = md.digest();

			for (int i = 0; i < cResult.length; i++) {
				if (cResult[i] < 0)
					cResult[i] += 128;
				String sTemp = Integer.toHexString(cResult[i]).toUpperCase();
				if (cResult[i] < 16)
					sTemp = "0" + sTemp;
				sResult += sTemp;
			}
		} catch (Exception e) {
			sResult = "";
		}
		return sResult;
	}
}