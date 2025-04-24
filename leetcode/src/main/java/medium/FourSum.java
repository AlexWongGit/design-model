package medium;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * TODO <br>
 *
 * @Author wangzf
 * @Date 2025/4/24
 */
public class FourSum {

/*    public List<List<Integer>> fourSum(int[] nums, int target) {
        List<List<Integer>> result = new ArrayList<>();
        Arrays.sort(nums);
        backtrack(nums, target, 0, 0, new ArrayList<>(), result);
        return result;
    }

    private void backtrack(int[] nums, int target, int start, int depth, List<Integer> path, List<List<Integer>> result) {
        if (depth == 4) {
            int sum = path.get(0) + path.get(1) + path.get(2) + path.get(3);
            if (sum == target) {
                result.add(new ArrayList<>(path));
            }
            return;
        }

        for (int i = start; i < nums.length; i++) {
            if (i > start && nums[i] == nums[i - 1]) continue;
            if (nums.length - i < 4 - depth) break;

            path.add(nums[i]);
            backtrack(nums, target, i + 1, depth + 1, path, result);
            path.remove(path.size() - 1);
        }
    }*/


    public List<List<Integer>> fourSum(int[] nums, int target) {
        List<List<Integer>> result = new ArrayList<>();
        int n = nums.length;
        if (n < 4) {
            return result;
        }

        Arrays.sort(nums);

        for (int i = 0; i < n - 3; i++) {
            if (i > 0 && nums[i] == nums[i - 1]) {
                continue;
            }

            for (int j = i + 1; j < n - 2; j++) {
                if (j > i + 1 && nums[j] == nums[j - 1]) {
                    continue;
                }

                int left = j + 1, right = n - 1;

                while (left < right) {
                    long sum = (long) nums[i] + nums[j] + nums[left] + nums[right];

                    if (sum == target) {
                        result.add(Arrays.asList(nums[i], nums[j], nums[left], nums[right]));


                        while (left < right && nums[left] == nums[left + 1]) {
                            left++;
                        }
                        while (left < right && nums[right] == nums[right - 1]) {
                            right--;
                        }

                        left++;
                        right--;
                    } else if (sum < target) {
                        left++;
                    } else {
                        right--;
                    }
                }
            }
        }

        return result;
    }


    public static void main(String[] args) {
        int[] nums = {1, 0, -1, 0, -2, 2};
        int target = 0;
        List<List<Integer>> lists = new FourSum().fourSum(nums, target);
        for (List<Integer> list : lists) {
            System.out.println(list);
        }
    }

}
