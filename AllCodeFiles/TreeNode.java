import java.lang.Math;
import java.util.HashMap;

//Course: 3642-01
//Student Name: Lucas Papadopoulos
//Student ID: 001078068
//Assignment #: 2
//Due Date: 10/18/24
// Signature: LP

/* TreeNode is the backbone of these algorithms. With this container, the Tree class can perform efficiently
 * and one can find nodes for references (via the back and child connections).
 * Each of the computations made for Manhattan distance or Nilson value are computed in this class.
 * They are included in its overloaded constructors as to not be included in algorithms that don't need them
 * (we don't need Nilson computation in A* manhattan). Go to each of the overloaded constructors to see an
 * explanation of the Manhattan distance and Nilson value computation. Each node has 5 pointers, 4 being the maximum
 * amount of children that a board position can have, and then one more for a parent. It also has values for its depth
 * in the search space tree, its manhattan distance, and its nilson value.
 *
 */

public class TreeNode {
    public TreeNode back, left, midLeft, midRight, right;
    public int[] data;
    public int depth;
    public int manhatDist;
    public int nilsonVal;

    public TreeNode() {
        this.manhatDist = 99;
        this.data = null;
        this.depth = 0;
        this.nilsonVal = 0;
    }

    //UCS Tree Node Constructor: simple container with an array inside.
    public TreeNode(int[] takeInArray) {
        this();

        //Set the node board position to be the one passed in. If it can't be done, throw exception
        if (takeInArray == null || takeInArray.length != 9) {
            throw new IllegalArgumentException("Data array must be of length 9");
        } else {
            data = takeInArray.clone();
        }
    }

    /* Manhattan distance constructor. It takes in the current position created by a parent being visited, and
     * takes in the goal indices created at the start of the algorithm.
     * Computing Manhattan distance can be done by analyzing the difference between the location of a tile
     * in the current board position and where it is in the goal position. This is the general rule I have found:
     * Take Y to be the manhattan contribution of a single tile. Take x to be the overall difference between the index
     * of the tile in the current board position, and the index of the tile in the goal position.
     *
     * Y = { x     if x is an element of {0,1,2}
     *     { x-2   if x is an element of {3,4,5}
     *     { x-4   if x is an element of {5,6,7}
     *
     * This always works, except for some special edge cases. Apparently, the diagonal differences from left to
     * right cause issues: that is, between ((2 and 3) and (5 and 6)), the left diagonal one row up, and
     * (6 and 2), the left diagonal two rows up. If the board were expanded by one more row on top of the top one,
     * in order (-3, -2, -1) left to right, there would be issues between index (0 and -1), (3 and -1), and (6 and -1).
     * I took care of these edge cases by brute force, by recognizing what each contribution is
     * supposed to be and adjusting accordingly.
     *
     * We therefore iterate through the index array and goalIndices array, and take the summation of all the
     * Y's of the tiles (y sub i's). This summation gives the manhattan distance of the current board position.
     */
    public TreeNode(int[] takeInArray, int[] goalIndices) {
        this();

        //Set the node board position to be the one passed in. If it can't be done, throw exception
        if (takeInArray == null || takeInArray.length != 9) {
            throw new IllegalArgumentException("Data array must be of length 9");
        } else {
            data = takeInArray.clone();
        }

        //Last check to see if the parameters passed in are valid
        if (goalIndices == null || goalIndices.length != 8) {
            throw new IllegalArgumentException("Goal indices need to be an index array of length 8");
        }

        //Start of Index Array Construction
        int[] indexArray = new int[8];

        for (int i = 0; i < data.length; i++) {
            if (data[i] == 0) {
                continue;
            }
            indexArray[data[i]-1] = i;
        }

        //End of Index Array Construction

        //Manhattan computation done here
        for (int i = 0; i < indexArray.length; i++) {
            //Take the overall difference of the two indices
            int positionDiff = Math.abs(indexArray[i] - goalIndices[i]);

            //Special Edge Cases
            //Dealing with 2 and 6: take y = x instead
            if ((indexArray[i] == 2 && goalIndices[i] == 6) || (indexArray[i] == 6 && goalIndices[i] == 2)){
                manhatDist += positionDiff;
                continue;
            }

            //Dealing with 3 and 2: take y = x+2 instead
            if ((indexArray[i] == 3 && goalIndices[i] == 2)|| (indexArray[i] == 2 && goalIndices[i] == 3) ){
                manhatDist += positionDiff+2;
                continue;
            }

            //Dealing with 6 and 5: take y = x+2 instead
            if ((indexArray[i] == 6 && goalIndices[i] == 5) || (indexArray[i] == 5 && goalIndices[i] == 6)) {
                manhatDist += positionDiff+2;
                continue;
            }
            //End of special cases

            //Manhattan Distance based on the difference of indices
            if (positionDiff < 3) {
                manhatDist += positionDiff;
            } else if (positionDiff < 6) {
                manhatDist += (positionDiff - 2);
            } else {
                manhatDist += (positionDiff - 4);
            }

        }

    }

    //End of Manhattan Constructor

