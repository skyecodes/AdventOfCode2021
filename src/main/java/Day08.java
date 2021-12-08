import com.google.common.primitives.Ints;

import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

public class Day08 {
    public static void main(String[] args) throws IOException {
        var input = Util.readInput("Day08").stream()
                .map(line -> line.split(" \\| "))
                .map(Arrays::stream)
                .map(patterns -> patterns
                        .map(s -> s.split(" "))
                        .map(Arrays::stream)
                        .map(digit -> digit
                                .map(String::chars)
                                .map(d -> d.map(i -> i - 97).toArray())
                                .map(Digit::new)
                                .toArray(Digit[]::new))
                        .toArray(Digit[][]::new))
                .map(digits -> new Input(digits[0], digits[1]))
                .toList();
        System.out.println("Part 1: " + part1(input));
        System.out.println("Part 2: " + part2(input));
    }

    static long part1(List<Input> input) {
        return input.stream()
                .map(i -> i.output)
                .flatMap(Arrays::stream)
                .mapToInt(digit -> digit.lines.length)
                .filter(len -> len == 2 || len == 3 || len == 4 || len == 7)
                .count();
    }


    static long part2(List<Input> input) {
        return input.stream()
                .mapToInt(Input::decode)
                .sum();
    }

    record Input(Digit[] patterns, Digit[] output) {
        int decode() {
            Arrays.sort(patterns, Comparator.comparingInt(pattern -> pattern.lines.length));
            var reversedMappings = new int[7];
            var rightLines = Ints.asList(patterns[0].lines);
            reversedMappings[0] = Arrays.stream(patterns[1].lines).filter(i -> !rightLines.contains(i)).findFirst().orElseThrow();
            var topLeftAndMiddleLines = Arrays.stream(patterns[2].lines).filter(i -> !rightLines.contains(i)).boxed().toList();
            var bottomLeftAndBottomLines = Arrays.stream(patterns[9].lines).filter(i -> i != reversedMappings[0] && !rightLines.contains(i) && !topLeftAndMiddleLines.contains(i)).boxed().toList();
            var rightCount = count(rightLines);
            var b = rightCount[0] == 8;
            reversedMappings[2] = rightLines.get(b ? 0 : 1);
            reversedMappings[5] = rightLines.get(b ? 1 : 0);
            var topLeftAndMiddleCount = count(topLeftAndMiddleLines);
            b = topLeftAndMiddleCount[0] == 6;
            reversedMappings[1] = topLeftAndMiddleLines.get(b ? 0 : 1);
            reversedMappings[3] = topLeftAndMiddleLines.get(b ? 1 : 0);
            var bottomLeftAndBottomCount = count(bottomLeftAndBottomLines);
            b = bottomLeftAndBottomCount[0] == 4;
            reversedMappings[4] = bottomLeftAndBottomLines.get(b ? 0 : 1);
            reversedMappings[6] = bottomLeftAndBottomLines.get(b ? 1 : 0);
            var mappings = new int[7];
            IntStream.range(0, 7).forEach(i -> mappings[reversedMappings[i]] = i);
            return Integer.parseInt(Arrays.stream(output)
                    .map(digit -> Arrays.stream(digit.lines)
                            .map(line -> mappings[line])
                            .toArray())
                    .map(Digit::new)
                    .map(Digit::read)
                    .reduce(new StringBuilder(), StringBuilder::append, StringBuilder::append).toString());
        }

        /** Counts the number of occurrences of a certain line in the patterns */
        int[] count(List<Integer> lines) {
            return lines.stream()
                    .map(line -> Arrays.stream(patterns)
                            .map(n -> Ints.asList(n.lines))
                            .filter(ls -> ls.contains(line))
                            .count())
                    .mapToInt(Long::intValue)
                    .toArray();
        }
    }

    record Digit(int[] lines) {
        int read() {
            var list = Ints.asList(lines);
            return switch (lines.length) {
                case 2 -> 1;
                case 3 -> 7;
                case 4 -> 4;
                case 7 -> 8;
                case 5 -> {
                    if (list.contains(2)) {
                        if (list.contains(4)) {
                            yield 2;
                        }
                        yield 3;
                    }
                    yield 5;
                }
                case 6 -> {
                    if (!list.contains(3)) {
                        yield 0;
                    }
                    if (list.contains(4)) {
                        yield 6;
                    }
                    yield 9;
                }
                default -> throw new RuntimeException();
            };
        }
    }
}