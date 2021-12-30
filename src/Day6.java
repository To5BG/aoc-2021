import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;

public class Day6 {
    public static void main(String[] args) throws IOException {
        String input = Files.readString(Path.of("inputs\\day6.txt"));
        System.out.println("Part 1: " + day6(input, 80));
        System.out.println("Part 2: " + day6(input, 256));
    }

    public static long day6(String in, int days) {
        LinkedList<Long> fish = new LinkedList<>();
        for (int i = 0; i < 9; i++) fish.addLast(0L);
        for (String s : in.split(",")) fish.set(8 - Integer.parseInt(s), fish.get(8 - Integer.parseInt(s)) + 1L);
        while (days-- > 0) {
            fish.addFirst(fish.getLast());
            fish.set(2, fish.get(2) + fish.removeLast());
        }
        return fish.stream().reduce(0L, Long::sum);
    }
}
