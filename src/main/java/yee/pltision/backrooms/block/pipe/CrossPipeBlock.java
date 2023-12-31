package yee.pltision.backrooms.block.pipe;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class CrossPipeBlock extends Block implements CrossPipe {
    public CrossPipeBlock(Properties p_49795_) {
        super(p_49795_);
        registerDefaultState(defaultBlockState().setValue(NORTH,false).setValue(SOUTH,false).setValue(EAST,false).setValue(WEST,false).setValue(UP,false).setValue(DOWN,false));
    }

    @Override
    public void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        CrossPipe.super.createBlockStateDefinition(builder);
    }

    @Override
    public void tick(@NotNull BlockState p_60462_, @NotNull ServerLevel p_60463_, @NotNull BlockPos p_60464_, @NotNull Random p_60465_) {
        CrossPipe.super.tick(p_60462_, p_60463_, p_60464_, p_60465_);
    }

    @Override
    public void neighborChanged(@NotNull BlockState p_60509_, @NotNull Level p_60510_, @NotNull BlockPos p_60511_, @NotNull Block p_60512_, @NotNull BlockPos p_60513_, boolean p_60514_) {
        CrossPipe.super.neighborChanged(p_60509_, p_60510_, p_60511_, p_60512_, p_60513_, p_60514_);
    }

    @Override
    public @NotNull BlockState updateShape(@NotNull BlockState state, @NotNull Direction direction, @NotNull BlockState change, @NotNull LevelAccessor p_60544_, @NotNull BlockPos p_60545_, @NotNull BlockPos p_60546_) {
        return getState(state,p_60544_,p_60545_);
    }

    public BlockState getState(BlockState state, LevelReader level, BlockPos pos){

        for(var direction:Direction.values()){
            BlockState t=level.getBlockState(pos.relative(direction));
            if(t.getBlock()instanceof Pipe pipe&&pipe.isConnected(t,direction.getOpposite()))
                state=state.setValue(CrossPipe.getPropertyFromDirection(direction),true);
            else state=state.setValue(CrossPipe.getPropertyFromDirection(direction),false);
        }
        return state;
    }

}
