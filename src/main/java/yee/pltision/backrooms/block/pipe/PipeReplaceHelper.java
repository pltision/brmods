package yee.pltision.backrooms.block.pipe;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class PipeReplaceHelper {
    final public Map<Block, Function<BlockState,BlockState>> stateCopyMap;

    public PipeReplaceHelper() {
        this.stateCopyMap = new HashMap<>();
    }

//    public @Nullable BlockState getReplaced(BlockState pipeState){
//
//    }
    public static abstract class StateCopier implements Function<BlockState,BlockState> {
        final BlockState defaultState;
        public StateCopier(BlockState defaultState) {
            this.defaultState = defaultState;
        }

        public abstract BlockState apply(BlockState state);
    }
}
