package yee.pltision.backrooms.block.moss;

import net.minecraft.world.level.block.state.BlockState;

public interface MossableBlock {

    BlockState getMossed(BlockState old);
}
