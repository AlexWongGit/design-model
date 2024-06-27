package simple;


import org.apache.poi.hssf.record.DVALRecord;

import java.util.HashSet;

public class ListNodeRing {

    static class ListNode {
        int val;
        ListNode next;
        ListNode(int x) {
            val = x;
            next = null;
        }
    }

    public static void main(String[] args) {
        ListNode listNode = new ListNode(3);
        ListNode listNode1 = new ListNode(2);
        ListNode listNode2 = new ListNode(0);
        ListNode listNode3 = new ListNode(4);
        listNode3.next = listNode2;
        listNode2.next = listNode3;
        listNode1.next = listNode2;
        listNode.next = listNode1;
        //boolean b = hasCycle(listNode);
        boolean b = hasCycle2(listNode);
        System.out.println(b);
    }

    /**
     * @description 暴力解法
     * @return boolean
     * @param head
     */
    public static boolean hasCycle(ListNode head) {
        if (head == null || head.next == null) {
            return false;
        }

        HashSet<ListNode> listNodes = new HashSet<>();
        while (head != null) {
            if (listNodes.contains(head)) {
                return true;
            }
            listNodes.add(head);
            head = head.next;
        }

        return false;
    }


    /**
     * @description 快慢双指针（一快一慢，两指针相遇说明有环）
     * @return boolean
     * @param head
     */
    public static boolean hasCycle2(ListNode head) {

        if (head == null || head.next == null) {
            return false;
        }
        ListNode slow = head;
        ListNode fast = head;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
            if (slow == fast) {
                return true;
            }
        }
        return false;
    }


}
