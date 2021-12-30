import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Day25 {
    public static void main(String[] args) throws IOException {
        System.out.println("Result: " + day25("inputs\\day25.txt"));
    }

    public static int day25(String filepath) throws IOException {
        char[][] table = Files.readAllLines(Path.of(filepath)).stream().map(String::toCharArray)
                .toArray(char[][]::new);
        int steps = 0;
        boolean moved;
        do {
            steps++;
            moved = false;
            for (int r = 0; r < table.length; r++) {
                boolean canWrap = true;
                for (int c = 0; c < table[0].length; c++) {
                    if (table[r][c] != '>' || table[r][(c + 1) % table[0].length] != '.') continue;
                    if (c == 0) canWrap = false;
                    moved = true;
                    if (canWrap || c != table[0].length - 1) {
                        table[r][c] = '.';
                        table[r][((c++) + 1) % table[0].length] = '>';
                    }
                }
            }
            for (int c = 0; c < table[0].length; c++) {
                boolean canWrap = true;
                for (int r = 0; r < table.length; r++) {
                    if (table[r][c] != 'v' || table[(r + 1) % table.length][c] != '.') continue;
                    if (r == 0) canWrap = false;
                    moved = true;
                    if (canWrap || r != table.length - 1) {
                        table[r][c] = '.';
                        table[((r++) + 1) % table.length][c] = 'v';
                    }
                }
            }
        } while (moved);
        return steps;
    }
}
