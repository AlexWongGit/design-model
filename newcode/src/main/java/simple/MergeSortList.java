package simple;

public class MergeSortList {

    public static class ListNode {
        int val;
        ListNode next = null;

        public ListNode(int val) {
            this.val = val;
        }
    }

    public static void main(String[] args) {
        ListNode listNode = new ListNode(1);
        listNode.next = new ListNode(3);
        listNode.next.next = new ListNode(5);
        ListNode listNode2 = new ListNode(2);
        listNode2.next = new ListNode(4);
        listNode2.next.next = new ListNode(6);
        System.out.println(merge(listNode, listNode2));
    }

    public static ListNode merge(ListNode pHead1, ListNode pHead2) {
        // write code here
        ListNode cur1 = pHead1;
        ListNode cur2 = pHead2;
        ListNode res = new ListNode(0);

        while (cur1 != null || cur2 != null) {
            ListNode copy = res;
            ListNode tail;
            while (res.next != null) {
                res = res.next;
            }
            tail = res;
            res = copy;
            if (cur1 == null) {
                tail.next = cur2;
                break;
            }


            if (cur2 == null) {
                tail.next = cur1;
                break;
            }
            if (cur1.val < cur2.val) {
                ListNode temp = cur1.next;
                cur1.next = null;
                tail.next = cur1;
                cur1 = temp;
            } else {
                ListNode temp = cur2.next;
                cur2.next = null;
                tail.next = cur2;
                cur2 = temp;
            }
        }
        return res.next;
    }
}
