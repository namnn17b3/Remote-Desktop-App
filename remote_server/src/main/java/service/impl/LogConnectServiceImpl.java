package service.impl;

import model.ConnectHisFile;
import service.LogConnectService;
import utils.StringUtils;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LogConnectServiceImpl implements LogConnectService {
    private static final String logDirectoryPath = "log_connect";
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static final String subFix = "_log.txt";

    @Override
    public void saveLogConnectToFile(String log) {
        Date currentDate = new Date();
        String dateString = dateFormat.format(currentDate);
        File logDirectory = new File(logDirectoryPath);
        if (!logDirectory.exists()) {
            logDirectory.mkdirs();
        }
        String logFileName = dateString + subFix;
        File logFile = new File(logDirectory, logFileName);
        try {
            FileWriter fileWriter = new FileWriter(logFile, true); // Sử dụng true để thêm log vào cuối file
            fileWriter.write(log);
            fileWriter.close();
            System.out.println("save log connect to file success");
        } catch (IOException e) {
            System.out.println("error when save log file");
            e.printStackTrace();
        }
    }

    @Override
    public List<ConnectHisFile> getLogFromFile(String fileName) {
        // truyền fileName = null -> lấy hết log kết nối
        // muốn lấy log kết nối theo ngày: ví dụ 2023-11-02 -> truyền fileName = 2023-11-02_log.txt;
        return readAllLogFilesInDirectory(fileName);
    }


    public List<ConnectHisFile> readAllLogFilesInDirectory(String fileName) {
        List<ConnectHisFile> res = new ArrayList<>();
        File logDirectory = new File(logDirectoryPath);
        if (logDirectory.exists() && logDirectory.isDirectory()) {
            File[] logFiles = logDirectory.listFiles();
            if (logFiles != null) {
                for (File logFile : logFiles) {
                    if (logFile.isFile() && (fileName == null || logFile.getName().equals(fileName))) {
//                        System.out.println("Reading log from file: " + logFile.getName());
                        List<ConnectHisFile> listTmp = readLogFromFile(logFile.getAbsolutePath());
                        res.addAll(listTmp);
                    }
                }
            } else {
                System.out.println("No log files found in the directory.");
            }
        } else {
            System.out.println("The specified directory does not exist or is not a directory.");
        }
        return res;
    }

    public List<ConnectHisFile> readLogFromFile(String filePath) {
        List<ConnectHisFile> listConnectHisFileLine = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line;
            while ((line = reader.readLine()) != null) {
                ConnectHisFile connectHisFile = StringUtils.getConnectHisFromStr(line);
                listConnectHisFileLine.add(connectHisFile);
            }
            reader.close();
            return listConnectHisFileLine;
        } catch (IOException e) {
            System.out.println("Error when reading the log file");
            e.printStackTrace();
        }
        return listConnectHisFileLine;
    }


}
