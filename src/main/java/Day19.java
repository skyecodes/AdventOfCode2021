import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

public class Day19 {
    static Orientation[] orientations = {
            Orientation.DEFAULT,
            p -> new Pos(p.x, -p.y, -p.z),
            p -> new Pos(p.x, p.z, -p.y),
            p -> new Pos(p.x, -p.z, p.y),
            p -> new Pos(-p.x, p.y, -p.z),
            p -> new Pos(-p.x, -p.y, p.z),
            p -> new Pos(-p.x, p.z, p.y),
            p -> new Pos(-p.x, -p.z, -p.y),
            p -> new Pos(p.y, p.x, -p.z),
            p -> new Pos(p.y, -p.x, p.z),
            p -> new Pos(p.y, p.z, p.x),
            p -> new Pos(p.y, -p.z, -p.x),
            p -> new Pos(-p.y, p.x, p.z),
            p -> new Pos(-p.y, -p.x, -p.z),
            p -> new Pos(-p.y, p.z, -p.x),
            p -> new Pos(-p.y, -p.z, p.x),
            p -> new Pos(p.z, p.x, p.y),
            p -> new Pos(p.z, -p.x, -p.y),
            p -> new Pos(p.z, p.y, -p.x),
            p -> new Pos(p.z, -p.y, p.x),
            p -> new Pos(-p.z, p.x, -p.y),
            p -> new Pos(-p.z, -p.x, p.y),
            p -> new Pos(-p.z, p.y, p.x),
            p -> new Pos(-p.z, -p.y, -p.x)
    };

    public static void main(String[] args) throws IOException {
        var input = Util.readInput("Day19");
        var remainingScanners = new ArrayList<Scanner>();
        var scanner = new Scanner();
        for (var s : input) {
            if (s.isEmpty()) {
                remainingScanners.add(scanner);
                scanner = new Scanner();
            } else if (!s.startsWith("---")) {
                int[] pos = Arrays.stream(s.split(",")).mapToInt(Integer::parseInt).toArray();
                scanner.beacons.add(new Pos(pos[0], pos[1], pos[2]));
            }
        }
        remainingScanners.add(scanner);
        var foundScanners = new ArrayList<Scanner>();
        scanner = remainingScanners.remove(0);
        scanner.setAbsoluteBeacons(Pos.ZERO, Orientation.DEFAULT);
        foundScanners.add(scanner);
        for (int i = 0; i < foundScanners.size(); i++) {
            var a = foundScanners.get(i);
            for (Iterator<Scanner> iterator = remainingScanners.iterator(); iterator.hasNext();) {
                Scanner b = iterator.next();
                if (checkOverlap(a, b)) {
                    iterator.remove();
                    foundScanners.add(b);
                }
            }
        }
        System.out.println("Part 1: " + foundScanners.stream().flatMap(Scanner::getAbsoluteBeacons).distinct().count());
        System.out.println("Part 2: " + foundScanners.stream().map(s -> s.absolutePos).flatMapToInt(a -> foundScanners.stream().map(s -> s.absolutePos).mapToInt(b -> b.manhattanDistance(a))).max().orElseThrow());
    }

    static boolean checkOverlap(Scanner a, Scanner b) {
        for (Orientation o : orientations) {
            for (Pos absolutePos : a.getAbsoluteBeacons().flatMap(posA -> b.getBeacons(o).map(posA::subtract)).toList()) {
                if (a.getAbsoluteBeacons().filter(posA -> b.getBeacons(absolutePos, o).anyMatch(posA::equals)).count() >= 12) {
                    b.setAbsoluteBeacons(absolutePos, o);
                    return true;
                }
            }
        }
        return false;
    }

    interface Orientation {
        Orientation DEFAULT = pos -> pos;

        Pos rotate(Pos p);
    }

    static class Scanner {
        List<Pos> beacons = new ArrayList<>();
        List<Pos> absoluteBeacons;
        Pos absolutePos;

        Stream<Pos> getAbsoluteBeacons() {
            return absoluteBeacons.stream();
        }

        Stream<Pos> getBeacons(Orientation o) {
            return beacons.stream().map(o::rotate);
        }

        Stream<Pos> getBeacons(Pos absolutePos, Orientation o) {
            return getBeacons(o).map(pos -> pos.add(absolutePos));
        }

        void setAbsoluteBeacons(Pos absolutePos, Orientation orientation) {
            absoluteBeacons = getBeacons(absolutePos, orientation).toList();
            this.absolutePos = absolutePos;
        }


    }

    record Pos(int x, int y, int z) {
        static Pos ZERO = new Pos(0, 0, 0);

        int manhattanDistance(Pos pos) {
            pos = subtract(pos);
            return Math.abs(pos.x) + Math.abs(pos.y) + Math.abs(pos.z);
        }

        Pos add(Pos pos) {
            return new Pos(x + pos.x, y + pos.y, z + pos.z);
        }

        Pos subtract(Pos pos) {
            return new Pos(x - pos.x, y - pos.y, z - pos.z);
        }
    }
}
