package yee.pltision.backrooms;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import yee.pltision.backrooms.block.mushroom.BrMyceliumBlock;
import yee.pltision.backrooms.block.mushroom.MushroomType;
import yee.pltision.mushroom.MushroomData;
import yee.pltision.mushroom.MushroomGetter;

import java.util.HashMap;
import static yee.pltision.backrooms.block.BrBlocks.*;


@Mod.EventBusSubscriber
public class BrMushrooms {
    public static final int
            RED_MUSHROOM_HEAD=  0b011101,
            BLOOD_MUSHROOM_HEAD=0b110101,
            BROWN_MUSHROOM_HEAD=0b011010,
            WHITE_MUSHROOM_HEAD=0b011110;

    @SubscribeEvent
    public static void inti(FMLCommonSetupEvent event){
        //System.out.println("inti运行");

        RedMushroom.MYCELIUM_REPLACE.put(Level0.BRICKS.get().defaultBlockState(),Level0.MYCELIUM.get().defaultBlockState().setValue(BrMyceliumBlock.TYPE, MushroomType.RED_MUSHROOM));
        BrownMushroom.MYCELIUM_REPLACE.put(Level0.BRICKS.get().defaultBlockState(),Level0.MYCELIUM.get().defaultBlockState().setValue(BrMyceliumBlock.TYPE, MushroomType.BROWN_MUSHROOM));

        MushroomData.MUSHROOM_TYPES[RED_MUSHROOM_HEAD]=new BrMushrooms.RedMushroom();
        MushroomData.MUSHROOM_TYPES[BROWN_MUSHROOM_HEAD]=new BrMushrooms.BrownMushroom();

        //MushroomData.MUSHROOM_TYPE[0]

    }
    public static class RedMushroom implements MushroomGetter {
        public static HashMap<BlockState,BlockState> MYCELIUM_REPLACE=new HashMap<>();

        @Override
        public @NotNull BlockState getMycelium(BlockState replace) {
            return MYCELIUM_REPLACE.get(replace);
        }

        @Override
        public @Nullable BlockState getMushroom() {
            return RED_MUSHROOM.get().defaultBlockState();
        }
    }

    public static class BrownMushroom implements MushroomGetter{
        public static HashMap<BlockState,BlockState> MYCELIUM_REPLACE=new HashMap<>();

        @Override
        public @NotNull BlockState getMycelium(BlockState replace) {
            return MYCELIUM_REPLACE.get(replace);
        }

        @Override
        public @Nullable BlockState getMushroom() {
            return BROWN_MUSHROOM.get().defaultBlockState();
        }
    }

}
