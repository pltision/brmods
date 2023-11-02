package yee.pltision.mushroom;

import org.jetbrains.annotations.Nullable;
import yee.pltision.backrooms.BrMushrooms;

import java.io.Serializable;
import java.util.ArrayList;

public class MushroomData {
    public static final int TYPE_LENGTH=6;
    public static final @Nullable MushroomGetter [] MUSHROOM_TYPES =new MushroomGetter[1<<TYPE_LENGTH];

    public static final ArrayList<Integer> DEFINED_TYPES=new ArrayList<>(2);

    static {
        DEFINED_TYPES.add(BrMushrooms.BROWN_MUSHROOM_HEAD);
        DEFINED_TYPES.add(BrMushrooms.RED_MUSHROOM_HEAD);

    }
}
