package yee.pltision.backrooms.dimension.feature.level0;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import org.jetbrains.annotations.NotNull;
import yee.pltision.backrooms.block.BrBlocks;
import yee.pltision.backrooms.block.normal.XZLight;

import java.util.List;
import java.util.Random;

public class Level0LightFeature extends Feature<NoneFeatureConfiguration> {
    public Level0LightFeature() {
        super(NoneFeatureConfiguration.CODEC);
    }

    public static Holder<PlacedFeature> PLACED_FEATURE;
    public static @NotNull Feature<?> feature(){
        Level0LightFeature feature= new Level0LightFeature();
        Holder<ConfiguredFeature<NoneFeatureConfiguration,?>> configuredFeature=FeatureUtils.register("level0_light",feature, FeatureConfiguration.NONE);
        PLACED_FEATURE= PlacementUtils.register("level0_light",
                configuredFeature
                , List.of());
        return feature;
    }
    public static Holder<PlacedFeature> placedFeature(){
        return PLACED_FEATURE;
    }

    @Override
    public boolean place(@NotNull FeaturePlaceContext<NoneFeatureConfiguration> context) {
        Random r=context.random();
        WorldGenLevel level=context.level();
        BlockPos origin=context.origin();

        final int X=origin.getX(),Z= origin.getZ(),Y=54;


        for(int i=0;i<10;i++){
            BlockPos pos=new BlockPos(X+(r.nextInt()&0b1111),Y,Z+(r.nextInt()&0b1111));
            int wight=30;

            Direction.Axis axis;
            if(r.nextBoolean()){
                axis=Direction.Axis.X;

                if(!(level.getBlockState(pos.west()).getBlock() instanceof AirBlock)){
                    wight>>=1;
                }

                BlockState light= BrBlocks.Level0.LIGHT.get().defaultBlockState().setValue(XZLight.AXIS,axis);

                while(true){
                    if(
                            !(
                                    (level.getBlockState(pos.north()).getBlock() instanceof AirBlock)&&
                                    (level.getBlockState(pos.east()).getBlock() instanceof AirBlock)&&
                                    (level.getBlockState(pos.south()).getBlock() instanceof AirBlock)
                            )
                    ){
                        wight>>=1;
                    }
                    if((r.nextInt()&0b11111)>wight) break;
                    if(!(level.getBlockState(pos).getBlock() instanceof AirBlock))break;
                    level.setBlock(pos,light,3);
                    pos=pos.east();

                    wight-=8;
                }
            }
            else{
                axis=Direction.Axis.Z;

                if(!(level.getBlockState(pos.north()).getBlock() instanceof AirBlock)){
                    wight>>=1;
                }

                BlockState light= BrBlocks.Level0.LIGHT.get().defaultBlockState().setValue(XZLight.AXIS,axis);

                while(true){
                    if(
                            !(
                                    (level.getBlockState(pos.west()).getBlock() instanceof AirBlock)&&
                                    (level.getBlockState(pos.east()).getBlock() instanceof AirBlock)&&
                                    (level.getBlockState(pos.south()).getBlock() instanceof AirBlock)
                            )
                    ){
                        wight>>=1;
                    }
                    if((r.nextInt()&0b11111)>wight) break;
                    if(!(level.getBlockState(pos).getBlock() instanceof AirBlock))break;
                    level.setBlock(pos,light,3);
                    pos=pos.south();

                    wight-=8;
                }
            }
        }

        return true;
    }
}
