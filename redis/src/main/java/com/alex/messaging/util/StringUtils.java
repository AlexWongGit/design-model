package com.alex.messaging.util;

/**
 * Utility functions for String operations
 * @author yathiraj
 *
 */
public class StringUtils {

	public static boolean isEmpty(String str) {
		if (str == null || str.equals("")) {
			return true;
		}
		return false;
	}
}
