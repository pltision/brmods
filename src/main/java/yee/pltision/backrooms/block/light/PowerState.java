package yee.pltision.backrooms.block.light;

import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public enum PowerState implements StringRepresentable {
    NO_POWER("no_power"),FLASHING("flashing"),STEADY("steady");
    final String name;

    PowerState(String name) {
        this.name = name;
    }

    @Override
    public @NotNull String getSerializedName() {
        return name;
    }
}
