package yee.pltision.backrooms.worldgen.feature.level1;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import org.jetbrains.annotations.NotNull;
import yee.pltision.backrooms.block.BrBlocks;
import yee.pltision.backrooms.worldgen.densityfunctioncontext.CFunctionContext;

import java.util.ArrayList;
import java.util.List;

import static yee.pltision.backrooms.worldgen.BackroomsFunction.*;


public class Level1ColumnFeature extends Feature<NoneFeatureConfiguration> {
    public Level1ColumnFeature() {
        super(NoneFeatureConfiguration.CODEC);
    }

    public static Holder<PlacedFeature> PLACED_FEATURE;

    public static @NotNull Feature<?> feature(){
        //System.out.println("feature被调用");
        Level1ColumnFeature feature= new Level1ColumnFeature();
        Holder<ConfiguredFeature<NoneFeatureConfiguration,?>> configuredFeature=FeatureUtils.register("level1/columns",feature, FeatureConfiguration.NONE);
        PLACED_FEATURE= PlacementUtils.register("level1/columns",
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
        if(COLUMN_NOISE==null) return false;

        BlockPos origin=context.origin();
        int x=origin.getX(),z=origin.getZ();

        COLUMN.get(((int)(COLUMN_NOISE.compute(new CFunctionContext(x,0,z))*2)&Integer.MAX_VALUE)% Level1ColumnFeature.COLUMN.size()).fill(context.level(),x,z);

        return true;
    }

    @FunctionalInterface
    public interface Filler{
        void fill(WorldGenLevel level,int x,int z);
    }

    public static ArrayList<Filler> COLUMN=new ArrayList<>(List.of(
            (level,x,z)-> fill(level,BrBlocks.Concretes.CONCRETE.get().defaultBlockState(), x,LEVEL1_MINABLE_BOTTOM,z,x+2,LEVEL1_MINABLE_TOP,z+2),
            (level,x,z)-> {
                fill(level,BrBlocks.Concretes.CONCRETE.get().defaultBlockState(), x-1,LEVEL1_MINABLE_BOTTOM,z,x+3,LEVEL1_MINABLE_TOP,z+2);
                fill(level,BrBlocks.Concretes.CONCRETE.get().defaultBlockState(), x,LEVEL1_MINABLE_BOTTOM,z-1,x+2,LEVEL1_MINABLE_TOP,z);
                fill(level,BrBlocks.Concretes.CONCRETE.get().defaultBlockState(), x,LEVEL1_MINABLE_BOTTOM,z+2,x+2,LEVEL1_MINABLE_TOP,z+3);
            },
            (level,x,z)-> fill(level,BrBlocks.Concretes.CONCRETE.get().defaultBlockState(), x-1,LEVEL1_MINABLE_BOTTOM,z-1,x+2,LEVEL1_MINABLE_TOP,z+2),
            (level,x,z)-> fill(level,BrBlocks.Concretes.CONCRETE.get().defaultBlockState(), x-1,LEVEL1_MINABLE_BOTTOM,z-1,x+3,LEVEL1_MINABLE_TOP,z+3)

    ));

    public static void fill(WorldGenLevel level, final BlockState state, final int x, final int y, final int z, final int toX, final int toY, final int toZ){
        //System.out.println(x+" "+y+" "+z);
        for(int i=x;i<toX;i++){
            for(int j=z;j<toZ;j++){
                for(int k=y;k<toY;k++){
                    level.setBlock(new BlockPos(i,k,j),state,3);
                }
            }
        }
    }
}
