package yee.pltision.backrooms.block.light;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.jetbrains.annotations.NotNull;

public interface RandomNoPowerLight {
    /**
     * 有电时切换到默认状态，没电时切换到熄灭状态。
     */
    BooleanProperty NO_POWER=BooleanProperty.create("no_power");

    default void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        builder.add(NO_POWER);
    }
}
