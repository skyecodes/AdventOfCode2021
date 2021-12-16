import java.io.IOException;
import java.util.Comparator;
import java.util.HashSet;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day15 {
    static int[][] riskMap, totalRiskMap;
    static int xSize, ySize;

    public static void main(String[] args) throws IOException {
        riskMap = Util.readInput("Day15").stream()
                .map(s -> s.chars().map(Character::getNumericValue).toArray())
                .toArray(int[][]::new);
        System.out.println("Part 1: " + dijkstra());
        var newRiskMap = new int[xSize * 5][ySize * 5];
        IntStream.range(0, 5).forEach(dy -> IntStream.range(0, 5).forEach(dx -> IntStream.range(0, ySize).forEach(y -> IntStream.range(0, xSize).forEach(x -> newRiskMap[(ySize * dy) + y][(xSize * dx) + x] = (riskMap[y][x] + dx + dy - 1) % 9 + 1))));
        riskMap = newRiskMap;
        System.out.println("Part 2: " + dijkstra());
    }

    static int dijkstra() {
        ySize = riskMap.length;
        xSize = riskMap[0].length;
        totalRiskMap = new int[ySize][xSize];
        var unsettledNodes = new HashSet<Node>();
        var settledNodes = new HashSet<Node>();
        IntStream.range(0, ySize).forEach(y -> IntStream.range(0, xSize).forEach(x -> totalRiskMap[y][x] = Integer.MAX_VALUE));
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
            return riskMap[y][x];
        }

        int getTotalRisk() {
            return totalRiskMap[y][x];
        }

        void setTotalRisk(int dist) {
            totalRiskMap[y][x] = dist;
        }
    }
}
