/* *****************************************************************************
 *  Synopsis: performs Monte Carlo simulation to determine percolation threshold.
 *            Returns mean percolation threshold, stdev and 95% confidence interval.
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private double[] num;
    private int Trials;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n < 1 || trials < 1) {
            throw new IllegalArgumentException("n and #trials must be greater than 0!");
        }
        Trials = trials;
        num = new double[trials];
        for (int i = 0; i < trials; i++) {
            Percolation test = new Percolation(n);
            while (!test.percolates()) {
                int rrow = StdRandom.uniformInt(n) + 1;
                int rcol = StdRandom.uniformInt(n) + 1;
                if (!test.isOpen(rrow, rcol)) {
                    test.open(rrow, rcol);
                }
            }
            num[i] = (double) test.numberOfOpenSites() / (n * n);
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(num);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(num);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return (StdStats.mean(num) - 1.96 * StdStats.stddev(num) / Math.sqrt(Trials));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return (StdStats.mean(num) + 1.96 * StdStats.stddev(num) / Math.sqrt(Trials));
    }

    // test client
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);
        PercolationStats testrun = new PercolationStats(n, trials);
        StdOut.printf("mean = %f%n", testrun.mean());
        StdOut.printf("stddev = %f%n", testrun.stddev());
        StdOut.printf("95%% confidence interval = [%f, %f]", testrun.confidenceLo(),
                      testrun.confidenceHi());
    }

}
