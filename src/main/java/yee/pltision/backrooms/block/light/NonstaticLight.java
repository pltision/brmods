package yee.pltision.backrooms.block.light;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.jetbrains.annotations.NotNull;

import java.util.function.ToIntFunction;

/**
 * This interface is for Block.
 */
public interface NonstaticLight {
    BooleanProperty STEADY=BooleanProperty.create("steady");
    BooleanProperty DEFAULT_STATE=BooleanProperty.create("default");
    BooleanProperty LIGHTING=BooleanProperty.create("lighting");
//    EnumProperty<PowerState> POWER_STATE=EnumProperty.create("power",PowerState.class);

    default void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        builder.add(STEADY);
        builder.add(DEFAULT_STATE);
        builder.add(LIGHTING);
//        builder.add(POWER_STATE);
    }

    static ToIntFunction<BlockState> getLightGetter(final int whenLighting){
        return state->state.getValue(LIGHTING)?whenLighting:0;
    }
    static ToIntFunction<BlockState> maxLightGetter(){
        return state->state.getValue(LIGHTING)?15:0;
    }
}
