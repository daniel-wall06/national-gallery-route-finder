package org.example.controllers;

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

    private GraphAL graph;
    private Map<Integer, double[]> roomCoordinates = new HashMap<>();

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

        List<Room> route = graph.findShortestRouteDijkstra(
                startRoom.getRoomNumber(),
                destRoom.getRoomNumber(),
                avoidRooms
        );
        if (route == null) {
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
            List<List<Room>> routes = graph.findRoutesDFS(
                    startRoom.getRoomNumber(),
                    destRoom.getRoomNumber(),
                    maxRoutes,
                    avoidRooms
            );

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
            StringBuilder sb = new StringBuilder("Shortest Route:\n");
            for (Room room : route) {
                sb.append(room.toString()).append("\n");
            }
            resultsArea.setText(sb.toString());
        }

    }
    @FXML
    private void runBFS() {
    }
    private void initRoomCoordinates() {
        roomCoordinates.put(1, new double[]{401.0, 374.5});
        roomCoordinates.put(2, new double[]{360.0, 354.5});
        roomCoordinates.put(4, new double[]{325.0, 354.5});
        roomCoordinates.put(5, new double[]{325.0, 306.5});
        roomCoordinates.put(6, new double[]{288.0, 353.5});
        roomCoordinates.put(7, new double[]{258.0, 375.5});
        roomCoordinates.put(8, new double[]{225.0, 341.5});
        roomCoordinates.put(9, new double[]{226.0, 269.5});
        roomCoordinates.put(10, new double[]{282.0, 266.5});
        roomCoordinates.put(11, new double[]{324.0, 268.5});
        roomCoordinates.put(12, new double[]{364.0, 267.5});
        roomCoordinates.put(14, new double[]{325.0, 233.5});
        roomCoordinates.put(15, new double[]{239.0, 190.5});
        roomCoordinates.put(16, new double[]{204.0, 204.5});
        roomCoordinates.put(17, new double[]{203.0, 180.5});
        roomCoordinates.put(18, new double[]{206.0, 160.5});
        roomCoordinates.put(19, new double[]{238.0, 110.5});
        roomCoordinates.put(20, new double[]{195.0, 139.5});
        roomCoordinates.put(21, new double[]{196.0, 111.5});
        roomCoordinates.put(22, new double[]{197.0, 78.5});
        roomCoordinates.put(23, new double[]{283.0, 82.5});
        roomCoordinates.put(24, new double[]{282.0, 110.5});
        roomCoordinates.put(25, new double[]{282.0, 137.5});
        roomCoordinates.put(26, new double[]{326.0, 137.5});
        roomCoordinates.put(27, new double[]{267.0, 160.5});
        roomCoordinates.put(28, new double[]{296.0, 159.5});
        roomCoordinates.put(29, new double[]{324.0, 160.5});
        roomCoordinates.put(30, new double[]{324.0, 188.5});
        roomCoordinates.put(31, new double[]{431.0, 191.5});
        roomCoordinates.put(32, new double[]{434.0, 152.5});
        roomCoordinates.put(33, new double[]{540.0, 191.5});
        roomCoordinates.put(34, new double[]{626.0, 190.5});
        roomCoordinates.put(35, new double[]{625.0, 268.5});
        roomCoordinates.put(36, new double[]{583.0, 267.5});
        roomCoordinates.put(37, new double[]{539.0, 268.5});
        roomCoordinates.put(38, new double[]{541.0, 226.5});
        roomCoordinates.put(39, new double[]{497.0, 267.5});
        roomCoordinates.put(40, new double[]{467.0, 267.5});
        roomCoordinates.put(41, new double[]{541.0, 308.5});
        roomCoordinates.put(42, new double[]{638.0, 339.5});
        roomCoordinates.put(43, new double[]{607.0, 377.5});
        roomCoordinates.put(44, new double[]{577.0, 364.5});
        roomCoordinates.put(45, new double[]{540.0, 364.5});
        roomCoordinates.put(46, new double[]{506.0, 364.5});
        roomCoordinates.put(51, new double[]{464.0, 370.5});
        roomCoordinates.put(52, new double[]{92.0, 255.5});
        roomCoordinates.put(53, new double[]{91.0, 233.5});
        roomCoordinates.put(54, new double[]{100.0, 293.5});
        roomCoordinates.put(55, new double[]{100.0, 318.5});
        roomCoordinates.put(56, new double[]{99.0, 346.5});
        roomCoordinates.put(57, new double[]{99.0, 376.5});
        roomCoordinates.put(58, new double[]{96.0, 401.5});
        roomCoordinates.put(59, new double[]{59.0, 387.5});
        roomCoordinates.put(60, new double[]{62.0, 347.5});
        roomCoordinates.put(61, new double[]{63.0, 306.5});
        roomCoordinates.put(62, new double[]{62.0, 253.5});
        roomCoordinates.put(63, new double[]{25.0, 325.5});
        roomCoordinates.put(66, new double[]{32.0, 396.5});
    }
    private void drawRoute(List<Room> route) {
        GraphicsContext gc = mapCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, mapCanvas.getWidth(), mapCanvas.getHeight());

        if (route == null || route.size() < 2) return;

        gc.setStroke(Color.RED);
        gc.setLineWidth(3);

        for (int i = 0; i < route.size() - 1; i++) {
            double[] from = roomCoordinates.get(route.get(i).getRoomNumber());
            double[] to = roomCoordinates.get(route.get(i + 1).getRoomNumber());
            if (from != null && to != null) {
                gc.strokeLine(from[0], from[1], to[0], to[1]);
            }
        }

        // Draw circles at each room
        gc.setFill(Color.RED);
        for (Room room : route) {
            double[] coords = roomCoordinates.get(room.getRoomNumber());
            if (coords != null) {
                gc.fillOval(coords[0] - 5, coords[1] - 5, 10, 10);
            }
        }
    }
}
