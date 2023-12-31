package yee.pltision.backrooms.block.moss;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.AirBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.jetbrains.annotations.NotNull;
import yee.pltision.backrooms.block.BrBlocks;

import java.util.Random;

public interface Moss {

    //BlockState getDecayed(BlockState state);
    //BlockState getDesiccated(BlockState state);
    BlockState getNoMoss(BlockState state);

    void mossCrackBlock(BlockState state,Level level, BlockPos pos);
    static void tryCrack(Level level,BlockPos pos){
        BlockState state= level.getBlockState(pos);
        if(state.getBlock()instanceof MossCrackableBlock block&&!(block instanceof Moss)){
            level.setBlockAndUpdate(pos,block.mossCracked(state));
        }
    }

    double getPlantNutrient(BlockState state, LevelReader level, BlockPos pos);
    double getWet(BlockState state, LevelReader level,BlockPos pos);

    EnumProperty<MossState> WATER = EnumProperty.create("water",MossState.class);

    double MOSS_MIN_WET=0.5,MOSS_MAX_WET=1.5;
    double GROW_NUTRIENT=0.35;

    default void randomTick(@NotNull BlockState state, @NotNull ServerLevel level, @NotNull BlockPos pos, @NotNull Random random){
        if((random.nextInt()&0b11)==0){
            mossCrackBlock(state,level,pos);
        }
        int water= state.getValue(WATER).value;
        if(water!=MossState.DECAYED.value&&water!=MossState.DESICCATED.value){
            double wet= getWet(state,level,pos);
            if(wet<MOSS_MIN_WET) water--;
            else if(wet>MOSS_MAX_WET) water++;
            else {
                if(water!=0)level.setBlockAndUpdate(pos,state.setValue(WATER,MossState.HEALTHY));
//                System.out.println(getPlantNutrient(state,level,pos)+" "+ IFarmland.biomeNutrientFactor(level,pos));
                if(getPlantNutrient(state,level,pos)>=GROW_NUTRIENT) {//繁殖
                    int x=pos.getX(),y=pos.getY(),z=pos.getZ();
                    BlockPos newPos=new BlockPos(x+random.nextInt()%2, y+random.nextInt()%2, z+random.nextInt()%2);
                    BlockState newState=level.getBlockState(newPos);
                    boolean isUnder=level.getBlockState(newPos.above()).getMaterial().isSolid();
                    if(newState.getBlock()instanceof MossableBlock mossable){
                        newState=mossable.getMossed(newState);
                        wet=((Moss)newState.getBlock()).getWet(newState,level,newPos);
                        if(newState.canSurvive(level,newPos)&&
                                (isUnder||(wet>MOSS_MIN_WET&&wet<MOSS_MAX_WET)))
                            level.setBlockAndUpdate(newPos,newState);
                    }
                    else if(newState.getBlock()instanceof AirBlock){
                        newState= BrBlocks.Mosses.MOSS_CARPET.get().defaultBlockState();
                        wet=((Moss)newState.getBlock()).getWet(newState,level,newPos);
                        if(newState.canSurvive(level,newPos)&&
                                (isUnder||(wet>MOSS_MIN_WET&&wet<MOSS_MAX_WET))){
                            level.setBlockAndUpdate(pos,newState);
                        }
                    }
                }

                return;
            }
            level.setBlockAndUpdate(pos,state.setValue(WATER,MossState.DECAYED.getFromValue(water)));
        }
        else{
            if((random.nextInt()&0b11)==0){
                level.setBlockAndUpdate(pos,((Moss)state.getBlock()).getNoMoss(state));
            }
        }
    }

    default void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> p_54935_) {
        p_54935_.add(WATER);
    }

}
