import java.util.Arrays;

public class Puzzle {

    /** Solve the word puzzle.
     * @param args three words from the command line (addend1, addend2 and sum)
     */
    public static void main(String[] args) {
        if (args == null || args.length < 3) {
            return;
        }

        String addend1 = args[0];
        String addend2 = args[1];
        String sum = args[2];

        System.out.println(addend1 + " + " + addend2 + " = " + sum);

        Solver solver = new Solver(addend1, addend2, sum);
        solver.solve();

        if (solver.firstSolution == null) {
            System.out.println("No solutions.");
        } else {
            System.out.println("One solution: " + solver.formatSolution());
            System.out.println("Numeric form: "
                    + solver.wordToDigits(addend1) + " + "
                    + solver.wordToDigits(addend2) + " = "
                    + solver.wordToDigits(sum));
        }

        System.out.println("Total solutions: " + solver.solutionCount);
    }

    private static class Solver {
        private final String a;
        private final String b;
        private final String c;
        private final int maxLen;

        private final int[] map = new int[26];
        private final boolean[] used = new boolean[10];
        private final boolean[] leading = new boolean[26];

        private int[] firstSolution;
        private long solutionCount;

        Solver(String a, String b, String c) {
            this.a = a;
            this.b = b;
            this.c = c;
            this.maxLen = Math.max(a.length(), Math.max(b.length(), c.length()));
            Arrays.fill(map, -1);

            markLeading(a);
            markLeading(b);
            markLeading(c);
        }

        void solve() {
            if (!isValidWord(a) || !isValidWord(b) || !isValidWord(c)) {
                return;
            }

            boolean[] present = new boolean[26];
            markPresent(a, present);
            markPresent(b, present);
            markPresent(c, present);

            int distinct = 0;
            for (boolean x : present) {
                if (x) {
                    distinct++;
                }
            }

            if (distinct > 10) {
                return;
            }

            if (c.length() < Math.max(a.length(), b.length())
                    || c.length() > Math.max(a.length(), b.length()) + 1) {
                return;
            }

            solveColumn(0, 0, 0);
        }

        private void solveColumn(int columnFromRight, int row, int partialSum) {
            if (columnFromRight == maxLen) {
                if (partialSum == 0) {
                    solutionCount++;
                    if (firstSolution == null) {
                        firstSolution = map.clone();
                    }
                }
                return;
            }

            if (row == 0) {
                processAddendChar(charFromRight(a, columnFromRight), columnFromRight, 1, partialSum);
            } else if (row == 1) {
                processAddendChar(charFromRight(b, columnFromRight), columnFromRight, 2, partialSum);
            } else {
                processResultChar(charFromRight(c, columnFromRight), columnFromRight, partialSum);
            }
        }

        private void processAddendChar(char ch, int columnFromRight, int nextRow, int partialSum) {
            if (ch == 0) {
                solveColumn(columnFromRight, nextRow, partialSum);
                return;
            }

            int letter = ch - 'A';
            if (map[letter] != -1) {
                solveColumn(columnFromRight, nextRow, partialSum + map[letter]);
                return;
            }

            for (int digit = 0; digit <= 9; digit++) {
                if (used[digit]) {
                    continue;
                }
                if (digit == 0 && leading[letter]) {
                    continue;
                }

                assign(letter, digit);
                solveColumn(columnFromRight, nextRow, partialSum + digit);
                unassign(letter, digit);
            }
        }

        private void processResultChar(char ch, int columnFromRight, int partialSum) {
            if (ch == 0) {
                return;
            }

            int neededDigit = partialSum % 10;
            int carry = partialSum / 10;
            int letter = ch - 'A';

            if (map[letter] != -1) {
                if (map[letter] == neededDigit) {
                    solveColumn(columnFromRight + 1, 0, carry);
                }
                return;
            }

            if (used[neededDigit]) {
                return;
            }
            if (neededDigit == 0 && leading[letter]) {
                return;
            }

            assign(letter, neededDigit);
            solveColumn(columnFromRight + 1, 0, carry);
            unassign(letter, neededDigit);
        }

        private void assign(int letter, int digit) {
            map[letter] = digit;
            used[digit] = true;
        }

        private void unassign(int letter, int digit) {
            map[letter] = -1;
            used[digit] = false;
        }

        private char charFromRight(String s, int columnFromRight) {
            int index = s.length() - 1 - columnFromRight;
            return index >= 0 ? s.charAt(index) : 0;
        }

        private void markLeading(String s) {
            if (s.length() > 1) {
                leading[s.charAt(0) - 'A'] = true;
            }
        }

        private void markPresent(String s, boolean[] present) {
            for (int i = 0; i < s.length(); i++) {
                present[s.charAt(i) - 'A'] = true;
            }
        }

        private boolean isValidWord(String s) {
            for (int i = 0; i < s.length(); i++) {
                char ch = s.charAt(i);
                if (ch < 'A' || ch > 'Z') {
                    return false;
                }
            }
            return true;
        }

        String formatSolution() {
            int[] solution = firstSolution != null ? firstSolution : map;
            StringBuilder sb = new StringBuilder();
            boolean first = true;

            for (int i = 0; i < 26; i++) {
                if (solution[i] != -1) {
                    if (!first) {
                        sb.append(", ");
                    }
                    sb.append((char) ('A' + i)).append("=").append(solution[i]);
                    first = false;
                }
            }
            return sb.toString();
        }

        String wordToDigits(String word) {
            int[] solution = firstSolution != null ? firstSolution : map;
            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < word.length(); i++) {
                sb.append(solution[word.charAt(i) - 'A']);
            }
            return sb.toString();
        }
    }
}