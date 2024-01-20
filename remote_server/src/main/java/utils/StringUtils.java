package utils;

import model.ConnectHisFile;

import java.util.HashMap;
import java.util.Map;

public class StringUtils {

    public static String formatLogConnectToFile(ConnectHisFile connectHisFile){
        return String.format("ipServer: %s - ipClient: %s - start: %s - end: %s - about: %s\n",
                connectHisFile.getIpServer(),
                connectHisFile.getIpClient(),
                connectHisFile.getTimeCreated(),
                connectHisFile.getTimeEnded(),
                connectHisFile.getConnectedTimePeriod());
    }

    public static ConnectHisFile getConnectHisFromStr(String input){
        String[] parts = input.split(" - ");
        Map<String, String> map = new HashMap<>();
        for (String part : parts) {
            String[] keyValue = part.split(": ");
            if (keyValue.length == 2) {
                String key = keyValue[0].trim();
                String value = keyValue[1].trim();
                map.put(key, value);
            }
        }
        return new ConnectHisFile.Builder()
                .withIpClient(map.get("ipClient"))
                .withConnectedTimePeriod(map.get("ipClient"))
                .withIpServer(map.get("ipServer"))
                .withTimeCreated(map.get("start"))
                .withTimeEnded(map.get("end"))
                .withConnectedTimePeriod(map.get("about"))
                .build();
    }
}
