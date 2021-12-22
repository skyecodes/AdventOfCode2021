import java.io.IOException;
import java.util.*;
import java.util.stream.IntStream;

public class Day22 {
    static List<Step> steps;

    public static void main(String[] args) throws IOException {
        steps = Util.readInput("Day22").stream().map(s -> {
            var s0 = s.split(" ");
            var on = s0[0].equals("on");
            var coords = Arrays.stream(s0[1].split(",")).flatMap(s2 -> Arrays.stream(s2.split("=")[1].split("\\.\\."))).mapToInt(Integer::parseInt).toArray();
            return Step.of(on, coords);
        }).toList();
        System.out.println("Part 1: " + part1());
        System.out.println("Part 2: " + part2());
    }

    static long part1() {
        var initArea = new Area(-50, 50, -50, 50, -50, 50);
        var cubes = new boolean[initArea.xlength()][initArea.ylength()][initArea.zlength()];
        steps.stream().filter(step -> initArea.contains(step.area)).forEach(step -> IntStream.rangeClosed(step.area.xmin, step.area.xmax).forEach(x -> IntStream.rangeClosed(step.area.ymin, step.area.ymax).forEach(y -> IntStream.rangeClosed(step.area.zmin, step.area.zmax).forEach(z -> cubes[x - initArea.xmin][y - initArea.ymin][z - initArea.zmin] = step.on))));
        return Arrays.stream(cubes).flatMap(Arrays::stream).flatMap(b -> IntStream.range(0, b.length).mapToObj(i -> b[i])).filter(b -> b).count();
    }

    static long part2() {
        var areas = new HashSet<Area>();
        for (var step : steps) {
           var newAreas = new HashSet<Area>();
           for (var current : areas) {
               newAreas.addAll(current.removeIntersection(step.area));
           }
           if (step.on) {
               newAreas.add(step.area);
           }
           areas = newAreas;
        }
        return areas.stream().mapToLong(Area::volume).sum();
    }

    record Step(boolean on, Area area) {
        public static Step of(boolean on, int[] coords) {
            return new Step(on, new Area(coords[0], coords[1], coords[2], coords[3], coords[4], coords[5]));
        }
    }

    record Area(int xmin, int xmax, int ymin, int ymax, int zmin, int zmax) {
        int xlength() {
            return xmax - xmin + 1;
        }

        int ylength() {
            return ymax - ymin + 1;
        }

        int zlength() {
            return zmax - zmin + 1;
        }

        long volume() {
            return (long) xlength() * ylength() * zlength();
        }

        boolean contains(Area other) {
            return other.xmin >= xmin && other.xmax <= xmax && other.ymin >= ymin && other.ymax <= ymax && other.zmin >= zmin && other.zmax <= zmax;
        }

        Set<Area> removeIntersection(Area other) {
            return intersection(other).map(intersection -> intersection == this ? Collections.<Area>emptySet() : remove(intersection)).orElse(Collections.singleton(this));
        }

        Set<Area> remove(Area intersection) {
            var newAreas = new HashSet<Area>();
            var zmin = intersection.zmax + 1;
            if (zmin <= this.zmax) {
                newAreas.add(new Area(this.xmin, this.xmax, this.ymin, this.ymax, zmin--, this.zmax));
            } else {
                zmin = this.zmax;
            }
            var zmax = intersection.zmin - 1;
            if (zmax >= this.zmin) {
                newAreas.add(new Area(this.xmin, this.xmax, this.ymin, this.ymax, this.zmin, zmax++));
            } else {
                zmax = this.zmin;
            }
            var xmin = intersection.xmax + 1;
            if (xmin <= this.xmax) {
                newAreas.add(new Area(xmin--, this.xmax, this.ymin, this.ymax, zmax, zmin));
            } else {
                xmin = this.xmax;
            }
            var xmax = intersection.xmin - 1;
            if (xmax >= this.xmin) {
                newAreas.add(new Area(this.xmin, xmax++, this.ymin, this.ymax, zmax, zmin));
            } else {
                xmax = this.xmin;
            }
            var ymin = intersection.ymax + 1;
            if (ymin <= this.ymax) {
                newAreas.add(new Area(xmax, xmin, ymin, this.ymax, zmax, zmin));
            }
            var ymax = intersection.ymin - 1;
            if (ymax >= this.ymin) {
                newAreas.add(new Area(xmax, xmin, this.ymin, ymax, zmax, zmin));
            }
            return newAreas;
        }

        Optional<Area> intersection(Area other) {
            if (other.xmin > xmax || other.xmax < xmin || other.ymin > ymax || other.ymax < ymin || other.zmin > zmax || other.zmax < zmin) {
                return Optional.empty();
            }
            return Optional.of(new Area(Math.max(this.xmin, other.xmin), Math.min(this.xmax, other.xmax), Math.max(this.ymin, other.ymin), Math.min(this.ymax, other.ymax), Math.max(this.zmin, other.zmin), Math.min(this.zmax, other.zmax)));
        }
    }
}
