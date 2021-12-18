import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class Day18 {
    static List<PairElement> input;

    public static void main(String[] args) throws IOException {
        input = Util.readInput("Day18").stream().map(new SnailfishNumberParser()::parse).toList();
        System.out.println("Part 1: " + part1());
        System.out.println("Part 2: " + part2());
    }

    static int part1() {
        return input.stream().reduce(PairElement::sum).map(Element::value).orElseThrow();
    }

    static int part2() {
        return input.stream().flatMapToInt(a -> input.stream().mapToInt(b -> PairElement.sum(a, b).value())).max().orElseThrow();
    }

    static class SnailfishNumberParser {
        int index;

        PairElement parse(String s) {
            index = 0;
            return parsePair(s);
        }

        PairElement parsePair(String s) {
            var res = new PairElement(s.charAt(++index) == '[' ? parsePair(s) : parseRegularNumber(s), s.charAt(index += 2) == '[' ? parsePair(s) : parseRegularNumber(s));
            index++;
            return res;
        }

        NumberElement parseRegularNumber(String s) {
            return new NumberElement(Integer.parseInt(String.valueOf(s.charAt(index))));
        }
    }

    interface Element {
        void setParent(PairElement parent);

        int value();

        boolean trySplit(Side side);

        Element copy();
    }

    static abstract class AbstractElement implements Element {
        PairElement parent;

        @Override
        public void setParent(PairElement parent) {
            this.parent = parent;
        }
    }

    static class NumberElement extends AbstractElement {
        int value;

        NumberElement(int value) {
            this.value = value;
        }

        @Override
        public int value() {
            return value;
        }

        @Override
        public boolean trySplit(Side side) {
            if (value > 9) {
                var left = value / 2;
                var right = left + value % 2;
                parent.set(side, new PairElement(new NumberElement(left), new NumberElement(right)));
                return true;
            }
            return false;
        }

        @Override
        public Element copy() {
            return new NumberElement(value);
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }

    static class PairElement extends AbstractElement {
        Element left, right;

        PairElement(Element left, Element right) {
            left(left);
            right(right);
        }

        Element get(Side side) {
            return switch (side) {
                case LEFT -> left;
                case RIGHT -> right;
            };
        }

        void set(Side side, Element e) {
            switch (side) {
                case LEFT -> left(e);
                case RIGHT -> right(e);
            }
        }

        void left(Element e) {
            left = e;
            e.setParent(this);
        }

        void right(Element e) {
            right = e;
            e.setParent(this);
        }

        void reduce() {
            while (true) {
                if (tryExplode(0, null)) {
                    continue;
                }
                if (trySplit(null)) {
                    continue;
                }
                break;
            }
        }

        boolean tryExplode(int depth, Side side) {
            if (depth == 4) {
                findNumber(Side.LEFT).ifPresent(e -> e.value += left.value());
                findNumber(Side.RIGHT).ifPresent(e -> e.value += right.value());
                parent.set(side, new NumberElement(0));
                return true;
            }
            return (left instanceof PairElement leftPair && leftPair.tryExplode(depth + 1, Side.LEFT)) ||
                    (right instanceof PairElement rightPair && rightPair.tryExplode(depth + 1, Side.RIGHT));
        }

        Optional<NumberElement> findNumber(Side side) {
            var self = this;
            var parent = this.parent;
            while (parent != null && parent.get(side) == self) {
                self = parent;
                parent = parent.parent;
            }
            if (parent == null) {
                return Optional.empty();
            }
            var el = parent.get(side);
            while (el instanceof PairElement p) {
                el = p.get(side.other());
            }
            return Optional.of((NumberElement) el);
        }

        @Override
        public boolean trySplit(Side side) {
            return left.trySplit(Side.LEFT) || right.trySplit(Side.RIGHT);
        }

        @Override
        public Element copy() {
            return new PairElement(left.copy(), right.copy());
        }

        @Override
        public int value() {
            return 3 * left.value() + 2 * right.value();
        }

        @Override
        public String toString() {
            return "[%s,%s]".formatted(left, right);
        }

        static PairElement sum(Element a, Element b) {
            var sum = new PairElement(a.copy(), b.copy());
            sum.reduce();
            return sum;
        }
    }

    enum Side {
        LEFT, RIGHT;

        Side other;
        Side other() {
            return other == null ? other = switch (this) {
                case LEFT -> RIGHT;
                case RIGHT -> LEFT;
            } : other;
        }
    }
}
