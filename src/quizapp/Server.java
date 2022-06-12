package quizapp;

import java.io.IOException;
import java.net.*;
import java.util.Random;

public class Server {

    static DatagramSocket serverSocket;

    static DatagramPacket receivePacket = null;
    static DatagramPacket sendPacket = null;

    static String client_message = "";
    static String server_response = "";

    static byte[] receiveData = new byte[2048];
    static byte[] sendData = new byte[2048];

    static String username = "";
    static InetAddress client_ip = null;
    static int client_port = -1;

    static String[] questions = {"Q1: (5+13)*2", "Q2: ((28-31)+12)*2", "Q3: ((31+13)*5)-12", "Q4: (12+28)*5", "Q5: ((67-21)-12)*3", "Q6: ((13+73)*10)-19", "QUIZ IS END", "------"};

    static Random rnd = new Random();

    public static void main(String[] args) throws SocketException, IOException {
        serverSocket = new DatagramSocket(9877);
        System.out.println("QuizApp is running...");
        int count = 0;
        while (true) {
            receiveData = new byte[2048];
            receivePacket = new DatagramPacket(receiveData, receiveData.length);
            serverSocket.receive(receivePacket);

            client_message = new String(receivePacket.getData());
            client_ip = receivePacket.getAddress();
            client_port = receivePacket.getPort();

            String[] message_parts = client_message.split("#");
            if (message_parts[0].equals("JOIN")) {
                accept_user(message_parts);
                count = 0;
            } else if (message_parts[0].equals("ANSWER")) {
                display_answers();
            } else if (message_parts[0].equals("QUESTION")) {
                Allquestions();

            } else if (message_parts[0].equals("QUIT")) {
                quit();
            } else if (message_parts[0].equals("1")) {
                if (count == 6) {
                    quit_from_exam();
                } else {
                    questions(count);
                    count++;
                }
            }
        }
    }

    public static boolean accept_user(String[] message_parts) throws IOException {
        System.out.println("User " + message_parts[1] + " wants to connect to QuizAPP.");
        System.out.println("Accepting the connection.");
        username = message_parts[1];
        server_response = "ACCEPT#1) Type '1' to START The Quiz\n" + "2) Type 'quit' to EXIT from the program\n" + "3) Type 'questions' to DISPLAY all QUESTIONS \n"
                + "4) Type 'answers' to DISPLAY all ANSWERS\n" + "5) Press any button to Skip the question\n"
                + "Select an option.\n";
        send_response();
        return true;
    }

    public static void quit() throws IOException {
        System.out.println("User " + username + " wants to exit from QuizAPP.");
        server_response = "See you again. Bye";
        send_response();

        username = "";
        client_ip = null;
        client_port = -1;
    }

    public static void quit_from_exam() throws IOException {
        System.out.println("User " + username + " wants to exit from quiz and QuizAPP");
        server_response = "\nThe answers are:\n"
                + "(5+13)*2 = 36\n"
                + "((28-31)+12)*2 = 18\n"
                + "((31+13)*5)-12 = 208\n"
                + "(12+28)*5 = 200\n"
                + "((67-21)-12)*3 = 102\n"
                + "((13+73)*10)-19 = 841\n"
                + "\nCONGRATULATIONS.. You Solved All of the Questions\n"
                + "\nQuizApp is closing..\n"
                + "\nSEE YOU AGAIN. Bye";
        send_response();
        username = "";
        client_ip = null;
        client_port = -1;
    }

    public static void display_answers() throws IOException {
        System.out.println("User " + username + " wants to display all answers.");
        server_response = "The answers are:\n"
                + "(5+13)*2 = 36\n"
                + "((28-31)+12)*2 = 18\n"
                + "((31+13)*5)-12 = 208\n"
                + "(12+28)*5 = 200\n"
                + "((67-21)-12)*3 = 102\n"
                + "((13+73)*10)-19 = 841\n";
        send_response();
    }

    public static void questions(int count) throws IOException {
        server_response = questions[count];
        send_response();
        if (count == 5) {
            quit_from_exam();
        }
    }

    public static void Allquestions() throws IOException {
        server_response = "The questions are:\n"
                + "(5+13)*2 = ?\n"
                + "((28-31)+12)*2 = ?\n"
                + "((31+13)*5)-12 = ?\n"
                + "(12+28)*5 = ?\n"
                + "((67-21)-12)*3 = ?\n"
                + "((13+73)*10)-19 = ?\n";
        send_response();
    }

    public static void send_response() throws IOException {
        sendData = new byte[2048];
        sendData = server_response.getBytes();
        sendPacket = new DatagramPacket(sendData, sendData.length, client_ip, client_port);
        serverSocket.send(sendPacket);
    }
}
