package Game.LevelScripts;

import Data.Coordinate;
import Data.SerializationVersion;
import Game.DialogueParser;
import Game.Entities.Entity;
import Game.Entities.GameCharacter;
import Game.Item;
import Game.Registries.ItemRegistry;

import java.util.ArrayList;

public class CinemaRockyHighlands extends CinematicLevelScript {

    private static final long serialVersionUID = SerializationVersion.SERIALIZATION_VERSION;

    @Override
    public String[] getMaskNames() {
        return new String[]{"Conversation"};
    }

    private boolean conversationHappened = false;
    private boolean waitingForPolarBearCrown = false;
    private boolean playerPreviouslyInZone = false; //This prevents spammed dialogue messages and whatnot.

    @Override
    public void onTurnEnd() {
        if (getMaskDataAt("Conversation", gi.getPlayer().getLocation())) {
            if (playerPreviouslyInZone) return;
            if (!conversationHappened) {
                conversationHappened = true;
                String conversation = "#0#\"Hey you!<p1> Get off the bridge!<np>This river marks our territory, and scrawny kids like you aren't allowed!\"!speakername|Cranston!\"Wait, don't we own the whole mountain range?\"!speakername|Robb!\"Shut up! And you too, kid. Take one step further and we'll kill you!\"{Hey, didn't mean to bother you. I'll go somewhere else.=1|Not if I kill you first!=2|But what if I want to join you?=3}#1#\"That's more like it. Now scram!\"<4>#2#!trigger|angerbandits!<4>#3#\"We don't need wimps like you.\"!speakername|Cranston!\"Hey Robb, may I speak with you for <ss>just<sn> a moment?\"!trigger|bandithuddle!#4#";
                //Find Robb
                ArrayList<Entity> entities = getEntitiesOfName("Robb");
                if (entities.size() > 0 && getFirstEntityofName("Robb") != null && getFirstEntityofName("Cranston") != null) {
                    DialogueParser parser = new DialogueParser(gi, conversation);
                    parser.startParser((GameCharacter) entities.get(0));
                }
            }
            if (waitingForPolarBearCrown && !gi.getPlayer().getFactionAlignments().contains("bandit")){
                String conversation = "#0#[ifitem|2501x1?2:1]#1#{Wait, could you tell me again where the crown is?=5|I can't find it. Do you take bribes?=6}#2#{I have the crown; now let me be a Bandit.=10|I have the crown, but it's too pretty to just give away...=20}#5#!speakername|Cranston!\"<ss>*Sigh*<sn> the crown you're looking for is at the <cb>northern peak<cw> with all those polar bears.<np>Head through the abandoned city, turn right at the fork in the road and head up the mountain.<np>Man, it's like you don't even know this place...\";#6#!speakername|Robb!\"I like your thinking. <cy>$200<cw> is the minimum rate.\"<7>#7#$3000x200||3|25|0|8$#8#\"If you can't make the cut, get lost!\";#10#[ifevent|banditsDiscussCrown?12:11]#11#!speakername|Robb!\"No way! Kid, you may look like a shrimp, but you got talent. Welcome aboar-\"!speakername|Cranston!\"<cs>You can't be serious, Robb. This wasn't supposed to happen!\"!speakername|Robb!\"<cs>What are we gonna do? Tell him 'no?' Not even we can defeat them...\"!speakername|Cranston!\"I apologize for the delay. Before we can continue, we'll need you to hand over the crown.\"!record|banditsDiscussCrown!<13>#12#!speakername|Cranston!\"As stated before, you gotta hand it over or there is no deal.\"<13>#13#$2501x1||3|26|0|15$#15#!speakername|Robb!\"Membership ain't free. No crown, no deal.\";#20#!speakername|Cranston!\"The price of that crown is about <cy>$200<cw>. If you've got the cash, we'll take it as an alternative.\"<7>#25#!speakername|Cranston!\"I don't know where you found all of this money, but that's a good skill to have as a Bandit. You'll fit right in.\"<27>#26#!speakername|Robb!\"That crown is even better up close... Kid, you're hired!\"<27>#27#!trigger|joinbandits!;";
                if (getFirstEntityofName("Robb") != null && getFirstEntityofName("Cranston") != null) {
                    DialogueParser parser = new DialogueParser(gi, conversation);
                    parser.startParser((GameCharacter) getFirstEntityofName("Robb"));
                }
            }
            playerPreviouslyInZone = true;
        } else
            playerPreviouslyInZone = false;
    }

    private boolean playerHasCrown(){
        for (Item item : gi.getPlayer().getItems())
            if (item.getItemData().getItemId() == ItemRegistry.ID_POLAR_BEAR_CROWN)
                return true;
        return false;
    }

    @Override
    public void onTrigger(String phrase) {
        GameCharacter robb = (GameCharacter)getFirstEntityofName("Robb");
        GameCharacter cranston = (GameCharacter)getFirstEntityofName("Cranston");
        if (phrase.equals("angerbandits")){
            gi.getFactionManager().getFaction("bandit").addOpinion("player", -10);
            robb.setTarget(gi.getPlayer());
            cranston.setTarget(gi.getPlayer());
        } else if (phrase.equals("bandithuddle")){
            ArrayList<Entity> duo = new ArrayList<>();
            duo.add(robb); duo.add(cranston);
            pathfindCombatEntities(duo, new Coordinate(283, 48), 5, 125);
            DialogueParser parser = new DialogueParser(gi, "#0#\"<cs>Do those clothes remind you of anything?\"!speakername|Robb!\"<cs>Yeah, kinda...\"!speakername|Cranston!\"<cs>I gotta plan, see? I know how to chase'm away.\"!trigger|banditresolution!");
            parser.startParser(cranston);
        } else if (phrase.equals("banditresolution")){
            ArrayList<Entity> duo = new ArrayList<>();
            duo.add(robb); duo.add(cranston);
            pathfindCombatEntities(duo, new Coordinate(276, 46), 5, 125);
            DialogueParser parser = new DialogueParser(gi, "#0#\"We'll cut you deal:<nl>There are these polar bears up on the <cb>northern peak<cw> that we don't take a liking to.<np>Kill their king and return with the crown.<np>If a shrimp like you can do that, you're tough enough to be a Bandit.<p1><nl>Until then, you're as good as dead to us!\"");
            parser.startParser(cranston);
            waitingForPolarBearCrown = true;
        } else if (phrase.equals("joinbandits")){
            gi.getPlayer().getFactionAlignments().add("bandit");
        }
    }
}
