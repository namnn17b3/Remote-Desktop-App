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
class ClientDraw {
  public static void main(String[] args) throws Exception {
    try(Socket socket = new Socket("192.168.1.4", 25000)){
      BufferedImage image = ImageIO.read(socket.getInputStream());
      JLabel label = new JLabel(new ImageIcon(image));
      JFrame f = new JFrame("vnc");
      f.getContentPane().add(label);
      f.pack();
      f.setVisible(true);
    }
  }
}