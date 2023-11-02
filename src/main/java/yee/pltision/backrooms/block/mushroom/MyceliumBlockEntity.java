package yee.pltision.backrooms.block.mushroom;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import yee.pltision.backrooms.block.BrBlocks;
import yee.pltision.backrooms.block.lootblock.LootBlock;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MyceliumBlockEntity extends BlockEntity {
    public static final ArrayList<Block> ENTITY_BLOCKS=new ArrayList<>();

    public static final RegistryObject<BlockEntityType<MyceliumBlockEntity>> MYCELIUM_BLOCK_ENTITY_TYPE =
            BrBlocks.BLOCK_ENTITY_REGISTER.register("mycelium_blockentity", () -> BlockEntityType.Builder.of( MyceliumBlockEntity::new, LootBlock.getBlockArray(ENTITY_BLOCKS)).build(null));


    public MyceliumBlockEntity( BlockPos p_155229_, BlockState p_155230_) {
        super(MYCELIUM_BLOCK_ENTITY_TYPE.get(), p_155229_, p_155230_);
    }

    public long mushroomData;

    @Override
    public void saveAdditional(CompoundTag tag){
        tag.putLong("mushroom",mushroomData);
    }

    @Override
    public void load(@NotNull CompoundTag tag){
        mushroomData= tag.getLong("mushroom");
    }

}
