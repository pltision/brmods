package yee.pltision.backrooms.block.pipe;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;
import yee.pltision.backrooms.block.BrBlocks;

@Mod.EventBusSubscriber
public class FakeLiquidBlockEntity extends BlockEntity {
    final static public RegistryObject<BlockEntityType<?>> TYPE= BrBlocks.BLOCK_ENTITY_REGISTER.register("fake_liquid_block_entity",()->BlockEntityType.Builder.of(FakeLiquidBlockEntity::new,BrBlocks.Pipes.FAKE_LIQUID.get()).build(null));

    public FakeLiquidBlockEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(TYPE.get(), p_155229_, p_155230_);
        neighbors=new int[5];
    }

    int blocking;
    final int[] neighbors;


}
