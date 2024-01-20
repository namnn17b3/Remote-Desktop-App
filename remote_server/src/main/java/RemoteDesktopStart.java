import service.GUIService;
import service.impl.GUIServiceImpl;

public class RemoteDesktopStart {
    public static void main(String[] args) {
        GUIService guiService = new GUIServiceImpl();
        guiService.initGui();
    }
}
