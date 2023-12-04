package yee.pltision.backrooms.dimension;


import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.mojang.datafixers.util.Pair;
import joptsimple.internal.AbbreviationMap;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.Pools;
import net.minecraft.data.worldgen.ProcessorLists;
import net.minecraft.data.worldgen.StructureFeatures;
import net.minecraft.data.worldgen.TaigaVillagePools;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.*;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.structure.BuiltinStructureSets;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.*;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;
import yee.pltision.Util;
import yee.pltision.backrooms.dimension.feature.TestFeature;
import yee.pltision.backrooms.dimension.feature.level0.*;
import yee.pltision.backrooms.dimension.feature.level1.Level1ColumnFeature;
import yee.pltision.backrooms.dimension.feature.level1.Level1LightGenerator;
import yee.pltision.backrooms.dimension.feature.level1.Level1RoomsStructure;
import yee.pltision.backrooms.dimension.feature.level1.Level1StreetGenerator;

import java.net.PortUnreachableException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;


@Mod.EventBusSubscriber
public class FeatureAppends {

    public static class FeatureGenerationData{
        public final @Nullable Set<ResourceLocation> generatableBiomes;
        public final GenerationStep.Decoration decoration;
        public final Supplier<Holder<PlacedFeature>> placedFeature;
        FeatureGenerationData(@Nullable Set<ResourceLocation> generatableBiomes,  GenerationStep.Decoration decoration, Supplier<Holder<PlacedFeature>> feature){
            this.generatableBiomes=generatableBiomes;
            this.decoration=decoration;
            this.placedFeature=feature;
        }
        FeatureGenerationData(@Nullable Set<ResourceLocation> generatableBiomes,  Supplier<Holder<PlacedFeature>> feature){
            this.generatableBiomes=generatableBiomes;
            this.placedFeature=feature;
            decoration= GenerationStep.Decoration.UNDERGROUND_STRUCTURES;
        }
    }
    public static final ArrayList<FeatureGenerationData> FEATURE_GENERATION_DATA=new ArrayList<>(4);
    public static final DeferredRegister<Feature<?>> REGISTER=DeferredRegister.create(ForgeRegistries.FEATURES,Util.MODID);
    public static final DeferredRegister<StructureFeature<?>> STRUCTURE_REGISTER=DeferredRegister.create(ForgeRegistries.STRUCTURE_FEATURES,Util.MODID);

    public static final RegistryObject<Feature<?>> LEVEL0_FEATURE=registry("level0_feature",Level0Feature::feature,
            new FeatureGenerationData(Set.of(new ResourceLocation("backrooms:level0/main")),Level0Feature::placedFeature)
    );
    public static final RegistryObject<Feature<?>> LEVEL0_LIGHT_FEATURE=registry("level0_light", Level0LightFeature::feature,
            new FeatureGenerationData(Set.of(new ResourceLocation("backrooms:level0/main")),Level0LightFeature::placedFeature)
    );
    public static final RegistryObject<Feature<?>> LEVEL0_MUSHROOM=registry("level0_mushroom", Level0MushroomFeature::feature,
            new FeatureGenerationData(Set.of(new ResourceLocation("backrooms:level0/main")), Level0MushroomFeature::placedFeature)
    );

