package yee.pltision.backrooms.block.lootblock;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class StackableShelfBlock extends ShelfBlock{

    public static final IntegerProperty STACK=IntegerProperty.create("stack",1,4);
    public StackableShelfBlock(Properties p_49795_, Supplier<BlockState> empty) {
        super(p_49795_, empty);
        registerDefaultState(this.stateDefinition.any()
                .setValue(STACK, 1)
        );
    }
    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> p_54935_) {
        p_54935_.add(STACK);
    }
}
