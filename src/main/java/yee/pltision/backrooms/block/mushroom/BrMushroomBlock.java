package yee.pltision.backrooms.block.mushroom;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.PlantType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BrMushroomBlock extends BushBlock implements EntityBlock, IPlantable {
    //public static final EnumProperty<MushroomType> TYPE=BrMyceliumBlock.TYPE;
    public static final VoxelShape SHAPE = Block.box(5.0D, 0.0D, 5.0D, 11.0D, 6.0D, 11.0D);
    public VoxelShape getShape(BlockState p_54889_, BlockGetter p_54890_, BlockPos p_54891_, CollisionContext p_54892_) {
        return SHAPE;
    }

    /*protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_54935_) {
        p_54935_.add(TYPE);
    }*/

    public BrMushroomBlock(Properties p_49795_) {
        super(p_49795_);
        MyceliumBlockEntity.ENTITY_BLOCKS.add(this);
        ItemBlockRenderTypes.setRenderLayer(this, renderType -> renderType == RenderType.cutout());
    }
    @Deprecated
    public List<ItemStack> getDrops(BlockState blockState, LootContext.Builder builder) {
        BlockEntity entity=builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
        ItemStack item=this.asItem().getDefaultInstance();
        long data=0;
        if(entity instanceof MyceliumBlockEntity myceliumEntity){
            data=myceliumEntity.mushroomData;
        }
        CompoundTag tag=new CompoundTag();
        tag.putLong("mushroom",data);
        item.setTag(tag);
        return List.of(item);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos p_153215_, @NotNull BlockState p_153216_) {
        return new MyceliumBlockEntity(p_153215_,p_153216_);
    }
    public boolean canSurvive(@NotNull BlockState p_54880_, LevelReader p_54881_, BlockPos p_54882_) {
        BlockPos blockpos = p_54882_.below();
        BlockState blockstate = p_54881_.getBlockState(blockpos);
        return blockstate.canSustainPlant(p_54881_, blockpos, net.minecraft.core.Direction.UP, this);
    }


    protected boolean mayPlaceOn(BlockState p_54894_, @NotNull BlockGetter p_54895_, @NotNull BlockPos p_54896_) {
        return p_54894_.isSolidRender(p_54895_, p_54896_);
    }

}
