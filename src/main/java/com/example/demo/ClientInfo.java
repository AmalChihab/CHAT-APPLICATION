package com.example.demo;

public class ClientInfo {
    private int id ;
    private String username ;
    private String password ;

    private int port ;

    public ClientInfo(){ super();}

    public ClientInfo(int id , String username , String password){
         this.setId(id);
         this.setUsername(username);
         this.setPassword(password);
    }

    public ClientInfo(String username){
        this.username = username ;
    }

    public ClientInfo(int id , String username , int port){
        this.id = id ;
        this.username = username ;
        this.port = port ;
    }

    public ClientInfo(int id , String username ){
        this.id = id ;
        this.username = username ;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
