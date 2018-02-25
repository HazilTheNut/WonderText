package Editor.ArtTools;

import Editor.EditorTextPanel;
import Engine.Layer;
import Engine.SpecialText;

/**
 * Created by Jared on 2/25/2018.
 */
public abstract class ArtTool {



    //Ran upon pressing left click down
    public void onDrawStart(Layer layer, Layer highlight, int col, int row, SpecialText text) {}

    //Ran while left click is pressed
    public void onDraw(Layer layer, Layer highlight, int col, int row, SpecialText text) {}

    //Ran upon releasing left click
    public void onDrawEnd(Layer layer, Layer highlight, int col, int row, SpecialText text) {}
}
