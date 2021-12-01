import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Day01 {
    public static void main(String[] args) throws IOException {
        List<Integer> measurements = Files.readAllLines(Paths.get("resources/Day01/input.txt")).stream().map(Integer::parseInt).toList();
        part1(measurements);
        part2(measurements);
    }

    private static void part1(List<Integer> measurements) {
        int previous = -1, result = -1;
        for (int measurement : measurements) {
            if (measurement > previous) {
                result++;
            }
            previous = measurement;
        }
        System.out.println("Part 1: " + result);
    }

    private static void part2(List<Integer> measurements) {
        int[] window = {-1, -1, -1};
        int sum, previousSum = -1, result = -3;
        for (int measurement : measurements) {
            window[0] = window[1];
            window[1] = window[2];
            window[2] = measurement;
            sum = window[0] + window[1] + window[2];
            if (sum > previousSum) {
                result++;
            }
            previousSum = sum;
        }
        System.out.println("Part 2: " + result);
    }
}
