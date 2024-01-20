package service.tcp;

//import org.opencv.core.*;
//import org.opencv.imgcodecs.Imgcodecs;
//import org.opencv.imgproc.Imgproc;

import service.LogConnectService;
import service.impl.LogConnectServiceImpl;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ReceiveScreen extends Thread {
    private ObjectInputStream cObjectInputStream = null;
    private JPanel cPanel = null;
    private boolean continueLoop = true;
    private DataInputStream dis = null;
    InputStream oin = null;
    BufferedImage image1 = null;
    private LogConnectService logConnectService;

    public ReceiveScreen(DataInputStream disIn, JPanel p) {
//		oin = in;
        dis = disIn;
        cPanel = p;
        logConnectService = new LogConnectServiceImpl();
        start();

    }

    // có nén. mờ hơn nhưng xử lý mượt hơn
//	public void run(){
//		try{
//			//Read screenshots of the client and then draw them
//			while(continueLoop){
//				byte[] bytes = new byte[1024*1024];
//				int count = 0;
//				do{
//					count+=oin.read(bytes,count,bytes.length-count);
//				}while(!(count>4 && bytes[count-2]==(byte)-1 && bytes[count-1]==(byte)-39));
//
//				image1 = ImageIO.read(new ByteArrayInputStream(bytes));
//				image1 = image1.getScaledInstance(cPanel.getWidth(),cPanel.getHeight(),Image.SCALE_FAST);
//
//				//Draw the received screenshots
//
//				Graphics graphics = cPanel.getGraphics();
//				graphics.drawImage(image1, 0, 0, cPanel.getWidth(), cPanel.getHeight(), cPanel);
//			}
//
//		} catch(IOException ex) {
//			ex.printStackTrace();
//		}
//	}
    // không nén , nét hơn và xử lý k mượt bằng
    public void run() {
//		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        try {
            while (continueLoop) {
//				System.out.println("receive image from server");

                // Receive the length of the image data
                int imageDataLength = dis.readInt();

                // Prepare a byte array to receive the image data
                byte[] imageBytes = new byte[imageDataLength];

                // Read the image data
                int bytesRead = 0;
                while (bytesRead < imageDataLength) {
                    int result = dis.read(imageBytes, bytesRead, imageDataLength - bytesRead);
                    if (result == -1) {
                        // Handle the end of the stream or an error
                        break;
                    }
                    bytesRead += result;
                }

                if (bytesRead > 0) {
//					System.out.println("Received " + bytesRead + " bytes.");
                    ByteArrayInputStream bais = new ByteArrayInputStream(imageBytes, 0, bytesRead);
                    image1 = ImageIO.read(bais);

//					image1 = applyGaussianBlur(imageBytes);

                    // Draw the received screenshots
                    Graphics graphics = cPanel.getGraphics();
                    graphics.drawImage(image1, 0, 0, cPanel.getWidth(), cPanel.getHeight(), cPanel);
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

//	private BufferedImage applyGaussianBlur(byte[] imageBytes) {
//		Mat image = Imgcodecs.imdecode(new MatOfByte(imageBytes), Imgcodecs.IMREAD_COLOR);
//		Mat destination = new Mat(image.rows(),image.cols(),image.type());
//		Imgproc.GaussianBlur(image, destination, new Size(0,0), 10);
//		Core.addWeighted(image, 1.5, destination, -0.5, 0, destination);
//		return matToBufferedImage(image);
//	}
//
//	private BufferedImage matToBufferedImage(Mat matrix) {
//		int cols = matrix.cols();
//		int rows = matrix.rows();
//		int elemSize = (int) matrix.elemSize();
//		byte[] data = new byte[cols * rows * elemSize];
//		matrix.get(0, 0, data);
//		BufferedImage image = new BufferedImage(cols, rows, BufferedImage.TYPE_3BYTE_BGR);
//		final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
//		System.arraycopy(data, 0, targetPixels, 0, data.length);
//		return image;
//	}
}
