package quizapp;

import java.io.IOException;
import java.net.*;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) throws SocketException, UnknownHostException, IOException {
        Scanner scn = new Scanner(System.in);

        DatagramSocket clientSocket = new DatagramSocket();
        InetAddress server_ip = InetAddress.getByName("localhost");

        byte[] sendData = new byte[2048];
        byte[] receiveData = new byte[2048];
        String client_message = "";
        String server_response = "";

        DatagramPacket sendPacket = null;
        DatagramPacket receivePacket = null;

        System.out.print("Enter Y/N to connect to QuizAPP or quit : ");
        char option = scn.nextLine().charAt(0);

        if (option == 'Y') {
            System.out.print("Enter your username: ");
            String username = scn.nextLine();

            System.out.println("\nConnected\n");
            client_message = "JOIN#" + username;

            sendData = client_message.getBytes();
            sendPacket = new DatagramPacket(sendData, sendData.length, server_ip, 9877);
            clientSocket.send(sendPacket);

            receivePacket = new DatagramPacket(receiveData, receiveData.length);
            clientSocket.receive(receivePacket);

            server_response = new String(receivePacket.getData());
            String[] response_parts = server_response.split("#");

            if (response_parts[0].equals("ACCEPT")) {
                System.out.println(response_parts[1]);
                while (option != 'N') {
                    client_message = scn.nextLine();
                    if (client_message.contains("questions")) {
                        client_message = "QUESTION#" + client_message;
                    } else if (client_message.contains("quit")) {
                        client_message = "QUIT#" + client_message;
                    } else if (client_message.contains("answers")) {
                        client_message = "ANSWER#" + client_message;
                    } else {
                        client_message = "1#" + client_message;
                    }
                    sendData = new byte[2048];
                    sendData = client_message.getBytes();
                    sendPacket = new DatagramPacket(sendData, sendData.length, server_ip, 9877);
                    clientSocket.send(sendPacket);

                    receiveData = new byte[2048];
                    receivePacket = new DatagramPacket(receiveData, receiveData.length);
                    clientSocket.receive(receivePacket);
                    server_response = new String(receivePacket.getData());

                    System.out.println(server_response);
                    if (server_response.contains("Bye")) {
                        clientSocket.close();
                        System.out.println("Connection is terminated.");
                        break;
                    }
                }
            } else {
                System.out.println(response_parts[1]);

            }
        } else {
            System.out.println("Closing the application..");
        }
    }
}
