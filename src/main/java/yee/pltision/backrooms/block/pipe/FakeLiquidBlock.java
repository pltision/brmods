package yee.pltision.backrooms.block.pipe;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FakeLiquidBlock extends AirBlock implements EntityBlock {


    public FakeLiquidBlock( Properties p_54695_) {
        super( p_54695_);
        registerDefaultState(defaultBlockState().setValue(BLOCKING,false));
    }


    public @NotNull ItemStack pickupBlock(@NotNull LevelAccessor p_153772_, @NotNull BlockPos p_153773_, @NotNull BlockState p_153774_) {
        return ItemStack.EMPTY;
    }

    public static final int BLOCKING_PAUSE=15;
    public static final BooleanProperty BLOCKING= BooleanProperty.create("blocking");

    @Override
    public void neighborChanged(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull  Block p_60512_, @NotNull  BlockPos p_60513_, boolean p_60514_) {
        check(state,level,pos);
    }

//    @Override
//    public void tick(@NotNull BlockState p_60462_, @NotNull ServerLevel level, @NotNull BlockPos p_60464_, @NotNull Random p_60465_) {
//        check(p_60462_,level,p_60464_);
//
//    }


    public void check(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos) {
        boolean doClear=true,noChange=true;
        boolean blocking=state.getValue(BLOCKING);
        FakeLiquidBlockEntity entity= (FakeLiquidBlockEntity) level.getBlockEntity(pos);
        assert entity != null;

        int it=0;
        for(Direction direction:Direction.values()){
            BlockState t=level.getBlockState(pos.relative(direction));
            if(direction!=Direction.UP){
                if(blocking){
                    int thisLevel;
                    if(t.getBlock() instanceof LiquidBlock&&!(t.getBlock() instanceof FakeLiquidBlock)){
                        thisLevel=t.getValue(LiquidBlock.LEVEL);
                    }
                    else{
                        thisLevel=-1;
                    }
                    if(entity.neighbors[it]!=thisLevel){
                        entity.neighbors[it]=thisLevel;
                        noChange=false;
                        System.out.println(thisLevel);
                    }
                    it++;
                }
                else if(t.getBlock() instanceof AirBlock&&!(t.getBlock() instanceof FakeLiquidBlock)) {
                    if(state.getValue(LiquidBlock.LEVEL)>1)
                        level.setBlockAndUpdate(pos.relative(direction), Blocks.WATER.defaultBlockState().setValue(LiquidBlock.LEVEL,state.getValue(LiquidBlock.LEVEL)));
                }
            }

            if (t.getBlock() instanceof ComplexPipe &&
                    t.getValue(ComplexPipe.getPropertyFromDirection(direction.getOpposite())) == ConnectState.OPEN &&
                    t.getValue(ComplexPipe.LIQUID) == state.getValue(ComplexPipe.LIQUID)) {
//                System.out.println(""+t+pos.relative(direction)+direction);
                doClear=false;
            }
//            if(t.getBlock()instanceof LiquidBlock liquid){
////                System.out.println(""+t+pos.relative(direction)+direction);
//                noChange=false;
//            }
        }
        if(blocking) {
            //level.setBlock(pos,state.setValue(BLOCKING,true),3);
            if (noChange) {
                if (entity.blocking<1) {
                    level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
                }
                else {
                    entity.blocking--;
                }
            }
            else{
                //level.setBlock(pos,state.setValue(BLOCKING,true),3);
                entity.blocking=BLOCKING_PAUSE;
            }
        }
        else {
            if(doClear) {
                entity.blocking=BLOCKING_PAUSE;
                level.setBlockAndUpdate(pos,state.setValue(BLOCKING,true));
            }
        }
    }


    @Override
    protected void createBlockStateDefinition(@NotNull StateDefinition.Builder<Block, BlockState> p_49915_) {
        super.createBlockStateDefinition(p_49915_);
        p_49915_.add(ComplexPipe.LIQUID);
        p_49915_.add(BLOCKING);
        p_49915_.add(LiquidBlock.LEVEL);
    }

    @Override
    public @NotNull FluidState getFluidState(@NotNull BlockState state) {
        if(state.getValue(BLOCKING)){
            Fluid fluid=state.getValue(ComplexPipe.LIQUID).fluid;
            if(fluid.defaultFluidState().hasProperty(FlowingFluid.LEVEL))return fluid.defaultFluidState().setValue(FlowingFluid.LEVEL,1);
        }
        else {
            Fluid fluid=state.getValue(ComplexPipe.LIQUID).fluid;
            int level=state.getValue(LiquidBlock.LEVEL);
            if(level<1) level=1;
            else if(level>8)level=8;
            if(fluid.defaultFluidState().hasProperty(FlowingFluid.LEVEL))return fluid.defaultFluidState().setValue(FlowingFluid.LEVEL,level);
            else return fluid.defaultFluidState();
        }

        return Fluids.EMPTY.defaultFluidState();
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos p_153215_,@NotNull  BlockState p_153216_) {
        return new FakeLiquidBlockEntity(p_153215_,p_153216_);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level p_153212_,@NotNull  BlockState p_153213_,@NotNull  BlockEntityType<T> p_153214_) {
        return (p_155253_, p_155254_, p_155255_, p_155256_) -> check(p_153213_,p_153212_,p_155254_);
    }

    @Override
    public boolean canBeReplaced(@NotNull BlockState p_60535_, @NotNull Fluid p_60536_) {
        return true;
    }

}
