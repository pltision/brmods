package yee.pltision.backrooms.block.concrete;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import yee.pltision.backrooms.block.BrBlocks;
import yee.pltision.backrooms.block.lootblock.farmland.IFarmland;
import yee.pltision.backrooms.block.lootblock.farmland.IWaterBlock;

import java.util.Random;

public class SeepingConcreteBlock extends ConcreteBlock implements IWaterBlock,IFarmland {
    public SeepingConcreteBlock(Properties p_49795_) {
        super(p_49795_);
    }
    @Override
    public double getWet(BlockState state, LevelReader level, BlockPos pos) {
        return 0.9;
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

    ///fill ~-10 ~-1 ~-10 ~10 ~-1 ~10 backrooms:concrete/seeping
    @Override
    public void randomTick(@NotNull BlockState state, @NotNull ServerLevel level, @NotNull BlockPos pos, @NotNull Random random) {
//        System.out.println("yee");
        if((random.nextInt()&0b11)==0) {
//            System.out.println("yee");
            int x=pos.getX(),y=pos.getY(),z=pos.getZ();
            double dry=IFarmland.randomUpToDownWet(1, level, pos);
            if(dry<0.5){
                //System.out.println(dry);
                if((random.nextInt()&0b11)==0){
                    level.setBlock(pos,BrBlocks.Concretes.CRACKING_CONCRETE.get().defaultBlockState(),3);
                }
            }

            BlockPos newPos=new BlockPos(x+random.nextInt()%2, y+random.nextInt()%2, z+random.nextInt()%2);
            double newDry = IFarmland.randomUpToDownWet(1, level, newPos);
            BlockState newState = level.getBlockState(newPos);
            if (newState.getBlock() instanceof IFarmland farmland) {
                if (newDry > 0.8&&newDry < 2) {
                    BlockState dryState=farmland.beWet(newState, level, newPos);
                    if(newState!=dryState) level.setBlockAndUpdate(newPos,dryState);
                }
            }


        }


    }
}
