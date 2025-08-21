/* *****************************************************************************
 *  Synopsis: generates an n-by-n grid in order to solve the Percolation problem.
 *            All sites are initialised as closed. Uses weighted quick union to
 *            keep track of open sites and virtual sites at top and bottom to
 *            check for percolation.
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private boolean[][] grid;
    private WeightedQuickUnionUF id;
    private int uvirt, dvirt, counter, N;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("n must be greater than 0!");
        }
        grid = new boolean[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                grid[i][j] = false;
            }
        }
        id = new WeightedQuickUnionUF(n * n + 2);
        uvirt = 0;
        dvirt = n * n + 1;
        N = n;
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (row > N || row < 1 || col > N || col < 1) {
            throw new IllegalArgumentException("row/col index from 1 to n!");
        }
        if (!grid[row - 1][col - 1]) {
            grid[row - 1][col - 1] = true;
            counter += 1;

            int site = (row - 1) * N + col;
            int usite = (row - 2) * N + col;
            int dsite = row * N + col;
            int lsite = (row - 1) * N + col - 1;
            int rsite = (row - 1) * N + col + 1;

            if (row > 1 && row < N) {
                if (grid[row - 2][col - 1]) {
                    id.union(usite, site);
                }
                if (grid[row][col - 1]) {
                    id.union(dsite, site);
                }
                if (col > 1 && grid[row - 1][col - 2]) {
                    id.union(lsite, site);
                }
                if (col < N && grid[row - 1][col]) {
                    id.union(rsite, site);
                }
            }
            if (row == 1) {
                id.union(uvirt, site);
                if (grid[row][col - 1]) {
                    id.union(dsite, site);
                }
                if (col > 1 && grid[row - 1][col - 2]) {
                    id.union(lsite, site);
                }
                if (col < N && grid[row - 1][col]) {
                    id.union(rsite, site);
                }
            }
            if (row == N) {
                id.union(dvirt, site);
                if (grid[row - 2][col - 1]) {
                    id.union(usite, site);
                }
                if (col > 1 && grid[row - 1][col - 2]) {
                    id.union(lsite, site);
                }
                if (col < N && grid[row - 1][col]) {
                    id.union(rsite, site);
                }
            }
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (row > grid.length || row < 1 || col > grid.length || col < 1) {
            throw new IllegalArgumentException("row/col index from 1 to n!");
        }
        return grid[row - 1][col - 1];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (row > grid.length || row < 1 || col > grid.length || col < 1) {
            throw new IllegalArgumentException("row/col index from 1 to n!");
        }
        int site = (row - 1) * N + col;
        return id.find(site) == id.find(uvirt);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return counter;
    }

    // does the system percolate?
    public boolean percolates() {
        return id.find(uvirt) == id.find(dvirt);
    }

    // test client
    public static void main(String[] args) {
        int n = 4;
        Percolation test = new Percolation(n);
        while (!test.percolates()) {
            int rrow = StdRandom.uniformInt(n) + 1;
            int rcol = StdRandom.uniformInt(n) + 1;
            if (!test.isOpen(rrow, rcol)) {
                test.open(rrow, rcol);
            }
        }
        StdOut.println(test.numberOfOpenSites());
    }

}
