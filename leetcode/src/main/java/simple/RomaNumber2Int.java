package simple;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wangzf
 * @description 罗马数字包含以下七种字符: I， V， X， L，C，D 和 M。
 * <p>
 * 字符          数值
 * I             1
 * V             5
 * X             10
 * L             50
 * C             100
 * D             500
 * M             1000
 * 例如， 罗马数字 2 写做 II ，即为两个并列的 1 。12 写做 XII ，即为 X + II 。 27 写做  XXVII, 即为 XX + V + II 。
 * <p>
 * 通常情况下，罗马数字中小的数字在大的数字的右边。但也存在特例，例如 4 不写做 IIII，而是 IV。数字 1 在数字 5 的左边，所表示的数等于大数 5 减小数 1 得到的数值 4 。同样地，数字 9 表示为 IX。这个特殊的规则只适用于以下六种情况：
 * <p>
 * I 可以放在 V (5) 和 X (10) 的左边，来表示 4 和 9。
 * X 可以放在 L (50) 和 C (100) 的左边，来表示 40 和 90。
 * C 可以放在 D (500) 和 M (1000) 的左边，来表示 400 和 900。
 * 给定一个罗马数字，将其转换成整数。
 * @date 2024/5/7
 */
public class RomaNumber2Int {

    public static void main(String[] args) {
        String s = "IV";
        Integer res = getIntByRomaNumber(s);
        System.out.println(res);
    }

    private static Integer getIntByRomaNumber(String romaNumber) {
        Map<String, Integer> dict = new HashMap<>(7);
        dict.put("I", 1);
        dict.put("V", 5);
        dict.put("X", 10);
        dict.put("L", 50);
        dict.put("C", 100);
        dict.put("D", 500);
        dict.put("M", 1000);
        char[] chars = romaNumber.toCharArray();
        int res = 0;
        int preValue = 0;
        for (int i = chars.length - 1; i >= 0; i--) {
            int num = dict.get(String.valueOf(chars[i]));
            if (num < preValue) {
                res -= num;
            } else {
                res += num;
            }
            preValue = num;
        }
        return res;
    }

    private static Integer getIntByRomaNumber2(String s) {
        char[] chs = s.toCharArray();
        int len = chs.length;
        int sum = 0;
        for(int i=0;i<len;i++){
            if(chs[i]=='I'){
                if(i+1<len&&chs[i+1]=='V'){
                    sum+=4;
                    i++;
                }
                else if(i+1<len&&chs[i+1]=='X'){
                    sum+=9;
                    i++;
                }
                else sum+=1;
            }else if(chs[i]=='X'){
                if(i+1<len&&chs[i+1]=='L'){
                    sum+=40;
                    i++;
                }
                else if(i+1<len&&chs[i+1]=='C'){
                    sum+=90;
                    i++;
                }
                else sum+=10;
            }else if(chs[i]=='C'){
                if(i+1<len&&chs[i+1]=='D'){
                    sum+=400;
                    i++;
                }
                else if(i+1<len&&chs[i+1]=='M'){
                    sum+=900;
                    i++;
                }
                else sum+=100;
            }else if(chs[i]=='V')sum+=5;
            else if(chs[i]=='L')sum+=50;
            else if(chs[i]=='D')sum+=500;
            else if(chs[i]=='M')sum+=1000;
        }
        return sum;
    }



}
