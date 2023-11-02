package yee.pltision.backrooms.dimension;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import yee.pltision.Util;


public interface DimensionInti {
    ResourceKey<Level> LEVEL0_KEY=ResourceKey.create(
                    Registry.DIMENSION_REGISTRY,
                    new ResourceLocation(Util.MODID,"level0")
            );

    ResourceKey<DimensionType> LEVEL0_TYPE=
            ResourceKey.create(Registry.DIMENSION_TYPE_REGISTRY,LEVEL0_KEY.getRegistryName());

    static void load(){
        System.out.println("DimensionInti类已加载");
    }
}
