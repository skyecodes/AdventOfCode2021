import java.io.IOException;
import java.util.Arrays;

public class Day06 {
    public static void main(String[] args) throws IOException {
        var initialFishes = Arrays.stream(Util.readInput("Day06").get(0).split(","))
                .mapToInt(Integer::parseInt)
                .toArray();
        System.out.println("Part 1: " + run(initialFishes, 80));
        System.out.println("Part 2: " + run(initialFishes, 256));
    }

    static long run(int[] fishes, int days) {
        long[] values = new long[9];
        for (int fish : fishes) {
            values[fish]++;
        }
        for (int i = 0; i < days; i++) {
            long[] newValues = new long[9];
            System.arraycopy(values, 1, newValues, 0, values.length - 1);
            newValues[6] += values[0];
            newValues[8] = values[0];
            values = newValues;
        }
        return Arrays.stream(values).sum();
    }
}
