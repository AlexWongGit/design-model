package medium;

/**
 * TODO <br>
 *
 * @Author wangzf
 * @Date 2025/4/24
 */
public class SearchRange {

    public int[] searchRange(int[] nums, int target) {
        int left = 0, right = nums.length - 1;
        int[] result = {-1, -1};
        while (left <= right) {
            if (nums[left] == target && nums[right] == target) {
                result[0] = left;
                result[1] = right;
                break;
            }
            if (nums[left] < target) {
                left++;
            }
            if (nums[right] > target) {
                right--;
            }
        }
        return result;
    }

    public int[] searchRangeNew(int[] nums, int target) {
        int[] result = {-1, -1};
        if (nums.length == 0 || target < nums[0] || target > nums[nums.length - 1]) {
            return result;
        }
        binarySearch(nums, target, 0, nums.length - 1, result);
        return result;
    }

    public void binarySearch(int[] nums, int target, int left, int right, int[] result) {
        if (left > right) {
            return;
        }
        int mid = (left + right) / 2;
        if (nums[mid] > target) {
            if (right == mid && nums[mid - 1] != target) {
                return;
            }
            if (left == right - 1) {
                right = left;
            } else {
                right = mid;
            }
            binarySearch(nums, target, left, right, result);
        } else if (nums[mid] < target) {
            if (mid == left && nums[mid + 1] != target) {
                return;
            }
            if (left == right - 1) {
                left = right;
            } else {
                left = mid;
            }
            binarySearch(nums, target, left, right, result);
        } else {
            left = mid;
            right = mid;
            while (left >= 0 && nums[left] == target) {
                left--;
            }
            while (right < nums.length && nums[right] == target) {
                right++;
            }
            result[0] = left + 1;
            result[1] = right - 1;
        }
    }


    public int[] searchRangeNewGpt(int[] nums, int target) {
        int[] result = {-1, -1};
        if (nums == null || nums.length == 0) return result;

        result[0] = findFirst(nums, target);
        result[1] = findLast(nums, target);
        return result;
    }

    private int findFirst(int[] nums, int target) {
        int left = 0, right = nums.length - 1;
        int idx = -1;

        while (left <= right) {
            int mid = left + (right - left) / 2;

            if (nums[mid] >= target) {
                right = mid - 1;
            } else {
                left = mid + 1;
            }

            if (nums[mid] == target) idx = mid;
        }

        return idx;
    }

    private int findLast(int[] nums, int target) {
        int left = 0, right = nums.length - 1;
        int idx = -1;

        while (left <= right) {
            int mid = left + (right - left) / 2;

            if (nums[mid] <= target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }

            if (nums[mid] == target) idx = mid;
        }

        return idx;
    }


    public static void main(String[] args) {
        int[] nums = {0,1,2,3,4,4,4};
        int target = 2;
        int[] result = new SearchRange().searchRangeNew(nums, target);
        System.out.println(result[0] + " " + result[1]);
    }
}
