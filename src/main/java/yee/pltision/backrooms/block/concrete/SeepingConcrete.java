package yee.pltision.backrooms.block.concrete;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import yee.pltision.backrooms.block.farmland.IFarmland;
import yee.pltision.backrooms.block.farmland.IWaterBlock;

import java.util.Random;

import static yee.pltision.Util.RANDOM;

public class SeepingConcrete extends ConcreteBlock implements IWaterBlock,IFarmland {
    public SeepingConcrete(Properties p_49795_) {
        super(p_49795_);
    }
    @Override
    public double getDry(BlockState state, LevelReader level, BlockPos pos) {
        return 1;
    }

    @Override
    public double getRocky(BlockState state, LevelReader level, BlockPos pos) {
        return 0.8;
    }

    @Override
    public double nutrientFactor(BlockState state, @NotNull LevelReader level, BlockPos pos) {
        return 0.5* super.nutrientFactor(state,level,pos);
    }

    @Override
    public double getWater() {
        return 0.3;
    }

    @Override
    public void randomTick(@NotNull BlockState state, @NotNull ServerLevel level, BlockPos pos, @NotNull Random random) {
        int x=pos.getX(),y=pos.getY(),z=pos.getZ();
        BlockPos newPos=new BlockPos(x+RANDOM.nextInt()%2, y+x+RANDOM.nextInt()%2, z+RANDOM.nextInt()%2);
        double dry=IFarmland.randomComputeDry(0.3,level,newPos);
        if(dry>1&&dry<2&&(random.nextInt()&0b1111)>0){
            BlockState newState =level.getBlockState(newPos);
            if(newState.getBlock() instanceof IFarmland farmland){
                farmland.beDry(newState,level,newPos);
            }
        }
    }
}
