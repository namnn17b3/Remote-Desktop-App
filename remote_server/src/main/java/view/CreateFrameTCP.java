package view;

import service.tcp.ReceiveScreen;
import service.tcp.SaveRecordEvents;
import service.tcp.SendEvents;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyVetoException;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CreateFrameTCP extends Thread {

    private Date timeCreated;
    String width = "", height = "";
    private JFrame frame = new JFrame();
    private Thread thread;
    private java.util.List<String> driveNames;
    private String clientIp;
    private String serverIp;
    //JDesktopPane represents the main container that will contain all connected clients' screens

    private JDesktopPane desktop = new JDesktopPane();
    private Socket cSocket = null;
    private JInternalFrame interFrame = new JInternalFrame("Server Screen", true, true, true);
    private JPanel cPanel = new JPanel();

    public CreateFrameTCP(Socket cSocket, String width, String height, Date timeCreated, java.util.List<String> driveNames, String clientIp,String serverIp) {
        this.width = width;
        this.height = height;
        this.cSocket = cSocket;
        this.timeCreated = timeCreated;
        this.driveNames = driveNames;
        this.clientIp = clientIp;
        this.serverIp=serverIp;
        start();
    }

    public void setFrame(String frameSize) {
        if (frameSize.equals("1280x720")) {
            frame.setSize(1280, 720);
        } else if (frameSize.equals("1920x1080")) {
            frame.setSize(1920, 1080);
        } else if (frameSize.equals("1600x900")) {
            frame.setSize(1600, 900);
        } else if (frameSize.equals("1280x1024")) {
            frame.setSize(1280, 1024);
        } else if (frameSize.equals("1024x768")) {
            frame.setSize(1024, 768);
        } else {
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        }

    }

    //Draw GUI per each connected client
    @Deprecated
    public void drawGUI() {
        frame.add(desktop, BorderLayout.CENTER);
        frame.setIconImage(new javax.swing.ImageIcon(getClass().getResource("/icon server/icon-remote.png")).getImage()); // set icon
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // add event to save log
        frame.addWindowListener(new SaveRecordEvents(frame, thread, cSocket, timeCreated)); // Bắt sự kiện close và save history connect
        //Show thr frame in maximized state
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        //Bat su kien ngat ket noi
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                sendCloseDriveRequest();
                System.exit(0);
            }

            private void sendCloseDriveRequest() {
                int serverPort = 9876;
                try ( Socket socket = new Socket(serverIp, serverPort);  OutputStream outputStream = socket.getOutputStream()) {

                    System.out.println("Connected to server delete drive");

                    // Send data to the server
                    String dataToSend = clientIp;
                    outputStream.write(dataToSend.getBytes());
                    outputStream.flush();
                    System.out.println("Sent to server: " + dataToSend);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);        //CHECK THIS LINE
        frame.setVisible(true);
        interFrame.setLayout(new BorderLayout());
        interFrame.getContentPane().add(cPanel, BorderLayout.CENTER);
        interFrame.setSize(100, 100);
        desktop.add(interFrame);

        try {
            //Initially show the internal frame maximized
            interFrame.setMaximum(true);
        } catch (PropertyVetoException ex) {
            ex.printStackTrace();
        }

        //This allows to handle KeyListener events
        cPanel.setFocusable(true);
        interFrame.setVisible(true);

    }

    public void deleteDrive(java.util.List<String> listDriveNames) {
//        String temp = "net use %s: /delete";
//        java.util.List<String> listName = new ArrayList<>();
//        if (listDriveNames.size()==1) {
//            listName.add("X");
//        }
//        if (listDriveNames.size() ==2) {
//            listName.add("X");
//            listName.add("Y");
//        }
//        if (listDriveNames.size() ==3) {
//            listName.add("X");
//            listName.add("Y");
//            listName.add("Z");
//        }
//        for (String driveName : listName) {
//            String command = String.format(temp, driveName);
//            ProcessBuilder builder = new ProcessBuilder(
//                    "cmd.exe", "/c", command);
//            try {
//                builder.redirectErrorStream(true);
//                Process p = builder.start();
//            } catch (IOException ex) {
//                Logger.getLogger(CreateFrameTCP.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
//
    }

    @Override
    public void run() {
        // init thread
        thread = Thread.currentThread();
        //Used to read screenshots
        DataInputStream in = null;
        //start drawing GUI
        drawGUI();

        try {
            in = new DataInputStream(cSocket.getInputStream());
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        //Start receiving screenshots
        new ReceiveScreen(in, cPanel);
        //Start sending events to the client
        new SendEvents(cSocket, cPanel, width, height);
    }
}
