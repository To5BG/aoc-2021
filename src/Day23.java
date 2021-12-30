import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Day23 {
    public static void main(String[] args) throws IOException {
        List<String> input = Files.readAllLines(Path.of("inputs\\day23.txt"));
        System.out.println("Part 1: " + day23(input, false));
        System.out.println("Part 2: " + day23(input, true));
    }

    public static int typenum;

    public static long day23(List<String> input, boolean extend) {
        if (extend) {
            input.add(3, "  #D#C#B#A#  ");
            input.add(4, "  #D#B#A#C#  ");
        }
        typenum = input.size() - 3;
        int[] startingPositions = new int[4 * typenum];
        for (int i = 0; i < typenum; i++) {
            String line = input.get(i + 2);
            for (int j = 0; j < 4; j++) {
                char c = line.charAt(2 * j + 3);
                int unit = (c - 'A') * typenum;
                while (startingPositions[unit] != 0) unit++;
                startingPositions[unit] = 4 * i + j + 7;
            }
        }
        PriorityQueue<GameState> q = new PriorityQueue<>(Comparator.comparingLong(GameState::cost));
        q.add(new GameState(startingPositions, 0));
        long best = Long.MAX_VALUE;
        Map<String, Long> done = new HashMap<>();
        while (!q.isEmpty()) {
            GameState curr = q.poll();
            if (curr.cost >= best) break;
            for (int unit = 0; unit < 4 * typenum; unit++) {
                boolean[] validPos = findValidPos(curr.pos, unit, curr.pos[unit] < 7);
                for (int i = 0; i < validPos.length; i++) {
                    if (!validPos[i]) continue;
                    GameState next = curr.moveUnit(unit, i, price(unit, curr.pos[unit], i));
                    if (next.isComplete()) best = Math.min(best, next.cost);
                    else {
                        String repr = next.getRepr();
                        if (next.cost < done.getOrDefault(repr, Long.MAX_VALUE)) {
                            done.put(repr, next.cost);
                            q.add(next);
                        }
                    }
                }
            }
        }
        return best;
    }

    private static int getType(int unit) {
        return (unit == -1) ? -1 : unit / typenum;
    }

    private static boolean[] findValidPos(int[] positions, int unit, boolean isRoom) {
        int[] occupied = new int[4 * typenum + 7];
        for (int i = 0; i < 4 * typenum + 7; i++) occupied[i] = -1;
        for (int i = 0; i < 4 * typenum; i++) occupied[positions[i]] = i;
        if (isRoom) { //room
            boolean[] res = new boolean[4 * typenum + 7];
            int curroom = getType(unit) + 7;
            if (!checkHallwayClear(positions[unit], curroom, occupied)) return res;
            int next = curroom;
            for (int i = 0; i < typenum; i++) {
                if (occupied[curroom + 4 * i] == -1) next = curroom + 4 * i;
                else if (getType(occupied[curroom + 4 * i]) != getType(unit)) return res;
            }
            res[next] = true;
            return res;
        } else { //hall
            boolean[] res = new boolean[7];
            int currpos = positions[unit];
            int type = getType(unit);
            for (int i = currpos - 4; i > 6; i -= 4)
                if (occupied[i] > -1) return res;
            if ((currpos + 1) % 4 == type) {
                boolean mustMove = false;
                for (int i = currpos + 4; i < 4 * typenum + 7; i += 4) {
                    if (getType(occupied[i]) != type) {
                        mustMove = true;
                        break;
                    }
                }
                if (!mustMove) return res;
            }
            int effpos = currpos;
            while (effpos > 10) effpos -= 4;
            for (int i = 0; i < 7; i++)
                if (occupied[i] == -1 && checkHallwayClear(i, effpos, occupied)) res[i] = true;
            return res;
        }
    }

    private static boolean checkHallwayClear(int hpos, int rpos, int[] occupied) {
        for (int i = Math.min(hpos + 1, rpos - 5); i <= Math.max(hpos - 1, rpos - 6); i++)
            if (occupied[i] != -1) return false;
        return true;
    }

    private static int price(int unit, int start, int end) {
        if (start > end) {
            int temp = start;
            start = end;
            end = temp;
        }
        int depth = (end - 3) / 4;
        int tgtHall = ((end + 1) % 4) * 2 + 3;
        int discount = (start == 0 || start == 6) ? 1 : 0;
        return (int) Math.pow(10, getType(unit)) * (Math.abs(2 * start - tgtHall) + depth - discount);
    }

    private record GameState(int[] pos, long cost) {
        public GameState moveUnit(int unit, int position, int price) {
            int[] newPositions = Arrays.copyOf(pos, pos.length);
            newPositions[unit] = position;
            return new GameState(newPositions, cost + price);
        }

        public boolean isComplete() {
            for (int i = 0; i < pos.length; i++) {
                if (pos[i] < 7 || (pos[i] + 1) % 4 != getType(i)) return false;
            }
            return true;
        }

        public String getRepr() {
            int[] occupied = new int[4 * typenum + 7];
            for (int i = 0; i < 4 * typenum + 7; i++) occupied[i] = -1;
            for (int i = 0; i < 4 * typenum; i++) occupied[pos[i]] = i;
            StringBuilder res = new StringBuilder();
            for (int i = 0; i < 4 * typenum + 7; i++) {
                int type = getType(occupied[i]);
                if (type == -1) res.append("x");
                else res.append(type);
            }
            return res.toString();
        }
    }

}
