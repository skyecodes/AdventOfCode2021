import com.google.common.collect.Lists;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day04 {
    public static void main(String[] args) throws IOException {
        var data = Util.readInput("Day04").stream()
                .filter(s -> !s.isEmpty())
                .toList();
        var drawnNumbers = Arrays.stream(data.get(0).split(","))
                .map(Integer::parseInt)
                .toList();
        var boardNumbers = data.subList(1, data.size()).stream()
                .flatMap(s -> Arrays.stream(s.split(" ")))
                .filter(s -> !s.isEmpty())
                .map(Integer::parseInt)
                .map(BoardNumber::new)
                .toList();
        var boards = Lists.partition(boardNumbers, 25).stream()
                .map(Board::new)
                .toList();
        System.out.println("Part 1: " + part1(drawnNumbers, boards));
        System.out.println("Part 2: " + part2(drawnNumbers, boards));
    }

    static int part1(List<Integer> drawnNumbers, List<Board> boards) {
        for (int drawnNumber : drawnNumbers) {
            for (var board : boards) {
                board.drawNumber(drawnNumber);
                if (board.won) {
                    return board.score() * drawnNumber;
                }
            }
        }
        return 0;
    }

    static int part2(List<Integer> drawnNumbers, List<Board> boards) {
        var remainingBoards = new ArrayList<>(boards);
        for (int drawnNumber : drawnNumbers) {
            var boardIterator = remainingBoards.iterator();
            while (boardIterator.hasNext()) {
                var board = boardIterator.next();
                board.drawNumber(drawnNumber);
                if (board.won) {
                    if (remainingBoards.size() == 1) {
                        return board.score() * drawnNumber;
                    }
                    boardIterator.remove();
                }
            }
        }
        return 0;
    }

    static class Board {
        List<BoardNumber> numbers;
        boolean won = false;

        Board(List<BoardNumber> numbers) {
            this.numbers = numbers;
        }

        void drawNumber(int drawnNumber) {
            for (var number : numbers) {
                if (number.value == drawnNumber) {
                    number.drawn = true;
                }
            }
            checkWin();
        }

        void checkWin() {
            var row = new ArrayList<BoardNumber>();
            var col = new ArrayList<BoardNumber>();
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 5; j++) {
                    row.add(numbers.get(i * 5 + j));
                    col.add(numbers.get(j * 5 + i));
                }
                if (row.stream().allMatch(number -> number.drawn)) {
                    won = true;
                    return;
                }
                if (col.stream().allMatch(number -> number.drawn)) {
                    won = true;
                    return;
                }
                row.clear();
                col.clear();
            }
        }

        int score() {
            return numbers.stream()
                    .filter(number -> !number.drawn)
                    .mapToInt(number -> number.value)
                    .sum();
        }
    }

    static class BoardNumber {
        int value;
        boolean drawn = false;

        BoardNumber(int value) {
            this.value = value;
        }
    }
}
