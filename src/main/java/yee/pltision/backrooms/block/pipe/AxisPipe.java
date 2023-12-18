package yee.pltision.backrooms.block.pipe;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.jetbrains.annotations.NotNull;

public interface AxisPipe extends Pipe{
    EnumProperty<Direction.Axis> AXIS= BlockStateProperties.AXIS;
    @Override
    default void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        Pipe.super.createBlockStateDefinition(builder);
        builder.add(AXIS);
    }


    @Override
    default boolean isConnected(BlockState state, Direction face){
        return state.getValue(AXIS)==face.getAxis();
    }

    @Override
    default boolean isOpen(BlockState state, Direction face){
        return state.getValue(AXIS)==face.getAxis();
    }
}
