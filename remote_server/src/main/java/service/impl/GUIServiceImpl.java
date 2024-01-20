package service.impl;

import service.GUIService;
import view.Ultraview;

public class GUIServiceImpl implements GUIService {

    private Ultraview ultraview;

    public GUIServiceImpl(){
        this.ultraview = new Ultraview();
    }

    @Override
    public void initGui() {
          this.ultraview.initGuiRemoteDesktop();
    }
}
