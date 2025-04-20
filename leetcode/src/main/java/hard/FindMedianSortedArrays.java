package hard;

import java.util.ArrayList;

/**
 * TODO <br>
 *
 * @Author wangzf
 * @Date 2025/4/20
 */
public class FindMedianSortedArrays {

    public static double findMedianSortedArrays(int[] nums1, int[] nums2) {
        int newLength = nums1.length + nums2.length;
        int target = newLength / 2 + 1;
        int[] arr = new int[target + 1];
        boolean flag = newLength % 2 == 0;

        int cur1 = 0;
        int cur2 = 0;
        for (int i = 0; i <= target; i++) {
            if ((cur1 <= nums1.length - 1)
                    && (cur2 >= nums2.length || nums1[cur1] <= nums2[cur2])) {
                arr[i] = nums1[cur1++];
            } else if (cur2 < nums2.length) {
                arr[i] = nums2[cur2++];
            }
        }
        if (flag) {
            return (arr[target - 2] + arr[target - 1]) / 2.0;
        } else {
            return arr[target - 1];
        }
    }

    public static double findMedianSortedArraysNew(int[] nums1, int[] nums2) {
        int length1 = nums1.length;
        int length2 = nums2.length;
        if (length1 == 0 && length2 == 0) {
            return 0.0;
        }
        int totalLength = length1 + length2;
        if (totalLength % 2 == 1) {
            int midIndex = totalLength / 2;
            return getKthElement(nums1, nums2, midIndex + 1);
        } else {
            int midIndex1 = totalLength / 2 - 1;
            int midIndex2 = totalLength / 2;
            return (getKthElement(nums1, nums2, midIndex1 + 1) + getKthElement(nums1, nums2, midIndex2 + 1)) / 2.0;
        }

    }

    public static int getKthElement(int[] nums1, int[] nums2, int k) {
        /* 主要思路：要找到第 k (k>1) 小的元素，那么就取 pivot1 = nums1[k/2-1] 和 pivot2 = nums2[k/2-1] 进行比较
         * 这里的 "/" 表示整除
         * nums1 中小于等于 pivot1 的元素有 nums1[0 .. k/2-2] 共计 k/2-1 个
         * nums2 中小于等于 pivot2 的元素有 nums2[0 .. k/2-2] 共计 k/2-1 个
         * 取 pivot = min(pivot1, pivot2)，两个数组中小于等于 pivot 的元素共计不会超过 (k/2-1) + (k/2-1) <= k-2 个
         * 这样 pivot 本身最大也只能是第 k-1 小的元素
         * 如果 pivot = pivot1，那么 nums1[0 .. k/2-1] 都不可能是第 k 小的元素。把这些元素全部 "删除"，剩下的作为新的 nums1 数组
         * 如果 pivot = pivot2，那么 nums2[0 .. k/2-1] 都不可能是第 k 小的元素。把这些元素全部 "删除"，剩下的作为新的 nums2 数组
         * 由于我们 "删除" 了一些元素（这些元素都比第 k 小的元素要小），因此需要修改 k 的值，减去删除的数的个数
         */

        int length1 = nums1.length;
        int length2 = nums2.length;
        int index1 = 0;
        int index2 = 0;


        while (true) {
            // 边界情况
            if (index1 == length1) {
                return nums2[index2 + k - 1];
            }
            if (index2 == length2) {
                return nums1[index1 + k - 1];
            }
            if (k == 1) {
                return Math.min(nums1[index1], nums2[index2]);
            }

            // 正常情况
            int half = k / 2;
            // 防止越界(所以下面不能直接减k/2，要按照实际删除的个数来)
            int newIndex1 = Math.min(index1 + half, length1) - 1;
            int newIndex2 = Math.min(index2 + half, length2) - 1;
            int pivot1 = nums1[newIndex1];
            int pivot2 = nums2[newIndex2];
            // 删除的元素个数为 newIndex1 - index1 + 1 或者 newIndex2 - index2 + 1
            // 如果 pivot1 < pivot2，那么我们需要删除 nums1[0 .. newIndex1]，同时修改 k 的值
            // 如果 pivot1 > pivot2，那么我们需要删除 nums2[0 .. newIndex2]，同时修改 k 的值
            if (pivot1 <= pivot2) {
                // 删除 nums1[0 .. newIndex1]
                k -= (newIndex1 - index1 + 1);
                // 修改 nums1 的起始位置
                index1 = newIndex1 + 1;
            } else {
                k -= (newIndex2 - index2 + 1);
                index2 = newIndex2 + 1;
            }
        }
    }


    public static void main(String[] args) {
        int[] nums1 = {1, 2, 3, 4, 5};
        int[] nums2 = {6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17};
        double median = findMedianSortedArraysNew(nums1, nums2);
        System.out.println("The median is: " + median);
    }
}