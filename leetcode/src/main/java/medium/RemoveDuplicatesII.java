package medium;

/**
 * 80. 删除有序数组中的重复项 II
 *
 * @Author wangzf
 * @Date 2025/4/25
 */
public class RemoveDuplicatesII {

    public int removeDuplicates(int[] nums) {
        int n = nums.length;
        if (n <= 2) {
            return n;
        }
        int slow = 2, fast = 2;
        while (fast < n) {
            if (nums[slow - 2] != nums[fast]) {
                nums[slow] = nums[fast];
                ++slow;
            }
            ++fast;
        }
        return slow;
    }

    public int removeDuplicatesByRange(int[] nums) {
        int i = 0;
        for (int num : nums) {
            if (i < 2 || num > nums[i - 2]) {
                nums[i++] = num;
            }
        }

        return i;
    }

    public static void main(String[] args) {
        int[] nums = {1, 1, 2, 2, 2, 2, 2, 2, 3, 4, 5};
        int i = new RemoveDuplicatesII().removeDuplicates(nums);
        for (int j = 0; j < i; j++) {
            System.out.println(nums[j]);
        }
        System.out.println(i);
    }
}
