package yee.pltision.mushroom.visual;

import yee.pltision.Util;
import yee.pltision.mushroom.visual.bar.Bar;
import yee.pltision.mushroom.visual.bar.BarBlock;
import yee.pltision.mushroom.data.EntityMushroomData;

import java.awt.*;
import java.util.HashMap;
import java.util.stream.Stream;

public class EffectBar implements Bar {
    public static final HashMap<Long, Color> COLOR_MAP=new HashMap<>();

    final public EntityMushroomData player;

    public EffectBar(EntityMushroomData player) {
        this.player = player;
    }

    public static Color getColor(long type){
        Color color=COLOR_MAP.get( type);
        if(color==null) COLOR_MAP.put(type,color=new Color((int) (Util.RANDOM.nextInt()*0.75f)));
        return color;
    }

    @Override
    public Stream<? extends BarBlock> getBlocks() {
        return player.effects.values().stream();
    }
}
