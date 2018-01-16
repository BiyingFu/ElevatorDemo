package com.biying.elevator.server;


import com.sun.tools.internal.ws.processor.model.Request;

import java.util.*;

import static java.lang.Thread.sleep;

/**
 * Created by biying on 10/28/17.
 */
public class ElevatorManager implements Runnable {
    private MessageCenter messageCenter;
    private Boolean active = true;
    private Elevator elevator;

    private PriorityQueue<Integer> minQueue;
    private PriorityQueue<Integer> maxQueue;

    public ElevatorManager(MessageCenter messageCenter) {
        this.messageCenter = messageCenter;
        new Thread(messageCenter).start();
        elevator = new Elevator();

        minQueue = new PriorityQueue<>(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1 - o2;
            }
        });
        maxQueue = new PriorityQueue<>(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o2 - o1;
            }
        });
    }

    public void run() {
        while (active) {
            try {
                sleep(10000);
            } catch (Exception e) {
                e.printStackTrace();
            }
            processRequest();
        }
    }

    private void processRequest() {
        List<FloorRequest> requestList = messageCenter.getRequestList();
        if (maxQueue.size() != 0) {
            elevator.setDirection(Elevator.Direction.DOWN);
        }
        else if (minQueue.size() != 0) {
            elevator.setDirection(Elevator.Direction.UP);
        }
        else {
            elevator.setDirection(Elevator.Direction.IDLE);
        }

        Iterator<FloorRequest> iterator = requestList.iterator();
        while(iterator.hasNext()) {
            FloorRequest request = iterator.next();
            if (elevator.getDirection() == Elevator.Direction.DOWN) {
                if (request.getFloor() <= elevator.getCurrentFloor()
                        && (request.getDirection() == FloorRequest.Direction.DOWN
                        || request.getDirection() == FloorRequest.Direction.UNKNOWN)) {
                    maxQueue.add(request.getFloor());
                    iterator.remove();
                }
            }
            else if (elevator.getDirection() == Elevator.Direction.UP) {
                if (request.getFloor() >= elevator.getCurrentFloor()
                        && (request.getDirection() == FloorRequest.Direction.UP
                        || request.getDirection() == FloorRequest.Direction.UNKNOWN)) {
                    minQueue.add(request.getFloor());
                    iterator.remove();
                }
            }
            else {
                if (request.getFloor() >= elevator.getCurrentFloor()) {
                    elevator.setDirection(Elevator.Direction.UP);
                    minQueue.add(request.getFloor());
                }
                else {
                    elevator.setDirection(Elevator.Direction.DOWN);
                    maxQueue.add(request.getFloor());
                }
                iterator.remove();
            }
        }

        if (elevator.getDirection() == Elevator.Direction.DOWN && maxQueue.size() != 0) {
            Integer targetFloor = maxQueue.poll();
            while (targetFloor == maxQueue.peek()) {
                targetFloor = maxQueue.poll();
            }
            elevator.moveTo(targetFloor);
        }
        else if (elevator.getDirection() == Elevator.Direction.UP && minQueue.size() != 0) {
            Integer targetFloor = minQueue.poll();
            while (targetFloor == minQueue.peek()) {
                targetFloor = minQueue.poll();
            }
            elevator.moveTo(targetFloor);
        }
    }
}
