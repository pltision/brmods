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

public class MossConcreteRubbleBlock extends ConcreteRubbleBlock implements Moss {
    public MossConcreteRubbleBlock(Properties p_49795_) {
        super(p_49795_);
        registerDefaultState(defaultBlockState().setValue(WATER, MossState.HEALTHY));
    }

    @Override
    public BlockState getNoMoss(BlockState state) {
        return BrBlocks.Concretes.CONCRETE_RUBBLE.get().defaultBlockState();
    }

    @Override
    public void mossCrackBlock(BlockState state, Level level, BlockPos pos) {}

    @Override
    public double getPlantNutrient(BlockState state, LevelReader level, BlockPos pos) {
        return nutrientFactor(state,level,pos);
    }

    @Override
    public void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> p_54935_) {
        Moss.super.createBlockStateDefinition(p_54935_);
    }
    public void randomTick(@NotNull BlockState state, @NotNull ServerLevel level, @NotNull BlockPos pos, @NotNull Random random){
        Moss.super.randomTick(state,level,pos,random);
    }
}
