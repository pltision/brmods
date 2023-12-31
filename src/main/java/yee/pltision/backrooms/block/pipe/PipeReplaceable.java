package yee.pltision.backrooms.block.pipe;

import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public interface PipeReplaceable{
    @Nullable BlockState replaceToPipe(BlockState replace, BlockState pipe);
}
