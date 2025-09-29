import java.util.Arrays;
import java.util.HashSet;
//Course: 3642-01
//Student Name: Lucas Papadopoulos
//Student ID: 001078068
//Assignment #: 2
//Due Date: 10/18/24
// Signature: LP


/* aSManhattan is a heuristic based search that visits nodes of a search space based on the minimum heuristic value
 * of each available, open node in the process of expansion. The heuristic used is a Manhattan distance,
 * the summation of each tile's distance from its correct position in the goal state. A priority queue is used to sort
 * and maintain the order of visiting nodes based on this heuristic. See TreeNode and manPriorityQueue classes for an
 * explanation of how priority and the heuristic values are calculated.
 *
 * A Hashset is constructed as the closed list.  An array goalIndices is constructed. It keeps track of the indices
 * of each tile in their correct position. All TreeNodes are constructed passing this array in as a parameter.
 *
 * The algorithm takes in a board position to set as root of the search space tree, expansionTree. Then, it enqueues
 * the root node into manPriorityQueue, while also passing in the indices of each tile in the goal state.
 *
 * The root node is checked for its similarly with the goal state. A boolean variable
 * check is used to keep track whether a board position equals the goal state: we use the check() method to check this
 * equality each time. If the root isn't a match, the node is enqueued into the priority queue, manPriorityQueue
 *
 * The main loop runs as long as no board position matches the goal state, and priority queue is not empty. The priority
 * queue should never be empty unless the goal state is reached or the closed list has visited every possible
 * permutation of the original board position (the search tree is exhausted without finding the board position).
 *
 * The loop starts by de-queueing the first element in manPriorityQueue. We instantiate TreeNode currentNode with the
 * data from the priority queue, and set it equal to the node in the search tree that matches that board position.
 * Once the reference matter is resolved, we begin by finding the index of the blank space of currentNode.
 * All possible moves from a board position are defined by the location of its blank space, by definition.
 * Based on the blank space, a board position can generate 2, 3, or 4 board positions
 * (which may or may not be unique: there is a 1 child case).
 *
 ****If the blank space is in index 4, there are four different board positions one can make from it: moving the tile
 * from index 1 to the middle, index 3 to the middle, index 5 to the middle, or index 7 to the middle.
 *
 ****If the blank space is in an odd index, there are three different board positions it can take on.
 *    If the blank space is in the top or bottom row, we can move the tile from the left into the blank,
 *    the tile to the right into the blank, or the tile in the middle into the blank.
 *    If it is in the middle row, we can move the tile from above into the blank, from below into the blank,
 *    or from the middle into the blank.
 *
 ****If the blank space is in an even index, a corner, there are two different board positions that it can take on.
 *   These swaps are determined by case.
 *
 * If one of these positions match the goal position, we call buildSolutionQueue.
 * That method throws the matching board position onto a stack, and throws all of its ancestors onto the same stack
 * via the back node tree traversal. It then constructs the solution queue
 * (the order for solving the sliding puzzle problem).
 *
 * If a child of the currentNode does not match the goal, and it has not been visited already (check closedList),
 * a TreeNode is created for it. This TreeNode is connected to its parent: if it is the first child to be added,
 * we call addLeft. The next child will call addMidLeft, the one after that will call addMidRight, and the last one
 * will call addRight. Once all the potential children have been created for a board position, we loop back to the next
 * node in the priority Queue and repeat the process until we have visited every node, or we find the goal position.
 */
public class aSManhattan {
    public static Queue solutionQueue;
    public static int visitedNodes;

    public aSManhattan() {
        solutionQueue = new Queue();
        visitedNodes = 0;
    }

    public aSManhattan(int[] currentList, int[] goal) {
        this();
        solutionQueue = manExpansion(currentList, goal);
    }

