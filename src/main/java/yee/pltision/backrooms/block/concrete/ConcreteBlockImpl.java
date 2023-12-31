package yee.pltision.backrooms.block.concrete;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import yee.pltision.backrooms.block.pipe.PipeReplaceable;

public class ConcreteBlockImpl extends ConcreteBlock implements PipeReplaceable {
    public ConcreteBlockImpl(Properties p_49795_) {
        super(p_49795_);
    }

    @Override
    public @Nullable BlockState replaceToPipe(BlockState replace, BlockState pipe) {
        return null;
    }
}
