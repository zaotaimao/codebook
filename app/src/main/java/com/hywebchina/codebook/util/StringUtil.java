package com.hywebchina.codebook.util;

import java.security.MessageDigest;

public class StringUtil {
	public static boolean isBlank(String str){
		return str==null||str.trim().equalsIgnoreCase("");
	}
	
	public static String md5Digest(String src) {           
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("MD5");
			byte[] b = md.digest(src.getBytes("utf-8"));                        
			return byte2HexStr(b).toLowerCase(); 
		} catch (Exception e) {
			e.printStackTrace();
			return src;
		}           
     
	}
	
	private static String byte2HexStr(byte[] b) {
		StringBuilder sb = new StringBuilder();           
		for (int i = 0; i < b.length; i++) {               
			String s = Integer.toHexString(b[i] & 0xFF);               
			if (s.length() == 1) {                    
				sb.append("0");               
			}                                  
			sb.append(s.toUpperCase());            
		}                           
		return sb.toString();       
	}
}