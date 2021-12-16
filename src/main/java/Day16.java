import java.io.IOException;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class Day16 {
    static String data;
    static int index;

    public static void main(String[] args) throws IOException {
        data = Util.readInput("Day16").get(0).chars()
                .mapToObj(Character::toString)
                .map(s -> Integer.parseInt(s, 16))
                .map(Integer::toBinaryString)
                .map(s -> "0".repeat(4 - s.length()) + s)
                .collect(Collectors.joining());
        System.out.println("Part 1: " + part1());
        System.out.println("Part 2: " + part2());
    }

    static int part1() {
        index = 0;
        return readVersionNumber();
    }

    static int readVersionNumber() {
        int v = readInt(3);
        int t = readInt(3);
        if (t == 4) {
            int s;
            do {
                s = readInt(1);
                index += 4;
            } while (s != 0);
        } else {
            int i = readInt(1);
            if (i == 0) {
                int l = readInt(15);
                int max = index + l;
                while (index != max) {
                    v += readVersionNumber();
                }
            } else {
                int l = readInt(11);
                for (int j = 0; j < l; j++) {
                    v += readVersionNumber();
                }
            }
        }
        return v;
    }

    static long part2() {
        index = 0;
        return readValue();
    }

    static long readValue() {
        readInt(3);
        int t = readInt(3);
        if (t == 4) {
            int s;
            var n = new StringBuilder();
            do {
                s = readInt(1);
                n.append(data, index, index += 4);
            } while (s != 0);
            return parseLong(n.toString());
        } else {
            var stream = LongStream.builder();
            int i = readInt(1);
            if (i == 0) {
                int l = readInt(15);
                int max = index + l;
                while (index != max) {
                    stream.accept(readValue());
                }
            } else {
                int l = readInt(11);
                for (int j = 0; j < l; j++) {
                    stream.accept(readValue());
                }
            }
            var values = stream.build();
            return switch (t) {
                case 0 -> values.sum();
                case 1 -> values.reduce((a, b) -> a * b).orElseThrow();
                case 2 -> values.min().orElseThrow();
                case 3 -> values.max().orElseThrow();
                case 5 -> {
                    var array = values.toArray();
                    yield array[0] > array[1] ? 1 : 0;
                }
                case 6 -> {
                    var array = values.toArray();
                    yield array[0] < array[1] ? 1 : 0;
                }
                case 7 -> {
                    var array = values.toArray();
                    yield array[0] == array[1] ? 1 : 0;
                }
                default -> throw new RuntimeException();
            };
        }
    }

    static int readInt(int length) {
        return Integer.parseInt(data.substring(index, index += length), 2);
    }

    static long parseLong(String s) {
        return Long.parseLong(s, 2);
    }
}
