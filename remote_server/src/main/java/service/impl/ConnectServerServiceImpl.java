package service.impl;

import constant.Constant;
import service.ConnectService;
import service.tcp.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;

import view.ServerFileSend;

import javax.crypto.NoSuchPaddingException;
import javax.imageio.ImageIO;

import view.CreateFrameTCP;
import view.ServerDrawMenu;

public class ConnectServerServiceImpl implements ConnectService {

    ServerSocket socket = null;
    DataInputStream password = null;
    DataOutputStream verify = null;
    String width = "";
    String height = "";
    private String pass = "";
    private boolean isShareDrive = false;
    private Socket cSocketClipboard = null;
    private String ipClient;
    private Map<String, Set<String>> listIpClient_Drive = new HashMap<>();

    private ServerDrawMenu serverDrawMenu = new ServerDrawMenu();
    ResourceBundle resourceBundle = ResourceBundle.getBundle("port");

    public ConnectServerServiceImpl(String pass, boolean chooseShareChipBoard) {
        this.pass = pass;
        Constant.IS_SELECTED_SHARE_CLIPBOARD = chooseShareChipBoard;
    }

    @Override
    public void InitConnectRemote() {

        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                ServerFileSend fileSend = new ServerFileSend();
                try {
                    fileSend.startServer();
                } catch (IOException ex) {
                    Logger.getLogger(ConnectServerServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                Robot robot = null;
                Rectangle rectangle = null;
                try {
                    String portServer = resourceBundle.getString("serve-port");
                    socket = new ServerSocket(Integer.parseInt(portServer));
                    System.out.println("server is running with port = " + portServer + " Awaiting Connection from Client");
                    GraphicsEnvironment gEnv = GraphicsEnvironment.getLocalGraphicsEnvironment();
                    GraphicsDevice gDev = gEnv.getDefaultScreenDevice();

                    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
                    String width = "" + dim.getWidth();
                    String height = "" + dim.getHeight();
                    rectangle = new Rectangle(dim);
                    robot = new Robot(gDev);
                    System.out.println("isChooseShareClipboard = "+ Constant.IS_SELECTED_SHARE_CLIPBOARD);
                    while (true) {
                        Socket sc = socket.accept();
                        ipClient = sc.getInetAddress().getHostAddress();
                        System.out.println("Client Connected with IP: " + ipClient);
                        password = new DataInputStream(sc.getInputStream());
                        verify = new DataOutputStream(sc.getOutputStream());
                        String pssword = password.readUTF();
                        if (pssword.equals(pass) || pssword.equals("autoConnect")) {
                            verify.writeUTF("valid");
                            verify.writeUTF(width);
                            verify.writeUTF(height);
                            // initThreadShareClipBoardInServer pub/sub to server
                            if(Constant.IS_SELECTED_SHARE_CLIPBOARD) {
                                System.out.println("isChooseShareClipboard = "+ Constant.IS_SELECTED_SHARE_CLIPBOARD);
                                initThreadShareClipboard(ipClient);
                            }
                            //init draw
                            if(!serverDrawMenu.isVisible())serverDrawMenu.setVisible(true);

                            //init share drives
                            receiveDrives();
                            // init thread send screen
                            new SendScreen(sc, robot, rectangle);
                            // init thread receive screen
                            new ReceiveEvents(sc, robot);
                        } else {
                            verify.writeUTF("Invalid");
                        }
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        Thread thread3 = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try (ServerSocket serv = new ServerSocket(25000)) {
                        System.out.println("waiting...");
                        try (Socket socket = serv.accept()) {
                            BufferedImage screencapture = new Robot().createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
                            System.out.println("client connected");
                            ImageIO.write(screencapture, "jpg", socket.getOutputStream());
                            System.out.println("sent");
                        }
                    } catch (IOException | AWTException ex) {
                        System.out.println("Cannot get run screenshot server");
                    }
                }
            }

        });

        Thread thread4 = new Thread(new Runnable() {
            @Override
            public void run() {
                final int port = 9876;

                try (ServerSocket serverSocket = new ServerSocket(port)) {
                    System.out.println("Server turning off drives listening on port " + port);

                    while (true) {
                        try (Socket clientSocket = serverSocket.accept(); InputStream inputStream = clientSocket.getInputStream()) {

                            System.out.println("Client connected: " + clientSocket.getInetAddress());

                            // Read data from the client
                            byte[] buffer = new byte[1024];
                            int bytesRead;
                            while ((bytesRead = inputStream.read(buffer)) != -1) {
                                String receivedData = new String(buffer, 0, bytesRead);
                                System.out.println("Received ip to delete drives: " + receivedData);
                                deleteDrive(receivedData);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();

    }

//    private void initThreadShareClipBoardInServer() throws InterruptedException, IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
//        CountDownLatch serverReady = new CountDownLatch(1);
//        // init thread share clipboard
//        new Broadcast(serverReady).start();
//        // wait main thread ready?
//        serverReady.await();
//        // init socket client for share clipboard - local server
//        Socket socketClient = new Socket("127.0.0.1", 9011);
//        // init thread share clipboard server equals client ShareClipboardClient
//        new ShareClipboardClient(socketClient);
//    }


    @Deprecated
    private void initThreadShareClipboard(String ipClient) {
        try {
            int timeout = 10000; // timeout == 10s
            this.cSocketClipboard = new Socket();
            System.out.println("start timeout 10s wait connect share clipboard");
            this.cSocketClipboard.connect(new InetSocketAddress(ipClient, 9011), timeout);
            ShareClipboardClient shareClipboard = new ShareClipboardClient(cSocketClipboard);
            System.out.println("connect share clipboard success");
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IOException e) {
            System.out.println("error init thread share clipboard");
            try {
                cSocketClipboard.close();
                System.out.println("socket close");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            Thread.currentThread().stop();
            System.out.println("thread destroy");
            throw new RuntimeException(e);
        }
    }

    @Override
    public void shareDrives(java.util.List<String> list) {
//        try {
//            ServerSocket sc = new ServerSocket(9012);
//            System.out.println("Server share clip start....");
//            Socket s = sc.accept();
//            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
//
//            for (String temp : list) {
//                bw.write(temp);
//                bw.newLine();
//            }
//            bw.flush();
//        } catch (IOException ex) {
//            Logger.getLogger(ConnectServerServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

    public static String convertNumber(int number) {
        switch (number) {
            case 0:
                return "F";
            case 1:
                return "G";
            case 2:
                return "H";
            case 3:
                return "I";
            case 4:
                return "J";
            case 5:
                return "K";
            case 6:
                return "M"; // Or throw an exception for unknown input
            default:
                return "Z";
        }
    }

    public static String generateCommand(String driveName, int len, String ipClent, String username, String password, String driveShareName) {
        String temp = "net use %s: \\" + "\\" + "%s\\" + "%s /u:%s %s";
        String command = String.format(temp, driveShareName, ipClent, driveName, username, password);
        return command;
    }

    private static void addValue(Map<String, Set<String>> map, String key, String value) {
        // If the key is not present, create a new set for the key
        map.computeIfAbsent(key, k -> new HashSet<>()).add(value);
    }

    public void receiveDrives() {
        try {
            Socket socket = new Socket(this.ipClient, 9012);
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String line;
            while ((line = br.readLine()) != null) {
//                System.out.println(line);
                String driveShareName = convertNumber(this.listIpClient_Drive.size());
                String command = generateCommand(line, this.listIpClient_Drive.size(), this.ipClient, "Admins", "iop", driveShareName); // tài khoản windows
                addValue(this.listIpClient_Drive, this.ipClient, driveShareName);

                for (Map.Entry<String, Set<String>> entry : this.listIpClient_Drive.entrySet()) {
                    String ip = entry.getKey();
                    System.out.print("IP: " + ip);
                    System.out.println(Arrays.asList(entry.getValue()));
                }
                runShareDriveWindow(command);
            }
            socket.close();
        } catch (SocketException e) {
            if (e.getMessage().equals("Connection reset")) {
                System.err.println("Connection reset by the server. Make sure the server is keeping the connection open.");
            } else {
                e.printStackTrace();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void runShareDriveWindow(String command) {
        System.out.println("Command: " + command);
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
            Logger.getLogger(ConnectClientServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void deleteDrive(String clientIp) {
        System.out.println("Client IP to delete drive: " + clientIp);
        for (Map.Entry<String, Set<String>> entry : this.listIpClient_Drive.entrySet()) {
            String ip = entry.getKey();
            System.out.println("IP in listIpClient_Drive: " + ip);
            if (ip.equals(clientIp)) {
                String temp = "net use %s: /delete";
                Set<String> listDriveName = entry.getValue();
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
        this.listIpClient_Drive.remove(clientIp);

    }

}