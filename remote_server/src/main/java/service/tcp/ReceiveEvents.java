package service.tcp;

import utils.Commands;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.Key;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
/* Used to recieve server commands then execute them at the client side*/

public class ReceiveEvents extends Thread {
    Socket socket = null;
    Robot robot = null;
    boolean continueLoop = true;

    private Character[] vnCharacters = {
            'Á', 'á', 'À', 'à', 'Ả', 'ả', 'Ã', 'ã', 'Ạ', 'ạ',
            'Ă', 'ă', 'Ắ', 'ắ', 'Ằ', 'ằ', 'Ẳ', 'ẳ', 'Ẵ', 'ẵ', 'Ặ', 'ặ',
            'Â', 'â', 'Ấ', 'ấ', 'Ầ', 'ầ', 'Ẩ', 'ẩ', 'Ẫ', 'ẫ', 'Ậ', 'ậ',
            'Đ', 'đ',
            'É', 'é', 'È', 'è', 'Ẻ', 'ẻ', 'Ẽ', 'ẽ', 'Ẹ', 'ẹ',
            'Ê', 'ê', 'Ế', 'ế', 'Ề', 'ề', 'Ể', 'ể', 'Ễ', 'ễ', 'Ệ', 'ệ',
            'Í', 'í', 'Ì', 'ì', 'Ỉ', 'ỉ', 'Ĩ', 'ĩ', 'Ị', 'ị',
            'Ó', 'ó', 'Ò', 'ò', 'Ỏ', 'ỏ', 'Õ', 'õ', 'Ọ', 'ọ',
            'Ô', 'ô', 'Ố', 'ố', 'Ồ', 'ồ', 'Ổ', 'ổ', 'Ỗ', 'ỗ', 'Ộ', 'ộ',
            'Ơ', 'ơ', 'Ớ', 'ớ', 'Ờ', 'ờ', 'Ở', 'ở', 'Ỡ', 'ỡ', 'Ợ', 'ợ',
            'Ú', 'ú', 'Ù', 'ù', 'Ủ', 'ủ', 'Ũ', 'ũ', 'Ụ', 'ụ',
            'Ư', 'ư', 'Ứ', 'ứ', 'Ừ', 'ừ', 'Ử', 'ử', 'Ữ', 'ữ', 'Ự', 'ự',
            'Ý', 'ý', 'Ỳ', 'ỳ', 'Ỷ', 'ỷ', 'Ỹ', 'ỹ', 'Ỵ', 'ỵ'
    };

    private String[] unikeySyntaxs = {
            "ACS", "AS", "ACFC", "AF", "ACRC", "AR", "ACXC", "AX", "ACJC", "AJ",
            "ACWC", "AW", "ACWSC", "AWS", "ACWFC", "AWF", "ACWRC", "AWR", "ACWXC", "AWX", "ACWJC", "AWJ",
            "ACAC", "AA", "ACASC", "AAS", "ACAFC", "AAF", "ACARC", "AAR", "ACAXC", "AAX", "ACAJC", "AAJ",
            "DCDC", "DD",
            "ECSC", "ES", "ECFC", "EF", "ECRC", "ER", "ECXC", "EX", "ECJC", "EJ",
            "ECEC", "EE", "ECESC", "EES", "ECEFC", "EEF", "ECERC", "EER", "ECEXC", "EEX", "ECEJC", "EEJ",
            "ICSC", "IS", "ICFC", "IF", "ICRC", "IR", "ICXC", "IX", "ICJC", "IJ",
            "OCSC", "OS", "OCFC", "OF", "OCRC", "OR", "OCXC", "OX", "OCJC", "OJ",
            "OCOC", "OO", "OCOSC", "OOS", "OCOFC", "OOF", "OCORC", "OOR", "OCOXC", "OOX", "OCOJC", "OOJ",
            "OCWC", "OW", "OCWSC", "OWS", "OCWFC", "OWF", "OCWRC", "OWR", "OCWXC", "OWX", "OCWJC", "OWJ",
            "UCSC", "US", "UCFC", "UF", "UCRC", "UR", "UCXC", "UX", "UCJC", "UJ",
            "UCWC", "UW", "UCWSC", "UWS", "UCWFC", "UWF", "UCWRC", "UWR", "UCWXC", "UWX", "UCWJC", "UWJ",
            "YCSC", "YS", "YCFC", "YF", "YCRC", "YR", "YCXC", "YX", "YCJC", "YJ"
    };

