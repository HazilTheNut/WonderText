package Editor;

import Engine.Layer;
import Engine.LayerManager;
import Engine.SpecialText;
import Engine.ViewWindow;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Jared on 2/18/2018.
 */
public class EditorFrame extends JFrame {

    public EditorFrame(){

        setLayout(new BorderLayout());

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | UnsupportedLookAndFeelException | IllegalAccessException e) {
            e.printStackTrace();
        }

        Container c = getContentPane();

        ViewWindow window = new ViewWindow();
        c.addComponentListener(window);
        c.addKeyListener(window);

        Layer levelBackdrop = new Layer(new SpecialText[window.RESOLUTION_WIDTH][window.RESOLUTION_HEIGHT], "level_backdrop", 0, 0);
        for (int col = 0; col < levelBackdrop.getCols(); col++){
            levelBackdrop.editLayer(col, 0, new SpecialText('#', Color.RED));
            levelBackdrop.editLayer(col, levelBackdrop.getRows()-1, new SpecialText('#', Color.RED));
        }
        for (int row = 0; row < levelBackdrop.getRows(); row++){
            levelBackdrop.editLayer(0, row, new SpecialText('#', Color.RED));
            levelBackdrop.editLayer(levelBackdrop.getCols()-1, row, new SpecialText('#', Color.RED));
        }

        LevelData ldata = new LevelData(levelBackdrop);

        Layer mouseHighlight = new Layer(new SpecialText[window.RESOLUTION_WIDTH*4][window.RESOLUTION_HEIGHT*4], "mouse", 0, 0);
        mouseHighlight.fixedScreenPos = true;

        Layer tileDataLayer = ldata.getTileDataLayer();
        tileDataLayer.setVisible(false);

        LayerManager manager = new LayerManager(window);
        manager.addLayer(ldata.getBackdrop());
        manager.addLayer(ldata.getTileDataLayer());
        manager.addLayer(ldata.getEntityLayer());
        manager.addLayer(ldata.getWarpZoneLayer());
        manager.addLayer(mouseHighlight);

        c.add(window, BorderLayout.CENTER);

        EditorTextPanel textPanel = new EditorTextPanel();
        c.add(textPanel, BorderLayout.LINE_START);

        EditorMouseInput mi = new EditorMouseInput(window, manager, mouseHighlight, textPanel, ldata.getBackdrop());
        window.addMouseListener(mi);
        window.addMouseMotionListener(mi);
        window.addMouseWheelListener(mi);

        EditorToolPanel toolPanel = new EditorToolPanel(mi, manager, ldata);
        c.add(toolPanel, BorderLayout.LINE_END);

        textPanel.setToolPanel(toolPanel);

        c.validate();

        setSize(new Dimension(850, 750));

        setVisible(true);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

}
