package yee.pltision.backrooms.block.pipe;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.FrontAndTop;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class AxisPipeBlock extends Block implements AxisPipe {
    public static final BooleanProperty POSITIVE =BooleanProperty.create("p"), NEGATIVE =BooleanProperty.create("n");

    public AxisPipeBlock(Properties p_49795_) {
        super(p_49795_);
    }

    @Override
    public void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        AxisPipe.super.createBlockStateDefinition(builder);
        builder.add(POSITIVE).add(NEGATIVE);
    }

    public BlockState getState(BlockState state, LevelReader level, BlockPos pos){
        boolean n=false,p=false;
        BlockState t;
        switch (state.getValue(AXIS)){
            case X -> {
                t=level.getBlockState(pos.west());
                n=t.getBlock() instanceof Pipe pipe&&pipe.isConnected(t,Direction.EAST);
                t=level.getBlockState(pos.east());
                p=t.getBlock() instanceof Pipe pipe&&pipe.isConnected(t,Direction.WEST);
            }
            case Y -> {
                t=level.getBlockState(pos.below());
                n=t.getBlock() instanceof Pipe pipe&&pipe.isConnected(t,Direction.UP);
                t=level.getBlockState(pos.above());
                p=t.getBlock() instanceof Pipe pipe&&pipe.isConnected(t,Direction.DOWN);
            }
            case Z -> {
                t=level.getBlockState(pos.north());
                n=t.getBlock() instanceof Pipe pipe&&pipe.isConnected(t,Direction.NORTH);
                t=level.getBlockState(pos.south());
                p=t.getBlock() instanceof Pipe pipe&&pipe.isConnected(t,Direction.SOUTH);
            }
        }
        return state.setValue(NEGATIVE,!n).setValue(POSITIVE,!p);
    }

    @Override
    public @NotNull BlockState updateShape(@NotNull BlockState state, @NotNull Direction direction, @NotNull BlockState change, @NotNull LevelAccessor p_60544_, @NotNull BlockPos p_60545_, @NotNull BlockPos p_60546_) {
        return getState(state,p_60544_,p_60545_);
    }

    @Override
    public void tick(@NotNull BlockState p_60462_, @NotNull ServerLevel p_60463_, @NotNull BlockPos p_60464_, @NotNull Random p_60465_) {
        AxisPipe.super.tick(p_60462_, p_60463_, p_60464_, p_60465_);
    }

    @Override
    public void neighborChanged(@NotNull BlockState p_60509_, @NotNull Level p_60510_, @NotNull BlockPos p_60511_, @NotNull Block p_60512_, @NotNull BlockPos p_60513_, boolean p_60514_) {
        AxisPipe.super.neighborChanged(p_60509_, p_60510_, p_60511_, p_60512_, p_60513_, p_60514_);
    }

    public BlockState getStateForPlacement(BlockPlaceContext p_54227_) {
        return this.defaultBlockState().setValue(AXIS,p_54227_.getClickedFace().getAxis());
    }
}
