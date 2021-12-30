import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Day4 {
    public static void main(String[] args) throws IOException {
        int[] res = day4(Files.readString(Path.of("inputs\\day4.txt")));
        System.out.println("Part 1: " + res[0]);
        System.out.println("Part 2: " + res[1]);
    }

    public static int[] day4(String input) {
        Scanner scr = new Scanner(input);
        List<Integer> rolls = Arrays.stream(scr.nextLine().split(",")).map(Integer::parseInt).toList();
        List<int[]> tables = new ArrayList<>();
        scr.nextLine();
        String line;
        int[] curt = new int[25];
        int d = 0;
        while (scr.hasNextLine()) {
            line = scr.nextLine();
            if (line.equals("")) {
                d = 0;
                tables.add(curt);
                curt = new int[25];
            } else {
                String[] temp = line.trim().split("\\s+");
                for (int i = 0; i < 5; i++) curt[d + i] = Integer.parseInt(temp[i]);
                d += 5;
            }
        }
        tables.add(curt);
        int first = Integer.MIN_VALUE;
        for (int curroll : rolls) {
            for (int j = 0; j < tables.size(); j++) {
                curt = tables.get(j);
                for (int k = 0; k < 25; k++)
                    if (curt[k] == curroll) curt[k] = -1;
                for (int l = 0; l < 5; l++) {
                    if ((curt[l] == -1 && curt[l + 5] == -1 && curt[l + 10] == -1 &&
                            curt[l + 15] == -1 && curt[l + 20] == -1) ||
                            (curt[5 * l] == -1 && curt[5 * l + 1] == -1 && curt[5 * l + 2] == -1 &&
                                    curt[5 * l + 3] == -1 && curt[5 * l + 4] == -1)) {
                        if (first == Integer.MIN_VALUE)
                            first = Arrays.stream(curt).filter(t -> t > 0).sum() * curroll;
                        if (tables.size() != 1) {
                            tables.remove(curt);
                            j = -1;
                            break;
                        }
                        return new int[]{first, Arrays.stream(curt).filter(t -> t > 0).sum() * curroll};
                    }
                }
            }
        }
        return new int[2];
    }
}
