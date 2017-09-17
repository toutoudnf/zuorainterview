package com.zuora.interview;

import org.junit.Test;

/**
 * Created by toutoudnf on 2017/9/17.
 */

public class TopNPopularPathServiceTest {

    @Test
    public void caseInQuestion() {
        String[][] testData = {{"U1", "/"}, {"U1", "login"}, {"U1", "subscriber"},
                {"U2", "/"}, {"U2", "login"}, {"U2", "subscriber"},
                {"U3", "/"}, {"U3", "login"}, {"U3", "product"},
                {"U1", "/"},
                {"U4", "/"}, {"U4", "login"}, {"U4", "product"},
                {"U5", "/"}, {"U5", "login"}, {"U5", "subscriber"},};
        // TOP 10
        TopNPopularPathService topNPopularPathService = new TopNPopularPathServiceImpl();
        topNPopularPathService.setup(testData);
        String[] resultForTop10 = topNPopularPathService.getTopNPopularPaths(10);
        for (String path : resultForTop10)
            System.out.println(path);

        // TOP 2
        String[] resultForTop2 = topNPopularPathService.getTopNPopularPaths(2);
        for (String path : resultForTop2)
            System.out.println(path);
    }
}
