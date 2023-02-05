package byow.Core;

import byow.Core.Objects.Room;
import byow.Core.Objects.Edge;
import byow.Core.Objects.PrimObject;
import byow.Core.DataStructures.PrimObjectHeap;
import byow.TileEngine.TETile;

import java.util.Random;


/**
 * Dungeon generator
 * Applies Prim's Algorithm to find paths and connect rooms
 */
public class DungeonGenerator {

    private final RoomFactory roomFactory;
    private final DungeonVisualizer visualizer;

    /**
     * Dungeon Generator
     * initializes the room factory and prepares for a dungeon to be generated
     *
     * @param roomFactory Factory that provides the rooms
     * @see RoomFactory
     */
    public DungeonGenerator(RoomFactory roomFactory) {
        this.roomFactory = roomFactory;
        this.visualizer = new DungeonVisualizer();
    }

    public DungeonGenerator() {
        this(new RoomFactory());
    }

    public DungeonGenerator(Random seed) {
        this(new RoomFactory(seed));
    }

    public void setMaximumX(int x) {
        this.roomFactory.setMaximumX(x);
    }
    public void setMaximumY(int y) {
        this.roomFactory.setMaximumY(y);
    }

    /**
     *
     * Get minimum spanning tree with Prim's algorithm
     *
     * @param rooms Rooms which to be connected
     * @return Minimum spanning tree represented as edges
     */
    private Edge[] primAlgorithm(Room[] rooms) {
        // Generate the graph from given rooms
        // ID of each room is the index in which the room is located at.
        int rootIndex = 0;

        PrimObjectHeap heap = new PrimObjectHeap(rooms.length);
        PrimObject[] objects = new PrimObject[rooms.length];
        Room[] parent = new Room[rooms.length];

        // Initialize all objects
        for (int i = 0; i < rooms.length; i++) {
            parent[i] = null;
            int INFINITY = 999999;
            objects[i] = new PrimObject(rooms[i], INFINITY, i);
        }
        objects[rootIndex].setValue(0);

        // Insert all prim objects to heap
        for (PrimObject object : objects) {
            heap.insert(object);
        }

        // Edges between rooms
        Edge[] edges = new Edge[rooms.length - 1]; // (V - 1)
        int edgePointer = 0;

        // Actual Prim's Algorithm
        while (!heap.isEmpty()) {
            // Delete the minimum
            PrimObject currentObject = heap.removeMin();
            // Add edge
            if (parent[currentObject.getIndex()] != null) {
                Edge edge = new Edge(currentObject.getRoom(), parent[currentObject.getIndex()]);
                edges[edgePointer] = edge;
                edgePointer++;
            }

            // Go through all the rooms
            for (int i = 0; i < objects.length; i++) {
                // No paths to self
                if (currentObject.getIndex() != i) {
                    double distance = currentObject.getRoom().distance(objects[i].getRoom());
                    if (distance < objects[i].getValue() && heap.contains(objects[i])) {
                        parent[i] = currentObject.getRoom();
                        objects[i].setValue(distance);
                        heap.update(objects[i]);
                    }
                }
            }
        }

        return edges;
    }

    /**
     * Generate a dungeon with given number of rooms.
     *
     * @param rooms Amount of rooms
     * @return Generated dungeon
     */
    public Dungeon generateDungeon(int rooms) {
        Room[] dungeonRooms = new Room[rooms];

        // Builds the rooms
        for (int i = 0; i < rooms; i++) {
            Room room = this.roomFactory.produceRoom();
            dungeonRooms[i] = room;
        }

        // Calculate the edges between the rooms
        Edge[] edges = primAlgorithm(dungeonRooms);

        TETile[][] world = visualizer.createMap(dungeonRooms, edges);

        return new Dungeon(dungeonRooms, world);
    }
}
