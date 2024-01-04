package yee.pltision.backrooms.block.concrete;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.jetbrains.annotations.NotNull;
import yee.pltision.backrooms.block.BrBlocks;
import yee.pltision.backrooms.block.farmland.Farmland;
import yee.pltision.backrooms.block.moss.MossableBlock;

import java.util.Random;

public class CrackingConcreteBlock extends ConcreteBlock implements MossableBlock {
    public static final IntegerProperty STAGE = IntegerProperty.create("stage",1,2);

    public CrackingConcreteBlock(Properties p_49795_) {
        super(p_49795_);
        registerDefaultState(defaultBlockState().setValue(STAGE,1));
    }
    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> p_54935_) {
        p_54935_.add(STAGE);
    }

    @Override
    public double getRocky(BlockState state, LevelReader level, BlockPos pos) {
        return 0.75;
    }

    public BlockState beWet(BlockState state, @NotNull LevelReader level, BlockPos pos){
        int value=state.getValue(STAGE);
        if(value==1) return BrBlocks.Concretes.CONCRETE.get().defaultBlockState();
        else return state.setValue(STAGE,value-1);
    }

    @Override
    public void randomTick(@NotNull BlockState state, @NotNull ServerLevel level, @NotNull BlockPos pos, @NotNull Random random) {
        if((random.nextInt()&0b111)==0 && Farmland.randomUpToDownWet(1, level, pos)<0.5) {
            int value=state.getValue(STAGE);
            if(value==2) level.setBlock(pos,BrBlocks.Concretes.CONCRETE_RUBBLE.get().defaultBlockState(),3);
            else level.setBlock(pos,state.setValue(STAGE,value+1),3);
        }
    }

    @Override
    public BlockState mossCracked(BlockState old) {
        int stage=old.getValue(STAGE);
        return stage==2?
                BrBlocks.Concretes.CONCRETE_RUBBLE.get().defaultBlockState():
                old.setValue(STAGE,stage+1);

    }


    @Override
    public BlockState getMossed(BlockState old) {
        return BrBlocks.Concretes.MOSS_CRACKING_CONCRETE.get().defaultBlockState().setValue(STAGE,old.getValue(STAGE));
    }
}
