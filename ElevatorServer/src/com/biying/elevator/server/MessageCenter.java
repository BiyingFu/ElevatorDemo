package com.biying.elevator.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by biying on 10/28/17.
 */


public class MessageCenter implements Runnable {
    private ServerSocket serverSocket;
    private Socket socket;
    private BufferedReader in;
    private String line;
    private Boolean active = true;

    private List<FloorRequest> requestList;

    public MessageCenter(int port) {
        try {
            System.out.println("Create MessageCenter");
            this.serverSocket = new ServerSocket(port);
            System.out.println("Socket established at port:" + port);
        } catch (IOException e) {
            System.out.println("Could not liston on port " + port);
            e.printStackTrace();
            System.exit(-1);
        }
        this.requestList = new LinkedList<>();
    }

    public void run() {
        try {
            this.socket = this.serverSocket.accept();
        } catch (IOException e) {
            System.out.println("Could not accept input stream");
            System.exit(-1);
        }

        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            System.out.println("Read failed");
            System.exit(-1);
        }

        Scanner scanner = new Scanner(in);
        while (active) {
            line = scanner.nextLine();
            System.out.println("Received stream: " + line);
            receiveLine(line);
        }
        try {
            serverSocket.close();
            System.out.print("Socket closed");
        } catch (IOException es) {
            System.out.println("Close socket failed");
            System.exit(-1);
        }
    }

    private void receiveLine(String line) {
        Integer floor = Integer.valueOf(line.substring(0, line.length() - 1));
        char dir = line.charAt(line.length() - 1);
        FloorRequest.Direction direction;
        if (dir == 'u') direction = FloorRequest.Direction.UP;
        else if (dir == 'd') direction = FloorRequest.Direction.DOWN;
        else direction = FloorRequest.Direction.UNKNOWN;

        FloorRequest floorRequest = new FloorRequest(floor, direction);
        receiveRequest(floorRequest);
    }

    private synchronized void receiveRequest(FloorRequest request) {
        System.out.println("Received request:" + request.toString());
        requestList.add(request);
    }

    public synchronized List<FloorRequest> getRequestList() {
        return requestList;
    }
}
