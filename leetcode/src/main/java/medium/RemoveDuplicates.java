package medium;

import java.util.Arrays;

/**
 * TODO <br>
 *
 * @Author wangzf
 * @Date 2025/4/23
 */
public class RemoveDuplicates {


    public int removeDuplicates(int[] nums) {
        int[] newNums = new int[0];
        int[] newNums2 = new int[0];
        if (nums[0] <= 0 && nums[nums.length - 1] >= 0) {
            newNums = new int[nums[nums.length - 1] - nums[0] + 1];
            newNums2 = new int[nums[nums.length - 1] - nums[0] + 1];
        } else if (nums[0] > 0 && nums[nums.length - 1] >= 0) {
            // 都是正数
            newNums = new int[nums[nums.length - 1] + 1];
        } else {
            // 都是负数
            newNums2 = new int[-nums[0] + 1];
        }
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] <= 0) {
                newNums2[-nums[i]] = 1;
                continue;
            }
            newNums[nums[i]] = 1;
        }
        int count = 0;
        if (newNums2.length > 0) {
            for (int i = newNums2.length - 1; i >= 0; i--) {
                if (newNums2[i] == 1) {
                    nums[count] = -i;
                    count++;
                }
            }
        }

        if (newNums.length > 0) {
            for (int i = 0; i < newNums.length; i++) {
                if (newNums[i] == 1) {
                    nums[count] = i;
                    count++;
                }
            }
        }

        return count;
    }

    public int removeDuplicatesNew(int[] nums) {
        int min = nums[0], max = nums[0];
        for (int i = 1; i < nums.length; i++) {
            if (nums[i] < min) {
                min = nums[i];
            }
            if (nums[i] > max) {
                max = nums[i];
            }
        }

        int size = max - min + 1;
        boolean[] flags = new boolean[size];
        for (int num : nums) {
            flags[num - min] = true;
        }

        int index = 0;
        for (int i = 0; i < size; i++) {
            if (flags[i]) {
                nums[index++] = i + min;
            }
        }

        return index;
    }

    public int removeDuplicatesDoublePointer(int[] nums) {
        if (nums.length == 0) return 0;

        int write = 1;
        for (int read = 1; read < nums.length; read++) {
            if (nums[read] != nums[read - 1]) {
                nums[write] = nums[read];
                write++;
            }
        }
        return write;
    }


    public static void main(String[] args) {
        RemoveDuplicates removeDuplicates = new RemoveDuplicates();
        int[] nums = {-3, -1};
        int count = removeDuplicates.removeDuplicates(nums);
        System.out.println(count);
        for (int i = 0; i < count; i++) {
            System.out.print(nums[i] + " ");
        }
    }
}
