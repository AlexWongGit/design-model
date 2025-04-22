package medium;

import java.util.*;

/**
 * 三数之和
 *
 * @Author wangzf
 * @Date 2025/4/21
 */
public class TreeSum {

    /**
     * 排序+双指针： https://leetcode.cn/problems/3sum/description/
     * 第二个循环为左指针，第三个循环为右指针
     * @return java.util.List<java.util.List < java.lang.Integer>>
     * @param nums
     */
    public List<List<Integer>> threeSum(int[] nums) {
        Arrays.sort(nums);
        List<List<Integer>> retList = new ArrayList<>();

        for (int i = 0; i < nums.length; ++i) {
            if (i > 0 && nums[i] == nums[i - 1]) {
                continue;
            }
            int target = -nums[i];
            int k = nums.length - 1;
            for (int j = i + 1; j < nums.length; ++j) {
                if (j > i + 1 && nums[j] == nums[j - 1]) {
                    continue;
                }
                while (j < k && nums[j] + nums[k] > target) {
                    --k;
                }
                if (j == k) {
                    continue;
                }
                if (nums[j] + nums[k] == target) {
                    List<Integer> list = new ArrayList<>();
                    list.add(nums[i]);
                    list.add(nums[j]);
                    list.add(nums[k]);
                    retList.add(list);
                }
            }
        }

        return retList;
    }

   /* public List<List<Integer>> threeSum(int[] nums) {
        List<List<Integer>> resultList = new ArrayList<>();
        List<String> distinct = new ArrayList<>();
        for (int i = 0; i < nums.length; i++) {
            for (int j = 0; j < nums.length; j++) {
                for (int k = 0; k < nums.length; k++) {
                    if (i ==j ||i==k|| j == k) {
                        continue;
                    }
                    if (nums[i] + nums[j] + nums[k] == 0) {
                        if (distinct.contains("i:" + nums[i] + "j:" + nums[j] + "k:" + nums[k])) {
                            continue;
                        } else {
                            distinct.add("i:" + nums[i] + "j:" + nums[j] + "k:" + nums[k]);
                            distinct.add("i:" + nums[j] + "j:" + nums[i] + "k:" + nums[k]);
                            distinct.add("i:" + nums[k] + "j:" + nums[i] + "k:" + nums[j]);
                            distinct.add("i:" + nums[j] + "j:" + nums[k] + "k:" + nums[i]);
                            distinct.add("i:" + nums[k] + "j:" + nums[j] + "k:" + nums[i]);
                            distinct.add("i:" + nums[i] + "j:" + nums[k] + "k:" + nums[j]);
                        }
                        List<Integer> list = new ArrayList<>();
                        list.add(nums[i]);
                        list.add(nums[j]);
                        list.add(nums[k]);
                        resultList.add(list);
                    }

                }
            }
        }
        return resultList;
    }*/

    public static void main(String[] args) {
        int[] nums = {1,0, -1,-1};
        TreeSum treeSum = new TreeSum();
        List<List<Integer>> lists = treeSum.threeSum(nums);
        for (List<Integer> list : lists) {
            System.out.println(list);
        }
    }
}
