package yee.pltision.backrooms.dimension;


import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;
import yee.pltision.Util;
import yee.pltision.backrooms.dimension.feature.level0.Level0Feature;
import yee.pltision.backrooms.dimension.feature.level0.Level0LightFeature;
import yee.pltision.backrooms.dimension.feature.level0.Level0MushroomFeature;

import java.util.ArrayList;
import java.util.Set;
import java.util.function.Supplier;

@Mod.EventBusSubscriber
public class WorldLoadingEvents {
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
    public static final ArrayList<FeatureGenerationData> FEATURE_GENERATION_DATA=new ArrayList<>(2);
    public static final DeferredRegister<Feature<?>> REGISTER=DeferredRegister.create(ForgeRegistries.FEATURES,Util.MODID);
    public static final RegistryObject<Feature<?>> LEVEL0_FEATURE=registry("level0_feature",Level0Feature::feature,
            new FeatureGenerationData(Set.of(new ResourceLocation("backrooms:level0")),Level0Feature::placedFeature)
    );
    public static final RegistryObject<Feature<?>> LEVEL0_LIGHT_FEATURE=registry("level0_light", Level0LightFeature::feature,
            new FeatureGenerationData(Set.of(new ResourceLocation("backrooms:level0")),Level0LightFeature::placedFeature)
    );
    public static final RegistryObject<Feature<?>> LEVEL0_MUSHROOM=registry("level0_mushroom", Level0MushroomFeature::feature,
            new FeatureGenerationData(Set.of(new ResourceLocation("backrooms:level0")), Level0MushroomFeature::placedFeature)
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
            }
        }
    }
}
