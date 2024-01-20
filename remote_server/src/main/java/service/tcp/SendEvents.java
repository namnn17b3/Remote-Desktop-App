package service.tcp;

import utils.Commands;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.*;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;


public class SendEvents implements KeyListener, MouseMotionListener, MouseListener, MouseWheelListener {
	private Socket cSocket = null;
	private JPanel cPanel = null;
	private PrintWriter writer = null;
	String width = "", height = "";
	double w;
	double h;

	private static int mouseButtonMask = InputEvent.BUTTON1_MASK;
	public SendEvents(Socket s, JPanel p, String width, String height){
		cSocket = s;
		cPanel = p;
		this.width = width;
		this.height = height;
		w = Double.parseDouble(width.trim());
		h = Double.parseDouble(height.trim());

		//Associate event listeners to the panel

		cPanel.addKeyListener(this);
		cPanel.addMouseMotionListener(this);
		cPanel.addMouseListener(this);
		cPanel.addMouseWheelListener(this);


		try{
			//Prepare PrintWriter which will be used to send commands to the client
			writer = new PrintWriter(new OutputStreamWriter(
					cSocket.getOutputStream(), StandardCharsets.UTF_8), true);
			// Khởi tạo một OutputStreamWriter với mã hóa UTF-8asd
//			OutputStreamWriter osw = new OutputStreamWriter(cSocket.getOutputStream(), StandardCharsets.UTF_8);
//			writer = new PrintWriter(osw);
		} catch(IOException ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void mouseDragged(MouseEvent e){
		double xScale = (double)w/cPanel.getWidth();
		double yScale = (double)h/cPanel.getHeight();
		writer.println(Commands.MOVE_MOUSE.getAbbrev());
		writer.println((int)(e.getX()*xScale));
		writer.println((int)(e.getY()*yScale));
//		System.out.println("xScale = "+  xScale +", yScale = " + yScale);
		writer.flush();
	}

	@Override
	public void mouseMoved(MouseEvent e){
		double xScale = (double)w/cPanel.getWidth();
		double yScale = (double)h/cPanel.getHeight();
		writer.println(Commands.MOVE_MOUSE.getAbbrev());
		writer.println((int)(e.getX()*xScale));
		writer.println((int)(e.getY()*yScale));
//		System.out.println("xScale = "+  xScale +", yScale = " + yScale);
		writer.flush();
	}

	@Override
	public void mouseClicked(MouseEvent e){
	}

	@Override
	public void mousePressed(MouseEvent e){
		writer.println(Commands.PRESS_MOUSE.getAbbrev());
		int button = e.getButton();
		if (button == 1) {
			SendEvents.mouseButtonMask = InputEvent.BUTTON1_MASK;
		}
		else if (button == 2) {
			SendEvents.mouseButtonMask = InputEvent.BUTTON2_MASK;
		}
		else if(button == 3) {
			SendEvents.mouseButtonMask = InputEvent.BUTTON3_MASK;
		}
		writer.println(SendEvents.mouseButtonMask);
		writer.flush();
	}

	@Override
	public void mouseReleased(MouseEvent e){
		System.out.println("call mouse mount");
		writer.println(Commands.RELEASE_MOUSE.getAbbrev());
		int button = e.getButton();
		if (button == 1) {
			SendEvents.mouseButtonMask = InputEvent.BUTTON1_MASK;
		}
		else if (button == 2) {
			SendEvents.mouseButtonMask = InputEvent.BUTTON2_MASK;
		}
		else if(button == 3) {
			SendEvents.mouseButtonMask = InputEvent.BUTTON3_MASK;
		}
		writer.println(SendEvents.mouseButtonMask);
		writer.flush();
	}

	@Override
	public void mouseEntered(MouseEvent e){
	}

	public void mouseExited(MouseEvent e){
	}

	public void keyTyped(KeyEvent e){

	}

	public void keyPressed(KeyEvent e){
//		// Kiểm tra xem Ctrl+V đã được nhấn (Ctrl = KeyEvent.CTRL_DOWN_MASK)
//		if (e.getKeyCode() == KeyEvent.VK_V && (e.getModifiersEx() & KeyEvent.CTRL_DOWN_MASK) != 0) {
//			// Lấy dữ liệu từ clipboard
//			String clipboardData = getClipboardData();
//
//			// Gửi dữ liệu từ clipboard qua Socket
////			if (clipboardData != null && !clipboardData.isEmpty()) {
////				writer.println(Commands.SEND_CLIPBOARD_DATA.getAbbrev());
////				writer.println(clipboardData);
////				writer.flush();
////			}
//			System.out.println("data clipboard =  "+ clipboardData);
//		}
		int keyCode = e.getKeyCode();
		System.out.println("Key Pressed: " + KeyEvent.getKeyText(keyCode) + "," + keyCode + "." + e.getKeyChar() + "_" + (int)e.getKeyChar());
		writer.println(Commands.PRESS_KEY.getAbbrev());
		writer.println(e.getKeyCode());
		writer.println((int) e.getKeyChar());
		writer.flush();
	}

//	public void keyReleased(KeyEvent e){
////		System.out.println("rời phím " + e.getKeyCode());
//		/*if(e.getKeyCode() != 0){
////			CustomKeyListener.sendKeyEvent(writer, CustomKeyListener.keyReleaseds, Commands.RELEASE_KEY.getAbbrev());
//			writer.println(Commands.RELEASE_KEY.getAbbrev());
//			writer.println(e.getKeyCode());
//			writer.println(e.getKeyChar());
//			writer.flush();
//		}*/
//	}

	public void keyReleased(KeyEvent e){
//		System.out.println("rời phím " + e.getKeyCode());
		int keyCode = e.getKeyCode();
		System.out.println("Key Released: " + KeyEvent.getKeyText(keyCode) + "," + keyCode + "." + e.getKeyChar() + "_" + (int)e.getKeyChar());
		writer.println(Commands.RELEASE_KEY.getAbbrev());
		writer.println(e.getKeyCode());
		writer.println((int) e.getKeyChar());
		writer.flush();
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		System.out.println("call method scroll mouse");
		// thưc hiện thêm code xử lý lăn chuột
		int rotation = e.getWheelRotation(); // Lấy số lượng "kliks" (lên hoặc xuống)
		// Thực hiện xử lý dựa trên hướng lăn chuột (lên hoặc xuống)
		if (rotation < 0) {
			// Lăn lên
			System.out.println("lăn chuột lên");

		} else {
			// Lăn xuống
			System.out.println("Lăn chuột xuống");
		}
		// Gửi dữ liệu tới máy chủ (nếu cần)
		// Ví dụ:
		writer.println(Commands.SCROLL_MOUSE.getAbbrev());
		writer.println(rotation); // Gửi số lượng kliks lăn chuột
		writer.flush();
	}

	public static String getClipboardData() {
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		Transferable transferable = clipboard.getContents(null);

		if (transferable.isDataFlavorSupported(DataFlavor.stringFlavor)) {
			try {
				return (String) transferable.getTransferData(DataFlavor.stringFlavor);
			} catch (UnsupportedFlavorException | IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