    public static final RegistryObject<Feature<?>> LEVEL0_X_CORRIDOR_FEATURE=registry("level0_x_corridor", XCorridorFeature::feature,
            new FeatureGenerationData(Set.of(new ResourceLocation("backrooms:level0/x_corridor")),GenerationStep.Decoration.UNDERGROUND_DECORATION,XCorridorFeature::placedFeature)
    );
    public static final RegistryObject<Feature<?>> LEVEL0_Z_CORRIDOR_FEATURE=registry("level0_z_corridor",ZCorridorFeature::feature,
            new FeatureGenerationData(Set.of(new ResourceLocation("backrooms:level0/z_corridor")),GenerationStep.Decoration.UNDERGROUND_DECORATION,ZCorridorFeature::placedFeature)
    );
    public static final RegistryObject<Feature<?>> LEVEL0_C_CORRIDOR_FEATURE=registry("level0_c_corridor", CCorridorFeature::feature,
            new FeatureGenerationData(Set.of(new ResourceLocation("backrooms:level0/c_corridor")),GenerationStep.Decoration.UNDERGROUND_DECORATION,CCorridorFeature::placedFeature)
    );
    /*public static final RegistryObject<Feature<?>> LEVEL1_SQUARES=registry("level1/squares", Level1SquareGenerator::feature,
            new FeatureGenerationData(Set.of(new ResourceLocation("backrooms:level1/square")),GenerationStep.Decoration.RAW_GENERATION,Level1SquareGenerator::placedFeature)
    );*/
    public static final RegistryObject<Feature<?>> LEVEL1_COLUMN=registry("level1/columns", Level1ColumnFeature::feature,
            new FeatureGenerationData(Set.of(new ResourceLocation("backrooms:level1/square")),GenerationStep.Decoration.SURFACE_STRUCTURES,Level1ColumnFeature::placedFeature)
    );
//    public static final RegistryObject<Feature<?>> LEVEL1_STREET=registry("level1/street", Level1StreetGenerator::feature,
//            new FeatureGenerationData(Set.of(new ResourceLocation("backrooms:level1/square")),GenerationStep.Decoration.VEGETAL_DECORATION,Level1StreetGenerator::placedFeature)
//    );
    public static final RegistryObject<Feature<?>> LEVEL1_LIGHT=registry("level1/light", Level1LightGenerator::feature,
            new FeatureGenerationData(Set.of(new ResourceLocation("backrooms:level1/square")),GenerationStep.Decoration.VEGETAL_DECORATION,Level1LightGenerator::placedFeature)
    );
//    public static final RegistryObject<Feature<?>> LEVEL1_ROOMS=registry("level1/rooms", Level1RoomsStructure::feature,
//            new FeatureGenerationData(Set.of(new ResourceLocation("backrooms:level1/square")),GenerationStep.Decoration.UNDERGROUND_STRUCTURES,Level1RoomsStructure::placedFeature)
//    );

