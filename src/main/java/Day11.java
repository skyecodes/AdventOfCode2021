import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day11 {
    static int[][] octopuses;

    public static void main(String[] args) throws IOException {
        var input = Util.readInput("Day11").stream()
                .map(s -> s.chars().map(Character::getNumericValue).toArray())
                .toArray(int[][]::new);
        octopuses = cloneInput(input);
        System.out.println("Part 1: " + part1());
        octopuses = cloneInput(input);
        System.out.println("Part 2: " + part2());
    }

    private static int[][] cloneInput(int[][] input) {
        int length = input.length;
        int[][] target = new int[length][input[0].length];
        for (int i = 0; i < length; i++) {
            System.arraycopy(input[i], 0, target[i], 0, input[i].length);
        }
        return target;
    }

    static int part1() {
        int flashes = 0;
        var flashed = new HashSet<Pos>();
        for (int step = 0; step < 1000; step++) {
            runStep(flashed);
            flashed.forEach(pos -> octopuses[pos.y][pos.x] = 0);
            flashes += flashed.size();
        }
        return flashes;
    }

    static int part2() {
        int totalSize = octopuses.length * octopuses[0].length;
        var flashed = new HashSet<Pos>();
        for (int step = 0; step < 10000; step++) {
            runStep(flashed);
            if (flashed.size() == totalSize) {
                return step + 1;
            }
            flashed.forEach(pos -> octopuses[pos.y][pos.x] = 0);
        }
        throw new RuntimeException();
    }

    private static void runStep(Collection<Pos> flashed) {
        flashed.clear();
        Arrays.stream(octopuses).forEach(row -> IntStream.range(0, row.length).forEach(x -> row[x]++));
        boolean hasFlashed;
        do {
            hasFlashed = false;
            for (int y = 0; y < octopuses.length; y++) {
                var row = octopuses[y];
                for (int x = 0; x < row.length; x++) {
                    var pos = new Pos(x, y);
                    if (flashed.contains(pos)) continue;
                    if (row[x] > 9) {
                        pos.adjacent().forEach(adj -> octopuses[adj.y][adj.x]++);
                        hasFlashed = true;
                        flashed.add(pos);
                    }
                }
            }
        } while (hasFlashed);
    }

    record Pos(int x, int y) {
        Stream<Pos> adjacent() {
            var points = Stream.<Pos>builder();
            if (x != 0) {
                points.accept(new Pos(x - 1, y));
                if (y != 0) {
                    points.accept(new Pos(x - 1, y - 1));
                }
                if (y != octopuses.length - 1) {
                    points.accept(new Pos(x - 1, y + 1));
                }
            }
            if (x != octopuses[y].length - 1) {
                points.accept(new Pos(x + 1, y));
                if (y != 0) {
                    points.accept(new Pos(x + 1, y - 1));
                }
                if (y != octopuses.length - 1) {
                    points.accept(new Pos(x + 1, y + 1));
                }
            }
            if (y != 0) {
                points.accept(new Pos(x, y - 1));
            }
            if (y != octopuses.length - 1) {
                points.accept(new Pos(x, y + 1));
            }
            return points.build();
        }
    }
}
