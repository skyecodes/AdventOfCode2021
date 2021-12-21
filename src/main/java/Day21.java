import java.io.IOException;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

public class Day21 {
    static int[] positions;
    static int[] scores;

    public static void main(String[] args) throws IOException {
        var input = Util.readInput("Day21").stream().mapToInt(s -> Integer.parseInt(s.split(": ")[1])).toArray();
        positions = input.clone();
        System.out.println("Part 1: " + part1());
        positions = input.clone();
        System.out.println("Part 2: " + part2());
    }

    static int part1() {
        scores = new int[positions.length];
        int dieValue = 0;
        int rolls = 0;
        while (true) {
            for (int i = 0; i < positions.length; i++) {
                int roll = (dieValue++ % 100) + (dieValue++ % 100) + (dieValue % 100) + 3;
                dieValue = dieValue % 100 + 1;
                rolls += 3;
                positions[i] = (positions[i] + roll - 1) % 10 + 1;
                if ((scores[i] += positions[i]) >= 1000) {
                    return IntStream.of(scores).min().orElseThrow() * rolls;
                }
            }
        }
    }

    static long[] outcomes;

    static long part2() {
        scores = new int[positions.length];
        outcomes = new long[positions.length];
        roll(0, 1);
        return LongStream.of(outcomes).max().orElseThrow();
    }

    static void roll(int player, long multiplier) {
        IntStream.range(3, 10).forEach(roll -> computeOutcomes(player, roll, multiplier * switch (roll) {
            case 4, 8 -> 3;
            case 5, 7 -> 6;
            case 6 -> 7;
            default -> 1;
        }));
    }

    static void computeOutcomes(int player, int roll, long multiplier) {
        int oldPos = positions[player];
        int oldScore = scores[player];
        positions[player] = (oldPos + roll - 1) % 10 + 1;
        if ((scores[player] += positions[player]) >= 21) {
            outcomes[player] += multiplier;
        } else {
            roll((player + 1) % positions.length, multiplier);
        }
        positions[player] = oldPos;
        scores[player] = oldScore;
    }
}
