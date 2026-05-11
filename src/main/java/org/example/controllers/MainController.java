package org.example.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.canvas.GraphicsContext;
import org.example.data.GalleryDataLoader;
import org.example.graph.GraphAL;
import org.example.graph.GraphNodeAL;
import org.example.graph.PixelBFS;
import org.example.models.Room;

import  javafx.scene.image.ImageView;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

public class MainController {
    @FXML
    private ComboBox<Room> startRoomCombo;
    @FXML
    private ComboBox<Room> destRoomCombo;
    @FXML
    private TextField maxRoutesField;
    @FXML
    private TextField artistsField;
    @FXML
    private TextField avoidRoomsField;
    @FXML
    private TextArea resultsArea;
    @FXML
    private ImageView mapImageView;
    @FXML
    private Canvas mapCanvas;
    @FXML
    private TextField waypointsField;

    private GraphAL graph;
    private Map<Integer, double[]> roomCoordinates = new HashMap<>();
    private int[] bfsStart = null;
    private int[] bfsEnd = null;
    private Image bwImage;

    @FXML
    public void initialize() throws IOException {
        try {
            graph = new GalleryDataLoader().load();
            for (GraphNodeAL<Room> node : graph.getAllNodes()) {
                startRoomCombo.getItems().add(node.getData());
                destRoomCombo.getItems().add(node.getData());
            }

            startRoomCombo.setConverter(new javafx.util.StringConverter<Room>() {
                @Override
                public String toString(Room room) {
                    return room == null ? "" : room.toString();
                }

                @Override
                public Room fromString(String s) {
                    return null;
                }
            });
            destRoomCombo.setConverter(new javafx.util.StringConverter<Room>() {
                @Override
                public String toString(Room room) {
                    return room == null ? "" : room.toString();
                }

                @Override
                public Room fromString(String s) {
                    return null;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        Image mapImage = new Image(GalleryDataLoader.class.getModule().getResourceAsStream("org/example/images/mapCA2.png"));
        mapImageView.setImage(mapImage);
        mapCanvas.setWidth(mapImage.getWidth());
        mapCanvas.setHeight(mapImage.getHeight());
        initRoomCoordinates();
        System.out.println("Canvas size: " + mapCanvas.getWidth() + "x" + mapCanvas.getHeight());
        mapCanvas.setOnMouseClicked(e -> {
            System.out.println("x: " + e.getX() + ", y: " + e.getY());
        });
        bwImage = new Image(GalleryDataLoader.class.getModule().getResourceAsStream("org/example/images/mapBW.png"));

        mapCanvas.setOnMouseClicked(e -> {
            double scaleX = bwImage.getWidth() / mapCanvas.getWidth();
            double scaleY = bwImage.getHeight() / mapCanvas.getHeight();
            int scaledX = (int)(e.getX() * scaleX);
            int scaledY = (int)(e.getY() * scaleY);

            if (bfsStart == null) {
                bfsStart = new int[]{scaledX, scaledY};
                resultsArea.setText("Start point set. Click destination.");
            } else {
                bfsEnd = new int[]{scaledX, scaledY};
                runBFSPixel();
                bfsStart = null;
                bfsEnd = null;
            }
        });
    }

    @FXML
    private void runDijkstra() {
        Room startRoom = startRoomCombo.getValue();
        Room destRoom = destRoomCombo.getValue();

        if (startRoom == null || destRoom == null) {
            resultsArea.setText("Please select a start and destination room.");
            return;
        }
        List<Integer> avoidRooms = new ArrayList<>();
        if (!avoidRoomsField.getText().isEmpty()) {
            for (String s : avoidRoomsField.getText().split(",")) {
                avoidRooms.add(Integer.parseInt(s.trim()));
            }
        }
        List<Integer> waypoints = new ArrayList<>();
        if (!waypointsField.getText().isEmpty()) {
            for (String s : waypointsField.getText().split(",")) {
                waypoints.add(Integer.parseInt(s.trim()));
            }
        }

        List<Room> route;
        if (waypoints.isEmpty()) {
            route = graph.findShortestRouteDijkstra(
                    startRoom.getRoomNumber(),
                    destRoom.getRoomNumber(),
                    avoidRooms
            );
        } else {
            route = graph.findShortestRouteWithWaypoints(
                    startRoom.getRoomNumber(),
                    destRoom.getRoomNumber(),
                    waypoints
            );
        }

        if (route == null || route.isEmpty()) {
            resultsArea.setText("No route found.");
        } else {
            StringBuilder sb = new StringBuilder("Shortest Route:\n");
            for (Room room : route) {
                sb.append(room.toString()).append("\n");
            }
            resultsArea.setText(sb.toString());
        }
        drawRoute(route);
    }

    @FXML
    private void runDFS() {
        Room startRoom = startRoomCombo.getValue();
        Room destRoom = destRoomCombo.getValue();
        if (startRoom == null || destRoom == null) {
            resultsArea.setText("Please select a start and destination room.");
            return;
        }
        List<Integer> avoidRooms = new ArrayList<>();
        if (!avoidRoomsField.getText().isEmpty()) {
            for (String s : avoidRoomsField.getText().split(",")) {
                avoidRooms.add(Integer.parseInt(s.trim()));
            }
        }
        int maxRoutes = 5; // default
        if (!maxRoutesField.getText().isEmpty()) {
            maxRoutes = Integer.parseInt(maxRoutesField.getText().trim());
            List<Integer> waypoints = new ArrayList<>();
            if (!waypointsField.getText().isEmpty()) {
                for (String s : waypointsField.getText().split(",")) {
                    waypoints.add(Integer.parseInt(s.trim()));
                }
            }

            List<List<Room>> routes;
            if (waypoints.isEmpty()) {
                routes = graph.findRoutesDFS(
                        startRoom.getRoomNumber(),
                        destRoom.getRoomNumber(),
                        maxRoutes,
                        avoidRooms
                );
            } else {
                routes = graph.findRoutesDFSWithWaypoints(
                        startRoom.getRoomNumber(),
                        destRoom.getRoomNumber(),
                        maxRoutes,
                        waypoints,
                        avoidRooms
                );
            }

            if (routes.isEmpty()) {
                resultsArea.setText("No routes found.");
            } else {
                StringBuilder sb = new StringBuilder("Routes found:\n\n");
                for (int i = 0; i < routes.size(); i++) {
                    sb.append("Route ").append(i + 1).append(":\n");
                    for (Room room : routes.get(i)) {
                        sb.append(room.toString()).append("\n");
                    }
                    sb.append("\n");
                }
                resultsArea.setText(sb.toString());
            }

        }
    }
    @FXML
    private void runInteresting(){
        Room startRoom = startRoomCombo.getValue();
        Room destRoom = destRoomCombo.getValue();
        if (startRoom == null || destRoom == null) {
            resultsArea.setText("Please select a start and destination room.");
            return;
        }
        List<Integer> avoidRooms = new ArrayList<>();
        if (!avoidRoomsField.getText().isEmpty()) {
            for (String s : avoidRoomsField.getText().split(",")) {
                avoidRooms.add(Integer.parseInt(s.trim()));
            }
        }
        List<String> artists = new ArrayList<>();
        if(!artistsField.getText().isEmpty()) {
            for(String s : artistsField.getText().split(",")) {
                artists.add(s.trim());
            }
        }
        List<Room> route = graph.findMostInterestingRoute(
                startRoom.getRoomNumber(),
                destRoom.getRoomNumber(),
                artists,
                avoidRooms
        );
        if (route == null) {
            resultsArea.setText("No route found.");
        } else {
            StringBuilder sb = new StringBuilder("Most Interesting Route:\n");
            for (Room room : route) {
                sb.append(room.toString()).append("\n");
            }
            resultsArea.setText(sb.toString());
        }
        drawRoute(route);

    }
    @FXML
    private void runBFS() {
        bfsStart = null;
        bfsEnd = null;
        resultsArea.setText("BFS mode: Click a start point on the map, then a destination point.");
    }
    private void initRoomCoordinates() {
        roomCoordinates.put(1, new double[]{397.0, 371.0});
        roomCoordinates.put(2, new double[]{360.0, 351.0});
        roomCoordinates.put(3, new double[]{326.0, 349.0});
        roomCoordinates.put(4, new double[]{323.0, 306.0});
        roomCoordinates.put(5, new double[]{286.0, 353.0});
        roomCoordinates.put(6, new double[]{257.0, 371.0});
        roomCoordinates.put(7, new double[]{224.0, 340.0});
        roomCoordinates.put(8, new double[]{224.0, 267.0});
        roomCoordinates.put(9, new double[]{283.0, 267.0});
        roomCoordinates.put(10, new double[]{323.0, 269.0});
        roomCoordinates.put(11, new double[]{364.0, 266.0});
        roomCoordinates.put(12, new double[]{322.0, 232.0});
        roomCoordinates.put(13, new double[]{240.0, 190.0});
        roomCoordinates.put(14, new double[]{222.0, 171.0});
        roomCoordinates.put(15, new double[]{202.0, 202.0});
        roomCoordinates.put(16, new double[]{203.0, 180.0});
        roomCoordinates.put(17, new double[]{207.0, 155.0});
        roomCoordinates.put(18, new double[]{238.0, 108.0});
        roomCoordinates.put(19, new double[]{199.0, 138.0});
        roomCoordinates.put(20, new double[]{193.0, 107.0});
        roomCoordinates.put(21, new double[]{191.0, 81.0});
        roomCoordinates.put(22, new double[]{283.0, 77.0});
        roomCoordinates.put(23, new double[]{280.0, 102.0});
        roomCoordinates.put(24, new double[]{281.0, 133.0});
        roomCoordinates.put(25, new double[]{324.0, 133.0});
        roomCoordinates.put(26, new double[]{268.0, 158.0});
        roomCoordinates.put(27, new double[]{294.0, 155.0});
        roomCoordinates.put(28, new double[]{321.0, 158.0});
        roomCoordinates.put(29, new double[]{321.0, 190.0});
        roomCoordinates.put(30, new double[]{430.0, 191.0});
        roomCoordinates.put(31, new double[]{432.0, 152.0});
        roomCoordinates.put(32, new double[]{537.0, 190.0});
        roomCoordinates.put(33, new double[]{620.0, 186.0});
        roomCoordinates.put(34, new double[]{627.0, 269.0});
        roomCoordinates.put(35, new double[]{583.0, 261.0});
        roomCoordinates.put(36, new double[]{538.0, 267.0});
        roomCoordinates.put(37, new double[]{542.0, 223.0});
        roomCoordinates.put(38, new double[]{499.0, 262.0});
        roomCoordinates.put(39, new double[]{466.0, 267.0});
        roomCoordinates.put(40, new double[]{540.0, 307.0});
        roomCoordinates.put(41, new double[]{639.0, 336.0});
        roomCoordinates.put(42, new double[]{605.0, 373.0});
        roomCoordinates.put(43, new double[]{579.0, 363.0});
        roomCoordinates.put(44, new double[]{539.0, 365.0});
        roomCoordinates.put(45, new double[]{506.0, 364.0});
        roomCoordinates.put(46, new double[]{464.0, 374.0});
        roomCoordinates.put(47, new double[]{91.0, 255.0});
        roomCoordinates.put(48, new double[]{100.0, 289.0});
        roomCoordinates.put(49, new double[]{103.0, 316.0});
        roomCoordinates.put(50, new double[]{100.0, 348.0});
        roomCoordinates.put(51, new double[]{100.0, 371.0});
        roomCoordinates.put(52, new double[]{96.0, 398.0});
        roomCoordinates.put(53, new double[]{61.0, 382.0});
        roomCoordinates.put(54, new double[]{60.0, 348.0});
        roomCoordinates.put(55, new double[]{60.0, 307.0});
        roomCoordinates.put(56, new double[]{57.0, 248.0});
        roomCoordinates.put(57, new double[]{26.0, 255.0});
        roomCoordinates.put(58, new double[]{24.0, 289.0});
        roomCoordinates.put(59, new double[]{25.0, 319.0});
        roomCoordinates.put(60, new double[]{25.0, 352.0});
        roomCoordinates.put(61, new double[]{25.0, 369.0});
        roomCoordinates.put(62, new double[]{31.0, 393.0});
        roomCoordinates.put(63, new double[]{431.0, 269.0});
        roomCoordinates.put(64, new double[]{431.0, 269.0});
        roomCoordinates.put(65, new double[]{373.0, 230.0});
        roomCoordinates.put(66, new double[]{500.0, 304.0});
    }
    private void drawRoute(List<Room> route) {
        GraphicsContext gc = mapCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, mapCanvas.getWidth(), mapCanvas.getHeight());

        if (route == null || route.size() < 2) return;

        gc.setStroke(Color.BLACK);
        gc.setLineWidth(3);

        for (int i = 0; i < route.size() - 1; i++) {
            double[] from = roomCoordinates.get(route.get(i).getRoomNumber());
            double[] to = roomCoordinates.get(route.get(i + 1).getRoomNumber());
            if (from != null && to != null) {
                gc.strokeLine(from[0], from[1], to[0], to[1]);
            }
        }

        // Draw circles at each room
        gc.setFill(Color.BLACK);
        for (Room room : route) {
            double[] coords = roomCoordinates.get(room.getRoomNumber());
            if (coords != null) {
                gc.fillOval(coords[0] - 5, coords[1] - 5, 10, 10);
            }
        }
    }

    @FXML
    private void clearCanvas() {
        GraphicsContext gc = mapCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, mapCanvas.getWidth(), mapCanvas.getHeight());
        bfsStart = null;
        bfsEnd = null;
    }
    private void runBFSPixel() {
        List<int[]> path = PixelBFS.findPath(bwImage, bfsStart[0], bfsStart[1], bfsEnd[0], bfsEnd[1]);

        if (path.isEmpty()) {
            resultsArea.setText("No path found.");
            return;
        }

        GraphicsContext gc = mapCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, mapCanvas.getWidth(), mapCanvas.getHeight());
        gc.setStroke(Color.RED);
        gc.setLineWidth(2);

        double scaleX = mapCanvas.getWidth() / bwImage.getWidth();
        double scaleY = mapCanvas.getHeight() / bwImage.getHeight();

        for (int i = 0; i < path.size() - 1; i++) {
            gc.strokeLine(
                    path.get(i)[0] * scaleX, path.get(i)[1] * scaleY,
                    path.get(i+1)[0] * scaleX, path.get(i+1)[1] * scaleY
            );
        }
        resultsArea.setText("BFS path found! Distance: " + path.size() + " pixels.");
    }
}
