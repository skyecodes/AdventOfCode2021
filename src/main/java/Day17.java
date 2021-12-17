import java.io.IOException;
import java.util.Arrays;
import java.util.OptionalInt;
import java.util.stream.IntStream;

public class Day17 {
    static int xMin, xMax, yMin, yMax;

    public static void main(String[] args) throws IOException {
        int[] values = Arrays.stream(Util.readExample("Day17").get(0).split(": ")[1].split(", "))
                .map(s -> s.split("=")[1])
                .flatMapToInt(s -> Arrays.stream(s.split("\\.\\.")).mapToInt(Integer::parseInt))
                .toArray();
        xMin = values[0];
        xMax = values[1];
        yMin = values[2];
        yMax = values[3];
        System.out.println("Part 1: " + part1());
        System.out.println("Part 2: " + part2());
    }

    static int part1() {
        return IntStream.range(0, xMax + 1).flatMap(vx -> IntStream.range(-Math.abs(yMin), Math.abs(yMin)).map(vy -> fire(vx, vy).orElse(Integer.MIN_VALUE))).max().orElseThrow();
    }

    static long part2() {
        return IntStream.range(0, xMax + 1).boxed().flatMap(vx -> IntStream.range(-Math.abs(yMin), Math.abs(yMin)).mapToObj(vy -> fire(vx, vy))).filter(OptionalInt::isPresent).count();
    }

    static OptionalInt fire(int vx, int vy) {
        return fire(0, 0, vx, vy, -1);
    }

    static OptionalInt fire(int x, int y, int vx, int vy, int maxY) {
        x += vx;
        y += vy;
        if (x >= xMin && x <= xMax && y >= yMin && y <= yMax) {
            return OptionalInt.of(maxY);
        } else if (x > xMax || y < yMin) {
            return OptionalInt.empty();
        }
        return fire(x, y, vx + Integer.compare(0, vx), vy - 1, Math.max(y, maxY));
    }
}
