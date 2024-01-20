/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Enumeration;

/**
 *
 * @author trinh
 */
public class ServerEntity {
    private int id;
    private String ip;
    private String password;

    public ServerEntity() {
    }

    public ServerEntity(int id, String ip, String password) {
        this.id = id;
        this.ip = ip;
        this.password = password;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setIp() {
//        try {
//            InetAddress localHost = InetAddress.getLocalHost();
//            String ipAddress = localHost.getHostAddress();
//            System.out.println("IP Address of your laptop: " + ipAddress);
//            this.ip = ipAddress;
//        } catch (UnknownHostException e) {
//                        this.ip = "Unknown IP";
//        }
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();

            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();
                if (networkInterface.isUp() && !networkInterface.isLoopback()) {
                    Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
                    while (addresses.hasMoreElements()) {
                        InetAddress address = addresses.nextElement();
                        if (
                            address.isSiteLocalAddress() &&
                            !address.isLoopbackAddress() &&
                            address.getHostAddress().contains(".") &&
                            (networkInterface.getDisplayName().contains("Wi-Fi") ||
                            networkInterface.getDisplayName().contains("Wireless Adapter"))
                        ) {
                            System.out.println("Interface: " + networkInterface.getDisplayName());
                            System.out.println("IPv4 Address: " + address.getHostAddress());
                                        this.ip =address.getHostAddress();
                        }
                    }
                }
            }
        } catch (Exception e) {
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