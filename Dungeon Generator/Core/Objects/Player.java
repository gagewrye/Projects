package byow.Core.Objects;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

/* Playable character object that has health and is able to move across the map.
 */
public class Player {
    int xCoord;
    int yCoord;
    private int Health = 100;
    private TETile Avatar = Tileset.AVATAR;
    private final TETile[][] world;

    public String name;

    // spawns a player in the world
    public Player(int x, int y, TETile[][] world) {
        this.xCoord = x;
        this.yCoord = y;
        this.world = world;
        drawPlayer();
    }

    public Player(int x, int y, int health, TETile[][] world) {
        this.Health = health;
        this.xCoord = x;
        this.yCoord = y;
        this.world = world;
        drawPlayer();
    }

    public void setAvatar(TETile tile){
        removePlayer();
        this.Avatar = tile;
        drawPlayer();
    }

    public TETile getAvatar() {
        return this.Avatar;
    }


    // moves player
    public void move(int x, int y) {
        removePlayer();
        this.xCoord += x;
        this.yCoord += y;
        if (world[this.xCoord][this.yCoord] == Tileset.WALL) {
            this.xCoord -= x;
            this.yCoord -= y;
            this.harm(1);
        }
        drawPlayer();
    }

    public void teleport(int x, int y) {
        removePlayer();
        this.xCoord = x;
        this.yCoord = y;
        drawPlayer();
    }
    public int getxCoord() {
        return this.xCoord;
    }
    public int getyCoord() {
        return this.yCoord;
    }

    // erases player avatar
    private void removePlayer() {
        world[this.xCoord][this.yCoord] = Tileset.FLOOR;
    }

    // draws player avatar
    public void drawPlayer() {
        world[this.xCoord][this.yCoord] = Avatar;
    }
    private void setHealth(int x) {
        this.Health = x;
    }

    public int getHealth() {
        return this.Health;
    }

    public void heal(int amount) {
        this.setHealth(this.getHealth() + amount);
        if (this.getHealth() > 100) {
            this.setHealth(100);
        }
    }
    public void harm(int amount) {
        this.setHealth(this.getHealth() - amount);
    }
}
