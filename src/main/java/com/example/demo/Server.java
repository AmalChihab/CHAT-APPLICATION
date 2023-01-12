package com.example.demo;

import java.io.IOException;

import java.net.*;
import java.util.ArrayList;

public class Server {
    private static ArrayList<Integer> users = new ArrayList<>();
    private static ArrayList<ClientInfo> clients = new ArrayList<>();
    private static int portClient ;
    private static byte[] incoming = new byte[256];
    private final int PORT = 8000;
    private static InetAddress address = null;
    private static String username ;

    private static ArrayList<String> msgs = new ArrayList<>();
    private DatagramSocket socket;
    private DatagramPacket packet;


    public void Initialiser(){
        try {
            this.socket = new DatagramSocket(PORT);
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

    public static void clienttosend(String username1){
       username = username1 ;
    }

    public void receiveMessageFromClient() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    DatagramPacket packet = new DatagramPacket(incoming, incoming.length); // prepare packet
                    try {
                        getSocket().receive(packet); // receive packet
                        String message = new String(packet.getData(), 0, packet.getLength());
                        msgs.add(message);

                        if (message.contains("/")) {
                            String[] msgs = message.split("/");
                            System.out.println(msgs[0] +"////"+ msgs[1] +"/////////"+ packet.getPort());
                            clients.add(new ClientInfo(Integer.parseInt(msgs[0]), msgs[1], packet.getPort()));
                        } else if(message.contains("-")){
                            System.out.println("message : " + message);
                            String[] msgs = message.split("-");
                              for(int i = 0 ; i < clients.size() ; i++) {
                                System.out.println(clients.get(i).getUsername() + "ldlddl "+clients.get(i).getPort());
                                System.out.println(msgs[0] + " et le msg : "+msgs[1]);
                                if (msgs[0].equals(clients.get(i).getUsername())){
                                    System.out.println("client found");
                                    portClient = clients.get(i).getPort() ;
                                    break;
                                }
                            }
                            //ClientChatController.addLabel(message, vBox);
                            byte[] byteMessage = msgs[1].getBytes();
                            System.out.println("port : "+portClient);
                            DatagramPacket packet1 = new DatagramPacket(byteMessage, byteMessage.length, address, portClient);
                            socket.send(packet1);
                            System.out.println("packet de message send to client");
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                }
            }
        }).start();
    }

    public DatagramSocket getSocket() {
        return socket;
    }

    public void setSocket(DatagramSocket socket) {
        this.socket = socket;
    }


    public static void main(String[] args) {
        Server server = new Server();
        server.Initialiser();
        server.receiveMessageFromClient();
    }

}