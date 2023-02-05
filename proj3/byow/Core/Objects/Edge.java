package byow.Core.Objects;

/**
 * A connection between two rooms.
 */
public class Edge {

    private final Room first;
    private final Room second;

    private final double distance;

    /**
     * Create Edge between two rooms
     * @param first First Room
     * @param second Second Room
     */
    public Edge(Room first, Room second) {
        this.first = first;
        this.second = second;
        this.distance = first.distance(second);
    }

    // Returns distance between the rooms.
    public double getDistance() {
        return this.distance;
    }

    // Returns the first room.
    public Room getFirst() {
        return first;
    }

    // Returns the second room.
    public Room getSecond() {
        return second;
    }

    public int compareTo(Edge other) {
        int cmp = (int) ((int) distance - other.distance);
        return cmp == 0 ? 1 : cmp;
    }

    @Override
    public String toString() {
        return "[" + first + ", " + second + ", distance: " + this.distance + "]";
    }


}
