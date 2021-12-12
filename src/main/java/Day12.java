import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

public class Day12 {
    static Multimap<String, String> paths = HashMultimap.create();

    public static void main(String[] args) throws IOException {
        Util.readInput("Day12").stream()
                .map(s -> s.split("-"))
                .forEach(s -> {
            paths.put(s[0], s[1]);
            paths.put(s[1], s[0]);
        });
        System.out.println("Part 1: " + run(Day12::part1));
        System.out.println("Part 2: " + run(Day12::part2));
    }

    static int run(BiConsumer<Stack<String>, Set<String>> part) {
        var stack = new Stack<String>();
        var possiblePaths = new HashSet<String>();
        part.accept(stack, possiblePaths);
        return possiblePaths.size();
    }

    static void part1(Stack<String> stack, Set<String> possiblePaths) {
        visit("start", stack, possiblePaths, s -> true);
    }

    static void part2(Stack<String> stack, Set<String> possiblePaths) {
        paths.keySet().stream()
                .filter(s -> !s.equals("start") && !s.equals("end") && s.equals(s.toLowerCase()))
                .forEach(twice -> visit("start", stack, possiblePaths, current -> !current.equals(twice) || stack.stream().filter(s -> s.equals(current)).count() == 2));
    }

    static void visit(String s, Stack<String> stack, Set<String> possiblePaths, Predicate<String> condition) {
        if (s.equals("end")) {
            possiblePaths.add(String.join(",", stack));
            return;
        }
        if (s.equals(s.toLowerCase()) && stack.contains(s) && condition.test(s)) {
            return;
        }
        stack.push(s);
        paths.get(s).forEach(s1 -> visit(s1, stack, possiblePaths, condition));
        stack.pop();
    }
}
