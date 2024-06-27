package simple;

import org.apache.poi.hssf.record.DVALRecord;

import java.util.HashSet;

public class FirstNotRepeatingChar {

    public static void main(String[] args)
    {
        String str = "google";
        System.out.println(firstNotRepeatingChar(str));
    }

    private static int firstNotRepeatingChar(String str) {
        char[] chars = str.toCharArray();
        int length = str.length();
        HashSet<Integer> integers = new HashSet<>();
        for (int i = 0; i < length; i++) {
            char aChar = chars[i];
            String substring = str.substring(i + 1, length);
            if (substring.indexOf(aChar) == -1 && !integers.contains(i)) {
                return i;
            } else {
                integers.add(substring.indexOf(aChar)+i+1);
            }
        }
        return -1;
    }
}
