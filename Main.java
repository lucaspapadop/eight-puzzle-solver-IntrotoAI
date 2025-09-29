import java.util.Arrays;
import java.util.Random;
import java.time.Instant;

//Course: 3642-01
//Student Name: Lucas Papadopoulos
//Student ID: 001078068
//Assignment #: 2
//Due Date: 10/18/24
// Signature: LP

public class Main {
    public static void main(String[] args) {

        try {

            int[] goal = {1, 2, 3, 8, 0, 4, 7, 6, 5};

            Queue solutionQueueUCS;
            Queue solutionQueueAStarManhattan;
            Queue solutionQueueAStarNilson;

            //Start of Random Board Position Construction
            int[] currentList = new int[9];

            for (int i = 0; i < 9; i++) {
                currentList[i] = i;
            }

            Random random = new Random();

            //Populating a list of size 9 with random values, with a permutation of [1,2,3...8].
            //Uses Fisher-Yates, or Knuth Shuffle. Generates a random int with i as its bound and swaps i and j
            for (int i = currentList.length - 1; i > 0; i--) {
                int j = random.nextInt(i + 1);
                int temp = currentList[i];
                currentList[i] = currentList[j];
                currentList[j] = temp;
            }

            //End of Random Board Position Construction

            //Print of current board position
            System.out.println("The random starting position is: ");
            for (int i = 0; i < currentList.length; i++) {
                if (i % 3 == 0) {
                    System.out.print("[ " + currentList[i]);
                } else if (i == 1 || i == 4 || i == 7) {
                    System.out.print(" " + currentList[i] + " ");
                } else {
                    System.out.print(currentList[i] + " ] \n");
                }
            }
            System.out.println();

            Instant start = Instant.now();
            //ucsAlgorithm ucs = new ucsAlgorithm(currentList, goal);
            //aSManhattan aStarMan = new aSManhattan(currentList, goal);
            aSNilson nilson = new aSNilson(currentList, goal);

            //solutionQueueUCS = ucsAlgorithm.solutionQueue;
            //solutionQueueAStarManhattan = aSManhattan.solutionQueue;
            solutionQueueAStarNilson = aSNilson.solutionQueue;

            Instant end = Instant.now();

            //UCS Print


            /*while (!solutionQueueUCS.isEmpty()) {
                TreeNode printNode = solutionQueueUCS.dequeue();
                int[] printArray = printNode.data;
                System.out.println(Arrays.toString(printArray));
                if (Arrays.equals(goal, printArray)) {
                    System.out.println("The path cost is " + printNode.depth);
                }

            }

            System.out.println("The number of visited nodes is " + ucsAlgorithm.visitedNodes); */

            //Manhattan Print

            /* while (!solutionQueueAStarManhattan.isEmpty()) {
                TreeNode printNode = solutionQueueAStarManhattan.dequeue();
                int[] printArray = printNode.data;
                System.out.println(Arrays.toString(printArray));
                if (Arrays.equals(goal, printArray)) {
                    System.out.println("The path cost is " + printNode.depth);
                }

            }

            System.out.println("The number of visited nodes is " + aStarMan.visitedNodes); */

            //Nilson Print

            while (!solutionQueueAStarNilson.isEmpty()) {
                TreeNode printNode = solutionQueueAStarNilson.dequeue();
                int[] printArray = printNode.data;
                System.out.println(Arrays.toString(printArray));
                if (Arrays.equals(goal, printArray)) {
                    System.out.println("The path cost is " + printNode.depth);
                }

            }

            System.out.println("The number of visited nodes is " + aSNilson.visitedNodes);

            //Time print

            long timeElapsed = end.toEpochMilli() - start.toEpochMilli();
            System.out.println("Execution time: " + timeElapsed + " ms");

            //Best, Worst, and Average Calculations and Print

            /*

            int[] ucsVisitList = {172523,133573, 11506, 293726, 316794, 34146, 19648, 10594, 84969, 12204, 15523, 97124,
            103125, 21716, 151169, 8225, 95123, 301366, 145620, 21006};

            int[] ucsTime = {457, 413, 81, 732, 794, 132, 106, 79, 256, 75, 93, 341, 282, 130, 407, 63, 268, 727, 437, 116};

            double ucsAverageVisit = calculateAverage(ucsVisitList);
            double ucsAverageTime = calculateAverage(ucsTime);
            System.out.println("\nAverage UCS visited Nodes: " + ucsAverageVisit + "\nAverage UCS Time: " + ucsAverageTime + " ms");

            int[] manhattanVisitList = {111, 79, 1021, 134, 18, 36, 19, 10, 241, 806, 441, 419, 1018, 108, 184, 580,
            1102, 705, 170, 807};

            int[] manhattanTime = {8,8,19,9,6,6,12,4,10,18,14,18,22,9,14,17,21,18,10,22};

            double manhattanAverageVisit = calculateAverage(manhattanVisitList);
            double manhattanAverageTime = calculateAverage(manhattanTime);
            System.out.println("\n\nAverage Manhattan visited Nodes: " + manhattanAverageVisit + "\nAverage Manhattan Time: " + manhattanAverageTime + " ms");

            int[] nilsonVisitList = {194, 49, 40, 48, 64, 64, 88, 31, 49, 34, 135, 216, 68, 128, 78, 229, 27, 35, 119, 53};

            int[] nilsonTime = {10, 8, 8, 6, 8, 6, 10, 5, 6, 7, 9, 14, 10, 13, 9, 16, 5, 9, 10, 9};

            double nilsonAverageVisit = calculateAverage(nilsonVisitList);
            double nilsonAverageTime = calculateAverage(nilsonTime);
            System.out.println("\n\nAverage Nilson visited Nodes: " + nilsonAverageVisit + "\nAverage Nilson Time: " + nilsonAverageTime + " ms");

            */

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Used for calculating average in main
    public static double calculateAverage(int[] numbers) {
        int sum = 0;
        for (int num : numbers) {
            sum += num;
        }
        return sum / (double) numbers.length;
    }
}