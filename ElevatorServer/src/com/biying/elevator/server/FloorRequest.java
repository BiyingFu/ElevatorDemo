package com.biying.elevator.server;

/**
 * Created by biying on 10/28/17.
 */

public class FloorRequest {
    public enum Direction {
        UP,
        DOWN,
        UNKNOWN
    }
    private final Integer floor;
    private final Direction direction;

    public FloorRequest(final Integer floor, final Direction direction) {
        this.floor = floor;
        this.direction = direction;
    }

    public Integer getFloor() {
        return floor;
    }

    public Direction getDirection() {
        return direction;
    }

    public String toString() {
        return "floor:" + Integer.toString(floor) + "|direction:" + direction;
    }
}
