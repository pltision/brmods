package yee.pltision.backrooms.block.lootblock;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

@Mod.EventBusSubscriber
public class EmptyShelfBlock extends Block {
    public static List<EmptyShelfBlock> registeredBlocks =new LinkedList<>();
    public HashMap<Item, Replace> replaceMap;
    public List<MapInti> mapIntiList;

    public EmptyShelfBlock(Properties p_49795_) {
        super(p_49795_);
        replaceMap =new HashMap<>();
        mapIntiList=new ArrayList<>();
        registeredBlocks.add(this);
    }
    public EmptyShelfBlock(Properties p_49795_, List<MapInti> intiList) {
        super(p_49795_);
        replaceMap =new HashMap<>();
        mapIntiList=intiList;
        registeredBlocks.add(this);
        for (MapInti inti : intiList){
            System.out.println(inti.item.get());
            replaceMap.put(inti.item.get(),new Replace(inti.replace.get(), inti.placed));
        }
    }

    public @NotNull InteractionResult use(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult result) {
        if(player.isSecondaryUseActive())
            return super.use(state,level,pos,player,hand,result);
        Replace replace=replaceMap.get(player.getMainHandItem().getItem());
        if(replace!=null){
            level.setBlock(pos,replace.placed.getFinalState(replace.state,level,pos,player,hand,result),3);
        }
        return super.use(state,level,pos,player,hand,result);
    }

    public interface AfterPlaced {
        BlockState getFinalState(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos,
                                 @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult result);
    }
    public record Replace(BlockState state, AfterPlaced placed){}
    public record MapInti(Supplier<Item> item, Supplier<BlockState> replace, AfterPlaced placed){}

    @SubscribeEvent
    public static void inti(RegisterCapabilitiesEvent event) {
        System.out.println("架子初始化");
        for(EmptyShelfBlock block:registeredBlocks) {
            for (MapInti inti : block.mapIntiList){
                System.out.println(inti.item.get());
                block.replaceMap.put(inti.item.get(),new Replace(inti.replace.get(), inti.placed));
            }
        }

    }
}
