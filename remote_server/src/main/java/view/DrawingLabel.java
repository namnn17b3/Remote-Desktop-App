/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

class DrawingLabel extends JLabel {

    private Image backgroundImage;
    private BufferedImage drawingImage;
    private Graphics2D drawingGraphics;
    private Float sizeText;
    private String color;

    private int startX, startY;

    public DrawingLabel(ImageIcon icon, Float sizeText, String color) {
        this.backgroundImage = icon.getImage();
        this.drawingImage = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
        this.drawingGraphics = drawingImage.createGraphics();
        this.sizeText = sizeText;
        this.color = color;
        this.drawingGraphics.setStroke(new BasicStroke(sizeText));
        setPreferredSize(new Dimension(icon.getIconWidth(), icon.getIconHeight()));
//        setPreferredSize(new Dimension(1000, 1000));
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                startX = e.getX();
                startY = e.getY();
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                int endX = e.getX();
                int endY = e.getY();

                if (color.equals("BLUE")) {
                    drawingGraphics.setColor(Color.BLUE);
                } else if (color.equals("BLACK")) {
                    drawingGraphics.setColor(Color.BLACK);
                } else {
                    drawingGraphics.setColor(Color.RED);
                }

                drawingGraphics.drawLine(startX, startY, endX, endY);

                startX = endX;
                startY = endY;

                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw the background image
        g.drawImage(backgroundImage, 0, 0, this);

        // Draw the drawing image on top
        g.drawImage(drawingImage, 0, 0, this);
    }

    public static void startDraw(Float sizeText, String color) {
        try {
            BufferedImage image = new Robot().createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
//                Graphics2D g2d = image.createGraphics();
//                g2d.setColor(Color.WHITE);
//                g2d.fillRect(0, 0, 100, 100);
//                g2d.setColor(Color.BLUE);
//                g2d.drawRect(10, 10, 80, 80);
//                g2d.dispose();

            JFrame frame = new JFrame("Drawing on JLabel");
            frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
            ImageIcon icon = new ImageIcon(image);
            DrawingLabel drawingLabel = new DrawingLabel(icon, sizeText, color);

            frame.getContentPane().add(drawingLabel);
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        } catch (AWTException ex) {
            Logger.getLogger(DrawingLabel.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void main(String[] args) {
        startDraw(Float.valueOf("3.0"), "RED");
    }
}
