package yee.pltision.backrooms.block.test;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import yee.pltision.backrooms.block.BrBlocks;

@Mod.EventBusSubscriber
public class TestBlock extends Block implements EntityBlock {
    public static final RegistryObject<Block> TEST_BLOCK= BrBlocks.REGISTER.register("test",()->new TestBlock(
            BlockBehaviour.Properties.of(Material.STONE, MaterialColor.STONE).sound(SoundType.STONE).requiresCorrectToolForDrops().strength(5F, 14.0F).noCollission()
    ));

    public TestBlock(Properties p_49795_) {
        super(p_49795_);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos p_153215_, @NotNull BlockState p_153216_) {
        return new TestBlockEntity(p_153215_,p_153216_);
    }
}
