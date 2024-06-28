package com.alex.c;

public class FiveKeykeyBoard {

    public static void main(String[] args) {
        int[] command = {1, 1, 1};
        int res = print0(command);
        System.out.println(res);

    }

    private static int print0(int[] command) {
        int attach = 0;
        int curLength = 0;
        boolean select = false;
        for (int i = 0; i < command.length; i++) {
            switch (command[i]) {
                case 1:
                    if (select) {
                        curLength = 0;
                    }
                    curLength++;
                    select = false;
                    break;
                case 2:
                    if (select) {
                        attach = curLength;
                    } else {
                        attach = 1;
                    }
                    break;
                case 3:
                    attach = curLength;
                    curLength = 0;
                    select = false;
                    break;
                case 4:
                    if (select) {
                        curLength = 0;
                    }
                    curLength += attach;
                    select = false;
                    break;
                case 5:
                    if (curLength != 0) {
                        select = true;
                    }
                    break;
                default:
            }
        }
        return curLength;
    }

}

