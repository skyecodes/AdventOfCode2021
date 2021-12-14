import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class Day13 {
    public static void main(String[] args) throws IOException {
        var input = Util.readInput("Day13");
        var dots = input.stream().takeWhile(s -> !s.isEmpty())
                .map(s -> s.split(","))
                .map(s -> new Dot(Integer.parseInt(s[0]), Integer.parseInt(s[1])))
                .toList();
        var folds = input.stream().dropWhile(s -> !s.isEmpty()).skip(1)
                .map(s -> s.split(" ")[2])
                .map(s -> s.split("=")[0])
                .toList();
        int maxX = dots.stream().mapToInt(dot -> dot.x).max().orElseThrow();
        int maxY = dots.stream().mapToInt(dot -> dot.y).max().orElseThrow();
        var grid = new boolean[maxY + 1][maxX + 1];
        dots.forEach(dot -> grid[dot.y][dot.x] = true);
        System.out.println("Part 1: " + part1(grid, folds.get(0)));
        part2(grid, folds);
    }

    static boolean[][] fold(boolean[][] grid, List<String> folds) {
        for (var fold : folds) {
            boolean[][] newGrid;
            if (fold.equals("x")) {
                newGrid = new boolean[grid.length][(grid[0].length - 1) / 2];
                for (int y = 0; y < newGrid.length; y++) {
                    var row = newGrid[y];
                    for (int x = 0; x < row.length; x++) {
                        row[x] = grid[y][x] || grid[y][grid[0].length - 1 - x];
                    }
                }
            } else {
                newGrid = new boolean[(grid.length - 1) / 2][grid[0].length];
                for (int y = 0; y < newGrid.length; y++) {
                    var row = newGrid[y];
                    for (int x = 0; x < row.length; x++) {
                        row[x] = grid[y][x] || grid[grid.length - 1 - y][x];
                    }
                }
            }
            grid = newGrid;
        }
        return grid;
    }

    static long part1(boolean[][] grid, String fold) {
        grid = fold(grid, List.of(fold));
        return Arrays.stream(grid).flatMap(b -> IntStream.range(0, b.length).mapToObj(i -> b[i]))
                .filter(Boolean::booleanValue).count();
    }

    static void part2(boolean[][] grid, List<String> folds) {
        grid = fold(grid, folds);
        for (var row : grid) {
            for (var dot : row) {
                System.out.print(dot ? "#" : ".");
            }
            System.out.println();
        }
    }

    record Dot(int x, int y) {
    }
}
