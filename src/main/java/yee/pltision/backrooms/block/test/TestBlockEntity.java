package yee.pltision.backrooms.block.test;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.blockentity.TheEndPortalRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.TheEndPortalBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import yee.pltision.backrooms.block.BrBlocks;
import yee.pltision.backrooms.block.lootblock.LootBlock;
import yee.pltision.backrooms.block.mushroom.MyceliumBlockEntity;



@Mod.EventBusSubscriber
public class TestBlockEntity extends TheEndPortalBlockEntity {
    @SuppressWarnings("ConstantConditions")
    public static final RegistryObject<BlockEntityType<TestBlockEntity>> TEST_BLOCK_ENTITY_TYPE =
            BrBlocks.BLOCK_ENTITY_REGISTER.register("test_block_entity", () -> BlockEntityType.Builder.of( TestBlockEntity::new,TestBlock.TEST_BLOCK.get() ).build(null));

    public TestBlockEntity( BlockPos p_155229_, BlockState p_155230_) {
        super(TEST_BLOCK_ENTITY_TYPE.get(), p_155229_, p_155230_);
    }

    public boolean shouldRenderFace(@NotNull Direction p_59980_) {
        return true;
    }

    @SubscribeEvent
    public static void inti(FMLCommonSetupEvent event) {


    }
}
