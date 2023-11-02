package yee.pltision.backrooms.block.lootblock;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import yee.pltision.backrooms.block.BrBlocks;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

@Mod.EventBusSubscriber
public class LootBlock extends Block {


    public static LinkedList<BlockSetup> registeredBlocks=new LinkedList<>();
    public static LinkedList<LootBlock> nbtLootBlocks =new LinkedList<>();

    public static final RegistryObject<BlockEntityType<LootBlockEntity>> LOOT_BLOCK_ENTITY_TYPE =
            BrBlocks.BLOCK_ENTITY_REGISTER.register("item_block_blockentity", () -> BlockEntityType.Builder.of( LootBlockEntity::new, getBlockArray(nbtLootBlocks) ).build(null));

    public static Block[] getBlockArray(List<? extends Block> blockList){
        Block[] blockArray=new Block[blockList.size()];
        int[] index=new int[1];
        blockList.forEach(
                block -> blockArray[index[0]++]=block
        );
        return blockArray;
    }

    public BlockState replace;
    public ItemStack loot;
    public LootBlock(Properties p_49795_, Supplier<BlockState> replace) {
        super(p_49795_);
        registeredBlocks.add(new BlockSetup(this,replace));

    }
    public LootBlock(Properties p_49795_, Supplier<BlockState> replace, boolean hasEntity) {
        this(p_49795_,replace);
        if(hasEntity) nbtLootBlocks.add(this);
    }

    @SubscribeEvent
    public static void inti(FMLLoadCompleteEvent event) {
        for(BlockSetup block:registeredBlocks){
            block.inti();
        }
    }

   /* public InteractionResult use(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult result) {
        //level.setBlock(pos,replace,3);
        return super.use(state,level,pos,player,hand,result);
    }*/

    public record BlockSetup(LootBlock block,Supplier<BlockState> replace){
        public void inti(){
            block.replace=replace.get();
            //block.loot=loot.get();
        }
    }

}
