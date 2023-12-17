package yee.pltision.backrooms.block.pipe;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

@Deprecated
public class ComplexPipeBlock extends Block implements ComplexPipe {
    public ComplexPipeBlock(Properties p_49795_) {
        super(p_49795_);
    }

    @Override
    public void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        ComplexPipe.super.createBlockStateDefinition(builder);
    }

    @Override
    public void tick(@NotNull BlockState p_60462_, @NotNull ServerLevel p_60463_, @NotNull BlockPos p_60464_, @NotNull Random p_60465_) {
        ComplexPipe.super.tick(p_60462_,p_60463_,p_60464_,p_60465_);
    }

    @Override
    public @NotNull BlockState updateShape(@NotNull BlockState p_60541_, @NotNull  Direction p_60542_, @NotNull  BlockState p_60543_, @NotNull  LevelAccessor p_60544_, @NotNull  BlockPos p_60545_, @NotNull  BlockPos p_60546_) {
        return ComplexPipe.super.updateShape(p_60541_, p_60542_, p_60543_, p_60544_, p_60545_, p_60546_);
    }

    @Override
    public void neighborChanged(@NotNull BlockState p_60509_, @NotNull Level p_60510_, @NotNull BlockPos p_60511_,@NotNull  Block p_60512_,@NotNull  BlockPos p_60513_, boolean p_60514_) {
        ComplexPipe.super.neighborChanged(p_60509_, p_60510_, p_60511_, p_60512_, p_60513_, p_60514_);
    }



    //    @Override
//    public FluidState getFluidState(BlockState p_60577_) {
//        return Pipe.super.getFluidState(p_60577_);
//    }
}
