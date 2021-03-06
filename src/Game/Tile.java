package Game;

import Data.Coordinate;
import Data.SerializationVersion;
import Engine.SpecialText;
import Game.Tags.Tag;

import java.awt.*;
import java.io.Serializable;

/**
 * Created by Jared on 4/7/2018.
 */
public class Tile extends TagHolder implements Serializable {

    /**
     * Tile:
     *
     * A simple TagHolder that contains additional information regarding its position and containing level.
     *
     * Tile Tags are handled weirdly.
     * In most cases, each Tag belongs to a singular TagHolder and is individual.
     * However, if the Tag is generated during the Level loading process, then that Tag is actually re-used again by the Level.
     *
     * That has the odd consequence of some Tags owned by Tiles to belong to multiple Tiles.
     * This has some conditional consequences, such as the way the SplashySurface Tags figure out when and where to dispense animated tiles.
     */

    private static final long serialVersionUID = SerializationVersion.SERIALIZATION_VERSION;

    private Coordinate location;
    private String name;

    private Level level;

    private int age;

    public Tile(Coordinate loc, String name, Level level){
        location = loc;
        this.name = name;
        this.level = level;
    }

    public Coordinate getLocation() {
        return location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Level getLevel() {
        return level;
    }

    public void onTurn(GameInstance gi){
        TagEvent event = new TagEvent(0, this, gi.getCurrentLevel().getSolidEntityAt(location), gi, this);
        for (Tag tag : getTags()){
            tag.onTurn(event);
        }
        event.doFutureActions();
        if (event.eventPassed()){
            event.doCancelableActions();
        }
        age++;
    }

    public Tile copy(Coordinate newLoc){
        Tile copy = new Tile(newLoc, name, level);
        copy.getTags().clear();
        copy.getTags().addAll(getTags());
        return copy;
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof Tile && ((Tile) obj).location.equals(location) && ((Tile)obj).getTagList().equals(getTagList()));
    }


    public int getAge() {
        return age;
    }

    public void updateTileTagColor(){
        int numColors = 0; //The divisor which turns this addition operation into an averaging one.
        int[] rgbsum = {0, 0, 0}; //Working sums of RGB channels
        int alphasum = 0; //Exception made for the alpha channel; it is simply added together.
        //Add up the colors
        for (Tag tag : getTags()){
            Color col = tag.getTagTileColor();
            if (col != null) {
                numColors++;
                rgbsum[0] += col.getRed();
                rgbsum[1] += col.getGreen();
                rgbsum[2] += col.getBlue();
                alphasum += col.getAlpha();
            }
        }
        //Finalize and draw
        Color finalCol = (numColors == 0) ? new Color(0,0,0,0) : new Color(rgbsum[0] / numColors, rgbsum[1] / numColors, rgbsum[2] / numColors, Math.min(alphasum, 255));
        level.getTileTagLayer().editLayer(getLocation(), new SpecialText(' ', Color.WHITE, finalCol));
    }
}
