package yee.pltision.backrooms.block.pipe;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.jetbrains.annotations.NotNull;

public interface CrossPipe extends Pipe {
    BooleanProperty EAST=BooleanProperty.create("east"),
                    WEST=BooleanProperty.create("west"),
                    NORTH=BooleanProperty.create("north"),
                    SOUTH=BooleanProperty.create("south"),
                    UP=BooleanProperty.create("up"),
                    DOWN=BooleanProperty.create("down");

    @Override
    default void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        Pipe.super.createBlockStateDefinition(builder);
        builder.add(EAST).add(WEST).add(NORTH).add(SOUTH).add(UP).add(DOWN);
    }

    @Override
    default boolean isConnected(BlockState state, Direction face){
        return state.getValue(getPropertyFromDirection(face));
    }

    static BooleanProperty getPropertyFromDirection(Direction direction){
        switch (direction){
            case EAST -> {
                return EAST;
            }
            case WEST -> {
                return WEST;
            }
            case NORTH -> {
                return NORTH;
            }
            case SOUTH -> {
                return SOUTH;
            }
            case UP -> {
                return UP;
            }
            case DOWN -> {
                return DOWN;
            }
        }
        return EAST;
    }

    @Override
    default boolean isOpen(BlockState state, Direction face){
        return false;
    }
}
