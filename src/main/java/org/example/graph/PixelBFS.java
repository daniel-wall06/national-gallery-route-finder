package org.example.graph;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;

import java.util.*;

public class PixelBFS {

    public static List<int[]> findPath(Image bwImage, int startX, int startY, int destX, int destY) {
        PixelReader pr = bwImage.getPixelReader();
        int width = (int) bwImage.getWidth();
        int height = (int) bwImage.getHeight();

        boolean[][] visited = new boolean[height][width];
        Map<String, int[]> prev = new HashMap<>();
        Queue<int[]> queue = new LinkedList<>();

        queue.add(new int[]{startX, startY});
        visited[startY][startX] = true;
        int[][] directions = {{0,1},{0,-1},{1,0},{-1,0}};

        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int cx = current[0];
            int cy = current[1];

            if (cx == destX && cy == destY) {
                break;
            }

            for (int[] dir : directions) {
                int nx = cx + dir[0];
                int ny = cy + dir[1];

                if (nx >= 0 && nx < width && ny >= 0 && ny < height && !visited[ny][nx]) {
                    // check if pixel is white
                    javafx.scene.paint.Color color = pr.getColor(nx, ny);
                    if (color.getBrightness() > 0.5) {
                        visited[ny][nx] = true;
                        prev.put(nx + "," + ny, current);
                        queue.add(new int[]{nx, ny});
                    }
                }
            }
        }
        List<int[]> path = new ArrayList<>();
        int[] current = new int[]{destX, destY};

        while (current != null) {
            path.add(0, current);
            current = prev.get(current[0] + "," + current[1]);
        }

        if (path.isEmpty() || path.get(0)[0] != startX || path.get(0)[1] != startY) {
            return Collections.emptyList(); // no path found
        }

        return path;

    }
}