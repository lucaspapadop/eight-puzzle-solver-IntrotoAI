import java.util.Comparator;
import java.util.PriorityQueue;

//Course: 3642-01
//Student Name: Lucas Papadopoulos
//Student ID: 001078068
//Assignment #: 2
//Due Date: 10/18/24
// Signature: LP

/* manPriorityQueue is a standard Java priority queue of type TreeNode that sorts based upon a node's depth,
 *manhattan distance, and its Nilson value. It serves as the primary vehicle for choosing which nodes to visit via an A*
 *methodology: nodes with the minimum path costs and heuristic costs are served first. Recall that the A* search using
 *only Manhattan distance is defined as F(n) = g(n) + h(n). g(n) = depth and h(n) = manhattDist in this implementation,
 * with the nilsonVal equaling 0. Similarly, the Nilson A* search is defined as F(n) = 3(s(n)) + p(n).
 * 3(s(n)) = nilsonVal and p(n) = depth + manhattDist. This queue effectively models the minimum heuristic order
 * for visiting nodes presented in these two algorithms.
 */
public class manPriorityQueue {
    private final PriorityQueue<TreeNode> queue; //final?

    public manPriorityQueue() { //Comparator compares attributes of inserted nodes, sets that as a rule
        queue = new PriorityQueue<>(Comparator.comparingInt(n -> n.manhatDist + n.depth + n.nilsonVal));
    }

    //Adds a node to the heap, checks against the nodes within, then re-heapifies
    public void enqueue(TreeNode node) {
        queue.offer(node);
    }

    //Removes a node from the heap, then re-heapifies
    public TreeNode dequeue() {
        return queue.poll();
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }

}

