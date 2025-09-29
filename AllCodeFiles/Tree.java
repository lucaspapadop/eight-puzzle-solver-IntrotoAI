import java.util.Arrays;
import java.util.HashMap;

//Course: 3642-01
//Student Name: Lucas Papadopoulos
//Student ID: 001078068
//Assignment #: 2
//Due Date: 10/18/24
// Signature: LP

/* The Tree class is the foundation of the search space from the random board position to the goal state. It is
 * the data structure that keeps links and references from the starting position to all possible board positions
 * until the goal state is reached. It also serves as the key driver of identifying a board state with its parent
 * and potential children.
 * Instantiating a Tree with a board position (and other parameters based on the algorithm) creates a root node
 * for that board position. Each board position produces a maximum of 4 different board positions as children and a minimum
 * of 2. There are four ways to link a parent to a child: addLeft, addMidLeft, addMidRight, and addRight. Each different
 * method is called depending on the amount of children a board position potentially has. Every time a child is created,
 * its depth variable is increased. */
public class Tree {
    private final TreeNode root;

    public Tree(int[] rootData) {
        this.root = new TreeNode(rootData);
    }

    public Tree(int[] rootData, int[] goalIndices) {
        this.root = new TreeNode(rootData, goalIndices);
    }

    public Tree(int[] rootData, int[] goalIndices, HashMap<Integer, Integer> neighbors) {
        this.root = new TreeNode(rootData, goalIndices, neighbors);
    }

    public TreeNode getRoot() {
        return root;
    }

    //All board positions have at least one child
    public void addLeft(TreeNode parent, TreeNode childNode) {

        parent.left = childNode;
        childNode.back = parent;
        childNode.depth = parent.depth + 1;
    }

    //Called almost always: the exception being if the empty space is in a corner and we've already
    //visited one of the board positions.
    public void addMidLeft(TreeNode parent, TreeNode childNode) {

        parent.midLeft = childNode;
        childNode.back = parent;
        childNode.depth = parent.depth + 1;
    }

    //Called for empty spaces at index [1], [3], [5], or [7], and every child position is unique
    public void addMidRight(TreeNode parent, TreeNode childNode) {

        parent.midRight = childNode;
        childNode.back = parent;
        childNode.depth = parent.depth + 1;
    }

    //Only called if the empty space is in the middle, and the child positions are unique
    public void addRight(TreeNode parent, TreeNode childNode) {

        parent.right = childNode;
        childNode.back = parent;
        childNode.depth = parent.depth + 1;
    }

    /* We need this search method because of reference issues. One can't pass in the node from a previous iteration
     * without first knowing what the reference is to that node. Creating a new node with similar data does just that,
     * it creates a new node that is not the same as the node with that data.
     * We also use this search for finding the solution presented in the solution queue.
     *
     * It uses a recursive callstack to traverse through the tree, with order left, midleft, midRight, right.
     * It searches until it hits null at the bottom of the tree and continues the search inorder.
     * */
    public TreeNode searchNode(TreeNode node, int[] data) {
        if (node == null) {
            return null;
        }
        if (Arrays.equals(node.data, data)) {
            return node;
        }
        TreeNode foundNode = searchNode(node.left, data);
        if (foundNode == null) {
            foundNode = searchNode(node.midLeft, data);
        }
        if (foundNode == null) {
            foundNode = searchNode(node.midRight, data);
        }
        if (foundNode == null) {
            foundNode = searchNode(node.right, data);
        }
        return foundNode;
    }
}

