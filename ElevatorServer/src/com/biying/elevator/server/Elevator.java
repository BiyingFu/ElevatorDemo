package com.biying.elevator.server;

/**
 * Created by biying on 10/28/17.
 */
public class Elevator {
    public enum Direction {
        UP,
        DOWN,
        IDLE
    }

    private Elevator.Direction direction = Direction.IDLE;
    private Integer currentFloor = 1;

    public void moveTo(Integer target) {
        currentFloor = target;
        System.out.println("Arrived floor:" + currentFloor);
    }

    public void setDirection(Elevator.Direction direction) {
        this.direction = direction;
    }

    public Direction getDirection() {
        return direction;
    }

    public Integer getCurrentFloor() {
        return currentFloor;
    }
}
