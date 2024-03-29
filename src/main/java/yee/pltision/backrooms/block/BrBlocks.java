package yee.pltision.backrooms.block;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.ForgeSoundType;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import yee.pltision.Util;
import yee.pltision.backrooms.block.concrete.*;
import yee.pltision.backrooms.block.crate.CrateBlock;
import yee.pltision.backrooms.block.farmland.Farmland;
import yee.pltision.backrooms.block.light.NonstaticLight;
import yee.pltision.backrooms.block.light.NonstaticLightBlock;
import yee.pltision.backrooms.block.lootblock.EmptyShelfBlock;
import yee.pltision.backrooms.block.lootblock.StackableShelfBlock;
import yee.pltision.backrooms.block.moss.BrMossCarpetBlock;
import yee.pltision.backrooms.block.mushroom.BrMushroomBlock;
import yee.pltision.backrooms.block.mushroom.BrMyceliumBlock;
import yee.pltision.backrooms.block.mushroom.MushroomItem;
import yee.pltision.backrooms.block.light.WallLight;
import yee.pltision.backrooms.block.light.XZLight;
import yee.pltision.backrooms.block.pipe.AxisPipeBlock;
import yee.pltision.backrooms.block.pipe.CrossPipeBlock;
import yee.pltision.backrooms.block.pipe.FakeLiquidBlock;
import yee.pltision.backrooms.block.type.BackroomsHardBlock;

import java.net.PortUnreachableException;
import java.util.ArrayList;

@SuppressWarnings("unused")
public class BrBlocks {
    public static final DeferredRegister<Block> REGISTER=DeferredRegister.create(ForgeRegistries.BLOCKS, Util.MODID);
    public static final DeferredRegister<Fluid> FLUID_REGISTER=DeferredRegister.create(ForgeRegistries.FLUIDS, Util.MODID);
    public static final DeferredRegister<Item> ITEM_REGISTER=DeferredRegister.create(ForgeRegistries.ITEMS, Util.MODID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_REGISTER=DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, Util.MODID);

    @Mod.EventBusSubscriber
    public static class Level0{
        public static final RegistryObject<Block> LIGHT =
                REGISTER.register("level0_light",()-> new XZLight(
                        BlockBehaviour.Properties.of(Material.DECORATION).instabreak().lightLevel((p_187435_) -> 15).requiresCorrectToolForDrops().strength(1F, 10.0F).noCollission().sound(SoundType.GLASS)
                ));

