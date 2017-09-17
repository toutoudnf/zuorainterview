package com.zuora.interview;

/**
 * Created by toutoudnf on 2017/9/17.
 */
public interface TopNPopularPathService {

    /**
     * Setup init data.
     *
     * @param data init data in array.
     */
    void setup(String[][] data);

    /**
     * Get top n popular path with data set by method setup.
     *
     * @param n an positive integer for top n
     * @return the top n path stored in array if exists; null will be returned if no data setup before or param n is invalid
     */
    String[] getTopNPopularPaths(int n);

    /**
     * Merge two top N result into one.
     * Used when data size is big and should split data into M pieces, after M times of getTopNPopularPaths
     * invoke, use this method to merge and find the real top N.
     *
     * @param result        getTopNPopularPaths result array
     * @param anotherResult another getTopNPopularPaths result array
     * @param n             top n
     * @return the top n of the array above
     */
    String[] mergeResult(String[] result, String[] anotherResult, int n);
}
