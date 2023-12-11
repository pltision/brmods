package yee.pltision.backrooms.block.concrete;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import yee.pltision.backrooms.block.farmland.IFarmland;

public class ConcreteRubbleBlock extends Block implements IFarmland {
    public ConcreteRubbleBlock(Properties p_49795_) {
        super(p_49795_);
    }

    @Override
    public double getDry(BlockState state, LevelReader level, BlockPos pos) {
        return IFarmland.randomComputeDry(0.3,level,pos)*IFarmland.biomeDryFactor(level,pos);
    }

    @Override
    public double getRocky(BlockState state, LevelReader level, BlockPos pos) {
        return 0.7;
    }
    @Override
    public double nutrientFactor(BlockState state, @NotNull LevelReader level, BlockPos pos) {
        return 0.6* IFarmland.biomeNutrientFactor(level,pos);
    }
}