    public ReceiveEvents(Socket socket, Robot robot) {
        this.socket = socket;
        this.robot = robot;
        start(); // Start the thread and hence calling run method
    }

    public void handleKeyEvent(Robot robot, int keyCode, int keyChar, int command) {
        System.out.println(keyCode + " " + keyChar + " " + command);
        int key = keyChar;
        // keyChar là phím chức năng (khác kí tự và số)
        // keyChar => vietnamese character
        // keyCode: 0, keyChar: a | A
        if (keyCode == 0 && keyChar >= 'a' && keyChar <= 'z') {
            key = keyChar - 32;
        }
        else if (keyCode == 0 && keyChar >= 'A' && keyChar <= 'Z') {
            key = keyChar;
        }
        // tổ hợp phím: ví dụ Alt + A => keyCode: A, keyChar: 1
        else if (keyCode >= 'A' && keyCode <= 'Z') {
            key = keyCode;
        }
        // phím chức năng Ctrl => keyCode: 17(Ctrl), keyChar: 65535
        else if (keyChar == 65535) {
            key = keyCode;
        }
        // Tiếng Việt keyCode: 0, keyChar: â
        int index = Arrays.asList(vnCharacters).indexOf((char) key);
        if (index >= 0 && command == Commands.PRESS_KEY.getAbbrev()) {
            for (int i = 0; i < unikeySyntaxs[index].length(); i++) {
                if ((int) unikeySyntaxs[index].charAt(i) == 'C') {
                    continue;
                }
                robot.keyPress((int) unikeySyntaxs[index].charAt(i));
                robot.keyRelease((int) unikeySyntaxs[index].charAt(i));
            }
            return;
        }
        // Các phím chức năng, kí tự chữ thường, kí tự số => cho phép release phím
        if (command == Commands.PRESS_KEY.getAbbrev()) {
            try {
                robot.keyPress(key);
            } catch (IllegalArgumentException iaex) {
                System.out.println("Key release: Invalid key code " + key);
                robot.keyPress(keyCode);
            }
        }
        else {
            try {
                robot.keyRelease(key);
            } catch (IllegalArgumentException iaex) {
                System.out.println("Key release: Invalid key code " + key);
            }
        }
    }

    public void run() {
        Scanner scanner = null;
        try {
            scanner = new Scanner(socket.getInputStream());
            while (continueLoop) {
                //receive commands and respond accordingly
                int command = scanner.nextInt();
                switch (command) {
                    case -1:
                        robot.mousePress(scanner.nextInt());
                        break;
                    case -2:
                        robot.mouseRelease(scanner.nextInt());
                        break;
                    case -3:
                        handleKeyEvent(robot, scanner.nextInt(), scanner.nextInt(), -3);
                        // robot.keyPress(scanner.nextInt());
                        break;
                    case -4:
                        handleKeyEvent(robot, scanner.nextInt(), scanner.nextInt(), -4);
                        // robot.keyRelease(scanner.nextInt());
                        break;
                    case -5:
                        robot.mouseMove(scanner.nextInt(), scanner.nextInt());
                        break;
                    case -6:
                        robot.mouseWheel(scanner.nextInt());
                        break;
                    case -7:
                        robot.mouseMove(scanner.nextInt(), scanner.nextInt());
                    default:
                        System.out.println("not allow method");

                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
