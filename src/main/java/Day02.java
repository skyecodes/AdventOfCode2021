import java.io.IOException;
import java.util.List;

public class Day02 {
    public static void main(String[] args) throws IOException {
        var commands = Util.readInput("Day02").stream()
                .map(s -> s.split(" "))
                .map(s -> new Command(s[0], Integer.parseInt(s[1])))
        .toList();
        System.out.println("Part 1: " + part1(commands));
        System.out.println("Part 2: " + part2(commands));
    }

    static int part1(List<Command> commands) {
        var submarine = new Submarine();
        commands.forEach(command -> {
            switch (command.type) {
                case "forward" -> submarine.horizontalPos += command.value;
                case "up" -> submarine.depth -= command.value;
                case "down" -> submarine.depth += command.value;
            }
        });
        return submarine.result();
    }

    static int part2(List<Command> commands) {
        var submarine = new Submarine();
        commands.forEach(command -> {
            switch (command.type) {
                case "forward" -> {
                    submarine.horizontalPos += command.value;
                    submarine.depth += submarine.aim * command.value;
                }
                case "up" -> submarine.aim -= command.value;
                case "down" -> submarine.aim += command.value;
            }
        });
        return submarine.result();
    }

    record Command(String type, int value) {
    }

    static class Submarine {
        int horizontalPos = 0;
        int depth = 0;
        int aim = 0;

        int result() {
            return horizontalPos * depth;
        }
    }
}
