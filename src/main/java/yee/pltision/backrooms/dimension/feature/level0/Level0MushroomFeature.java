package yee.pltision.backrooms.dimension.feature.level0;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import org.jetbrains.annotations.NotNull;
import yee.pltision.backrooms.block.mushroom.BrMyceliumBlock;
import yee.pltision.backrooms.dimension.BackroomsFunction;
import yee.pltision.backrooms.BrMushrooms;
import yee.pltision.mushroom.MushroomData;

import java.util.List;
import java.util.Random;

public class Level0MushroomFeature extends Feature<NoneFeatureConfiguration>{
    public Level0MushroomFeature() {
        super(NoneFeatureConfiguration.CODEC);
    }

    int[] PLACE_TYPE={BrMushrooms.RED_MUSHROOM_HEAD, BrMushrooms.BROWN_MUSHROOM_HEAD};

    public static Holder<PlacedFeature> PLACED_FEATURE;
    public static @NotNull Feature<?> feature(){
        Level0MushroomFeature feature= new Level0MushroomFeature();
        Holder<ConfiguredFeature<NoneFeatureConfiguration,?>> configuredFeature= FeatureUtils.register("level0_mushroom",feature, FeatureConfiguration.NONE);
        PLACED_FEATURE= PlacementUtils.register("level0_mushroom",
                configuredFeature
                , List.of());
        return feature;
    }
    public static Holder<PlacedFeature> placedFeature(){
        return PLACED_FEATURE;
    }

    @Override
    public boolean place(@NotNull FeaturePlaceContext<NoneFeatureConfiguration> context) {
        Random random=context.level().getRandom();
        BlockPos origin=context.origin();
        final int placeHeight= BackroomsFunction.BASE_HEIGHT-2;

        int x=origin.getX()+(random.nextInt()&0b1111), z=origin.getZ()+(random.nextInt()&0b1111);
        WorldGenLevel level=context.level();

        if((random.nextInt()&0b1111)==0){
            long data=( (long) PLACE_TYPE[(random.nextInt()&0x7fffffff)%PLACE_TYPE.length] << (64- MushroomData.TYPE_LENGTH)) |
                    (random.nextLong()>>>MushroomData.TYPE_LENGTH);

            for(int i=random.nextInt()&0b11+1;i>=0;i--){
                BlockPos place=new BlockPos(x+random.nextInt()%3,placeHeight+random.nextInt()%2,z+random.nextInt()%3);
                BrMyceliumBlock.place( level,place,null,null,data);
                BrMyceliumBlock.group(level,place,null,data,1,random);
                //System.out.println(place);
            }
        }
        return true;
    }
}
