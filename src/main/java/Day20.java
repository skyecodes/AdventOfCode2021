import com.google.common.primitives.Booleans;

import java.io.IOException;
import java.util.Arrays;

public class Day20 {
    static boolean[] algo;
    static boolean[][] image;
    static boolean fillValue;

    public static void main(String[] args) throws IOException {
        var input = Util.readInput("Day20");
        algo = readString(input.get(0));
        image = input.subList(2, input.size()).stream().map(Day20::readString).toArray(boolean[][]::new);
        enhance();
        enhance();
        System.out.println("Part 1: " + count());
        for (int i = 0; i < 48; i++) {
            enhance();
        }
        System.out.println("Part 2: " + count());
    }

    static boolean[] readString(String s) {
        return Booleans.toArray(s.chars().mapToObj(i -> i == '#').toList());
    }

    static void enhance() {
        expandImage();
        enhanceImage();
        fillValue = fillValue ? algo[algo.length - 1] : algo[0];
    }

    static void expandImage() {
        var newImage = new boolean[image.length + 2][image[0].length + 2];
        for (int y = 0; y < newImage.length; y++) {
            if (y == 0 || y == newImage.length - 1) {
                Arrays.fill(newImage[y], fillValue);
            } else {
                boolean[] row = newImage[y];
                for (int x = 0; x < row.length; x++) {
                    if (x == 0 || x == row.length - 1) {
                        row[x] = fillValue;
                    } else {
                        row[x] = image[y - 1][x - 1];
                    }
                }
            }
        }
        image = newImage;
    }

    static void enhanceImage() {
        var newImage = new boolean[image.length][image[0].length];
        for (int y = 0; y < newImage.length; y++) {
            boolean[] row = newImage[y];
            for (int x = 0; x < row.length; x++) {
                row[x] = enhanceValue(x, y);
            }
        }
        image = newImage;
    }

    static boolean enhanceValue(int x, int y) {
        var builder = new StringBuilder();
        for (int yy = y - 1; yy <= y + 1; yy++) {
            if (yy < 0 || yy >= image.length) {
                builder.append(fillValue ? "111" : "000");
            } else {
                var row = image[yy];
                for (int xx = x - 1; xx <= x + 1; xx++) {
                    if (xx < 0 || xx >= row.length) {
                        builder.append(fillValue ? 1 : 0);
                    } else {
                        builder.append(row[xx] ? 1 : 0);
                    }
                }
            }
        }
        return algo[Integer.parseInt(builder.toString(), 2)];
    }

    static long count() {
        return Arrays.stream(image).flatMap(b -> Booleans.asList(b).stream()).filter(b -> b).count();
    }
}
