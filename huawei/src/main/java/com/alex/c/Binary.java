package com.alex.c;

import java.util.Arrays;
import java.util.Scanner;

public class Binary {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        int[] cnts = Arrays.stream(sc.nextLine().split(" ")).mapToInt(Integer::parseInt).toArray();
        int h = Integer.parseInt(sc.nextLine());

        System.out.println(getResult(cnts, h));
    }


    /**
     * @param cnts 数组数量表示桃树的数量，每个数字表明每棵桃树上蟠桃的数量
     * @param h    守卫离开的时间h
     * @return int
     * @description
     */
    public static int getResult(int[] cnts, int h) {
        // 桃树的数量
        int treeNums = cnts.length;
        if (treeNums > h) {
            return 0;
        }
        int max = Arrays.stream(cnts).max().orElse(0);
        if (treeNums == h) {
            return max;
        }

        // 如果只有1棵桃树，且这颗树上只有1个桃，那么吃桃速度可以是1
        int min = 1;

        // 当桃树数量少于h棵时，以max速度吃桃肯定可以吃完，但是不一定是最优解
        int ans = max;
        while (min <= max) {
            // 取中间值作为吃桃速度进行尝试
            int mid = (min + max) >> 1;

            // 如果以mid速度，可以在h小时内吃完cnts所有桃，那么mid就是一个可能解
            if (check(mid, h, cnts)) {
                ans = mid;
                // 继续尝试更小的速度
                max = mid - 1;
            } else {
                // 以mid速度无法在h小时内吃完cnts所有桃，那么mid就取小了，下次应该取更大的吃桃速度
                min = mid + 1;
            }
        }

        return ans;

    }

    public static boolean check(int speed, int limit, int[] cnts) {
        // 已花费时间
        int cost = 0;

        for (int cnt : cnts) {
            // 以speed速度吃完一颗桃树需要的时间，累加进cost
            cost += cnt / speed + (cnt % speed > 0 ? 1 : 0);

            // 如果已花费时间超过了limit限制，那么说明无法以speed速度在limit时间内吃完所有桃树，此时可以直接返回false
            if (cost > limit) {
                return false;
            }
        }

        // 可以以speed速度，在limit小时内吃完所有cnts桃树
        return true;
    }


}
