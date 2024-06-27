package simple;

public class BST {

    public static class TreeNode {
        int val = 0;
        TreeNode left = null;
        TreeNode right = null;

        public TreeNode(int val) {
            this.val = val;
        }
    }

    public static void main(String[] args) {
        int[] nums = {-1, 0, 1, 2};
        TreeNode treeNode = sortedArrayToBST(nums);
        System.out.println(treeNode.val);
    }

    /**
     * 代码中的类名、方法名、参数名已经指定，请勿修改，直接返回方法规定的值即可
     *
     * @param nums int整型一维数组
     * @return TreeNode类
     */
    public static TreeNode sortedArrayToBST(int[] nums) {
        // write code here
        if (nums.length == 0) {
            return null;
        }
        // 数组是一个升序数组，根据数组数量以及BST的定义确认根节点，在此索引之前的是左子树，之后的是右子树
        int index = nums.length / 2 > 1 ? nums.length / 2 - 1 : 0;
        TreeNode root = new TreeNode(nums[index]);
        // 递归创造出左右子树
        if (nums.length > 1) {
            int[] left = new int[index];
            int[] right = new int[nums.length - index - 1];
            for (int i = 0; i < index; i++) {
                left[i] = nums[i];
            }
            for (int i = index + 1; i < nums.length; i++) {
                right[i - index - 1] = nums[i];
            }
            root.left = sortedArrayToBST(left);
            root.right = sortedArrayToBST(right);
        }

        return root;
    }
}
