package medium;

/**
 * TODO <br>
 *
 * @Author wangzf
 * @Date 2025/4/20
 */
public class MaxArea {

    /**
     * Given n non-negative integers a1, a2, ..., an , where each represents a point at coordinate (i, ai).
     * n vertical lines are drawn such that the two endpoints of line i is at (i, ai) and (i, 0).
     * Find two lines, which together with the x-axis forms a container, such that the container contains the most water.
     * Note: You may not slant the container.
     * 双指针
     * 证明思路： 假设有i,j两个指针，i指向数组的第一个元素，j指向数组的最后一个元素，
     * 那么，假设当前i和j的元素值分别为ai和aj，那么，当前容量为min(ai,aj)*(j-i)，
     * 那么，如果ai<aj，那么，如果aj继续往下移动，那么，容量将变小，
     * 那么，如果ai继续往上移动，那么，容量将变大。
     * 所以，移动指针时，应该移动较小的那个指针。xsw
     *
     * @param height
     * @return
     */
    public int maxArea(int[] height) {
        int maxArea = 0;
        int left = 0;
        int right = height.length - 1;

        while (left < right) {
            int width = right - left;
            int minHeight = Math.min(height[left], height[right]);
            maxArea = Math.max(maxArea, width * minHeight);

            if (height[left] < height[right]) {
                left++;
            } else {
                right--;
            }
        }

        return maxArea;
    }
}
