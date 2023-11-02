package yee.pltision.backrooms.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.PipeBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.NotNull;

public class Table extends Block implements SimpleWaterloggedBlock {
    public static final BooleanProperty NORTH = PipeBlock.NORTH;
    public static final BooleanProperty EAST = PipeBlock.EAST;
    public static final BooleanProperty SOUTH = PipeBlock.SOUTH;
    public static final BooleanProperty WEST = PipeBlock.WEST;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final BooleanProperty EAST_CORNER = BooleanProperty.create("east_corner");
    public static final BooleanProperty WEST_CORNER = BooleanProperty.create("west_corner");

    public Table(Properties p_49795_) {
        super(p_49795_);
        registerDefaultState(this.stateDefinition.any()
                .setValue(NORTH, false)
                .setValue(EAST, false)
                .setValue(WEST, false)
                .setValue(SOUTH, false)
                .setValue(WATERLOGGED, false)
                .setValue(EAST_CORNER, false)
                .setValue(WEST_CORNER, false)
      );
    }
    public BlockState updateShape(BlockState p_52894_, Direction p_52895_, BlockState p_52896_, LevelAccessor p_52897_, BlockPos p_52898_, BlockPos p_52899_) {
        return getState(p_52897_,p_52898_);
    }

    @Override
    public @NotNull FluidState getFluidState(BlockState p_52362_) {
        return p_52362_.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(p_52362_);
    }
    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> p_54935_) {
        p_54935_.add(NORTH);
        p_54935_.add(EAST);
        p_54935_.add(SOUTH);
        p_54935_.add(WEST);
        p_54935_.add(WATERLOGGED);
        p_54935_.add(EAST_CORNER);
        p_54935_.add(WEST_CORNER);
    }

    public static BlockState getState(BlockGetter level, BlockPos pos){
        BlockState state=level.getBlockState(pos);
        if(!(state.getBlock() instanceof Table)) return state;

        boolean east= !(level.getBlockState(pos.east()).getBlock() instanceof Table);
        boolean west= !(level.getBlockState(pos.west()).getBlock() instanceof Table);
        boolean south=!(level.getBlockState(pos.south()).getBlock() instanceof Table);
        boolean north=!(level.getBlockState(pos.north()).getBlock() instanceof Table);

        state=state.setValue(EAST,east).setValue(WEST,west).setValue(SOUTH,south).setValue(NORTH,north).setValue(EAST_CORNER,false).setValue(WEST_CORNER,false);

        if(south&&!north){
            if(!(east&&west)){
                if(!west&&!(level.getBlockState(pos.north().west()).getBlock() instanceof Table))
                    state=state.setValue(EAST_CORNER,true);
                if(!east&&!(level.getBlockState(pos.north().east()).getBlock() instanceof Table))
                    state=state.setValue(WEST_CORNER,true);
            }
        }
        else if(north&&!south){
            if(!(east&&west)){
                if(!east&&!(level.getBlockState(pos.south().east()).getBlock() instanceof Table))
                    state=state.setValue(WEST_CORNER,true);
                if(!west&&!(level.getBlockState(pos.south().west()).getBlock() instanceof Table))
                    state=state.setValue(EAST_CORNER,true);

            }
        }

        return state;
    }
}
