package yee.pltision.backrooms.block;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.AirBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class FogBlock extends AirBlock {
    public FogBlock(Properties p_48756_) {
        super(p_48756_);
        ItemBlockRenderTypes.setRenderLayer(this, renderType -> renderType == RenderType.translucent());
    }
    public @NotNull RenderShape getRenderShape(@NotNull BlockState p_60550_) {
        return RenderShape.MODEL;
    }
}
