import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.IntStream;

public class Day07 {
    public static void main(String[] args) throws IOException {
        var crabs = Arrays.stream(Util.readInput("Day07").get(0).split(","))
                .map(Integer::parseInt)
                .toList();
        System.out.println("Part 1: " + align(crabs, Day07::align1));
        System.out.println("Part 2: " + align(crabs, Day07::align2));
    }

    static int align(List<Integer> crabs, BiFunction<List<Integer>, Integer, Integer> alignFun) {
        var stats = crabs.stream().mapToInt(Integer::intValue).summaryStatistics();
        var align = IntStream.builder();
        IntStream.rangeClosed(stats.getMin(), stats.getMax()).map(i -> alignFun.apply(crabs, i)).forEach(align);
        return align.build().min().getAsInt();
    }

    static int align1(List<Integer> crabs, int pos) {
        return crabs.stream().mapToInt(crab -> Math.abs(crab - pos)).sum();
    }

    static int align2(List<Integer> crabs, int pos) {
        return crabs.stream().mapToInt(crab -> Math.abs(crab - pos)).map(n -> IntStream.rangeClosed(1, n).sum()).sum();
    }
}
