package yee.pltision.backrooms;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.blockentity.TheEndPortalRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import yee.pltision.backrooms.block.test.TestRenderer;

import static yee.pltision.backrooms.block.test.TestBlockEntity.TEST_BLOCK_ENTITY_TYPE;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber
public class BrRenderTypes extends RenderType {
    private BrRenderTypes(String p_173178_, VertexFormat p_173179_, VertexFormat.Mode p_173180_, int p_173181_, boolean p_173182_, boolean p_173183_, Runnable p_173184_, Runnable p_173185_) {
        super(p_173178_, p_173179_, p_173180_, p_173181_, p_173182_, p_173183_, p_173184_, p_173185_);
    }

    public static final RenderType TEST_GATEWAY = create("end_gateway", DefaultVertexFormat.POSITION, VertexFormat.Mode.QUADS, 256, false, false,
            RenderType.CompositeState.builder()
                    .setShaderState(RENDERTYPE_LINES_SHADER)
                    .setTextureState(
                            RenderStateShard.MultiTextureStateShard.builder()
                                    .add(TheEndPortalRenderer.END_SKY_LOCATION, false, false)
                                    .add(TheEndPortalRenderer.END_PORTAL_LOCATION, false, false)
                                    .build()
                    ).createCompositeState(false));
    @SubscribeEvent
    public static void inti(FMLCommonSetupEvent event) {
        BlockEntityRenderers.register(TEST_BLOCK_ENTITY_TYPE.get(), TestRenderer::new);

    }
}
