package com.zuora.interview;

/**
 * Created by toutoudnf on 2017/9/17.
 */
public interface TopNPopularPathService {
    
    void setup(String[][] data);

    String[] getTopNPopularPaths(int n);
}
