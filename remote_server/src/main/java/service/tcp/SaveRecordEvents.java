package service.tcp;

import model.ConnectHisFile;
import service.LogConnectService;
import service.impl.LogConnectServiceImpl;
import utils.ComputerUtils;
import utils.DateUtils;
import utils.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.Socket;
import java.util.Date;

public class SaveRecordEvents extends WindowAdapter {
    private JFrame frame;
    private Thread thread;
    private Date timeCreated;
    private Socket socket;
    private LogConnectService logConnectService;
    public SaveRecordEvents(JFrame frame, Thread thread, Socket socket, Date timeCreated) {
        this.frame = frame;
        this.thread = thread;
        this.timeCreated = timeCreated;
        this.socket = socket;
        logConnectService = new LogConnectServiceImpl();
    }

    @Override
    public void windowClosing(WindowEvent e) {
        String ipServer = String.valueOf(this.socket.getInetAddress());
        System.out.println("server ip = "+ this.socket.getInetAddress());
        System.out.println("time created = "+ DateUtils.getFormatTimeConnect(this.timeCreated));
        System.out.println("time end = "+ DateUtils.getFormatTimeConnect(new Date()));
        Long aboutTime = (new Date().getTime() - this.timeCreated.getTime())/1000;

        ConnectHisFile connectHisFile = new ConnectHisFile.Builder()
                .withIpClient(ComputerUtils.getIpComputer())
                .withIpServer(ipServer.substring(1))
                .withTimeCreated(DateUtils.getFormatTimeConnect(this.timeCreated))
                .withTimeEnded(DateUtils.getFormatTimeConnect(new Date()))
                .withConnectedTimePeriod(String.valueOf(aboutTime))
                .build();
        String log = StringUtils.formatLogConnectToFile(connectHisFile);
        logConnectService.saveLogConnectToFile(log);
    }
}

class CustomDialog{

    public static void showCustomDialog(JFrame parentFrame) {
        JDialog customDialog = new JDialog(parentFrame, "", true);

        JLabel label = new JLabel("Bạn có chắc chắn muốn dừng điều khiển không?");
        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Cancel");

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                customDialog.setVisible(false);
                customDialog.dispose();
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Đóng dialog");
                customDialog.setVisible(false);
            }
        });

        customDialog.setLayout(new BoxLayout(customDialog.getContentPane(), BoxLayout.PAGE_AXIS));
        customDialog.add(label);
        customDialog.add(okButton);
        customDialog.add(cancelButton);

        customDialog.pack();
        customDialog.setLocationRelativeTo(parentFrame);
        customDialog.setVisible(true);
    }
}
