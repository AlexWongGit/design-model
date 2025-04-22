package simple;

/**
 * 移除元素
 *
 * @Author wangzf
 * @Date 2025/4/21
 */
public class RemoveElement {

    public int removeElement(int[] nums, int val) {
        int count = 0;
        for (int i = 0; i < nums.length; i++) {
            if (val != nums[i]) {
                nums[count] = nums[i];
                count++;
            }
        }
        return count;
    }

    public int removeElementNew(int[] nums, int val) {
        int count = 0;
        int left = 0;
        int right = nums.length - 1;
        while (left <= right) {
            if (nums[left] == val) {
                nums[left] = nums[right];
                right--;
            } else {
                count++;
                left++;
            }
        }
        return count;
    }
}
