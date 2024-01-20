package service.tcp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;

class ResolveShareClipBoardServer extends Thread{

    private final Integer portClipBoard = 9011;

    private Socket socket;
    private byte[] buffer = new byte[1024*32];
    private int bbuffer;
    private InputStream in;
    //OutputStream out;
    private static Set<Socket> sockets = new HashSet<>();   //Thread-safe

    public ResolveShareClipBoardServer(Socket clientSocket) {
        this.socket = clientSocket;
    }

    public void run() {
        try {
            in=socket.getInputStream();
            sockets.add(socket);
            System.out.println("socket connect share clipboard add to list in server");
            for (Socket socket: sockets){
                System.out.println("socket connect receive/send clipboard = " +socket.getInetAddress());
            }
            while (true) {
                bbuffer = in.read(buffer);
                if (bbuffer<=0) break;
                System.out.println("Received: " + bbuffer + " bytes from " + socket);
                for (Socket dest : sockets) {
                    if (dest!=socket)
                        try {
                            dest.getOutputStream().write(buffer, 0, bbuffer);
                        } catch (Exception e){
                            System.out.println("Error sendind " + socket);
                        }
                }
            }
        } catch (Exception ignored) {
            System.out.println("error init share clipboard");
        }
        finally {
            sockets.remove(socket);
            try {
                socket.close();
            } catch (IOException ignored) {}
        }
    }
}

public class Broadcast extends Thread {
    private CountDownLatch serverReady;

    public Broadcast(CountDownLatch serverReady) {
        this.serverReady = serverReady;
    }

    @Override
    public void run() {
        try {
            init();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void init() throws Exception {
        System.out.println("vao day");
        ServerSocket welcomeSocket = new ServerSocket(9011);
        System.out.println("server broadcast clipboard running port 9011");
        // Đánh dấu máy chủ sẵn sàng
        serverReady.countDown();
        while (true) {
            Socket connectionSocket = welcomeSocket.accept();
            System.out.println("client connect server broadcast");
            new ResolveShareClipBoardServer(connectionSocket).start();
        }
    }
}