    /* Nilson Value Constructor. It takes in the current position created by a parent being visited,
     * the goal indices created at the start of the algorithm, and a Hashmap containing the neighbors needed to
     * compute the Nilson value. A Hashmap is utilized because it has a lookup time of O(1)
     * Recall that this computation requires checking against the order of the
     * "windmill" list of the goal position, that is, the clockwise order of the tiles in the goal.
     * The clockwise rendering of a board position will hereafter be referred to as its windmill list
     *
     * To find the Nilson value, we first need to check if each value in this board position's windmill list
     * has its correct neighbor. We do this by constructing a windmill list for the current board position.
     * Then, we iterate through the windmill list and check if each element is contained in the Hashmap that's passed in
     * The Hashmap is used as a 2D array here, with each key being a position in the goal board's windmill list
     * and its associated value being its neighbor clockwise (index i+1).
     *
     * *** If the element is not contained in the Hashmap, that means the value is supposed
     * to be in the middle. It therefore isn't counted.
     * *** If the element is contained in the Hashmap, we simply check if the value associated with that element
     * is equal to the next element in the windmill list. If it isn't, it doesn't have the correct neighbor,
     * and we add 2 to the Nilson value.
     *
     * Next, we check if zero is in the middle, at index 4. We already pass over zero in the construction of the index
     * array for the Manhattan distance. As a result, we find zero in the current board position and are able to check
     * whether it(zero) is in position 4. If it isn't, we add 1.
     *
     * Finally, we find the Manhattan distance using the previous way (see TreeNode(currentList, goalIndices)),
     * and multiply the Nilson value calculated thus far by 3. This gives the Nilson value of the current board position
     */
    public TreeNode(int[] takeInArray, int[] goalIndices, HashMap<Integer, Integer> neighbors) {
        this();

        //Set the node board position to be the one passed in. If it can't be done, throw exception
        if (takeInArray == null || takeInArray.length != 9) {
            throw new IllegalArgumentException("Data array must be of length 9");
        } else {
            data = takeInArray.clone();
        }

        //Check if goalIndices is valid
        if (goalIndices == null || goalIndices.length != 8) {
            throw new IllegalArgumentException("Goal indices need to be an index array of length 8");
        }

        //Check if neighbors is valid
        if (neighbors == null || neighbors.size() != 8) {
            throw new IllegalArgumentException("Neighbours hashmap cannot be null");
        }

        //windmill construction for the current board position
        int[] windMillData = new int[8];

        for (int i = 0; i < 3; i++) {
            windMillData[i] = data[i];
        }

        windMillData[3] = data[5];

        int incVar = 4;
        for (int i = 8; i > 5; i--) {
            windMillData[incVar] = data[i];
            incVar++;
        }

        windMillData[7] = data[3];

        //End of windmill construction

        //Nilson calculation part 1
        for (int i = 0; i < windMillData.length; i++) {
            //Here, we check each index of windmill list of the current board position
            int key = windMillData[i];

            //check if the element at that index is in the hashmap
            if (neighbors.containsKey(key)) {
                //value is the correct neighbor
                int value = neighbors.get(key);

                //We iterate up to 7 because 8 wraps around to 1.T We check if neighbors are correct here.
                if (i < windMillData.length - 1) {
                    if (windMillData[(i + 1)] != value) {
                        nilsonVal++;
                    }
                } else { //The index 8 check.
                    if (windMillData[0] != value) {
                        nilsonVal++;
                    }
                }
            }
        }
        //End of Nilson calculation part 1

        //Manhattan Calculation: Index Array
        int[] indexArray = new int[8];
        int zeroIndex = -1;

        for (int i = 0; i < data.length; i++) {
            if (data[i] == 0) {
                //check to see where zero is in the board position
                zeroIndex = i;
                continue;
            }
            indexArray[data[i]-1] = i;
        }

        //End of Manhattan Index Array

        //Nilson calculation part 2
        if (zeroIndex == 4) {
            nilsonVal++;
        }
        //End of Nilson part 2

        //Manhattan Calculation: Computation
        //See TreeNode(int[] currentList, int[] goalIndices) for explanation of Manhattan distance computation
        for (int i = 0; i < indexArray.length; i++) {
            //Take the difference of the two indices
            int positionDiff = Math.abs(indexArray[i] - goalIndices[i]);

            //Special Edge Cases
            if ((indexArray[i] == 2 && goalIndices[i] == 6) || (indexArray[i] == 6 && goalIndices[i] == 2)){
                manhatDist += positionDiff;
                continue;
            }

            if ((indexArray[i] == 3 && goalIndices[i] == 2)|| (indexArray[i] == 2 && goalIndices[i] == 3) ){
                manhatDist += positionDiff+2;
                continue;
            }

            if ((indexArray[i] == 6 && goalIndices[i] == 5) || (indexArray[i] == 5 && goalIndices[i] == 6)) {
                manhatDist += positionDiff+2;
                continue;
            }

            //Manhattan Distance based on the difference of indices
            if (positionDiff < 3) {
                manhatDist += positionDiff;
            } else if (positionDiff < 6) {
                manhatDist += (positionDiff - 2);
            } else {
                manhatDist += (positionDiff - 4);
            }

        } //End of Manhattan calculation

        //Nilson calculation part 3: Multiply by 3.
        nilsonVal *= 3;

        //End of Nilson Constructor

    }


}










