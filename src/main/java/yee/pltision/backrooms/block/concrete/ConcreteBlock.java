package yee.pltision.backrooms.block.concrete;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import yee.pltision.backrooms.block.BrBlocks;
import yee.pltision.backrooms.block.farmland.IFarmland;
import yee.pltision.backrooms.block.moss.MossCrackableBlock;
import yee.pltision.backrooms.block.type.BackroomsHardBlock;

public class ConcreteBlock extends BackroomsHardBlock implements IFarmland , MossCrackableBlock {

    public ConcreteBlock(Properties p_49795_) {
        super(p_49795_);
    }

    @Override
    public double getWet(BlockState state, LevelReader level, BlockPos pos) {
        return IFarmland.randomPlaneWet(0.5,level,pos);
    }

    @Override
    public double getRocky(BlockState state, LevelReader level, BlockPos pos) {
        return 0.8;
    }

    @Override
    public double nutrientFactor(BlockState state, @NotNull LevelReader level, BlockPos pos) {
        return 0.5* IFarmland.biomeNutrientFactor(level,pos);
    }

    @Override
    public BlockState beWet(BlockState state, @NotNull LevelReader level, BlockPos pos) {
        return BrBlocks.Concretes.SEEPING_CONCRETE.get().defaultBlockState();
    }

    @Override
    public BlockState mossCracked(BlockState old) {
        return BrBlocks.Concretes.CRACKING_CONCRETE.get().defaultBlockState();
    }
}
