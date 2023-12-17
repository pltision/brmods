package yee.pltision.backrooms.block.pipe;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AirBlock;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.WaterFluid;
import org.jetbrains.annotations.NotNull;
import yee.pltision.backrooms.block.BrBlocks;

public enum PipeLiquidState implements StringRepresentable {
    NONE("none", Fluids.EMPTY, null, null),
    WATER("water", Fluids.FLOWING_WATER,
            (state, level, pos) -> {
                if(state.getBlock() instanceof LiquidBlock liquid){
                    if(liquid.getFluid().isSame(Fluids.WATER)){
                        int l=state.getValue(LiquidBlock.LEVEL);
                        if(l==0) l=9;
                        return l;
                    }
                }
                return 0;
            },
            (level,from, state, world, pos) -> {
                if(level>0){
                    if(state.getBlock()instanceof AirBlock){
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
        int isSource(BlockState state,Level world,BlockPos pos);
    }


    @Override
    public @NotNull String getSerializedName() {
        return name;
    }
}
