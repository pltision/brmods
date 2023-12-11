package yee.pltision.backrooms.block.concrete;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.jetbrains.annotations.NotNull;

public class CrackingConcreteBlock extends ConcreteBlock{
    public static final IntegerProperty STAGE = IntegerProperty.create("stage",1,2);

    public CrackingConcreteBlock(Properties p_49795_) {
        super(p_49795_);

    }
    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> p_54935_) {
        p_54935_.add(STAGE);
    }

    @Override
    public double getRocky(BlockState state, LevelReader level, BlockPos pos) {
        return 0.75;
    }

    public void beDry(BlockState state,@NotNull LevelReader level, BlockPos pos){}
}
