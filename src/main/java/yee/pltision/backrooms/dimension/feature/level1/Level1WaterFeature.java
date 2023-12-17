package yee.pltision.backrooms.dimension.feature.level1;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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

import java.util.List;
import java.util.Random;

public class Level1WaterFeature extends Feature<NoneFeatureConfiguration> {
    public Level1WaterFeature() {
        super(NoneFeatureConfiguration.CODEC);
    }

    public static Holder<PlacedFeature> PLACED_FEATURE;

    public static @NotNull Feature<?> feature(){
        //System.out.println("feature被调用");
        Level1WaterFeature feature= new Level1WaterFeature();
        Holder<ConfiguredFeature<NoneFeatureConfiguration,?>> configuredFeature=FeatureUtils.register("c_corridor",feature, FeatureConfiguration.NONE);
        PLACED_FEATURE= PlacementUtils.register("level1/water",
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
        Random random=context.random();
        if((random.nextInt()&0b1111)!=0) return false;

        BlockPos origin=context.origin();
        WorldGenLevel level=context.level();
        int x=origin.getX()+(random.nextInt()&0b1111),y=110+random.nextInt()%6,z=origin.getZ()+(random.nextInt()&0b1111);

        BlockState air= Blocks.AIR.defaultBlockState(),concrete=BrBlocks.Concretes.CONCRETE.get().defaultBlockState(),
                water=Blocks.WATER.defaultBlockState(),seeping=BrBlocks.Concretes.SEEPING_CONCRETE.get().defaultBlockState();

        int rx=x,ry=y,rz=z;
        for(int i=0;i<3;i++){
            for(int j=0;j<30;j++){
                BlockPos rPos=new BlockPos(
                        rx+random.nextInt()%3,
                        ry+random.nextInt()%3,
                        rz+random.nextInt()%3
                );
                level.setBlock(rPos,air,3);
            }
            for(int j=0;j<10;j++){
                BlockPos rPos=new BlockPos(
                        rx+random.nextInt()%3,
                        ry+random.nextInt()%3,
                        rz+random.nextInt()%3
                );
                level.setBlock(rPos,water,3);
                water.getBlock().updateShape(water, Direction.UP,level.getBlockState(rPos.above()),level,rPos,rPos.above());
            }
            for(int j=0;j<20;j++){
                BlockPos rPos=new BlockPos(
                        rx+random.nextInt()%4,
                        ry+random.nextInt()%4,
                        rz+random.nextInt()%4
                );
                BlockState state=level.getBlockState(rPos);
                if(state==concrete) level.setBlock(rPos,seeping,3);
                else if(state==air) {
                    level.setBlock(rPos,water,3);
                    water.getBlock().updateShape(water, Direction.UP,level.getBlockState(rPos.above()),level,rPos,rPos.above());
                }
            }
            rx+=random.nextInt()%3;
            ry+=random.nextInt()%3;
            rz+=random.nextInt()%3;
        }

        return true;
    }


}
