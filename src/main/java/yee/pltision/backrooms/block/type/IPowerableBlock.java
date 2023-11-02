package yee.pltision.backrooms.block.type;

import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public interface IPowerableBlock {
    BooleanProperty POWERED = BlockStateProperties.POWERED;
}
