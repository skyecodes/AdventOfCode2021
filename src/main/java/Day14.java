import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Day14 {
    public static void main(String[] args) throws IOException {
        var input = Util.readInput("Day14");
        var template = input.get(0);
        var pairs = input.stream().skip(2)
                .map(s -> s.split(" -> "))
                .collect(Collectors.toMap(s -> s[0], s -> s[1].charAt(0)));
        System.out.println("Part 1: " + part1(template, pairs));
        System.out.println("Part 2: " + part2(template, pairs));
    }

    static long part1(String template, Map<String, Character> pairs) {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < template.length() - 1; j += 2) {
                var pair = template.substring(j, j + 2);
                var insert = pairs.get(pair);
                template = template.substring(0, j + 1) + insert + template.substring(j + 1);
            }
        }
        var finalTemplate = template;
        var stats = template.chars().distinct().mapToLong(c -> finalTemplate.chars().filter(i -> i == c).count()).summaryStatistics();
        return stats.getMax() - stats.getMin();
    }

    static long part2(String template, Map<String, Character> pairs) {
        var pairCount = new HashMap<String, Long>();
        for (int i = 0; i < template.length() - 1; i++) {
            var pair = template.substring(i, i + 2);
            incr(pairCount, pair, 1);
        }
        for (int i = 0; i < 40; i++) {
            var newCount = new HashMap<String, Long>();
            pairCount.forEach((pair, count) -> {
                var insert = pairs.get(pair);
                incr(newCount, "" + pair.charAt(0) + insert, count);
                incr(newCount, "" + insert + pair.charAt(1), count);
            });
            pairCount = newCount;
        }
        var charCount = new HashMap<Character, Long>();
        pairCount.forEach((pair, count) -> incr(charCount, pair.charAt(0), count));
        incr(charCount, template.charAt(template.length() - 1), 1);
        var stats = charCount.values().stream().mapToLong(l -> l).summaryStatistics();
        return stats.getMax() - stats.getMin();
    }

    static <K> void incr(Map<K, Long> map, K key, long value) {
        map.merge(key, value, Long::sum);
    }
}
