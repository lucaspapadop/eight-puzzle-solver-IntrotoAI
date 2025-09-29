
//Course: 3642-01
//Student Name: Lucas Papadopoulos
//Student ID: 001078068
//Assignment #: 2
//Due Date: 10/18/24
// Signature: LP

/* Standard stack for the solution stack implementation: used for finding path from goal state to initial state */

public class Stack {
    private SolutionNode top;

    public Stack() {
        this.top = null;
    }

    public void push(TreeNode node) {
        SolutionNode newNode = new SolutionNode(node);
        if (top != null) {
            newNode.next = top;
            top.back = newNode;
        }
        top = newNode;
    }

    public TreeNode pop() {
        if (top == null) {
            throw new IllegalStateException("Stack is empty");
        }
        TreeNode data = top.data;
        top = top.next;
        if (top != null) {
            top.back = null;
        }
        return data;
    }

    public TreeNode peek() {
        if (top == null) {
            throw new IllegalStateException("Stack is empty");
        } else {
            return top.data;
        }
    }

    public boolean isEmpty() {
        return top == null;
    }
}

