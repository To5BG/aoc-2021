import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

public class Day11 {
    public static void main(String[] args) throws IOException {
        int[] res = day11(Files.readString(Path.of("inputs\\day11.txt")), 100);
        System.out.println("Part 1: " + res[0]);
        System.out.println("Part 2: " + res[1]);
    }

    public static int[] day11(String input, int steps) {
        String in = String.format("%s\n", "9".repeat(12));
        Scanner scr = new Scanner(input);
        //expand array by 1 grid in every direction
        while (scr.hasNextLine()) in = in.concat(String.format("9%s9\n", scr.nextLine()));
        int[][] octopus = String.format("%s%s", in, "9".repeat(12)).lines()
                .map(l -> l.chars().map(Character::getNumericValue).toArray()).toArray(int[][]::new);
        int res = 0, res2 = 0, s = 0;
        //trigger for part 2
        boolean trigger2 = true;
        boolean[][] exploded = new boolean[10][10];
        while (s++ < steps || trigger2) {
            boolean trigger = false;
            //flag if over 9
            for (int x = 1; x < 11; x++)
                for (int y = 1; y < 11; y++)
                    if (++octopus[x][y] > 9) trigger = true;
            while (trigger) {
                trigger = false;
                for (int x = 1; x < 11; x++)
                    for (int y = 1; y < 11; y++)
                        //if not recorded already, increment surrounding tiles
                        if (octopus[x][y] > 9 && !exploded[x - 1][y - 1]) {
                            trigger = true;
                            exploded[x - 1][y - 1] = true;
                            for (int xi = x - 1; xi <= x + 1; xi++)
                                for (int yi = y - 1; yi <= y + 1; yi++) octopus[xi][yi]++;
                        }
            }
            if (trigger2) res2 = s;
            //reset tiles and increment counter
            for (int x = 1; x < 11; x++)
                for (int y = 1; y < 11; y++) {
                    if (exploded[x - 1][y - 1]) {
                        exploded[x - 1][y - 1] = false;
                        octopus[x][y] = 0;
                        if (s <= steps) res++;
                    } else if (trigger2) res2 = 0;
                }
            if (res2 != 0) trigger2 = false;
        }
        return new int[]{res, res2};
    }
}
