
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import service.LogConnectService;
import service.impl.ConnectClientServiceImpl;
import service.impl.LogConnectServiceImpl;
import view.CreateFrameTCP;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
/**
 *
 * @author trinh
 */
public class testViewHistory {

    /**
     * @param args the command line arguments
     */
    private Map<String, List<String>> listIpClient_Drive = new HashMap<>();

    private static void addValue(Map<String, List<String>> map, String key, String value) {
        // If the key is not present, create a new list for the key
        map.computeIfAbsent(key, k -> new ArrayList<>()).add(value);
    }

    public void deleteDrive(String clientIp) {
        addValue(listIpClient_Drive, "192.168.1.7", "E");
        addValue(listIpClient_Drive, "192.168.1.7", "F");
        addValue(listIpClient_Drive, "192.168.1.5", "G");
        addValue(listIpClient_Drive, "192.168.1.5", "H");

        for (Map.Entry<String, List<String>> entry : this.listIpClient_Drive.entrySet()) {
            String ip = entry.getKey();
            if (ip.equals(clientIp)) {
                String temp = "net use %s: /delete";
                List<String> listDriveName = entry.getValue();
                for (String driveName : listDriveName) {
                    String command = String.format(temp, driveName);
                    System.out.println(command);
                    ProcessBuilder builder = new ProcessBuilder(
                            "cmd.exe", "/c", command);
                    try {
                        builder.redirectErrorStream(true);
                        Process p = builder.start();
                    } catch (IOException ex) {
                        Logger.getLogger(CreateFrameTCP.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

            }

        }

    }

    public void shareDriveWindow(String command) {
        ProcessBuilder builder = new ProcessBuilder(
                "cmd.exe", "/c", command);
        try {
            builder.redirectErrorStream(true);
            Process p = builder.start();
            BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while (true) {
                line = r.readLine();
                if (line == null) {
                    break;
                }
                System.out.println(line);
            }
        } catch (IOException ex) {
            Logger.getLogger(testViewHistory.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void deleteDrive(List<String> listDriveNames) {
        String temp = "net use %s: /delete";

        for (String driveName : listDriveNames) {
            String command = String.format(temp, driveName);
            ProcessBuilder builder = new ProcessBuilder(
                    "cmd.exe", "/c", command);
            try {
                builder.redirectErrorStream(true);
                Process p = builder.start();
            } catch (IOException ex) {
                Logger.getLogger(testViewHistory.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public static void main(String[] args) {
        testViewHistory history = new testViewHistory();
        history.deleteDrive("192.168.1.7");
    }

}
