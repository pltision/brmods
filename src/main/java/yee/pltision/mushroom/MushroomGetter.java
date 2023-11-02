package yee.pltision.mushroom;

import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public interface MushroomGetter {
    @Nullable BlockState getMycelium(BlockState replace);
    @Nullable BlockState getMushroom();
}
