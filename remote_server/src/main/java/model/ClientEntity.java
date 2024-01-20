/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 *
 * @author trinh
 */
public class ClientEntity {
    private int id;
    private String ip;
    private String password;

    public ClientEntity() {
    }

    public ClientEntity(int id, String ip, String password) {
        this.id = id;
        this.ip = ip;
        this.password = password;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setIp() {
        try {
            InetAddress localHost = InetAddress.getLocalHost();
            String ipAddress = localHost.getHostAddress();
            System.out.println("IP Address of your laptop: " + ipAddress);
            this.ip = ipAddress;
        } catch (UnknownHostException e) {
                        this.ip = "Unknown IP";
        }
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public String getIp() {
        return ip;
    }

    public String getPassword() {
        return password;
    }
    public Object[] toObject(){
        return new Object[]{
            id,ip,password
        };
    }
}
