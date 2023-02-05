package byow.Core;

import byow.Core.Objects.Player;
import byow.Core.Objects.Room;
import byow.TileEngine.TETile;

/**
 * Dungeon that contains rooms and the ASCII-representation.
 * Spawns the player when initialized
 */
public class Dungeon {

    private Room[] rooms = null;
    private final TETile[][] world;
    private final Player myPlayer;
    private boolean light = true;

    /***
     * Representation of a dungeon.
     * @param rooms Rooms of the dungeon
     * @param map map of the dungeon
     */
    public Dungeon(Room[] rooms, TETile[][] map) {
        this.rooms = rooms;
        this.world = map;
        this.myPlayer = new Player(rooms[0].getCenterY(), rooms[0].getCenterX(), world);
    }

    public Dungeon(TETile[][] map, Player player) {
        this.world = map;
        this.myPlayer = player;
    }

    /***
     * Returns a copy of the dungeon's map
     * @return map of the dungeon
     */
    public TETile[][] getMap() {
        return world.clone();
    }
    public boolean getLight() {return light;}
    public void lightSwitch() {light = !light;}
    /**
     *
     * @return player character
     */
    public Player getMyPlayer() {return myPlayer;}

    /**
     * Returns a copy of the rooms within the dungeon
     * @return An array of dungeon rooms.
     */
    public Room[] getRooms() {
        return rooms.clone();
    }

    @Override
    public String toString() {
        String mapString = "";
        for (TETile[] teTiles : world) {
            for (int j = 0; j < teTiles.length; j++) {
                mapString += teTiles[j];
            }
            mapString += "\n";
        }
        return mapString;
    }

}
