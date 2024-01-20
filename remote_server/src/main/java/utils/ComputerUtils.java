package utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

public class ComputerUtils {

    public static String getIpComputer(){
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
                            return address.getHostAddress();
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("can not find ip computer");
            return  null;
        }
        return null;
    }
}
