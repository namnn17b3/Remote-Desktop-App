package service.impl;

import java.io.*;

import service.ConnectService;
import service.tcp.Broadcast;
import service.tcp.ShareClipboardClient;
import view.CreateFrameTCP;

import java.net.ServerSocket;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;

import view.SendFile;

import javax.crypto.NoSuchPaddingException;

public class ConnectClientServiceImpl implements ConnectService {

    private Socket cSocket = null;
//    private Socket cSocketClipboard = null;
    DataOutputStream psswrchk = null;
    DataInputStream verification = null;
    String verify = "";
    String width = "", height = "";
    private String ipServer = null;
    private String ipClient = null;

    private String pass = "";
    private Date timeCreated;
    private boolean autoConnect;
    private boolean isShareDrive;


    private List<String> driveNames ;
    ResourceBundle resourceBundle = ResourceBundle.getBundle("port");

    public ConnectClientServiceImpl(String ip, String pass, boolean isShareDrive,List<String> driveNames,String ipClient,boolean autoConnect) {
        this.ipServer = ip;
        this.pass = pass;
        this.isShareDrive = isShareDrive;
        this.driveNames=driveNames;
        this.ipClient=ipClient;
        this.autoConnect=autoConnect;
    }

    @Override
    @Deprecated
    public void InitConnectRemote() {
        System.out.println("is requesting connection to server");
        String portClient = resourceBundle.getString("client-port");
        try {
            try {
                initThreadShareClipBoardInServer();
                System.out.println("init broadcast share clipboard success");
            } catch (InterruptedException | IOException | NoSuchAlgorithmException | NoSuchPaddingException |
                     InvalidKeyException e) {
                System.out.println("init broadcast share clipboard failed cause "+ e.getMessage());
            }
            cSocket = new Socket(this.ipServer, Integer.parseInt(portClient));
            // init socket connect broadcast clipboard
//            cSocketClipboard = new Socket(this.ipServer, 9011);
            System.out.println("Connecting to the Server");
            psswrchk = new DataOutputStream(cSocket.getOutputStream());
            verification = new DataInputStream(cSocket.getInputStream());
            if(this.autoConnect==true){
                psswrchk.writeUTF("autoConnect");
            }else psswrchk.writeUTF(pass);

            verify = verification.readUTF();
            this.timeCreated = new Date();
        } catch (IOException e) {
            System.out.println("socket can not connect");
            e.printStackTrace();
            Thread.currentThread().stop();
        }
        if (verify.equals("valid") || this.autoConnect==true) {
            try {
                width = verification.readUTF();
                height = verification.readUTF();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(this.isShareDrive){
                shareDrives(this.driveNames);
            }
            CreateFrameTCP abc = new CreateFrameTCP(cSocket, width, height, timeCreated, driveNames,ipClient,ipServer);
            SendFile clientSendFile = new SendFile(this.ipServer, abc);
            clientSendFile.setVisible(true);            //            ShareClipboardClient shareClipboardClient = new ShareClipboardClient(cSocketClipboard);
            // add thread resolve share clipboard server broadcast
//            try {
//                initThreadShareClipBoardInServer();
//                System.out.println("init broadcast share clipboard success");
//            } catch (InterruptedException | IOException | NoSuchAlgorithmException | NoSuchPaddingException |
//                     InvalidKeyException e) {
//                System.out.println("init broadcast share clipboard failed cause "+ e.getMessage());
//            }

        } else if(verify.equals("Invalid")) {
            System.out.println("enter the valid password");
            try {
                cSocket.close();
                Thread.currentThread().stop();
                // throw infor error pass
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

//    @Deprecated
//    private void initThreadShareClipboard() {
//        try {
//            ShareClipboardClient shareClipboard = new ShareClipboardClient(cSocketClipboard);
//        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IOException e) {
//            System.out.println("error init thread share clipboard");
//            try {
//                cSocketClipboard.close();
//                System.out.println("socket close");
//            } catch (IOException ex) {
//                throw new RuntimeException(ex);
//            }
//            Thread.currentThread().stop();
//            System.out.println("thread destroy");
//            throw new RuntimeException(e);
//        }
//    }

    private void initThreadShareClipBoardInServer() throws InterruptedException, IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
        CountDownLatch serverReady = new CountDownLatch(1);
        // init thread share clipboard
        new Broadcast(serverReady).start();
        // wait main thread ready?
        serverReady.await();
        // init socket client for share clipboard - local server
        Socket socketClient = new Socket("127.0.0.1", 9011);
        // init thread share clipboard server equals client ShareClipboardClient
        new ShareClipboardClient(socketClient);
    }

    @Override
    public void shareDrives(List<String> list) {
        try {
            ServerSocket sc = new ServerSocket(9012);
            System.out.println("Server share clip start....");
            Socket s = sc.accept();
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));

            for (String temp : list) {
                bw.write(temp);
                bw.newLine();
            }
            bw.flush();
        } catch (IOException ex) {
            Logger.getLogger(ConnectServerServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void receiveDrives() {
//        try {
//            Socket socket = new Socket(this.ip, 9012);
//            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//
//            String line;
//            while ((line = br.readLine()) != null) {
////                System.out.println(line);
//                this.driveNames.add(line);
//                runShareDriveWindow(line);
//            }
//
//            socket.close();
//        } catch (SocketException e) {
//            if (e.getMessage().equals("Connection reset")) {
//                System.err.println("Connection reset by the server. Make sure the server is keeping the connection open.");
//            } else {
//                e.printStackTrace();
//            }
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
    }

    public void runShareDriveWindow(String command) {
//        ProcessBuilder builder = new ProcessBuilder(
//                "cmd.exe", "/c", command);
//        try {
//            builder.redirectErrorStream(true);
//            Process p = builder.start();
//            BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
//            String line;
//            while (true) {
//                line = r.readLine();
//                if (line == null) {
//                    break;
//                }
//                System.out.println(line);
//            }
//        } catch (IOException ex) {
//            Logger.getLogger(ConnectClientServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

}