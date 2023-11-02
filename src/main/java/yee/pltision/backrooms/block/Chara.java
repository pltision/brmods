package yee.pltision.backrooms.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class Chara extends Block implements SimpleWaterloggedBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public static final VoxelShape Z_UNDER_AABB =
            Block.box(2.0D, 0.0D, 1.0D,
            14.0D, 8.0D, 15.0D);
    public static final VoxelShape X_UNDER_AABB =
            Block.box(1.0D, 0.0D, 2.0D,
                    15.0D, 8.0D, 14.0D);

    public static final VoxelShape EAST_AABB = Shapes.or(Z_UNDER_AABB,
            Block.box(2.0D, 0.0D, 1.0D,
                    4.0D, 20.0D, 15.0D));

    public static final VoxelShape SOUTH_AABB = Shapes.or(X_UNDER_AABB,
            Block.box(1.0D, 0.0D, 2.0D,
                    15.0D, 20.0D, 4.0D));
    public static final VoxelShape WEST_AABB = Shapes.or(Z_UNDER_AABB,
            Block.box(12.0D, 0.0D, 1.0D,
                    14.0D, 20.0D, 15.0D));
    public static final VoxelShape NORTH_AABB = Shapes.or(X_UNDER_AABB,
            Block.box(1.0D, 0.0D, 12.0D,
                    15.0D, 20.0D, 14.0D));


    public Chara(Properties p_49795_) {
        super(p_49795_);
        registerDefaultState(this.stateDefinition.any()
                .setValue(WATERLOGGED, false)
                .setValue(FACING, Direction.EAST)
        );
    }
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext p_153477_) {
        switch (state.getValue(FACING)){
            case SOUTH: return SOUTH_AABB;
            case WEST: return WEST_AABB;
            case NORTH: return NORTH_AABB;
            default: return EAST_AABB;
        }

    }

    @Override
    public @NotNull FluidState getFluidState(BlockState p_52362_) {
        return p_52362_.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(p_52362_);
    }
    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> p_54935_) {
        p_54935_.add(FACING);
        p_54935_.add(WATERLOGGED);
    }

    /**
        自动计算椅子的朝向状态
        @param level 获取周围的方块
        @param pos 椅子的位置
        @param r 随机选取朝向
     */
    public static BlockState getState(BlockGetter level, BlockPos pos, Random r){
        BlockState state=level.getBlockState(pos);
        if(!(state.getBlock() instanceof Chara)) return state;

        byte[]tries=new byte[4];
        BlockState tempState;
        if((tempState=level.getBlockState(pos.west())).getMaterial().isSolid()&&!(tempState.getBlock() instanceof Chara)){
            if(tempState.getBlock() instanceof Table) tries[0]=2;
            else tries[0]=1;
        }
        if((tempState=level.getBlockState(pos.north())).getMaterial().isSolid()&&!(tempState.getBlock() instanceof Chara)){
            if(tempState.getBlock() instanceof Table) tries[1]=2;
            else tries[1]=1;
        }
        if((tempState=level.getBlockState(pos.east())).getMaterial().isSolid()&&!(tempState.getBlock() instanceof Chara)){
            if(tempState.getBlock() instanceof Table) tries[2]=2;
            else tries[2]=1;
        }
        if((tempState=level.getBlockState(pos.south())).getMaterial().isSolid()&&!(tempState.getBlock() instanceof Chara)){
            if(tempState.getBlock() instanceof Table) tries[3]=2;
            else tries[3]=1;
        }

        int random= r.nextInt()&0b11;

        boolean unGotFromSolid=true;
        for(byte i=0;i<4;i++){
            switch (tries[(random+i)&0b11]){
                case 1:
                    if(unGotFromSolid){
                        state=state.setValue(FACING,getFacing(random+i));
                        unGotFromSolid=false;
                    }
                    break;
                case 2: //桌子优先
                    return state.setValue(FACING,getFacing(random+i+2));

            }
        }

        return unGotFromSolid?state.setValue(FACING,getFacing(random)):state;
    }
    public static Direction getFacing(int num){
        switch (num&0b11){
            case 0: return Direction.EAST;
            case 1: return Direction.SOUTH;
            case 2: return Direction.WEST;
            case 3: return Direction.NORTH;
        }
        throw new RuntimeException();
    }


}
