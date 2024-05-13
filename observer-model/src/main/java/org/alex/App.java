package org.alex;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        int line = 4;
        int floor = 5;
        int max = 24;
        for (int i = 1; i < floor; i++) {
            for (int j = 1; j < line; j++) {
                int space = (max - 2 * i * j) / 2 + 1;
                printSpace(space);
                print(j);
                System.out.println();
            }
        }
    }

    private static void print(int j) {
        for (int i1 = 0; i1 < j; i1++) {
            System.out.print("*");
            System.out.print(" ");
        }
    }

    private static void printSpace(int j) {
        for (int i1 = 0; i1 < j; i1++) {
            System.out.print(" ");
        }
    }
}
