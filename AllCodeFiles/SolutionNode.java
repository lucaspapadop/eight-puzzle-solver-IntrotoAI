
//Course: 3642-01
//Student Name: Lucas Papadopoulos
//Student ID: 001078068
//Assignment #: 2
//Due Date: 10/18/24
// Signature: LP

/* Standard node wrapper for a TreeNode*/

public class SolutionNode {
    public TreeNode data;
    public SolutionNode next;
    public SolutionNode back;

    public SolutionNode(TreeNode data) {
        this.data = data;
        this.next = null;
        this.back = null;
    }
}



