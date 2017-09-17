package com.zuora.interview;

import org.junit.Assert;
import org.junit.Test;

import java.util.Random;

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
                {"U5", "/"}, {"U5", "login"}, {"U5", "subscriber"}};
        
        // TOP 10
        TopNPopularPathService topNPopularPathService = new TopNPopularPathServiceImpl();
        topNPopularPathService.setup(testData);
        String[] resultForTop10 = topNPopularPathService.getTopNPopularPaths(10);
        Assert.assertEquals(3, resultForTop10.length);
        assertPathCount(resultForTop10[0], 3);
        assertPathCount(resultForTop10[1], 2);
        assertPathCount(resultForTop10[2], 1);
        for (String path : resultForTop10)
            System.out.println(path);

        // TOP 2
        String[] resultForTop2 = topNPopularPathService.getTopNPopularPaths(2);
        Assert.assertEquals(2, resultForTop2.length);
        assertPathCount(resultForTop10[0], 3);
        assertPathCount(resultForTop10[1], 2);
        for (String path : resultForTop2)
            System.out.println(path);
    }

    @Test
    public void emptyDataTest() {
        String[][] testData = null;
        TopNPopularPathService topNPopularPathService = new TopNPopularPathServiceImpl();
        topNPopularPathService.setup(testData);
        String[] result = topNPopularPathService.getTopNPopularPaths(10);
        Assert.assertNull(result);
    }

    @Test
    public void negativeNTest() {
        String[][] testData = {{"U1", "/"}, {"U1", "login"}, {"U1", "subscriber"},
                {"U2", "/"}, {"U2", "login"}, {"U2", "subscriber"},
                {"U3", "/"}, {"U3", "login"}, {"U3", "product"},
                {"U1", "/"},
                {"U4", "/"}, {"U4", "login"}, {"U4", "product"},
                {"U5", "/"}, {"U5", "login"}, {"U5", "subscriber"}};
        TopNPopularPathService topNPopularPathService = new TopNPopularPathServiceImpl();
        topNPopularPathService.setup(testData);
        String[] result = topNPopularPathService.getTopNPopularPaths(-1);
        Assert.assertNull(result);
    }

    @Test
    public void noThreeSenquentialPathTest() {
        TopNPopularPathService topNPopularPathService = new TopNPopularPathServiceImpl();
        topNPopularPathService.setup(TestDataGenerator.getTestData(100, 2));
        String[] result = topNPopularPathService.getTopNPopularPaths(10);
        Assert.assertNull(result);
    }

    @Test
    public void notEnoughUserTest() {
        TopNPopularPathService topNPopularPathService = new TopNPopularPathServiceImpl();
        topNPopularPathService.setup(TestDataGenerator.getTestData(9, 3));
        String[] result = topNPopularPathService.getTopNPopularPaths(10);
        Assert.assertEquals(9, result.length);
    }

    @Test
    public void samePathTest() {
        String[][] testData = new String[100][3];
        for (int i = 0; i < 100; i = i + 3) {
            String userName = "U" + (i / 3);
            for (int j = 0; i < 3; i++) {
                testData[i + j][0] = userName;
                testData[i + j][1] = "path" + j;
            }
        }
    }

    @Test
    public void topNWithBigDataTest() {
        long startTime = System.currentTimeMillis();
        TopNPopularPathService topNPopularPathService = new TopNPopularPathServiceImpl();
        topNPopularPathService.setup(TestDataGenerator.getTestData(10000000, 3));
        String[] result = topNPopularPathService.getTopNPopularPaths(10);
        Assert.assertEquals(10, result.length);
        for (String path : result) {
            System.out.println(path);
        }
        long endTime = System.currentTimeMillis();
        System.out.println("total time cost for topNWithBigData is " + (endTime - startTime) + " ms");
    }

    @Test
    public void mergeTest() {
        TopNPopularPathService topNPopularPathService = new TopNPopularPathServiceImpl();
        topNPopularPathService.setup(TestDataGenerator.getTestData(100, 10));
        String[] result1 = topNPopularPathService.getTopNPopularPaths(10);
        topNPopularPathService.setup(TestDataGenerator.getTestData(100, 10));
        String[] result2 = topNPopularPathService.getTopNPopularPaths(10);
        String[] finalResult = topNPopularPathService.mergeResult(result1, result2, 10);
        Assert.assertEquals(10, finalResult.length);
    }

    /**
     * Assert whether the path count info in path string as expected.
     *
     * @param path          the path in result
     * @param expectedCount the expected path count
     */
    private void assertPathCount(String path, int expectedCount) {
        Assert.assertEquals(String.valueOf(expectedCount), String.valueOf(path.charAt(path.length() - 1)));
    }

    /**
     * A test data generator.
     */
    static class TestDataGenerator {

        private final static Random random = new Random();

        private final static String[] pathPool = {"/", "login", "logout", "list", "query", "details", "subscriber", "products", "customs", "connect",
                "industries", "resources", "support", "about"};

        private TestDataGenerator() {
        }

        /**
         * Generate test data with specified user count and path limit.
         *
         * @param userCount   the number of user in test data
         * @param pathPerUser the number of path which one user visit
         * @return the test data
         */
        static String[][] getTestData(int userCount, int pathPerUser) {
            String[][] result = new String[userCount * pathPerUser][2];
            for (int i = 0; i < userCount * pathPerUser; i = i + pathPerUser) {
                String userName = randomUser(i / pathPerUser);
                for (int j = 0; j < pathPerUser; j++) {
                    result[i + j][0] = userName;
                    result[i + j][1] = randomPath();
                }
            }
            return result;
        }

        private static String randomUser(int index) {
            return "U" + index;
        }

        private static String randomPath() {
            return pathPool[random.nextInt(pathPool.length)];
        }
    }


}
