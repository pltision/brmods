package yee.pltision.backrooms.block.concrete;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import org.jetbrains.annotations.NotNull;
import yee.pltision.backrooms.block.BrBlocks;
import yee.pltision.backrooms.block.moss.Moss;
import yee.pltision.backrooms.block.moss.MossState;

import java.util.Random;

public class MossCrackingConcreteBlock extends CrackingConcreteBlock implements Moss {

    public MossCrackingConcreteBlock(Properties p_49795_) {
        super(p_49795_);
        registerDefaultState(defaultBlockState().setValue(WATER, MossState.HEALTHY));
    }

    @Override
    public BlockState beWet(BlockState state, @NotNull LevelReader level, BlockPos pos){return state;}

    @Override
    public BlockState getNoMoss(BlockState state) {
        return BrBlocks.Concretes.CRACKING_CONCRETE.get()
                .defaultBlockState().setValue(STAGE,state.getValue(STAGE));
    }

    @Override
    public void mossCrackBlock(BlockState state, Level level, BlockPos pos) {
        level.setBlockAndUpdate(pos,mossCracked(state));
    }

    @Override
    public double getPlantNutrient(BlockState state, LevelReader level, BlockPos pos) {
        return nutrientFactor(state,level,pos);
    }

    @Override
    public void randomTick(@NotNull BlockState state, @NotNull ServerLevel level, @NotNull BlockPos pos, @NotNull Random random) {
        Moss.super.randomTick(state,level,pos,random);
    }

    @Override
    public void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> p_54935_) {
        super.createBlockStateDefinition(p_54935_);
        Moss.super.createBlockStateDefinition(p_54935_);
    }

    @Override
    public BlockState mossCracked(BlockState old) {
        int stage=old.getValue(STAGE);
        return stage==2?
                BrBlocks.Concretes.MOSS_CONCRETE_RUBBLE.get().defaultBlockState():
                old.setValue(STAGE,stage+1);
    }
}
