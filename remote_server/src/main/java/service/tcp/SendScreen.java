package service.tcp;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import view.ServerFileSend;

public class SendScreen extends Thread {
    Socket socket=null;
    Robot robot=null;
    Rectangle rectangle=null;
    boolean continueLoop=true;

    OutputStream oos=null;

    public SendScreen(Socket socket,Robot robot,Rectangle rect) {
        this.socket=socket;
        this.robot=robot;
        rectangle=rect;
        start();
    }

//    public void run(){
//
//        try{
//            oos=socket.getOutputStream();
////            oos.flush();
//        }catch(IOException ex){
//            ex.printStackTrace();
//        }
//
//        while(continueLoop){
//            BufferedImage image=robot.createScreenCapture(rectangle);
//
//            try{
//                ImageIO.write(image,"jpeg",oos);
//            }catch(IOException ex){
//                ex.printStackTrace();
//            }
//
//            try{
//                Thread.sleep(10);
//            }catch(InterruptedException e){
//                e.printStackTrace();
//            }
//        }
//    }

    // ham nay net hon
    @Deprecated
    public void run() {
        try {
            oos = socket.getOutputStream();
            DataOutputStream dos = new DataOutputStream(oos);
            while (continueLoop && !Thread.currentThread().isInterrupted()) {
                BufferedImage image = robot.createScreenCapture(rectangle);
                try {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    ImageIO.write(image, "jpeg", baos);
                    byte[] imageBytes = baos.toByteArray();
                    if (!Thread.currentThread().isInterrupted()) {
                        dos.writeInt(imageBytes.length);
                        oos.write(imageBytes);
                        oos.flush();
                    } else {
                        // Handle thread interruption here if needed
                        break; // Exit the loop
                    }
                } catch (IOException ex) {
                    Thread.currentThread().stop(); // Re-interrupt the thread
                    // Handle the IOException and close the socket
                    ex.printStackTrace();
                    break; // Exit the loop
                }

                try {
                    Thread.sleep(0);
                } catch (InterruptedException e) {
                    // Handle thread interruption when sleeping
                    Thread.currentThread().stop(); // Re-interrupt the thread
                    e.printStackTrace();
                    break; // Exit the loop
                }
            }
        } catch (IOException ex) {
            try {
                Thread.currentThread().stop(); // Re-interrupt the thread
                socket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            // Handle any IOException that might occur at the outer try-catch
            ex.printStackTrace();
        }
        // Clean up or perform any necessary final actions here
    }
}
