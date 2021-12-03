import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Util {
    static List<String> read(String... path) throws IOException {
        return Files.readAllLines(Paths.get("resources", path));
    }

    static List<String> readExample(String day) throws IOException {
        return read(day, "example.txt");
    }

    static List<String> readInput(String day) throws IOException {
        return read(day, "input.txt");
    }
}
