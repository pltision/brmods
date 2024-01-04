package yee.pltision.backrooms.worldgen;


import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.*;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;
import yee.pltision.Util;
import yee.pltision.backrooms.worldgen.feature.TestFeature;
import yee.pltision.backrooms.worldgen.feature.level0.*;
import yee.pltision.backrooms.worldgen.feature.level1.*;

import java.util.ArrayList;
import java.util.Set;
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
            new FeatureGenerationData(Set.of(new ResourceLocation("backrooms:level0/main")),GenerationStep.Decoration.UNDERGROUND_ORES,Level0LightFeature::placedFeature)
    );
    public static final RegistryObject<Feature<?>> LEVEL0_MUSHROOM=registry("level0_mushroom", Level0MushroomFeature::feature,
            new FeatureGenerationData(Set.of(new ResourceLocation("backrooms:level0/main")), Level0MushroomFeature::placedFeature)
    );
    public static final RegistryObject<Feature<?>> LEVEL0_TABLE_ROOM=registry("level0/table_room",Level0TableRoom::feature,
            new FeatureGenerationData(Set.of(new ResourceLocation("backrooms:level0/main")),GenerationStep.Decoration.SURFACE_STRUCTURES,Level0TableRoom::placedFeature)
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
            new FeatureGenerationData(Set.of(new ResourceLocation("backrooms:level1/square"),new ResourceLocation("backrooms:level1/derelict")),GenerationStep.Decoration.SURFACE_STRUCTURES,Level1ColumnFeature::placedFeature)
    );
//    public static final RegistryObject<Feature<?>> LEVEL1_SPIRAL_STAIRCASE=registry("level1/spiral_staircase", SpiralStaircaseFeature::feature,
//            new FeatureGenerationData(Set.of(new ResourceLocation("backrooms:level1/spiral_staircase")),GenerationStep.Decoration.LAKES,SpiralStaircaseFeature::placedFeature)
//    );
//    public static final RegistryObject<Feature<?>> LEVEL1_STREET=registry("level1/street", Level1StreetGenerator::feature,
//            new FeatureGenerationData(Set.of(new ResourceLocation("backrooms:level1/square")),GenerationStep.Decoration.VEGETAL_DECORATION,Level1StreetGenerator::placedFeature)
//    );
    public static final RegistryObject<Feature<?>> LEVEL1_LIGHT=registry("level1/light", Level1LightGenerator::feature,
            new FeatureGenerationData(Set.of(new ResourceLocation("backrooms:level1/square")),GenerationStep.Decoration.VEGETAL_DECORATION,Level1LightGenerator::placedFeature)
    );
//    public static final RegistryObject<Feature<?>> LEVEL1_ROOMS=registry("level1/rooms", Level1RoomsStructure::feature,
//            new FeatureGenerationData(Set.of(new ResourceLocation("backrooms:level1/square")),GenerationStep.Decoration.UNDERGROUND_STRUCTURES,Level1RoomsStructure::placedFeature)
//    );

    public static final RegistryObject<Feature<?>> LEVEL1_WATER=registry("level1/water", Level1WaterFeature::feature,
            new FeatureGenerationData(Set.of(new ResourceLocation("backrooms:level1/square"),new ResourceLocation("backrooms:level1/derelict")),GenerationStep.Decoration.LAKES, Level1WaterFeature::placedFeature)
    );
    public static final RegistryObject<Feature<?>> LEVEL1_CRATE=registry("level1/crate", Level1CrateFeature::feature,
            new FeatureGenerationData(Set.of(new ResourceLocation("backrooms:level1/square")),GenerationStep.Decoration.TOP_LAYER_MODIFICATION, Level1CrateFeature::placedFeature)
    );

    public static final RegistryObject<Feature<?>> FUNCTION_TEST=registry("test_feature", TestFeature::feature,
            new FeatureGenerationData(Set.of(new ResourceLocation("backrooms:test")), TestFeature::placedFeature)
    );


    public static RegistryObject<Feature<?>> registry(String id, Supplier<? extends Feature<?>> builder, FeatureGenerationData data){
            //System.out.println("id"+"已被注册");
            FEATURE_GENERATION_DATA.add(data);
            return REGISTER.register(id,  builder);
    }

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