    public Queue manExpansion(int[] currentList, int[] goal) {

        boolean check;
        visitedNodes = -1;

        //need to clone as to not reference currentList directly
        int[] masterList = currentList.clone();

        manPriorityQueue priorityQueue = new manPriorityQueue();
        Queue solutionQueue = new Queue();
        HashSet<String> closedList = new HashSet<>();

        //goalIndices Construction Start
        int[] goalIndices = new int[8];

        for (int i = 0; i < goal.length; i++) {
            if (goal[i] == 0) {
                continue;
            }
            goalIndices[goal[i]-1] = i;
        }
        //goalIndices Construction End

        //search space tree. Also, priorityQueue enqueues the root
        Tree expansionTree = new Tree(masterList, goalIndices);
        priorityQueue.enqueue(expansionTree.getRoot());

        check = check(masterList, goal);

        if (check) {
            solutionQueue.enqueue(expansionTree.getRoot());
        }

        //Loop runs until goal state is reached or every board position is in closed list.
        //Every time it runs, we have visited a node: visitedNodes starts at -1 to account for the starting board
        //position, which doesn't count.
        while (!priorityQueue.isEmpty() && !check) {
            TreeNode currentNode = priorityQueue.dequeue();
            masterList = currentNode.data;
            currentNode = expansionTree.searchNode(currentNode, masterList);

            //Increment visitedNodes to indicate that we are expanding a board position. Then, find blank space.
            visitedNodes++;
            int positionVar = giveIndexZero(masterList);

            int[] child1;
            int[] child2;
            int[] child3 = null;
            int[] child4 = null;

            //Case that the blank space is in the middle
            if (positionVar == 4) {
                child1 = switchElements(masterList, positionVar, 1);
                child2 = switchElements(masterList, positionVar, 3);
                child3 = switchElements(masterList, positionVar, 5);
                child4 = switchElements(masterList, positionVar, 7);

            //Case that the blank space is in an odd index
            } else if (positionVar % 2 == 1) {
                //Middle Row
                if (positionVar == 3 || positionVar == 5) {
                    child1 = switchElements(masterList, positionVar, positionVar - 3);
                    child2 = switchElements(masterList, positionVar, positionVar + 3);
                //Top and Bottom Row
                } else {
                    child1 = switchElements(masterList, positionVar, positionVar - 1);
                    child2 = switchElements(masterList, positionVar, positionVar + 1);
                }
                child3 = switchElements(masterList, positionVar, 4);

            //Case that the blank space is in an even index, a corner
            } else {
                if (positionVar == 0) {
                    child1 = switchElements(masterList, positionVar, positionVar + 1);
                    child2 = switchElements(masterList, positionVar, positionVar + 3);
                } else if (positionVar == 2) {
                    child1 = switchElements(masterList, positionVar, positionVar - 1);
                    child2 = switchElements(masterList, positionVar, positionVar + 3);
                } else if (positionVar == 6) {
                    child1 = switchElements(masterList, positionVar, positionVar + 1);
                    child2 = switchElements(masterList, positionVar, positionVar - 3);
                } else { //positionVar == 8
                    child1 = switchElements(masterList, positionVar, positionVar - 1);
                    child2 = switchElements(masterList, positionVar, positionVar - 3);
                }
            }

            //If a child position hasn't been visited, make a new node, add connection to parent, check it against goal
            if (!closedList.contains(Arrays.toString(child1))){
                TreeNode childNode1 = new TreeNode(child1, goalIndices);
                expansionTree.addLeft(currentNode, childNode1);
                priorityQueue.enqueue(childNode1);
                check = check(child1, goal);

                //If state is the same as goal, find that state in expansionTree and build solution queue
                if (check) {
                    TreeNode traverseNode = expansionTree.searchNode(childNode1, child1);
                    solutionQueue = buildSolutionQueue(traverseNode);
                }

            }

            //If a child position hasn't been visited, make a new node, add connection to parent, check it against goal
            if (!closedList.contains(Arrays.toString(child2))) {
                TreeNode childNode2 = new TreeNode(child2, goalIndices);
                expansionTree.addMidLeft(currentNode, childNode2);
                priorityQueue.enqueue(childNode2);
                check = check(child2, goal);

                //If state is the same as goal, find that state in expansionTree and build solution queue
                if (check) {
                    TreeNode traverseNode = expansionTree.searchNode(childNode2, child2);
                    solutionQueue = buildSolutionQueue(traverseNode);
                }
            }

            //Check if a 3rd child is possible first
            //If a child position hasn't been visited, make a new node, add connection to parent, check it against goal
            if ((child3 != null) && !closedList.contains(Arrays.toString(child3))) {
                TreeNode childNode3 = new TreeNode(child3, goalIndices);
                expansionTree.addMidRight(currentNode, childNode3);
                priorityQueue.enqueue(childNode3);
                check = check(child3, goal);

                //If state is the same as goal, find that state in expansionTree and build solution queue
                if (check) {
                    TreeNode traverseNode = expansionTree.searchNode(childNode3, child3);
                    solutionQueue = buildSolutionQueue(traverseNode);
                }
            }

            //Check if a 4th child is possible first
            //If a child position hasn't been visited, make a new node, add connection to parent, check it against goal
            if ((child4 != null) && !closedList.contains(Arrays.toString(child4))) {
                TreeNode childNode4 = new TreeNode(child4,goalIndices);
                expansionTree.addRight(currentNode, childNode4);
                priorityQueue.enqueue(childNode4);
                check = check(child4, goal);

                //If state is the same as goal, find that state in expansionTree and build solution queue
                if (check) {
                    TreeNode traverseNode = expansionTree.searchNode(childNode4, child4);
                    solutionQueue = buildSolutionQueue(traverseNode);
                }
            }

            //add this currentNode to the closed list after all child connections have been made
            closedList.add(Arrays.toString(currentNode.data));

        }

        return solutionQueue;

    }

    //check function for checking whether a board position is the same as the goal state
    public boolean check(int[] list, int[] goal) {
        return Arrays.equals(list, goal);
    }

    //Iterates through a passed-in array to find where zero is in the array, and returns the index
    public int giveIndexZero(int[] list) {
        for (int i = 0; i < list.length; i++) {
            if (list[i] == 0) {
                return i;
            }
        }
        return -1;
    }

    //Method of switching two elements in an array
    public int[] switchElements(int[] arr, int index1, int index2) {
        int[] newArr = arr.clone();
        int temp = newArr[index1];
        newArr[index1] = newArr[index2];
        newArr[index2] = temp;
        return newArr;
    }

    /* Method for building the solution queue to be returned. It pushes the node called-in onto a stack
     *Then, the method pushes all the ancestors of that node onto the stack. Finally, it populates a solution queue
     *with all the popped off elements of the stack, producing a queue that gives the correct order from the
     *starting board position to the goal state
     */
    private Queue buildSolutionQueue(TreeNode goalNode) {
        Queue solutionQueue = new Queue();
        Stack stack = new Stack ();
        while (goalNode != null) {
            stack.push(goalNode);
            goalNode = goalNode.back;
        }
        while (!stack.isEmpty()) {
            solutionQueue.enqueue(stack.pop());
        }
        return solutionQueue;
    }


}
