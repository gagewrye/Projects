package byow.Core;

import byow.Core.Objects.Room;
import byow.Core.Objects.Edge;
import byow.TileEngine.Tileset;
import byow.TileEngine.TETile;

/**
 * Creates tiles to visually represent rooms
 */
public class DungeonVisualizer {
    /**
     * Draw given room into the world.
     *
     * @param room Room to be drawn
     * @param world map to be drawn into
     */
    private void drawRoom(Room room, TETile[][] world) {
        // Generate upper and lower wall
        for (int i = 0; i < room.getWidth(); i++) {
            world[room.getY()][room.getX() + i] = Tileset.WALL;
            world[room.getY() + room.getHeight() - 1][room.getX() + i] = Tileset.WALL;
        }

        // Generate left and right wall
        for (int i = 0; i < room.getHeight(); i++) {
            world[room.getY() + i][room.getX()] = Tileset.WALL;
            world[room.getY() + i][room.getX() + room.getWidth() - 1] = Tileset.WALL;
        }

        // Generate the floor within the room
        for (int i = 0; i < room.getWidth() - 2; i++) {
            for (int j = 0; j < room.getHeight() - 2; j++) {
                world[room.getY() + j + 1][room.getX() + i + 1] = Tileset.FLOOR;
            }
        }
    }

    /**
     *
     * Draw routes between two rooms
     *
     * @param edge Edge containing two rooms
     * @param world Character world
     */
    private void drawEdge(Edge edge, TETile[][] world) {
        int x = edge.getFirst().getCenterX();
        int y = edge.getFirst().getCenterY();
        int destinationX = edge.getSecond().getCenterX();
        int destinationY = edge.getSecond().getCenterY();

        char direction = '?';
        // Move horizontally
        while (x != destinationX) {
            if (x < destinationX) {
                x++;
                direction = 'r';
            } else if (x > destinationX) {
                x--;
                direction = 'l';
            }
            if (world[y][x] != Tileset.FLOOR) {
                world[y][x] = Tileset.FLOOR;
                world[y + 1][x] = Tileset.WALL;
                world[y - 1][x] = Tileset.WALL;
            }

        }

        // Draw turning point
        if (direction == 'l') {
            if (y > destinationY) {
                world[y - 1][x - 1] = Tileset.WALL;
                world[y + 1][x - 1] = Tileset.WALL;
                world[y][x - 1] = Tileset.WALL;
            } else if (y < destinationY) {
                world[y][x - 1] = Tileset.WALL;
                world[y - 1][x - 1] = Tileset.WALL;
            }
        } else if (direction == 'r') {
            if (y > destinationY) {
                world[y + 1][x + 1] = Tileset.WALL;
                world[y][x + 1] = Tileset.WALL;
            } else if (y < destinationY) {
                world[y + 1][x + 1] = Tileset.WALL;
                world[y][x + 1] = Tileset.WALL;
                world[y - 1][x + 1] = Tileset.WALL;
            }
        }

        // Move vertically
        while (y != destinationY) {
            if (y < destinationY) {
                y++;
            } else {
                y--;
            }
            if (world[y][x] != Tileset.FLOOR) {
                world[y][x] = Tileset.FLOOR;
                world[y][x + 1] = Tileset.WALL;
                world[y][x - 1] = Tileset.WALL;
            }

        }
    }

    /***
     * Draw a blank area within the room.
     * @param room Room which area will be drawn blank
     * @param world Map in which to draw
     */
    private void clearRoom(Room room, TETile[][] world) {
        for (int i = 0; i < room.getWidth() - 2; i++) {
            for (int j = 0; j < room.getHeight() - 2; j++) {
                world[room.getY() + j + 1][room.getX() + i + 1] = Tileset.FLOOR;
            }
        }
    }

    /**
     * Offset all rooms by given x and y offsets.
     *
     * @param rooms Array of rooms.
     * @param offsetX how much x will be offset
     * @param offsetY how much y will be offset
     */
    private void offsetRooms(Room[] rooms, int offsetX, int offsetY) {
        for (Room room : rooms) {
            room.setX(room.getX() + offsetX);
            room.setY(room.getY() + offsetY);
        }
    }

    /**
     * Generate a map from given dungeon rooms.
     *
     * @param dungeonRooms Array containing dungeon rooms
     * @return Character map containing rooms
     */
    public final TETile[][] createMap(Room[] dungeonRooms, Edge[] edges) {
        if (dungeonRooms.length == 0) {
            return new TETile[0][0];
        }

        int minimumX = dungeonRooms[0].getX();
        int minimumY = dungeonRooms[0].getY();
        for (Room room : dungeonRooms) {
            if (minimumX > room.getX()) {
                minimumX = room.getX();
            }
            if (minimumY > room.getY()) {
                minimumY = room.getY();
            }
        }

        this.offsetRooms(dungeonRooms, -minimumX, -minimumY);

        int maximumX = 0;
        int maximumY = 0;
        for (Room room : dungeonRooms) {
            if (maximumY < room.getHeight() + room.getY()) {
                maximumY = room.getHeight() + room.getY();
            }
            if (maximumX < room.getWidth() + room.getX()) {
                maximumX = room.getWidth() + room.getX();
            }
        }

        TETile[][] world = new TETile[maximumY][maximumX];
        for (int i = 0; i < maximumY; i++) {
            for (int j = 0; j < maximumX; j++) {
                world[i][j] = Tileset.NOTHING;
            }
        }

        for (Room room : dungeonRooms) {
            this.drawRoom(room, world);
        }

        // Draw edges
        for (Edge edge : edges) {
            drawEdge(edge, world);
        }

        for (Room dungeonRoom : dungeonRooms) {
            clearRoom(dungeonRoom, world);
        }
        return world;
    }
}
