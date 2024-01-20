package service;

import java.util.List;
import model.ConnectHisFile;

public interface LogConnectService {
    void saveLogConnectToFile(String log);
    List<ConnectHisFile> getLogFromFile(String fileName);
}
