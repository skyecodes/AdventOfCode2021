import java.io.IOException;
import java.util.Comparator;
import java.util.HashSet;
import java.util.stream.Stream;

public class Day15 {
    static int[][] riskGrid, totalRiskGrid;
    static int xSize, ySize;

    public static void main(String[] args) throws IOException {
        riskGrid = Util.readInput("Day15").stream()
                .map(s -> s.chars().map(Character::getNumericValue).toArray())
                .toArray(int[][]::new);
        System.out.println("Part 1: " + dijkstra());
        var newRiskGrid = new int[xSize * 5][ySize * 5];
        for (int dy = 0; dy < 5; dy++) {
            for (int dx = 0; dx < 5; dx++) {
                for (int y = 0; y < ySize; y++) {
                    for (int x = 0; x < xSize; x++) {
                        int val = riskGrid[y][x] + dx + dy;
                        if (val > 9) {
                            val = val - 9;
                        }
                        newRiskGrid[(ySize * dy) + y][(xSize * dx) + x] = val;
                    }
                }
            }
        }
        riskGrid = newRiskGrid;
        System.out.println("Part 2: " + dijkstra());
    }

    static int dijkstra() {
        ySize = riskGrid.length;
        xSize = riskGrid[0].length;
        totalRiskGrid = new int[ySize][xSize];
        var unsettledNodes = new HashSet<Node>();
        var settledNodes = new HashSet<Node>();
        for (int y = 0; y < ySize; y++) {
            for (int x = 0; x < xSize; x++) {
                totalRiskGrid[y][x] = Integer.MAX_VALUE;
            }
        }
        var start = new Node(0, 0);
        start.setTotalRisk(0);
        unsettledNodes.add(start);
        while (!unsettledNodes.isEmpty()) {
            var node = unsettledNodes.stream().min(Comparator.comparingInt(Node::getTotalRisk)).orElseThrow();
            unsettledNodes.remove(node);
            node.getAdjacentNodes().filter(n -> !settledNodes.contains(n)).forEach(adj -> {
                int totalRisk = node.getTotalRisk() + adj.getRisk();
                if (totalRisk < adj.getTotalRisk()) {
                    adj.setTotalRisk(totalRisk);
                }
                unsettledNodes.add(adj);
            });
            settledNodes.add(node);
        }
        return new Node(xSize - 1, ySize - 1).getTotalRisk();
    }

    record Node(int x, int y) {
        Stream<Node> getAdjacentNodes() {
            var nodes = Stream.<Node>builder();
            if (x != 0) {
                nodes.accept(new Node(x - 1, y));
            }
            if (x != xSize - 1) {
                nodes.accept(new Node(x + 1, y));
            }
            if (y != 0) {
                nodes.accept(new Node(x, y - 1));
            }
            if (y != ySize - 1) {
                nodes.accept(new Node(x, y + 1));
            }
            return nodes.build();
        }

        int getRisk() {
            return riskGrid[y][x];
        }

        int getTotalRisk() {
            return totalRiskGrid[y][x];
        }

        void setTotalRisk(int dist) {
            totalRiskGrid[y][x] = dist;
        }
    }
}
