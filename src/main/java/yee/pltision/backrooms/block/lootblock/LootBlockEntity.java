package yee.pltision.backrooms.block.lootblock;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import yee.pltision.backrooms.block.mushroom.BrMyceliumBlock;
import yee.pltision.backrooms.block.mushroom.MyceliumBlockEntity;

public class LootBlockEntity extends BlockEntity {
    public LootBlockEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(MyceliumBlockEntity.MYCELIUM_BLOCK_ENTITY_TYPE.get(), p_155229_, p_155230_);
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
