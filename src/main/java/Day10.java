import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Stack;

public class Day10 {
    public static void main(String[] args) throws IOException {
        var input = Util.readInput("Day10");
        System.out.println("Part 1: " + input.stream().mapToInt(Day10::part1).sum());
        var part2 = input.stream().mapToLong(Day10::part2).filter(n -> n != 0).sorted().toArray();
        System.out.println("Part 2: " + part2[(part2.length - 1) / 2]);
    }

    static int part1(String s) {
        var stack = new Stack<Bracket>();
        for (char c : s.toCharArray()) {
            var bracket = Bracket.find(c);
            if (c == bracket.open) {
                stack.push(bracket);
            } else if (stack.pop().close != c) {
                return bracket.score1;
            }
        }
        return 0;
    }

    static long part2(String s) {
        var stack = new Stack<Bracket>();
        for (char c : s.toCharArray()) {
            var bracket = Bracket.find(c);
            if (c == bracket.open) {
                stack.push(bracket);
            } else if (stack.pop().close != c) {
                return 0;
            }
        }
        Collections.reverse(stack);
        return stack.stream().mapToLong(b -> b.score2).reduce(0, (total, value) -> total * 5 + value);
    }

    enum Bracket {
        ROUND('(', ')', 3, 1),
        SQUARE('[', ']', 57, 2),
        CURLY('{', '}', 1197, 3),
        ARROW('<', '>', 25137, 4);

        char open, close;
        int score1, score2;

        Bracket(char open, char close, int score1, int score2) {
            this.open = open;
            this.close = close;
            this.score1 = score1;
            this.score2 = score2;
        }

        static Bracket find(char c) {
            return Arrays.stream(Bracket.values()).filter(b -> b.open == c || b.close == c).findAny().orElseThrow();
        }
    }
}
