import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Day03 {
    public static void main(String[] args) throws IOException {
        var diagnostic = Util.readInput("Day03");
        System.out.println("Part 1: " + part1(diagnostic));
        System.out.println("Part 2: " + part2(diagnostic));
    }

    private static int part1(List<String> diagnostic) {
        var sum = new int[diagnostic.get(0).length()];
        diagnostic.forEach(s -> {
            for (int i = 0; i < sum.length; i++) {
                if (s.charAt(i) == '1') {
                    sum[i]++;
                }
            }
        });
        var g = new StringBuilder();
        var e = new StringBuilder();
        for (int val : sum) {
            if (val > diagnostic.size() / 2) {
                g.append(1);
                e.append(0);
            } else {
                g.append(0);
                e.append(1);
            }
        }
        int gamma = bin2int(g.toString());
        int epsilon = bin2int(e.toString());
        return gamma * epsilon;
    }

    private static int part2(List<String> diagnostic) {
        var oxygenList = new ArrayList<>(diagnostic);
        var co2List = new ArrayList<>(diagnostic);
        int oxygen = bin2int(filter(oxygenList, 0, false));
        int co2 = bin2int(filter(co2List, 0, true));
        return oxygen * co2;
    }

    private static String filter(List<String> list, int index, boolean reversed) {
        var sum = list.stream().filter(s -> s.charAt(index) == '1').count();
        var mostCommonBit = sum >= list.size() / 2.;
        var bitToKeep = mostCommonBit ^ reversed ? '1' : '0';
        list.removeIf(s -> s.charAt(index) != bitToKeep);
        if (list.size() == 1) {
            return list.get(0);
        }
        return filter(list, index + 1, reversed);
    }

    private static int bin2int(String s) {
        return Integer.parseInt(s, 2);
    }
}
