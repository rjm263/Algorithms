/* *****************************************************************************
 *  Synopsis: create an n-by-n board for the 8Puzzle (or nPuzzle), defines twin boards
 *            (differing only in one tile) and defines two distances measuring how far apart
 *            boards are (Manhattan and Hamming).
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

public class Board {

    private final int[][] board;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        int n = tiles.length;
        this.board = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                this.board[i][j] = tiles[i][j];
            }
        }
    }

    // string representation of this board
    public String toString() {
        int n = board.length;
        String string = n + "\n";
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                string = string + "  " + board[i][j];
                if (j == n - 1) string = string + "\n";
            }
        }
        return string;
    }

    // board dimension n
    public int dimension() {
        return board.length;
    }

    // number of tiles out of place
    public int hamming() {
        int count = 0;
        int n = board.length;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (board[i][j] != 0 && board[i][j] != i * n + j + 1) count++;
            }
        }
        return count;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int mDist = 0;
        int n = board.length;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (board[i][j] != 0 && board[i][j] != i * n + j + 1) {
                    mDist = mDist + Math.abs(i - (board[i][j] - 1) / n) + Math.abs(
                            j - (board[i][j] - 1) % n);
                }
            }
        }
        return mDist;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return this.hamming() == 0;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == null) return false;
        if (!this.getClass().equals(y.getClass())) return false;
        else {
            Board that = (Board) y;
            if (this.dimension() != that.dimension()) return false;
            int n = board.length;
            int count = 0;
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (this.board[i][j] == that.board[i][j]) count++;
                }
            }
            return count == this.dimension() * this.dimension();
        }
    }

    // helper function that exchanges two entries a, b
    private Board exch(int[] a, int[] b) {
        int n = board.length;
        int[][] newTiles = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                newTiles[i][j] = board[i][j];
            }
        }

        int temp = newTiles[a[0]][a[1]];
        newTiles[a[0]][a[1]] = newTiles[b[0]][b[1]];
        newTiles[b[0]][b[1]] = temp;
        return new Board(newTiles);
    }

    // helper function that finds empty tile in n^2 time worst-case
    private int[] findEmpty() {
        int n = board.length;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (board[i][j] == 0) {
                    int[] a = { i, j };
                    return a;
                }
            }
        }
        return new int[0];
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        int[] zero = this.findEmpty();
        ArrayList<Board> boards = new ArrayList<>();
        int n = board.length;
        int[][] newTiles = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                newTiles[i][j] = board[i][j];
            }
        }
        Board copy = new Board(newTiles);
        if (zero[0] > 0) {
            boards.add(copy.exch(zero, new int[] { zero[0] - 1, zero[1] }));
        }
        if (zero[0] < n - 1) {
            boards.add(copy.exch(zero, new int[] { zero[0] + 1, zero[1] }));
        }
        if (zero[1] > 0) {
            boards.add(copy.exch(zero, new int[] { zero[0], zero[1] - 1 }));
        }
        if (zero[1] < n - 1) {
            boards.add(copy.exch(zero, new int[] { zero[0], zero[1] + 1 }));
        }

        return boards;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int n = board.length;
        int[][] newTiles = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                newTiles[i][j] = board[i][j];
            }
        }
        Board copy = new Board(newTiles);
        int[] zero = this.findEmpty();
        if (!Arrays.equals(zero, new int[] { 0, 0 }) && !Arrays.equals(zero, new int[] { 0, 1 }))
            return copy.exch(new int[] { 0, 0 }, new int[] { 0, 1 });
        if (!Arrays.equals(zero, new int[] { 0, 0 }) && !Arrays.equals(zero, new int[] { 1, 0 }))
            return copy.exch(new int[] { 0, 0 }, new int[] { 1, 0 });
        return copy.exch(new int[] { 0, 1 }, new int[] { 1, 0 });
    }

    // unit testing
    public static void main(String[] args) {
        int[][] tiles = { { 1, 2, 3 }, { 4, 6, 8 }, { 7, 0, 5 } };
        Board puzzle = new Board(tiles);
        StdOut.println(puzzle.toString());
        StdOut.println("hamming: " + puzzle.hamming());
        StdOut.println("Manhattan: " + puzzle.manhattan());
        StdOut.println("zero: " + Arrays.toString(puzzle.findEmpty()));
        Board twin = puzzle.twin();
        StdOut.printf(twin.toString());
        if (puzzle.isGoal()) StdOut.println("This is the goal board!");
        if (puzzle.equals(twin)) StdOut.println("The boards are equal!");
        StdOut.println("These are the neighbours:");
        for (Board b : puzzle.neighbors()) StdOut.printf(b.toString());
    }

}
