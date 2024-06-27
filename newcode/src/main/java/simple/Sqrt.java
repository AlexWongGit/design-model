package simple;

public class Sqrt {

    public static void main(String[] args) {

        System.out.println(sqrt(81));
    }

    public static int sqrt (int x) {
        // write code here
        if (x == 0) {
            return 0;
        }
        int left = 1;
        int right = x;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (mid == x / mid) {
                return mid;
            } else if (mid > x / mid) {
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }
        return right;
    }
}
