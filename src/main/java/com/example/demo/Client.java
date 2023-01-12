package com.example.demo;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;

public class Client {
    private static ArrayList<ClientInfo> clients = new ArrayList<>();
    private int portClient ;
    private static byte[] tampon = new byte[256];
    public static final int portS = 8000 ;
    public String username ;
    private static InetAddress address = null;
    public static DatagramSocket socket;

    public Client(){
        try {
            socket = new DatagramSocket();

        } catch (SocketException e) {
            throw new RuntimeException(e);
        }

    }

    static {
        try {
            address = InetAddress.getByName("localhost");
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    public static DatagramSocket getSocket() {
        return socket;
    }

    public static void setSocket(DatagramSocket socket) {
        Client.socket = socket;
    }

    public void sendMessageToServer(String messageToServer) {
        try {
            System.out.println(messageToServer);
            byte[] byteMessage = messageToServer.getBytes();
            DatagramPacket packet = new DatagramPacket(byteMessage, byteMessage.length, address, portS);
            getSocket().send(packet);

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error sending message to the Client!");
        }
    }

    public static void receiveMessageFromServer() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {

                    try {
                        DatagramSocket socket1 = new DatagramSocket() ;
                        DatagramPacket packet = new DatagramPacket(tampon, tampon.length); // prepare packet
                        System.out.println("attend packet");
                        socket.receive(packet); // receive packet
                        System.out.println("recieve");
                        String message = new String(packet.getData(), 0, packet.getLength());
                        System.out.println(message);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }

                }
            }
        }).start();
    }




    public static void main(String[] args) {
         receiveMessageFromServer();
    }

}