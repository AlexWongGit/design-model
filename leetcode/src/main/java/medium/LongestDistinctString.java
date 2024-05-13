package medium;

import org.apache.commons.lang3.StringUtils;

import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @author wangzf
 * @description 给定一个字符串 s ，请你找出其中不含有重复字符的 最长子串 的长度。
 * @date 2024/5/7
 */
public class LongestDistinctString {
    public static void main(String[] args) {
        String b = "adasfacajsfkjadd";
        System.out.println(b.length());
        int len = getLengthOfStringDistinctBest(b);
        System.out.println(len);
    }

    private static int getLengthOfStringDistinct(String str) {
        char[] chars = str.toCharArray();
        List<String> resultList = new ArrayList<>();
        for (int i = 0; i < chars.length; i++) {
            String res = String.valueOf(chars[i]);
            resultList.add(res);
            for (int j = i + 1; j < chars.length; j++) {
                if (containsChar(res, chars[j])) {
                    resultList.add(res);
                    break;
                }
                res += chars[j];
                resultList.add(res);
            }
        }
        return resultList.stream()
                .max(Comparator.comparing(String::length))
                .map(String::length)
                .orElse(0);
    }

    private static boolean containsChar(String res, char aChar) {

        char[] chars = res.toCharArray();
        for (char c : chars) {
            if (c == aChar) {
                return true;
            }
        }
        return false;
    }


    /**
     * @description 滑动窗口
     * @return int
     * @param s
     */
    private static int getLengthOfStringDistinctBest(String s) {
        // ASCII字符集
        int[] index = new int[128];
        int result = 0;
        int start = 0;

        for (int i = 0; i < s.length(); i++) {
            char currentChar = s.charAt(i);

            //比较上一个字符和当前字符的索引
            start = Math.max(start, index[currentChar]);
            // 存储当前字符下一位置的索引
            index[currentChar] = i + 1;

            result = Math.max(result, i - start + 1);
        }

        return result;
    }
}
