import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

public class Day09 {
    static int[][] heightmap;
    static List<Point> lowPoints;

    public static void main(String[] args) throws IOException {
        heightmap = Util.readInput("Day09").stream()
                .map(s -> s.chars().map(Character::getNumericValue).toArray())
                .toArray(int[][]::new);
        lowPoints = getLowPoints();
        System.out.println("Part 1: " + part1());
        System.out.println("Part 2: " + part2());
    }

    static List<Point> getLowPoints() {
        var lowPoints = Stream.<Point>builder();
        for (int y = 0; y < heightmap.length; y++) {
            var row = heightmap[y];
            for (int x = 0; x < row.length; x++) {
                int val = row[x];
                var pos = new Point(x, y);
                if (pos.adjacent().allMatch(adj -> val < adj.val())) {
                    lowPoints.accept(pos);
                }
            }
        }
        return lowPoints.build().toList();
    }

    static int part1() {
        return lowPoints.stream().mapToInt(p -> heightmap[p.y][p.x] + 1).sum();
    }

    static long part2() {
        return lowPoints.stream().map(Day09::buildBasin).map(Stream::count).sorted(Comparator.reverseOrder()).limit(3).reduce(1L, (a, b) -> a * b, (a, b) -> a * b);
    }

    static Stream<Point> buildBasin(Point pos) {
        return Stream.concat(Stream.of(pos), pos.adjacent().filter(adj -> adj.val() > pos.val() && adj.val() != 9).flatMap(Day09::buildBasin)).distinct();
    }

    record Point(int x, int y) {
        Stream<Point> adjacent() {
            var points = Stream.<Point>builder();
            if (x != 0) {
                points.accept(new Point(x - 1, y));
            }
            if (x != heightmap[y].length - 1) {
                points.accept(new Point(x + 1, y));
            }
            if (y != 0) {
                points.accept(new Point(x, y - 1));
            }
            if (y != heightmap.length - 1) {
                points.accept(new Point(x, y + 1));
            }
            return points.build();
        }

        int val() {
            return heightmap[y][x];
        }
    }
}
