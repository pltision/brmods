package yee.pltision.backrooms.block.mushroom;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.jetbrains.annotations.NotNull;

public class MushroomItem extends BlockItem {
    public MushroomItem(Block p_40565_, Properties p_40566_) {
        super(p_40565_, p_40566_);
    }

    public @NotNull InteractionResult place(@NotNull BlockPlaceContext p_40577_){
        InteractionResult result=super.place(p_40577_);
        if(result!=InteractionResult.FAIL&&
                p_40577_.getLevel().getBlockEntity(p_40577_.getClickedPos()) instanceof MyceliumBlockEntity entity)
        {
            CompoundTag tag=p_40577_.getItemInHand().getTag();
            if(tag!=null)
                entity.mushroomData=tag.getLong("mushroom");

        }
        return result;
    }

    public int getUseDuration(ItemStack p_41454_) {
        return 48;
    }


}
