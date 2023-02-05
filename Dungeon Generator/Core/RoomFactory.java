package byow.Core;

import byow.Core.Objects.Room;
import java.util.Random;

/**
 * Room factory that produces random sized rooms.
 */
public class RoomFactory {

    // Minimum
    private int minimumWidth;
    private int minimumHeight;

    // Maximum
    private int maximumWidth;
    private int maximumHeight;
    private int maximumX;
    private int maximumY;


    // Randomness
    private final Random random;

    /***
     * Create a room factory with custom random object
     * @param random Random class
     * @see Random
     */
    public RoomFactory(Random random) {
        this.random = random;
        this.maximumX = 200;
        this.maximumY = 250;
        this.maximumWidth = 15;
        this.maximumHeight = 10;
        this.minimumWidth = 8;
        this.minimumHeight = 5;
    }

    public RoomFactory() {
        this(new Random());
    }

    /***
     * Set maximum height of a room
     * @param maximumHeight Value for the maximum height
     */
    public void setMaximumHeight(int maximumHeight) {
        this.maximumHeight = maximumHeight;
    }

    /***
     * Set maximum width of a room
     * @param maximumWidth Value for the maximum width
     */
    public void setMaximumWidth(int maximumWidth) {
        this.maximumWidth = maximumWidth;
    }

    /***
     * Set minimum height
     * @param minimumHeight Value for the maximum height
     */
    public void setMinimumHeight(int minimumHeight) {
        this.minimumHeight = minimumHeight;
    }


    /**
     * Set minimum width
     * @param minimumWidth Value for the minimum width
     */
    public void setMinimumWidth(int minimumWidth) {
        this.minimumWidth = minimumWidth;
    }

    /***
     * Returns maximum height
     * @return Maximum height
     */
    public int getMaximumHeight() {
        return maximumHeight;
    }

    /***
     * Returns maximum width
     * @return Maximum width
     */
    public int getMaximumWidth() {
        return maximumWidth;
    }

    /***
     * Returns minimum height
     * @return Minimum height
     */
    public int getMinimumHeight() {
        return minimumHeight;
    }

    /***
     * Returns minimum width
     * @return Minimum width
     */
    public int getMinimumWidth() {
        return minimumWidth;
    }

    /***
     * Get the maximum X-coordinate in which the room can be generated to
     * @return maximum X-coordinate in which the room can be generated to
     */
    public int getMaximumX() {
        return maximumX;
    }

    /***
     * Sets the maximum X-coordinate in which the room can be generated to
     * @param maximumX maximum X-coordinate in which the room can be generated to
     */
    public void setMaximumX(int maximumX) {
        this.maximumX = maximumX;
    }

    /***
     * Get maximum Y-coordinate in which the room can be generated to
     * @return Maximum Y-coordinate in which the room can be generated to
     */
    public int getMaximumY() {
        return maximumY;
    }

    /**
     * Sets the maximum Y-coordinate in which the room can be generated to
     * @param maximumY maximum Y-coordinate in which the room can be generated to
     */
    public void setMaximumY(int maximumY) {
        this.maximumY = maximumY;
    }

    /***
     * Create a random sized room at a random location.
     * @return Room
     */
    public Room produceRoom() {
        int x = random.nextInt(maximumX);
        int y = random.nextInt(maximumY);
        int width = minimumWidth + random.nextInt(maximumWidth);
        int height = minimumHeight + random.nextInt(maximumHeight);
        return new Room(x, y, width, height);
    }
}
