package yee.pltision.backrooms.block.mushroom;

import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public enum MushroomType implements StringRepresentable {
    RED_MUSHROOM("red_mushroom"),
    BROWN_MUSHROOM("brown_mushroom"),
    WHITE_MUSHROOM("white_mushroom");

    MushroomType(String name){
        this.name=name;
    }

    public final String name;

    @Override
    public @NotNull String getSerializedName() {
        return name;
    }
}
