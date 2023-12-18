package yee.pltision.backrooms.block.pipe;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.AirBlock;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.NotNull;
import yee.pltision.backrooms.block.BrBlocks;

public enum PipeLiquidState implements StringRepresentable {
    NONE("none", Fluids.EMPTY, (check, world, pos) -> 0, (level, from, state, world, pos) -> {}),
    WATER("water", Fluids.FLOWING_WATER,
            (state, level, pos) -> {
                //BlockState state=level.getBlockState(pos);
                if(state.getBlock() instanceof LiquidBlock liquid){
                    //if(liquid.getFluid().isSame(Fluids.WATER)){
                        int l=state.getValue(LiquidBlock.LEVEL);
                        if(l==0) l=9;
                        return l-1;
                    //}
                }
                return 0;
            },
            (level,from, pipe, world, pos) -> {
                BlockState state=world.getBlockState(pos);
                if(level>0){
                    if(state.getBlock()instanceof AirBlock&&!(state.getBlock()instanceof FakeLiquidBlock)){
                        world.setBlock(pos,
                                BrBlocks.Pipes.FAKE_LIQUID.get().defaultBlockState().setValue(LiquidBlock.LEVEL,level).setValue(Pipe.LIQUID,water())
                                ,3);
                    }
                }
            });

    final public String name;
    final public Fluid fluid;
    final public SourceFunction sourceFunction;
    final public PlaceFunction placeFunction;

    static PipeLiquidState water(){
        return WATER;
    }

    PipeLiquidState(String name, Fluid fluid, SourceFunction sourceFunction, PlaceFunction placeFunction){
        this.name=name;
        this.fluid=fluid;
        this.sourceFunction = sourceFunction;
        this.placeFunction = placeFunction;
    }

    @FunctionalInterface
    public interface PlaceFunction{
        void place(int level, Direction from, BlockState state, Level world, BlockPos pos);
    }

    @FunctionalInterface
    public interface SourceFunction{
        /**
         * @return 如果是源头，返回源头的等级，0或更低则不是源头
         */
        int sourceLevel(BlockState check, LevelReader world, BlockPos pos);
    }


    @Override
    public @NotNull String getSerializedName() {
        return name;
    }
}
