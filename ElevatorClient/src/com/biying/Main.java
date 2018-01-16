package com.biying;


import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Main {
    public static Socket socket;
    public static Integer port = 4321;
    public static PrintWriter out;

    public static void main(String[] args) {
	// write your code here

        try {
            socket = new Socket("127.0.0.1", port);
        } catch (IOException e) {
            System.out.println("Could not connect to socket:" + port);
            e.printStackTrace();
            System.exit(-1);
        }

        try {
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            System.out.println("Could not write to socket");
            e.printStackTrace();
            System.exit(-1);
        }

        Scanner userInput = new Scanner(System.in);
        while (true) {
            System.out.println("Please input your floor request:");
            String input = userInput.nextLine();
            System.out.println("Your floor request is:" + input);
            if (!input.isEmpty()) {
                out.println(input);
            }
        }
    }
}
