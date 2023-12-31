package yee.pltision.backrooms.block.concrete;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import yee.pltision.backrooms.block.BrBlocks;
import yee.pltision.backrooms.block.lootblock.farmland.IFarmland;
import yee.pltision.backrooms.block.moss.MossableBlock;

public class ConcreteRubbleBlock extends Block implements IFarmland, MossableBlock {
    public ConcreteRubbleBlock(Properties p_49795_) {
        super(p_49795_);
    }

    @Override
    public double getWet(BlockState state, LevelReader level, BlockPos pos) {
        return IFarmland.randomPlaneWet(0.7,level,pos);
    }

    @Override
    public double getRocky(BlockState state, LevelReader level, BlockPos pos) {
        return 0.7;
    }
    @Override
    public double nutrientFactor(BlockState state, @NotNull LevelReader level, BlockPos pos) {
        return 0.7* IFarmland.biomeNutrientFactor(level,pos);
    }

    @Override
    public BlockState getMossed(BlockState old) {
        return BrBlocks.Concretes.MOSS_CONCRETE_RUBBLE.get().defaultBlockState();
    }
}