        public static final RegistryObject<Block> WALL =
                REGISTER.register("level0_wall",()->
                        new BackroomsHardBlock(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.STONE).sound(SoundType.WOOL).requiresCorrectToolForDrops().strength(5F, 14.0F))
                );
        public static final RegistryObject<Block> BRICKS =
                REGISTER.register("level0_bricks",()->
                        new BackroomsHardBlock(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.STONE).sound(SoundType.STONE).requiresCorrectToolForDrops().strength(5F, 14.0F))
                );
        public static final RegistryObject<Block> FLOOR =
                REGISTER.register("level0_floor",()->
                        new BackroomsHardBlock(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.STONE).sound(
                                new ForgeSoundType(1.0F, 1.0F, ()->SoundEvents.STONE_BREAK, ()->SoundEvents.WOOL_STEP, ()->SoundEvents.WOOL_PLACE, ()->SoundEvents.WOOL_HIT, ()->SoundEvents.WOOL_FALL)
                        ).requiresCorrectToolForDrops().strength(5F, 14.0F))
                );
        public static final RegistryObject<Block> CEILING =
                REGISTER.register("level0_ceiling",()->
                        new BackroomsHardBlock(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.STONE).sound(SoundType.STONE).requiresCorrectToolForDrops().strength(5F, 14.0F))
                );

        public static final RegistryObject<Item> WALL_ITEM =
                ITEM_REGISTER.register("level0_wall",()->
                        new BlockItem(WALL.get(),new Item.Properties())
                );
        public static final RegistryObject<Item> BRICKS_ITEM =
                ITEM_REGISTER.register("level0_bricks",()->
                        new BlockItem(BRICKS.get(),new Item.Properties())
                );
        public static final RegistryObject<Item> FLOOR_ITEM =
                ITEM_REGISTER.register("level0_floor",()->
                        new BlockItem(FLOOR.get(),new Item.Properties())
                );
        public static final RegistryObject<Item> CEILING_ITEM =
                ITEM_REGISTER.register("level0_ceiling",()->
                        new BlockItem(CEILING.get(),new Item.Properties())
                );
        public static final RegistryObject<Item> LIGHT_ITEM =
                ITEM_REGISTER.register("level0_light",()->
                        new BlockItem(Level0.LIGHT.get(),new Item.Properties())
                );


        public static final RegistryObject<Block> MYCELIUM =
                REGISTER.register("level0/mycelium",()->
                        new BrMyceliumBlock(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.STONE).sound(SoundType.STONE).requiresCorrectToolForDrops().strength(5F, 14.0F))
                );
    }

    public static class Level1 {
//        @Deprecated
//        public static final RegistryObject<Block> GENERATOR_DATA_BLOCK = REGISTER.register("level1/generator_data_block", () ->
//                new Level1GeneratorDataBlock(BlockBehaviour.Properties.of(Material.STONE).strength(-1.0F, 3600000.0F).noDrops()));
//        @Deprecated
//        public static final RegistryObject<Block> GENERATED_BLOCK = REGISTER.register("level1/generated_block", () ->
//                new BarrierBlock(BlockBehaviour.Properties.of(Material.STONE).strength(-1.0F, 3600000.0F).noDrops()));
    }

    public static final RegistryObject<Block> WOODEN_TABLE=
            REGISTER.register("wooden_table",()->
                    new Table(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.WOOD).sound(SoundType.WOOD).requiresCorrectToolForDrops().strength(3F, 10.0F).noOcclusion())
            );
    public static final RegistryObject<Block> WOODEN_CHARA=
            REGISTER.register("wooden_chara",()->
                    new Chara(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.WOOD).sound(SoundType.WOOD).requiresCorrectToolForDrops().strength(3F, 10.0F).noOcclusion())
            );

    public static final RegistryObject<Block> RED_MUSHROOM=REGISTER.register("red_mushroom",()->new BrMushroomBlock(BlockBehaviour.Properties.of(Material.PLANT, MaterialColor.COLOR_BROWN)
            .noCollission().randomTicks().instabreak().sound(SoundType.GRASS)));
    public static final RegistryObject<Block> BROWN_MUSHROOM=REGISTER.register("brown_mushroom",()->new BrMushroomBlock(BlockBehaviour.Properties.of(Material.PLANT, MaterialColor.COLOR_BROWN)
            .noCollission().randomTicks().instabreak().sound(SoundType.GRASS)));
    public static final  RegistryObject<Item> RED_MUSHROOM_ITEM =
            ITEM_REGISTER.register("red_mushroom",()->
                    new MushroomItem(RED_MUSHROOM.get(),new Item.Properties().stacksTo(16).food(
                            new FoodProperties.Builder().nutrition(1).saturationMod(0.8f)
                                    .effect(()-> new MobEffectInstance(MobEffects.POISON,((Util.RANDOM.nextInt()&0b11)+1)*20,0),1)
                                    .effect(()-> new MobEffectInstance(MobEffects.CONFUSION,((Util.RANDOM.nextInt()&0b11)+5)*20,0),1)
                                    .build()
                    ))
            );
    public static final  RegistryObject<Item> BROWN_MUSHROOM_ITEM =
            ITEM_REGISTER.register("brown_mushroom",()->
                    new MushroomItem(BROWN_MUSHROOM.get(),new Item.Properties().stacksTo(16).food(
                            new FoodProperties.Builder().nutrition(1).saturationMod(0.8f)
                            .effect(()->new MobEffectInstance(MobEffects.CONFUSION, ((Util.RANDOM.nextInt()&0b11)+5)*20,0),0.3F)
                            .build()
                    ))
            );

    public static final RegistryObject<Block> FOG=REGISTER.register("fog",()->new FogBlock(BlockBehaviour.Properties.of(Material.AIR).noCollission().noDrops().air()));
    public static final  RegistryObject<Item> FOG_ITEM = ITEM_REGISTER.register("fog",()-> new BlockItem(FOG.get(),new Item.Properties()));

    @Mod.EventBusSubscriber
    public static class WoodenShelves {

        public static final ArrayList<EmptyShelfBlock.MapInti> WOODEN_SHELF_MAP_INTIS =new ArrayList<>(1);
        public static final RegistryObject<Block> SHELF=
                REGISTER.register("shelf/wooden",()->
                        new EmptyShelfBlock(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.WOOD).sound(SoundType.WOOD).requiresCorrectToolForDrops().strength(3F, 10.0F).noOcclusion(),
                                WOODEN_SHELF_MAP_INTIS)
                );
        public static final RegistryObject<Block> CANNED_CARROTS=
                REGISTER.register("shelf/wooden/item/canned_carrots",()->
                        new StackableShelfBlock(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.WOOD).sound(SoundType.WOOD).requiresCorrectToolForDrops().strength(3F, 10.0F).noOcclusion(),
                                ()-> SHELF.get().defaultBlockState())
                        {
                            public @NotNull InteractionResult use(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult result) {
                                ItemStack item=player.getMainHandItem();
                                if(player.isSecondaryUseActive())
                                    return super.use(state,level,pos,player,hand,result);

                                int count=player.getMainHandItem().getCount();
                                if(item.is(LootItems.CANNED_CARROTS.get())&&count>1&&state.getValue(StackableShelfBlock.STACK)<4){
                                    player.getMainHandItem().setCount(count-1);
                                    level.setBlock(pos,state.setValue(StackableShelfBlock.STACK,state.getValue(StackableShelfBlock.STACK)+1),3);
                                }
                                else {
                                    ItemStack give = LootItems.CANNED_CARROTS.get().getDefaultInstance();
                                    give.setCount(level.getBlockState(pos).getValue(StackableShelfBlock.STACK));
                                    player.addItem(give);
                                    level.setBlock(pos, replace, 3);
                                }
                                return super.use(state,level,pos,player,hand,result);
                            }
                        }
                );

        @SubscribeEvent
        public static void inti(FMLLoadCompleteEvent event) {
            WOODEN_SHELF_MAP_INTIS.add(
                    new EmptyShelfBlock.MapInti(LootItems.CANNED_CARROTS, () -> CANNED_CARROTS.get().defaultBlockState(),
                            (state, level, pos, player, hand, result) -> {
                                int count=player.getMainHandItem().getCount();
                                if(count>=1) {
                                    player.getMainHandItem().setCount(count-1);
                                    state=state.setValue(StackableShelfBlock.STACK, 1);
                                }
                                return state;
                            })
            );

        }



    }

    @Mod.EventBusSubscriber
    public static class Normal{
        @Deprecated public static final RegistryObject<Block> CONCRETE=REGISTER.register("normal/concrete",()->
                new BackroomsHardBlock(BlockBehaviour.Properties.of(Material.STONE, DyeColor.BLACK).requiresCorrectToolForDrops().strength(5F,14F)));
        @Deprecated public static final RegistryObject<Block> WHITE_CONCRETE =REGISTER.register("normal/white_concrete",()->
                new BackroomsHardBlock(BlockBehaviour.Properties.of(Material.STONE, DyeColor.WHITE).requiresCorrectToolForDrops().strength(5F,14F)));
        public static final RegistryObject<Block> WALL_LIGHT =REGISTER.register("normal/wall_light",()->new WallLight(
                BlockBehaviour.Properties.of(Material.DECORATION).lightLevel(NonstaticLight.maxLightGetter()).requiresCorrectToolForDrops().strength(1F, 10.0F).noOcclusion().sound(SoundType.STONE).randomTicks(),
                3/128D,1/16D,1/32D,5/8D,2,1/3D,2/3D
        ));
        public static final RegistryObject<Block> CEILING_LAMP=REGISTER.register("normal/ceiling_lamp",()-> new XZLight(
                BlockBehaviour.Properties.of(Material.DECORATION).instabreak().lightLevel((p_187435_) -> 15).requiresCorrectToolForDrops().strength(1F, 10.0F).noCollission().sound(SoundType.GLASS)
        ){
            @Override
            protected void inti(XZLight light) {
                ItemBlockRenderTypes.setRenderLayer(light, renderType -> renderType == RenderType.cutout());
            }
        });

        @Deprecated public static final RegistryObject<Item> CONCRETE_ITEM =
                ITEM_REGISTER.register("normal/concrete",()-> new BlockItem(CONCRETE.get(),new Item.Properties()));
        @Deprecated public static final RegistryObject<Item> WHITE_CONCRETE_ITEM =
                ITEM_REGISTER.register("normal/white_concrete",()-> new BlockItem(WHITE_CONCRETE.get(),new Item.Properties()));
        public static final RegistryObject<Item> WALL_LIGHT_ITEM =
                ITEM_REGISTER.register("normal/wall_light",()-> new BlockItem(WALL_LIGHT.get(),new Item.Properties()));
        public static final RegistryObject<Item> CEILING_LAMP_ITEM =
                ITEM_REGISTER.register("normal/ceiling_lamp",()-> new BlockItem(CEILING_LAMP.get(),new Item.Properties()));

    }

    @Mod.EventBusSubscriber
    public static class Concretes{
        public static final RegistryObject<Block> CONCRETE=REGISTER.register("concrete",()->
                new ConcreteBlock(BlockBehaviour.Properties.of(Material.STONE, DyeColor.GRAY).requiresCorrectToolForDrops().strength(5.5F,14F)));
        public static final RegistryObject<Block> SEEPING_CONCRETE=REGISTER.register("concrete/seeping",()->
                new SeepingConcreteBlock(BlockBehaviour.Properties.of(Material.STONE, DyeColor.GRAY).requiresCorrectToolForDrops().strength(5.5F,14F).randomTicks()));

        public static final RegistryObject<Block> CRACKING_CONCRETE=REGISTER.register("concrete/cracking",()->
                new CrackingConcreteBlock(BlockBehaviour.Properties.of(Material.STONE, DyeColor.GRAY).requiresCorrectToolForDrops().strength(5F,13F).randomTicks()));
        public static final RegistryObject<Block> MOSS_CRACKING_CONCRETE=REGISTER.register("concrete/moss_cracking",()->
                new MossCrackingConcreteBlock(BlockBehaviour.Properties.of(Material.STONE, DyeColor.GRAY).requiresCorrectToolForDrops().strength(5F,13F).randomTicks()));

        public static final RegistryObject<Block> CONCRETE_RUBBLE =REGISTER.register("concrete_rubble",()->
                new ConcreteRubbleBlock(BlockBehaviour.Properties.of(Material.STONE, DyeColor.GRAY).requiresCorrectToolForDrops().strength(3F,13F)));
        public static final RegistryObject<Block> MOSS_CONCRETE_RUBBLE =REGISTER.register("concrete_rubble/moss",()->
                new MossConcreteRubbleBlock(BlockBehaviour.Properties.of(Material.STONE, DyeColor.GRAY).requiresCorrectToolForDrops().strength(3.5F,13F).randomTicks()));

        public static final RegistryObject<Block> WHITE_CONCRETE =REGISTER.register("white_concrete",()->
                new BackroomsHardBlock(BlockBehaviour.Properties.of(Material.STONE, DyeColor.WHITE).requiresCorrectToolForDrops().strength(5F,15F)));

        public static final RegistryObject<Item> CONCRETE_ITEM =
                ITEM_REGISTER.register("concrete",()-> new BlockItem(CONCRETE.get(),new Item.Properties()));
        public static final RegistryObject<Item> CONCRETE_RUBBLE_ITEM =
                ITEM_REGISTER.register("concrete_rubble",()-> new BlockItem(CONCRETE_RUBBLE.get(),new Item.Properties()));
        public static final RegistryObject<Item> WHITE_CONCRETE_ITEM =
                ITEM_REGISTER.register("white_concrete",()-> new BlockItem(WHITE_CONCRETE.get(),new Item.Properties()));
    }

    public static final RegistryObject<Item> FARMLAND_DEBUG_STICK=ITEM_REGISTER.register("farmland_debug_stick",()->new Item(new Item.Properties()){
        @Override
//        @OnlyIn(Dist.CLIENT)
        public @NotNull InteractionResult useOn(@NotNull UseOnContext context) {
            BlockState state=context.getLevel().getBlockState(context.getClickedPos());
            if(state.getBlock() instanceof Farmland farmland){
                double wet= farmland.getWet(state,context.getLevel(),context.getClickedPos());
                double nutrient= farmland.nutrientFactor(state,context.getLevel(),context.getClickedPos());
                if(context.getPlayer()!=null)
                    context.getPlayer().sendMessage(new TextComponent("wet="+wet+" nutrient="+nutrient), net.minecraft.Util.NIL_UUID);
            }
            return super.useOn(context);
        }
    });

    @Mod.EventBusSubscriber
    public static class Mosses{
        public static final RegistryObject<Block> MOSS_CARPET=REGISTER.register("moss_carpet",()->
                new BrMossCarpetBlock(BlockBehaviour.Properties.of(Material.PLANT, MaterialColor.COLOR_GREEN).strength(0.1F).sound(SoundType.MOSS_CARPET).randomTicks()));

        public static final RegistryObject<Item> MOSS_CARPET_ITEM =
                ITEM_REGISTER.register("moss_carpet",()-> new BlockItem(MOSS_CARPET.get(),new Item.Properties()));

    }

    @Mod.EventBusSubscriber
    public static class Pipes{
//        @Deprecated
//        public static final RegistryObject<Block> IRON_PIPE=REGISTER.register("pipe/iron",()->
//                new ComplexPipeBlock(BlockBehaviour.Properties.of(Material.STONE, DyeColor.GRAY).requiresCorrectToolForDrops().strength(3F,10F).noOcclusion()));
        public static final RegistryObject<Block> IRON_PIPE_AXIS =REGISTER.register("pipe/iron/axis",()->
                new AxisPipeBlock(BlockBehaviour.Properties.of(Material.STONE, DyeColor.GRAY).requiresCorrectToolForDrops().strength(3F,10F).noOcclusion()));
        public static final RegistryObject<Block> IRON_PIPE_CROSS =REGISTER.register("pipe/iron/cross",()->
                new CrossPipeBlock(BlockBehaviour.Properties.of(Material.STONE, DyeColor.GRAY).requiresCorrectToolForDrops().strength(3F,10F).noOcclusion()));
        public static final RegistryObject<Block> FAKE_LIQUID =REGISTER.register("fake_liquid",()->
                new FakeLiquidBlock( BlockBehaviour.Properties.of(Material.AIR).strength(-1.0F, 3600000.8F).noDrops().noOcclusion().lightLevel(LightBlock.LIGHT_EMISSION)));

//        @Deprecated
//        public static final RegistryObject<Item> IRON_PIPE_ITEM =
//                ITEM_REGISTER.register("pipe/iron",()-> new BlockItem(IRON_PIPE.get(),new Item.Properties()));
        public static final RegistryObject<Item> IRON_PIPE_ITEM =
                ITEM_REGISTER.register("pipe/iron/axis",()-> new BlockItem(IRON_PIPE_AXIS.get(),new Item.Properties()));
        public static final RegistryObject<Item> IRON_PIPE_CROSS_ITEM =
                ITEM_REGISTER.register("pipe/iron/cross",()-> new BlockItem(IRON_PIPE_CROSS.get(),new Item.Properties()));
    }

    @Mod.EventBusSubscriber
    public static class LootItems {
        public static final RegistryObject<Item> CANNED_CARROTS =
                ITEM_REGISTER.register("loot_items/canned_carrots",()->
                        new Item(new Item.Properties().stacksTo(16).food(
                                new FoodProperties.Builder().nutrition(6).saturationMod(4).build()
                                )){
                            public int getUseDuration(@NotNull ItemStack p_41454_) {
                                return 96;
                            }
                        }
                );
    }

    @Mod.EventBusSubscriber
    public static class LootBlock{
        public static final RegistryObject<Block> CRATE=REGISTER.register("crate",()->
                new CrateBlock(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.WOOD).sound(SoundType.WOOD).requiresCorrectToolForDrops().strength(3F, 10.0F)));
    }



}
