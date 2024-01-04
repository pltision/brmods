package yee.pltision.backrooms.block.crate;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BarrelBlock;
import net.minecraft.world.level.block.entity.BarrelBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ContainerOpenersCounter;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import yee.pltision.backrooms.block.BrBlocks;

@Mod.EventBusSubscriber
public class CrateBlockEntity extends RandomizableContainerBlockEntity {
    public static final RegistryObject<BlockEntityType<CrateBlockEntity>> CRATE_BLOCK_ENTITY_TYPE =
            BrBlocks.BLOCK_ENTITY_REGISTER.register("crate_block_entity", () ->
                    BlockEntityType.Builder.of( CrateBlockEntity::new, BrBlocks.LootBlock.CRATE.get()).build(null)
            );

    public CrateBlockEntity(BlockPos p_155052_, BlockState p_155053_) {
        super(CRATE_BLOCK_ENTITY_TYPE.get(),p_155052_, p_155053_);
    }
    public CrateBlockEntity(BlockEntityType<?> p_155629_,BlockPos p_155052_, BlockState p_155053_) {
        super(p_155629_,p_155052_, p_155053_);
    }

    private NonNullList<ItemStack> items = NonNullList.withSize(27, ItemStack.EMPTY);
    private ContainerOpenersCounter openersCounter = new ContainerOpenersCounter() {
        protected void onOpen(@NotNull Level p_155062_, @NotNull BlockPos p_155063_, @NotNull BlockState p_155064_) {
            CrateBlockEntity.this.playSound(p_155064_, SoundEvents.CHEST_OPEN);
            CrateBlockEntity.this.updateBlockState(p_155064_, true);
        }

        protected void onClose(@NotNull Level p_155072_, @NotNull BlockPos p_155073_, @NotNull BlockState p_155074_) {
            CrateBlockEntity.this.playSound(p_155074_, SoundEvents.CHEST_CLOSE);
            CrateBlockEntity.this.updateBlockState(p_155074_, false);
        }

        protected void openerCountChanged(@NotNull Level p_155066_, @NotNull BlockPos p_155067_, @NotNull BlockState p_155068_, int p_155069_, int p_155070_) {
        }

        protected boolean isOwnContainer(Player p_155060_) {
            if (p_155060_.containerMenu instanceof ChestMenu) {
                Container container = ((ChestMenu)p_155060_.containerMenu).getContainer();
                return container == CrateBlockEntity.this;
            } else {
                return false;
            }
        }
    };

    protected void saveAdditional(@NotNull CompoundTag p_187459_) {
        super.saveAdditional(p_187459_);
        if (!this.trySaveLootTable(p_187459_)) {
            ContainerHelper.saveAllItems(p_187459_, this.items);
        }

    }

    public void load(@NotNull CompoundTag p_155055_) {
        super.load(p_155055_);
        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        if (!this.tryLoadLootTable(p_155055_)) {
            ContainerHelper.loadAllItems(p_155055_, this.items);
        }

    }

    public int getContainerSize() {
        return 27;
    }

    protected @NotNull NonNullList<ItemStack> getItems() {
        return this.items;
    }

    protected void setItems(@NotNull NonNullList<ItemStack> p_58610_) {
        this.items = p_58610_;
    }

    protected @NotNull Component getDefaultName() {
        return new TranslatableComponent(level.getBlockState(getBlockPos()).getBlock().getName().getString());
    }

    protected @NotNull AbstractContainerMenu createMenu(int p_58598_, @NotNull Inventory p_58599_) {
        return ChestMenu.threeRows(p_58598_, p_58599_, this);
    }

    public void startOpen(@NotNull Player p_58616_) {
        if (!this.remove && !p_58616_.isSpectator()) {
            this.openersCounter.incrementOpeners(p_58616_, this.getLevel(), this.getBlockPos(), this.getBlockState());
        }

    }

    public void stopOpen(@NotNull Player p_58614_) {
        if (!this.remove && !p_58614_.isSpectator()) {
            this.openersCounter.decrementOpeners(p_58614_, this.getLevel(), this.getBlockPos(), this.getBlockState());
        }

    }

    public void recheckOpen() {
        if (!this.remove) {
            this.openersCounter.recheckOpeners(this.getLevel(), this.getBlockPos(), this.getBlockState());
        }

    }

    void updateBlockState(BlockState p_58607_, boolean p_58608_) {
//        this.level.setBlock(this.getBlockPos(), p_58607_.setValue(BarrelBlock.OPEN, Boolean.valueOf(p_58608_)), 3);
    }

    void playSound(BlockState p_58601_, SoundEvent p_58602_) {
//        Vec3i vec3i = p_58601_.getValue(BarrelBlock.FACING).getNormal();
//        double d0 = (double)this.worldPosition.getX() + 0.5D + (double)vec3i.getX() / 2.0D;
//        double d1 = (double)this.worldPosition.getY() + 0.5D + (double)vec3i.getY() / 2.0D;
//        double d2 = (double)this.worldPosition.getZ() + 0.5D + (double)vec3i.getZ() / 2.0D;
        this.level.playSound((Player)null, worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), p_58602_, SoundSource.BLOCKS, 0.5F, this.level.random.nextFloat() * 0.1F + 0.9F);
    }
}
