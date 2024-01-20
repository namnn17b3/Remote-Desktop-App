/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

/**
 *
 * @author trinh
 */
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.*;

import javax.imageio.ImageIO;
import javax.swing.*;

public class ServerDraw {
  public static void main(String[] args) throws Exception {
    BufferedImage screencapture = new Robot().createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
    try (ServerSocket serv = new ServerSocket(25000)) {
      System.out.println("waiting...");
      try (Socket socket = serv.accept()) {
        System.out.println("client connected");
        ImageIO.write(screencapture, "jpg", socket.getOutputStream());
        System.out.println("sent");
      }
    }
  }
}