package yee.pltision.backrooms.block.pipe;

import com.google.common.collect.HashBiMap;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;
import yee.pltision.backrooms.block.moss.MossState;

public enum ConnectState implements StringRepresentable {
    NONE("none"),
    CONNECT("connect"),
    OPEN("open");
    final public String name;

    ConnectState(String name){
        this.name=name;
    }
    @Override
    public @NotNull String getSerializedName() {
        return name;
    }
}
