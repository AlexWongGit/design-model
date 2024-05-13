package medium;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @description 最长回文
 * @author wangzf
 * @date 2024/5/8
 */
public class LongestPalindrome {

    public static void main(String[] args) {
        String s = "abba";
        String res = getLongestPalindrome(s);
        System.out.println(res);
    }

    private static String getLongestPalindrome(String s) {
        char[] chars = s.toCharArray();
        Map<String, Integer> charMap = new HashMap<>(1);
        Map<Integer, String> charIndexMap = new HashMap<>(1);
        for (char aChar : chars) {
            Integer orDefault = charMap.getOrDefault(String.valueOf(aChar), 0);
            charMap.put(String.valueOf(aChar),orDefault+1);
        }

        return null;
    }
}