    public static final RegistryObject<Feature<?>> FUNCTION_TEST=registry("test_feature", TestFeature::feature,
            new FeatureGenerationData(Set.of(new ResourceLocation("backrooms:test")), TestFeature::placedFeature)
    );
    /*public static final Holder<StructureTemplatePool> START = Pools.register(new StructureTemplatePool(new ResourceLocation("bastion/starts"), new ResourceLocation("empty"), ImmutableList.of(Pair.of(StructurePoolElement.single("bastion/units/air_base", ProcessorLists.BASTION_GENERIC_DEGRADATION), 1), Pair.of(StructurePoolElement.single("bastion/hoglin_stable/air_base", ProcessorLists.BASTION_GENERIC_DEGRADATION), 1), Pair.of(StructurePoolElement.single("bastion/treasure/big_air_full", ProcessorLists.BASTION_GENERIC_DEGRADATION), 1), Pair.of(StructurePoolElement.single("bastion/bridge/starting_pieces/entrance_base", ProcessorLists.BASTION_GENERIC_DEGRADATION), 1)), StructureTemplatePool.Projection.RIGID));

    public static final TagKey<Biome> LEVEL1_ROOMS_BIOME=TagKey.create(Registry.BIOME_REGISTRY, new ResourceLocation("backrooms","has_structure/level1_rooms"));

    public static final RegistryObject<StructureFeature<JigsawConfiguration>> LEVEL1_ROOMS = registryStructure("level1_rooms", ()->new JigsawFeature(JigsawConfiguration.CODEC,0,true,true, (p_197185_) -> {
        return true;
    }){
        public GenerationStep.Decoration step() {
            return GenerationStep.Decoration.SURFACE_STRUCTURES;
        }
    },new JigsawConfiguration(START,6), LEVEL1_ROOMS_BIOME);

    public static <C extends FeatureConfiguration> RegistryObject<StructureFeature<C>> registryStructure(String name, Supplier<StructureFeature<C>> feature, C config,TagKey<Biome>biomes){

        return STRUCTURE_REGISTER.register(name,feature);
    }

    public static Holder<StructureProcessorList> register(String p_206438_, ImmutableList<StructureProcessor> p_206439_) {
        ResourceLocation resourcelocation = new ResourceLocation(p_206438_);
        StructureProcessorList structureprocessorlist = new StructureProcessorList(p_206439_);
        return BuiltinRegistries.register(BuiltinRegistries.PROCESSOR_LIST, resourcelocation, structureprocessorlist);
    }

    private static final ProcessorRule ADD_GILDED_BLACKSTONE = new ProcessorRule(new RandomBlockMatchTest(Blocks.BLACKSTONE, 0.01F), AlwaysTrueTest.INSTANCE, Blocks.GILDED_BLACKSTONE.defaultBlockState());

    private static final ProcessorRule REMOVE_GILDED_BLACKSTONE = new ProcessorRule(new RandomBlockMatchTest(Blocks.GILDED_BLACKSTONE, 0.5F), AlwaysTrueTest.INSTANCE, Blocks.BLACKSTONE.defaultBlockState());

    public static final Holder<StructureProcessorList> BASTION_GENERIC_DEGRADATION = register("bastion_generic_degradation", ImmutableList.of(
            new RuleProcessor(ImmutableList.of(new ProcessorRule(
                    new RandomBlockMatchTest(Blocks.POLISHED_BLACKSTONE_BRICKS, 0.3F),
                    AlwaysTrueTest.INSTANCE, Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS.defaultBlockState()),
                    new ProcessorRule(new RandomBlockMatchTest(Blocks.BLACKSTONE, 1.0E-4F), AlwaysTrueTest.INSTANCE, Blocks.AIR.defaultBlockState()),
                    new ProcessorRule(new RandomBlockMatchTest(Blocks.GOLD_BLOCK, 0.3F), AlwaysTrueTest.INSTANCE, Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS.defaultBlockState()), REMOVE_GILDED_BLACKSTONE, ADD_GILDED_BLACKSTONE))));


    private static <C extends FeatureConfiguration> RegistryObject<StructureFeature<C>> register(String p_67090_, Supplier<StructureFeature<C>> p_67091_, GenerationStep.Decoration p_67092_) {
        return STRUCTURE_REGISTER.register(p_67090_,p_67091_);
    }*/



    public static RegistryObject<Feature<?>> registry(String id, Supplier<? extends Feature<?>> builder, FeatureGenerationData data){
            //System.out.println("id"+"已被注册");
            FEATURE_GENERATION_DATA.add(data);
            return REGISTER.register(id,  builder);
    }

/*    @SubscribeEvent
    public static void setup(WorldEvent.Load event){
        //LEVEL1_ROOMS.get().configured(new JigsawConfiguration(START,6), LEVEL1_ROOMS_BIOME);
        Holder<ConfiguredStructureFeature<?,?>> configuredFeature=BuiltinRegistries.register(BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE,
                ResourceKey.create(Registry.CONFIGURED_STRUCTURE_FEATURE_REGISTRY, new ResourceLocation("backrooms","level1_rooms")),
                LEVEL1_ROOMS.get().configured(new JigsawConfiguration(START, 6), LEVEL1_ROOMS_BIOME)
        );
        Holder<StructureSet> set = BuiltinRegistries.register(
                BuiltinRegistries.STRUCTURE_SETS,
                ResourceKey.create(Registry.STRUCTURE_SET_REGISTRY, new ResourceLocation("backrooms","level1_rooms")),
                new StructureSet(configuredFeature,new RandomSpreadStructurePlacement(20, 11, RandomSpreadType.TRIANGULAR, 10387313))
        );
    }*/

    @SubscribeEvent
    static public void biomeLoading(BiomeLoadingEvent event){


        for(FeatureGenerationData data: FEATURE_GENERATION_DATA){
            if(data.generatableBiomes==null||data.generatableBiomes.contains(event.getName())){
                event.getGeneration().getFeatures(data.decoration).add(data.placedFeature.get());
                //event.getGeneration().

                //event.getGeneration().getFeatures(GenerationStep.Decoration.UNDERGROUND_ORES).add(PLACED_FEATURE);

            }
        }
    }
}
