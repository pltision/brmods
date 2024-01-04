package yee.pltision.backrooms.worldgen.feature.level0;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import org.jetbrains.annotations.NotNull;
import yee.pltision.backrooms.block.BrBlocks;
import yee.pltision.backrooms.worldgen.densityfunctioncontext.AbsoluteYFunctionContext;

import java.util.List;

import static yee.pltision.backrooms.worldgen.BackroomsFunction.BASE_HEIGHT;
import static yee.pltision.backrooms.worldgen.BackroomsFunction.LEVEL0_CORRIDOR_NOISE;

@SuppressWarnings("ALL")
public class CCorridorFeature extends Feature<NoneFeatureConfiguration> {
    public CCorridorFeature() {
        super(NoneFeatureConfiguration.CODEC);
    }

    public static Holder<PlacedFeature> PLACED_FEATURE;

    public static @NotNull Feature<?> feature(){
        //System.out.println("feature被调用");
        CCorridorFeature feature= new CCorridorFeature();
        Holder<ConfiguredFeature<NoneFeatureConfiguration,?>> configuredFeature=FeatureUtils.register("c_corridor",feature, FeatureConfiguration.NONE);
        PLACED_FEATURE= PlacementUtils.register("c_corridor",
                configuredFeature
                , List.of());
        return feature;
    }
    public static Holder<PlacedFeature> placedFeature(){
        return PLACED_FEATURE;
    }


    @Override
    public boolean place(@NotNull FeaturePlaceContext<NoneFeatureConfiguration> context)
    {
        BlockPos origin=context.origin();
        int x=origin.getX(),z=origin.getZ();

        if(LEVEL0_CORRIDOR_NOISE.compute(new AbsoluteYFunctionContext(x&0xfffffff0,BASE_HEIGHT,z&0xfffffff0))!=0.12) return false;

        WorldGenLevel level=context.level();

        BlockState air=Blocks.AIR.defaultBlockState();
        BlockState wall= BrBlocks.Level0.WALL.get().defaultBlockState();
        for(int i=0;i<8;i++){
            for(int j=1;j<=5;j++){
                for(int k=2;k<6;k++){
                    level.setBlock(new BlockPos(x+i,BASE_HEIGHT+j,8+z+k), air,3);
                    level.setBlock(new BlockPos(8+x+k,BASE_HEIGHT+j,z+i), air,3);
                }
            }
        }
        for(int i=0;i<8;i++){
            for(int j=1;j<=5;j++){
                for(int k=0;k<2;k++){
                    level.setBlock(new BlockPos(x+i,BASE_HEIGHT+j,8+z+k), wall,3);
                    level.setBlock(new BlockPos(8+x+k,BASE_HEIGHT+j,z+i), wall,3);
                }
            }
        }
        for(int i=0;i<8;i++){
            for(int j=1;j<=5;j++){
                for(int k=6;k<8;k++){
                    level.setBlock(new BlockPos(x+i,BASE_HEIGHT+j,8+z+k), wall,3);
                    level.setBlock(new BlockPos(8+x+k,BASE_HEIGHT+j,z+i), wall,3);
                }
            }
        }

        for(int i=0;i<8;i++){
            for(int j=1;j<=5;j++){
                for(int k=0;k<8;k++){
                    level.setBlock(new BlockPos(x+i,BASE_HEIGHT+j,z+k), air,3);
                }
            }
        }



        return true;
    }
}
