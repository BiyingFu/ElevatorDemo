package com.biying;

import com.biying.elevator.server.ElevatorManager;
import com.biying.elevator.server.MessageCenter;

public class Main {

    public static void main(String[] args) {
        MessageCenter messageCenter = new MessageCenter(4321);
        ElevatorManager elevatorManager = new ElevatorManager(messageCenter);
        elevatorManager.run();
    }
}
