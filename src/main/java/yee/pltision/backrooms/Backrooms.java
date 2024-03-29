package yee.pltision.backrooms;

import com.mojang.logging.LogUtils;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import yee.pltision.Util;
import yee.pltision.backrooms.block.BrBlocks;
import yee.pltision.backrooms.block.lootblock.EmptyShelfBlock;
import yee.pltision.backrooms.block.lootblock.LootBlock;
import yee.pltision.backrooms.block.test.TestBlockEntity;
import yee.pltision.backrooms.worldgen.BackroomsFunction;
import yee.pltision.backrooms.worldgen.DimensionInti;
import yee.pltision.backrooms.worldgen.FeatureAppends;
import yee.pltision.backrooms.worldgen.feature.StructureType;
import yee.pltision.backrooms.worldgen.feature.level0.Level0Feature;
import yee.pltision.mushroom.mc.MushroomMobEffects;

import java.util.stream.Collectors;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Util.MODID)
public class Backrooms
{
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();


    public Backrooms()
    {
        IEventBus bus=FMLJavaModLoadingContext.get().getModEventBus();
        // Register the setup method for modloading
        bus.addListener(this::setup);
        // Register the enqueueIMC method for modloading
        bus.addListener(this::enqueueIMC);
        // Register the processIMC method for modloading
        bus.addListener(this::processIMC);
        bus.addListener(BrMushrooms::inti);
        bus.addListener(BrBlocks.WoodenShelves::inti);
        bus.addListener(EmptyShelfBlock::inti);
        bus.addListener(LootBlock::inti);
        bus.addListener(Level0Feature::inti);
        bus.addListener(TestBlockEntity::inti);
        bus.addListener(BrRenderTypes::inti);

        StructureType.REGISTER.register(bus);

        FeatureAppends.REGISTER.register(bus);

        BrBlocks.REGISTER.register(bus);
        BrBlocks.ITEM_REGISTER.register(bus);
        BrBlocks.BLOCK_ENTITY_REGISTER.register(bus);

        MushroomMobEffects.REGISTER.register(bus);

        //Mushrooms.inti();

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event)
    {
        // some preinit code
        LOGGER.info("HELLO FROM PREINIT");
        LOGGER.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());

        DimensionInti.load();
        BackroomsFunction.reg();
    }

    private void enqueueIMC(final InterModEnqueueEvent event)
    {
        // Some example code to dispatch IMC to another mod
        InterModComms.sendTo("examplemod", "helloworld", () -> { LOGGER.info("Hello world from the MDK"); return "Hello world";});
    }

    private void processIMC(final InterModProcessEvent event)
    {
        // Some example code to receive and process InterModComms from other mods
        LOGGER.info("Got IMC {}", event.getIMCStream().
                map(m->m.messageSupplier().get()).
                collect(Collectors.toList()));
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents
    {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent)
        {
            // Register a new block here
            LOGGER.info("HELLO from Register Block");
        }
    }
}
