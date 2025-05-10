import java.util.*;

public class EightPuzzleSolverAstr {

    private PriorityQueue<Node> priorityQueue;
    private Set<Node> settled;
    private Map<Node, Integer> distances;
    private int[][] goalState = {
            {6, 5, 4},
            {7, 0, 3},
            {8, 1, 2}
    };
    private int[] dx = {0, 1, 0, -1, 1, 1, -1, -1};
    private int[] dy = {1, 0, -1, 0, 1, -1, -1, 1};
    private List<int[][]> optimalPath = new ArrayList<>();
    private int extensionsCount = 0;

    public EightPuzzleSolverAstr() {
        this.settled = new HashSet<>();
        // Updated comparator to handle ties correctly
        this.priorityQueue = new PriorityQueue<>(Comparator.comparingInt(this::getTotalCost)
                                                                .thenComparingInt(node -> calculateHeuristic(node.state)));
        this.distances = new HashMap<>();
    }

    public int[][] Astar(int[][] initialState) {
        Node start = new Node(initialState, 0);
        priorityQueue.add(start);
        distances.put(start, 0);

        while (!priorityQueue.isEmpty()) {
            Node current = priorityQueue.poll();
            extensionsCount++;

            if (Arrays.deepEquals(current.state, goalState)) {
                System.out.println("Cost: " + distances.get(current));
                buildOptimalPath(current);
                return current.state;
            }

            settled.add(current);

            for (int i = 0; i < 8; i++) {
                int newX = current.emptyX + dx[i];
                int newY = current.emptyY + dy[i];

                if (isValid(newX, newY)) {
                    int[][] newState = swap(current.state, current.emptyX, current.emptyY, newX, newY);
                    int newCost = distances.get(current) + 1;

                    Node newNode = new Node(newState, newCost, newX, newY, current);

                    // Calculate Manhattan Distance heuristic
                    int heuristic = calculateHeuristic(newState);
                    int totalCost = newCost + heuristic;

                    // Check if the new node is not settled and its distance from start is less than previous
                    if (!settled.contains(newNode) && (distances.getOrDefault(newNode, Integer.MAX_VALUE) > newCost)) {
                        distances.put(newNode, newCost);
                        priorityQueue.add(newNode);
                    }
                }
            }
        }
        return null;
    }

    // Calculate total cost (distance from start + heuristic value)
    private int getTotalCost(Node node) {
        return distances.getOrDefault(node, 0) + calculateHeuristic(node.state);
    }

    // Calculate Manhattan Distance heuristic
    private int calculateHeuristic(int[][] state) {
        int count = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (state[i][j] != goalState[i][j] && state[i][j] != 0) {
                    int goalX = (state[i][j] - 1) / 3;
                    int goalY = (state[i][j] - 1) % 3;
                    count += Math.abs(i - goalX) + Math.abs(j - goalY);
                }
            }
        }
        return count;
    }

    private void buildOptimalPath(Node node) {
        optimalPath.clear();
        while (node != null) {
            optimalPath.add(0, node.state);
            node = node.parent;
        }
    }

    private boolean isValid(int x, int y) {
        return x >= 0 && x < 3 && y >= 0 && y < 3;
    }

    private int[][] swap(int[][] state, int x1, int y1, int x2, int y2) {
        int[][] newState = copyState(state);
        int temp = newState[x1][y1];
        newState[x1][y1] = newState[x2][y2];
        newState[x2][y2] = temp;
        return newState;
    }

    private int[][] copyState(int[][] state) {
        int[][] newState = new int[3][3];
        for (int i = 0; i < 3; i++) {
            System.arraycopy(state[i], 0, newState[i], 0, 3);
        }
        return newState;
    }

    public static void printState(int[][] state) {
        for (int[] row : state) {
            System.out.println(Arrays.toString(row));
        }
    }

    public static void printPath(List<int[][]> path) {
        for (int[][] state : path) {
            printState(state);
            System.out.println("------");
        }
    }

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        int[][] initialState = {
            {6, 0, 4},
            {1, 7, 3},
            {8, 5, 2}
        };
		// int[][] initialState = {
          //  {4, 0, 8},
            //{1, 7, 6},
            //{3, 5, 2}
        //};
		// int[][] initialState = {
          //  {6, 5, 0},
            //{7, 1, 3},
            //{8, 4, 2}
        //};
		// int[][] initialState = {
          //  {6, 5, 4},
            //{3, 2, 7},
            //{8, 1, 0}
        //};
		// int[][] initialState = {
          //  {6, 5, 4},
            //{0, 1, 3},
            //{8, 7, 2}
        //};



        EightPuzzleSolverAstr solver = new EightPuzzleSolverAstr();
        int[][] solution = solver.Astar(initialState);

        if (solution != null) {
            printPath(solver.optimalPath);
            System.out.println("Number of node extensions: " + solver.extensionsCount);
        } else {
            System.out.println("No solution found.");
        }
        scan.close();
    }

    static class Node {
        int[][] state;
        int emptyX;
        int emptyY;
        Node parent;

        public Node(int[][] state, int cost) {
            this.state = state;
            findEmptyPosition(state);
        }

        public Node(int[][] state, int cost, int emptyX, int emptyY, Node parent) {
            this.state = state;
            this.emptyX = emptyX;
            this.emptyY = emptyY;
            this.parent = parent;
        }

        private void findEmptyPosition(int[][] state) {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (state[i][j] == 0) {
                        emptyX = i;
                        emptyY = j;
                        return;
                    }
                }
            }
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Node)) return false;
            Node node = (Node) o;
            return Arrays.deepEquals(state, node.state);
        }

        @Override
        public int hashCode() {
            return Arrays.deepHashCode(state);
        }
    }
}
