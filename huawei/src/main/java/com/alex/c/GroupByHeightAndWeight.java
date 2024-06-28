package com.alex.c;

import java.util.Arrays;
import java.util.Scanner;
import java.util.StringJoiner;

public class GroupByHeightAndWeight {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int n = sc.nextInt();

        int[] heights = new int[n];
        for (int i = 0; i < n; i++) heights[i] = sc.nextInt();

        int[] weights = new int[n];
        for (int i = 0; i < n; i++) weights[i] = sc.nextInt();

        System.out.println(getResult(n, heights, weights));
    }

    private static String getResult(int n, int[] heights, int[] weights) {
        int[][] students = new int[n][3];

        for (int i = 0; i < n; i++) {
            students[i] = new int[] {heights[i], weights[i], i + 1};
        }

        Arrays.sort(
                students, (a, b) -> a[0] != b[0] ? a[0] - b[0] : a[1] != b[1] ? a[1] - b[1] : a[2] - b[2]);

        StringJoiner sj = new StringJoiner(" ");
        for (int[] student : students) {
            sj.add(student[2] + "");
        }
        return sj.toString();
    }


}
