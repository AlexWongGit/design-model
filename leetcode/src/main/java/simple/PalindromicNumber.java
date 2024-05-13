package simple;

/**
 * @author wangzf
 * @description 回文数
 * @date 2024/5/7
 */
public class PalindromicNumber {

    public static void main(String[] args) {
        int x = 121;

        System.out.println(isPalindromicNumber(x));
    }

    private static boolean isPalindromicNumber(int x) {
        if (x < 0)
            return false;
        int cur = 0 ;
        int num = x;
        while (num != 0) {
            cur = cur * 10 + num % 10;
            num /= 10;
        }
        return cur == num;
    }
}
