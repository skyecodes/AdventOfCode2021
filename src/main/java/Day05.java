import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.IntStream;

public class Day05 {
    public static void main(String[] args) throws IOException {
        var input = Util.readInput("Day05");
        var lines = input.stream()
                .map(s -> s.replaceAll(" ", ""))
                .map(s -> s.split("->"))
                .map(s -> Arrays.stream(s)
                        .map(s0 -> s0.split(","))
                        .map(s0 -> Arrays.stream(s0)
                                .mapToInt(Integer::parseInt)
                                .toArray())
                        .toArray(int[][]::new))
                .map(a -> new Line(a[0][0], a[0][1], a[1][0], a[1][1]))
                .toList();
        int maxX = lines.stream()
                .flatMapToInt(line -> IntStream.of(line.x1, line.x2))
                .max().getAsInt();
        int maxY = lines.stream()
                .flatMapToInt(line -> IntStream.of(line.y1, line.y2))
                .max().getAsInt();
        int[][] diagram1 = new int[maxY + 1][maxX + 1];
        int[][] diagram2 = new int[maxY + 1][maxX + 1];
        System.out.println("Part 1: " + part1(lines, diagram1));
        System.out.println("Part 2: " + part2(lines, diagram2));
    }

    static long part1(List<Line> lines, int[][] diagram) {
        return fillDiagram(lines, diagram, Line::valid1);
    }

    static long part2(List<Line> lines, int[][] diagram) {
        return fillDiagram(lines, diagram, Line::valid2);
    }

    static long fillDiagram(List<Line> lines, int[][] diagram, Predicate<Line> filter) {
        lines.stream().filter(filter).forEach(line -> line.fill(diagram));
        return Arrays.stream(diagram)
                .flatMapToInt(Arrays::stream)
                .filter(i -> i > 1).count();
    }

    static class Line {
        int x1, y1, x2, y2;
        boolean horiz, vert, diag;

        Line(int x1, int y1, int x2, int y2) {
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
            horiz = y1 == y2;
            vert = x1 == x2;
            diag = Math.abs(x1 - x2) == Math.abs(y1 - y2);
        }

        boolean valid1() {
            return horiz || vert;
        }

        boolean valid2() {
            return valid1() || diag;
        }

        void fill(int[][] diagram) {
            if (horiz) {
                int start = Math.min(x1, x2);
                int end = Math.max(x1, x2);
                for (int x = start; x <= end; x++) {
                    diagram[y1][x]++;
                }
            } else if (vert) {
                int start = Math.min(y1, y2);
                int end = Math.max(y1, y2);
                for (int y = start; y <= end; y++) {
                    diagram[y][x1]++;
                }
            } else { // diag
                int len = Math.max(x1, x2) - Math.min(x1, x2);
                int xDir = x1 < x2 ? 1 : -1;
                int yDir = y1 < y2 ? 1 : -1;
                for (int i = 0; i <= len; i++) {
                    diagram[y1 + i * yDir][x1 + i * xDir]++;
                }
            }
        }
    }
}
