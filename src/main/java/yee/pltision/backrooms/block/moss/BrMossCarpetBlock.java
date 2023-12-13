package yee.pltision.backrooms.block.moss;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CarpetBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import org.jetbrains.annotations.NotNull;
import yee.pltision.backrooms.block.farmland.IFarmland;
import yee.pltision.backrooms.block.farmland.IWaterBlock;

import java.util.Random;

public class BrMossCarpetBlock extends CarpetBlock implements IWaterBlock, Moss {
    @Override
    public BlockState getNoMoss(BlockState state) {
        return Blocks.AIR.defaultBlockState();
    }

    @Override
    public void mossCrackBlock(BlockState state, Level level, BlockPos pos) {
        Moss.tryCrack(level,pos.below());
    }

    public double getPlantNutrient(BlockState state, LevelReader level, BlockPos pos){
        return IFarmland.getNutrient(level,pos.below());
    }
    public double getWet(BlockState state, LevelReader level,BlockPos pos){
        BlockPos underPos=pos.below();
        BlockState underState=level.getBlockState(underPos);
        return underState.getBlock()instanceof IFarmland farmland?farmland.getWet(underState,level,underPos):0;
    }
    public BrMossCarpetBlock(Properties p_152915_) {
        super(p_152915_);
        registerDefaultState(defaultBlockState().setValue(WATER, MossState.HEALTHY));
    }
    public boolean canSurvive(@NotNull BlockState p_152922_, @NotNull LevelReader p_152923_, @NotNull BlockPos p_152924_) {
        return Block.isFaceFull(p_152923_.getBlockState(p_152924_.below()).getShape(p_152923_,p_152924_), Direction.UP);
    }

    @Override
    public double getWater() {
        return 0.05;
    }

    @Override
    public void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> p_49915_) {
        p_49915_.add(Moss.WATER);
    }
    public void randomTick(@NotNull BlockState state, @NotNull ServerLevel level, @NotNull BlockPos pos, @NotNull Random random){
        Moss.super.randomTick(state,level,pos,random);
    }
}
