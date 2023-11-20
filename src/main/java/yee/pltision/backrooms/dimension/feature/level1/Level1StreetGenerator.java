package yee.pltision.backrooms.dimension.feature.level1;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import org.jetbrains.annotations.NotNull;
import yee.pltision.backrooms.block.BrBlocks;
import yee.pltision.backrooms.block.level1.generator.Level1GeneratorDataEntity;
import yee.pltision.backrooms.dimension.densityfunctioncontext.DeviationableFunctionContext;

import java.util.List;
import java.util.Random;

import static yee.pltision.backrooms.dimension.BackroomsFunction.*;
import static yee.pltision.backrooms.dimension.feature.level1.Level1SquareGenerator.fill;

public class Level1StreetGenerator extends Feature<NoneFeatureConfiguration> {
    public Level1StreetGenerator() {
        super(NoneFeatureConfiguration.CODEC);
    }

    public static Holder<PlacedFeature> PLACED_FEATURE;

    public static @NotNull Feature<?> feature(){
        //System.out.println("feature被调用");
        Level1StreetGenerator feature= new Level1StreetGenerator();
        Holder<ConfiguredFeature<NoneFeatureConfiguration,?>> configuredFeature=FeatureUtils.register("level1/square_generator",feature, FeatureConfiguration.NONE);
        PLACED_FEATURE= PlacementUtils.register("level1/street_generator",
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
        if(STREET_NOISE==null)return false;

        BlockPos origin=context.origin();
        int x=origin.getX(),y=34,z=origin.getZ();

        WorldGenLevel level=context.level();
        BlockState air=Blocks.AIR.defaultBlockState();
        BlockState white=Blocks.WHITE_CONCRETE.defaultBlockState();

        if(isStreet(STREET_NOISE,x,z)) {
            if(isStreet(STREET_NOISE, x+16,z)&&isStreet(STREET_NOISE, x-16,z)){
                if(isStreet(STREET_NOISE, x,z+16)){
                    if(isStreet(STREET_NOISE, x,z-16)){
                        //TODO: 十字路口
                    }
                    else {
                        //TODO: z+分支三叉路口
                    }
                }
                else if (isStreet(STREET_NOISE, x,z-16)){
                    //TODO: z-分支十字路口
                }
                else{
                    //x方向道路
                    fill(level,air,x,y,z+3,x+16,y+3,z+14);

                    for(int i=0;i<16;i++){
                        if(((x+i)/5)%2==0){
                            level.setBlock(new BlockPos(x+i,y-1,z+8),
                                    white,3);
                        }
                    }
                    fill(level,white,x,y-1,z+4,x+16,y,z+5);
                    fill(level,white,x,y-1,z+12,x+16,y,z+13);
                }
            }
            else if(isStreet(STREET_NOISE, x,z+16)&&isStreet(STREET_NOISE, x,z-16)){
                if(isStreet(STREET_NOISE, x+16,z)){
                    //TODO: x+分支三叉路口
                }else if(isStreet(STREET_NOISE, x-16,z)){
                    //TODO: x-分支三叉路口
                }
                else {
                    //z方向道路
                    fill(level,air,x+3,y,z,x+14,y+3,z+16);

                    for(int i=0;i<16;i++){
                        if(((z+i)/5)%2==0){
                            level.setBlock(new BlockPos(x+8,y-1,z+i),
                                    white,3);
                        }
                    }
                    fill(level,white,x+4,y-1,z,x+5,y,z+16);
                    fill(level,white,x+12,y-1,z,x+13,y,z+16);
                }
            }
            //else 不生成道路
        }
        else return false;

        return true;
    }
    public static BlockPos getDataBlockPos(int x,int z){
        return new BlockPos(x,LEVEL1_TOP,z);
    }

    boolean isStreet(@NotNull DensityFunction function, int x, int z){
        DeviationableFunctionContext functionContext=new DeviationableFunctionContext(x,0,z);

        if(function.compute(functionContext)>0) return false;

        functionContext.dz=16;
        if(function.compute(functionContext)>0) return true;
        functionContext.dz=-16;
        if(function.compute(functionContext)>0) return true;

        functionContext.dz=0;
        functionContext.dx=16;
        if(function.compute(functionContext)>0) return true;
        functionContext.dz=16;
        if(function.compute(functionContext)>0) return true;
        functionContext.dz=-16;
        if(function.compute(functionContext)>0) return true;

        functionContext.dz=0;
        functionContext.dx=-16;
        if(function.compute(functionContext)>0) return true;
        functionContext.dz=16;
        if(function.compute(functionContext)>0) return true;
        functionContext.dz=-16;
        return function.compute(functionContext) > 0;

    }

}
