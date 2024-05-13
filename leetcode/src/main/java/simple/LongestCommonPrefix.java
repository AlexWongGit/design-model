package simple;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @description 最长公共前缀
 * @author wangzf
 * @date 2024/5/7
 */
public class LongestCommonPrefix {

    public static void main(String[] args) {
        String str1 = "dasfdsfdsfs";
        String str2 = "dasfdsfd6fs";
        String[] strings = {str1, str2};
        String prefix = getLongestCommonPrefix(strings);
        System.out.println(prefix);
    }

    private static String getLongestCommonPrefix(String[] strs) {
        int len = Integer.MAX_VALUE ;
        String prefix = "";
        ArrayList<char[]> arrayList = new ArrayList<>();
        for (int i = 0; i < strs.length; i++) {
            char[] chars = strs[i].toCharArray();
            if (chars.length == 0) {
                return prefix;
            }
            if (len > chars.length) {
                len = chars.length;
            }
            arrayList.add(chars);
        }

        for (int i = 0; i < len; i++) {
            int finalI = i;
            Set<Character> characters = arrayList.parallelStream().map(f -> f[finalI]).collect(Collectors.toSet());
            if (characters.size() == 1) {
                prefix += arrayList.get(0)[i];
            } else {
                break;
            }
        }
        return prefix;

    }

    private static String getLongestCommonPrefixBest(String[] strs) {
        int res=0;
        int minLength=Integer.MAX_VALUE;
        for(int i=0;i<strs.length;i++){
            if(strs[i].length()<minLength){
                minLength=strs[i].length();
            }
        }
        while(res<minLength){
            char e=strs[0].charAt(res);
            Boolean flag=true;
            for(String s:strs){
                if(s.charAt(res)!=e){
                    flag=false;
                    break;
                }
            }
            if(flag){
                res++;
            }else{
                break;
            }
        }
        return strs[0].substring(0,res);

    }
}
