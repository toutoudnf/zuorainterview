package com.zuora.interview;

import java.util.*;

/**
 * Created by toutoudnf on 2017/9/17.
 */
public class TopNPopularPathServiceImpl implements TopNPopularPathService {

    private ThreadLocal<String[][]> localDatas = new ThreadLocal<>(); // the data gonna be handled

    private static final String PATH_CONNECTOR = " -> "; // connector of those path( including the space )

    /**
     * Constructor of TopNPopularPathServiceImpl.
     * A thread-safe service implementation for TopNPopularPathService.
     */
    public TopNPopularPathServiceImpl() {
    }

    @Override
    public void setup(String[][] data) {
        this.localDatas.set(data);
    }

    @Override
    public String[] getTopNPopularPaths(int n) {

        if (!checkValid(n)) {
            return null;
        }

        // traverse all data and calculate total number for each path
        Map<String, Integer> pathsAndCount = groupByThreeSenquentialPath();
        if (pathsAndCount.isEmpty()) {
            System.out.println("can not find any three-senquential path, please add more effective data!");
            return null;
        }

        // sort with priority queue
        PriorityQueue<PathNode> sorted = sort(pathsAndCount);

        String[] result = new String[n > sorted.size() ? sorted.size() : n];
        PathNode pathNode = sorted.poll();
        for (int i = 0; null != pathNode && i < result.length; i++) {
            result[i] = pathNode.path + ": " + pathNode.count;
            pathNode = sorted.poll();
        }

        return result;
    }

    @Override
    public String[] mergeResult(String[] oneResult, String[] anotherResult, int n) {

        PriorityQueue<String> priorityQueue = new PriorityQueue<>();
        if (null != oneResult) {
            for (int i = 0; i < oneResult.length; i++) {
                priorityQueue.add(oneResult[i]);
            }
        }
        if (null != anotherResult) {
            for (int i = 0; i < anotherResult.length; i++) {
                priorityQueue.add(anotherResult[i]);
            }
        }

        String[] mergeResult = new String[priorityQueue.size() > n ? n : priorityQueue.size()];
        for (int i = 0; i < mergeResult.length; i++) {
            mergeResult[i] = priorityQueue.poll();
        }

        return mergeResult;
    }

    /**
     * Check whether the init data and param is valid.
     *
     * @param n top N
     * @return true if valid; otherwise is false
     */
    private boolean checkValid(int n) {

        if (localDatas.get() == null) {
            System.out.println("data should not be null!");
            return false;
        }

        if (n <= 0) {
            System.out.println("n must be a positive integer!");
            return false;
        }

        return true;
    }

    /**
     * Handle with the user and url path data stored in data array.
     * Store the result in map pathsAndCount like :
     * <p>
     * key: value
     * path1: 10
     * path2: 20
     */
    private Map<String, Integer> groupByThreeSenquentialPath() {
        Map<String, Integer> pathsAndCount = new HashMap<>();
        Map<String, LinkedList<String>> usersAndPaths = new HashMap<>();
        String[][] data = localDatas.get();

        for (int i = 0; i < data.length; i++) {
            if (usersAndPaths.containsKey(data[i][0])) {
                // find user already exists
                LinkedList<String> paths = usersAndPaths.get(data[i][0]);
                paths.add(data[i][1]);

                if (paths.size() == 3) {
                    // find a new three-senquential path
                    StringBuilder newThreeSequentialPath = new StringBuilder(3);
                    newThreeSequentialPath.append(paths.get(0)).append(PATH_CONNECTOR).append(paths.get(1)).append(PATH_CONNECTOR).append(paths.get(2));
                    if (pathsAndCount.containsKey(newThreeSequentialPath.toString())) {
                        int count = pathsAndCount.get(newThreeSequentialPath.toString()) + 1;
                        pathsAndCount.put(newThreeSequentialPath.toString(), count);
                    } else {
                        pathsAndCount.put(newThreeSequentialPath.toString(), 1);
                    }
                    paths.poll(); // move the oldest path
                }
            } else {
                // find new user
                LinkedList<String> paths = new LinkedList<>();
                paths.add(data[i][1]);
                usersAndPaths.put(data[i][0], paths);
            }
        }
        return pathsAndCount;
    }


    /**
     * Sort the path data by counts in descending order.
     */
    private PriorityQueue<PathNode> sort(Map<String, Integer> pathsAndCount) {
        PriorityQueue<PathNode> priorityQueue = new PriorityQueue<>(Collections.reverseOrder());
        for (String path : pathsAndCount.keySet()) {
            PathNode pathNode = new PathNode(pathsAndCount.get(path), path);
            priorityQueue.add(pathNode);
        }
        return priorityQueue;
    }

    /**
     * Structure for store path and path's count.
     */
    class PathNode implements Comparable {

        private int count;

        private String path;

        PathNode(int count, String path) {
            this.count = count;
            this.path = path;
        }

        @Override
        public int compareTo(Object o) {
            if (o instanceof PathNode) {
                return this.count - ((PathNode) o).count;
            } else {
                return -1;
            }
        }
    }
}
