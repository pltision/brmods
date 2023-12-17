package yee.pltision.backrooms.block.moss;

import com.google.common.collect.HashBiMap;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public enum MossState implements StringRepresentable {
    DESICCATED("desiccated",-4),
    DRY3("dry3",-3),
    DRY2("dry2",-2),
    DRY1("dry1",-1),
    HEALTHY("healthy",0),
    WET1("wet1",1),
    WET2("wet2",2),
    WET3("wet3",3),
    DECAYED("decayed",4);

    final public String name;
    final public int value;
    public static final HashBiMap<Integer, MossState> STATE_MAP= HashBiMap.create();
    MossState(String name,int value){
        this.name=name;
        this.value=value;

    }
    static {
        for(MossState state: MossState.values()){
            STATE_MAP.put(state.value,state);
        }
    }
    MossState getFromValue(int value){
        return STATE_MAP.get(value);
    }

    @Override
    public @NotNull String getSerializedName() {
        return name;
    }
}
