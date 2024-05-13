package medium;

import entity.ListNode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wangzf
 * @description 两数相加
 * 两个 非空 的链表，表示两个非负的整数。它们每位数字都是按照 逆序 的方式存储的，并且每个节点只能存储 一位 数字。
 * 请你将两个数相加，并以相同形式返回一个表示和的链表。
 * 你可以假设除了数字 0 之外，这两个数都不会以 0 开头。
 * 提示：
 * 每个链表中的节点数在范围 [1, 100] 内
 * 0 <= Node.val <= 9
 * 题目数据保证列表表示的数字不含前导零
 * @date 2024/5/7
 */
public class TwoNumberSum {

    public static void main(String[] args) {
        ListNode l1 = new ListNode(2, new ListNode(4, new ListNode(3)));
        ListNode l2 = new ListNode(5, new ListNode(6, new ListNode(4)));
        ListNode listNode = addTwoNumbers(l1, l2);
        System.out.println(listNode);
    }

    public static ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        //预先指针
        ListNode pre = new ListNode(0);
        ListNode cur = pre;
        int carry = 0;
        while (l1 != null || l2 != null) {
            int a = l1 == null ? 0 : l1.getVal();
            int b = l2 == null ? 0 : l2.getVal();
            int sum = a + b + carry;
            carry = sum / 10;
            //取余
            sum = sum % 10;
            cur.setNext(new ListNode(sum));

            cur = cur.getNext();
            if(l1 != null)
                l1 = l1.getNext();
            if(l2 != null)
                l2 = l2.getNext();
        }
        if(carry == 1) {
            cur.setNext(new ListNode(carry));
        }
        return pre.getNext();
    }


}
