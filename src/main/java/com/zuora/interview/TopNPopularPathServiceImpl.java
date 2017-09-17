package com.zuora.interview;

import java.util.*;

/**
 * Created by toutoudnf on 2017/9/17.
 */
public class TopNPopularPathServiceImpl implements TopNPopularPathService {

    private String[][] data = null;

    private static final String PATH_CONNECTOR = " -> ";

    public TopNPopularPathServiceImpl() {
    }

    @Override
    public void setup(String[][] data) {
        this.data = data;
    }

    @Override
    public String[] getTopNPopularPaths(int n) {
        if (data == null) {
            System.out.println("data should not be null!");
            return null;
        }

        if(n <=0 ) {
            System.out.println("n must be a positive integer!");
            return null;
        }

        Map<String, Integer> pathsAndCount = groupByThreeSenquentialPath();

        PriorityQueue<PathNode> sorted = sortWithHeap(pathsAndCount);

        String[] result = new String[n > sorted.size() ? sorted.size() : n];
        PathNode pathNode = sorted.poll();
        for (int i = 0; i < result.length && null != pathNode; i++) {
            result[i] = pathNode.path + ": " + pathNode.count;
            pathNode = sorted.poll();
        }

        return result;
    }

    /**
     * Handle with the user and url path data stored in data array.
     * Store the result in map pathsAndCount like :
     * <p>
     * path1: 10
     * path2: 20
     */
    private Map<String, Integer> groupByThreeSenquentialPath() {
        Map<String, Integer> pathsAndCount = new HashMap<String, Integer>();
        Map<String, LinkedList<String>> usersAndPaths = new HashMap<String, LinkedList<String>>();
        for (int i = 0; i < data.length; i++) {

            // check weather exists user in map
            if (usersAndPaths.containsKey(data[i][0])) {
                LinkedList<String> paths = usersAndPaths.get(data[i][0]);
                paths.add(data[i][1]);

                if (paths.size() == 3) {
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
                LinkedList<String> paths = new LinkedList<>();
                paths.add(data[i][1]);
                usersAndPaths.put(data[i][0], paths);
            }
        }
        return pathsAndCount;
    }


    /**
     * construct with max heap.
     */
    private PriorityQueue<PathNode> sortWithHeap(Map<String, Integer> pathsAndCount) {
        PriorityQueue<PathNode> priorityQueue = new PriorityQueue<PathNode>(Collections.reverseOrder());
        for (String path : pathsAndCount.keySet()) {
            PathNode pathNode = new PathNode(pathsAndCount.get(path), path);
            priorityQueue.add(pathNode);
        }
        return priorityQueue;
    }

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
