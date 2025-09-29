
//Course: 3642-01
//Student Name: Lucas Papadopoulos
//Student ID: 001078068
//Assignment #: 2
//Due Date: 10/18/24
// Signature:LP

/* Standard queue with regular queue functionalities*/

public class Queue {
    private SolutionNode front;
    private SolutionNode rear;

    public Queue() {
        this.front = null;
        this.rear = null;
    }

    public void enqueue(TreeNode node) {
        SolutionNode newNode = new SolutionNode(node);
        if (rear == null) {
            front = newNode;
            rear = newNode;
        } else {
            rear.next = newNode;
            newNode.back = rear;
            rear = newNode;
        }
    }

    public TreeNode dequeue() {
        if (front == null) {
            throw new IllegalStateException("Queue is empty");
        }
        TreeNode data = front.data;
        front = front.next;
        if (front != null) {
            front.back = null;
        } else {
            rear = null;
        }
        return data;
    }

    public boolean isEmpty() {
        return front == null;
    }
}




