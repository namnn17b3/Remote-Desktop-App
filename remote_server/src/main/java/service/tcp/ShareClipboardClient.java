package service.tcp;

import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.FlavorEvent;
import java.awt.datatransfer.FlavorListener;
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.imageio.ImageIO;

public class ShareClipboardClient extends Thread implements FlavorListener, ClipboardOwner {

    private final static byte CAT_STRING = 1;
    private final static byte CAT_PICTURE = 2;
    private final static byte CAT_HTML = 3;
    private final static java.awt.datatransfer.Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
    private static Cipher cipherin, cipherout;
    private static Socket socket;

    public ShareClipboardClient(Socket socketReq) throws
            NoSuchAlgorithmException,
            NoSuchPaddingException,
            InvalidKeyException,
            IOException {
        SecretKeySpec key = new SecretKeySpec(("ChangeMeChangeMe").getBytes("UTF-8"), "AES");
        cipherin = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipherin.init(Cipher.DECRYPT_MODE, key);
        cipherout = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipherout.init(Cipher.ENCRYPT_MODE, key);
        // set IP and port
        socket = socketReq;
        clip.setContents(clip.getContents(null), this);
        clip.addFlavorListener(this);
        start();
    }

    @Override
    public void run() {
        try {
            // run thread raw save data
            DataInputStream in = new DataInputStream(socket.getInputStream());
            while (true) {
                byte category = in.readByte();
                int size = in.readInt();
                byte[] data = new byte[size];
                in.readFully(data);
                System.out.println("received " + data.length);
                data = cipherin.doFinal(data);
                boolean clipok = false;
                Transferable t = null;
                switch (category) {
                    case CAT_HTML:
                        ByteArrayInputStream bais = new ByteArrayInputStream(data);
                        ObjectInputStream ois = new ObjectInputStream(bais);
                        byte[] html = (byte[]) ois.readObject();
                        byte[] text = (byte[]) ois.readObject();
                        String ht = new String(html, "UTF-8");
                        String te = new String(text, "UTF-8");
                        t = new HtmlSelection(ht, te);
                        break;
                    case CAT_STRING:
                        String st = new String(data, "UTF-8");
                        t = new StringSelection(st);
                        break;
                    case CAT_PICTURE:
                        t = new ImageSelection(ImageIO.read(new ByteArrayInputStream(data)));
                        break;
                }
                while (!clipok) {
                    try {
                        clip.setContents(t, null);
                        clipok = true;
                    } catch (IllegalStateException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        } catch (IOException | ClassNotFoundException | IllegalBlockSizeException | BadPaddingException ex) {
            ex.printStackTrace();
        }
    }


    @Override
    public void flavorsChanged(FlavorEvent e) {
        Transferable t = null;
        boolean clipok = false;
        while (!clipok) {
            try {
                t = clip.getContents(null);
                clipok = true;
            } catch (IllegalStateException ex) {
            }
        }
        clip.removeFlavorListener(this);
        clipok = false;
        while (!clipok) {
            try {
                clip.setContents(t, this);
                clipok = true;
            } catch (IllegalStateException ex) {
            }
        }
        try {
            byte[] data = null;
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            if (t.isDataFlavorSupported(DataFlavor.allHtmlFlavor) && t.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                out.writeByte(CAT_HTML);
                //System.out.println(t.getTransferData(DataFlavor.stringFlavor));
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(baos);
                oos.writeObject(((String) t.getTransferData(DataFlavor.allHtmlFlavor)).getBytes("UTF-8"));
                oos.writeObject(((String) t.getTransferData(DataFlavor.stringFlavor)).getBytes("UTF-8"));
                oos.flush();
                data = baos.toByteArray();
                oos.close();
            } else if (t.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                out.writeByte(CAT_STRING);
                //System.out.println(t.getTransferData(DataFlavor.stringFlavor));
                data = ((String) t.getTransferData(DataFlavor.stringFlavor)).getBytes("UTF-8");
            } else if (t.isDataFlavorSupported(DataFlavor.imageFlavor)) {
                out.writeByte(CAT_PICTURE);
                //System.out.println(t.getTransferData(DataFlavor.imageFlavor));
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write((BufferedImage) t.getTransferData(DataFlavor.imageFlavor), "png", baos);
                baos.flush();
                data = baos.toByteArray();
                baos.close();
            }
            if (data != null) {
                data = cipherout.doFinal(data);
                out.writeInt(data.length);
                out.write(data);
                System.out.println("sent "+data.length);
            }
        } catch (UnsupportedFlavorException | IOException | IllegalBlockSizeException | BadPaddingException ex) {
            Logger.getLogger(ShareClipboardClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            Thread.sleep(10L);
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }
        clip.addFlavorListener(this);
    }

    @Override
    public void lostOwnership(java.awt.datatransfer.Clipboard arg0, Transferable arg1) {
        //System.out.println("ownership losted");
    }
}

// This class is used to hold an image while on the clipboard.
class ImageSelection implements Transferable {

    private Image image;

    public ImageSelection(Image image) {
        this.image = image;
    }

    public DataFlavor[] getTransferDataFlavors() {
        return new DataFlavor[]{DataFlavor.imageFlavor};
    }


    public boolean isDataFlavorSupported(DataFlavor flavor) {
        return DataFlavor.imageFlavor.equals(flavor);
    }


    public Object getTransferData(DataFlavor flavor)
            throws UnsupportedFlavorException, IOException {
        if (!DataFlavor.imageFlavor.equals(flavor)) {
            throw new UnsupportedFlavorException(flavor);
        }
        return image;
    }
}

// This class is used to hold HTML on the clipboard.
class HtmlSelection implements Transferable {

    private String text;
    private String html;

    public HtmlSelection(String html,String text) {
        this.html = html;
        this.text = text;
    }

    public DataFlavor[] getTransferDataFlavors() {
        return new DataFlavor[]{DataFlavor.allHtmlFlavor,DataFlavor.stringFlavor};
    }

    public boolean isDataFlavorSupported(DataFlavor flavor) {
        return DataFlavor.allHtmlFlavor.equals(flavor) || DataFlavor.stringFlavor.equals(flavor);
    }

    public Object getTransferData(DataFlavor flavor)
            throws UnsupportedFlavorException, IOException {
        if (DataFlavor.allHtmlFlavor.equals(flavor)) {
            return html;
        } else if (DataFlavor.stringFlavor.equals(flavor)) {
            return text;
        }
        throw new UnsupportedFlavorException(flavor);
    }
}

