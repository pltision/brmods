package yee.pltision.backrooms.block.level1.generator;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.BarrierBlock;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class Level1GeneratorDataBlock extends BarrierBlock implements EntityBlock {
    public Level1GeneratorDataBlock(Properties p_49092_) {
        super(p_49092_);
    }

    @NotNull
    @Override
    public BlockEntity newBlockEntity(BlockPos p_153215_, BlockState p_153216_) {
        return new Level1GeneratorDataEntity(p_153215_,p_153216_);
    }
}
