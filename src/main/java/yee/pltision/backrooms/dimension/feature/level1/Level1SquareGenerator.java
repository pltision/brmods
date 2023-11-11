package yee.pltision.backrooms.dimension.feature.level1;

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
import yee.pltision.backrooms.block.level1.generator.Level1GeneratorDataEntity;

import java.util.List;
import java.util.Random;

import static yee.pltision.backrooms.dimension.BackroomsFunction.*;

@SuppressWarnings("ALL")
public class Level1SquareGenerator extends Feature<NoneFeatureConfiguration> {
    public Level1SquareGenerator() {
        super(NoneFeatureConfiguration.CODEC);
    }

    public static Holder<PlacedFeature> PLACED_FEATURE;

    public static @NotNull Feature<?> feature(){
        //System.out.println("feature被调用");
        Level1SquareGenerator feature= new Level1SquareGenerator();
        Holder<ConfiguredFeature<NoneFeatureConfiguration,?>> configuredFeature=FeatureUtils.register("level1/square_generator",feature, FeatureConfiguration.NONE);
        PLACED_FEATURE= PlacementUtils.register("level1/square_generator",
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

        BlockPos dataPos=getDataBlockPos(x,z);

        WorldGenLevel level=context.level();

        BlockState dataBlock=level.getBlockState(dataPos);

        Random random=context.random();

        Level1GeneratorDataEntity north,south,west,east;
        {
            if (dataBlock == BrBlocks.Level1.GENERATED_BLOCK.get().defaultBlockState()) return false;
            if (dataBlock != BrBlocks.Level1.GENERATOR_DATA_BLOCK.get().defaultBlockState()) {
                level.setBlock(dataPos, BrBlocks.Level1.GENERATOR_DATA_BLOCK.get().defaultBlockState(), 3);

                Level1GeneratorDataEntity entity = (Level1GeneratorDataEntity) level.getBlockEntity(dataPos);

            }

            BlockState placeing;
            BlockPos placeingPos;

            placeingPos=dataPos.north(16);
            placeing=level.getBlockState(placeingPos);
            if (placeing != BrBlocks.Level1.GENERATED_BLOCK.get().defaultBlockState()&&
                    placeing != BrBlocks.Level1.GENERATOR_DATA_BLOCK.get().defaultBlockState()){
                level.setBlock(placeingPos, BrBlocks.Level1.GENERATOR_DATA_BLOCK.get().defaultBlockState(), 3);
            }
            north = (Level1GeneratorDataEntity) level.getBlockEntity(placeingPos);

            placeingPos=dataPos.south(16);
            placeing=level.getBlockState(placeingPos);
            if (placeing != BrBlocks.Level1.GENERATED_BLOCK.get().defaultBlockState()&&
                    placeing != BrBlocks.Level1.GENERATOR_DATA_BLOCK.get().defaultBlockState()){
                level.setBlock(placeingPos, BrBlocks.Level1.GENERATOR_DATA_BLOCK.get().defaultBlockState(), 3);
            }
            south = (Level1GeneratorDataEntity) level.getBlockEntity(placeingPos);

            placeingPos=dataPos.east(16);
            placeing=level.getBlockState(placeingPos);
            if (placeing != BrBlocks.Level1.GENERATED_BLOCK.get().defaultBlockState()&&
                    placeing != BrBlocks.Level1.GENERATOR_DATA_BLOCK.get().defaultBlockState()){
                level.setBlock(placeingPos, BrBlocks.Level1.GENERATOR_DATA_BLOCK.get().defaultBlockState(), 3);
            }
            west = (Level1GeneratorDataEntity) level.getBlockEntity(placeingPos);

            placeingPos=dataPos.west(16);
            placeing=level.getBlockState(placeingPos);
            if (placeing != BrBlocks.Level1.GENERATED_BLOCK.get().defaultBlockState()&&
                    placeing != BrBlocks.Level1.GENERATOR_DATA_BLOCK.get().defaultBlockState()){
                level.setBlock(placeingPos, BrBlocks.Level1.GENERATOR_DATA_BLOCK.get().defaultBlockState(), 3);
            }
            east = (Level1GeneratorDataEntity) level.getBlockEntity(placeingPos);
        }

        Level1GeneratorDataEntity entity= (Level1GeneratorDataEntity) level.getBlockEntity(dataPos);

        if(entity.mainLayout.enable==false){
            if ((random.nextInt() & 0b1111) != 0) {
                entity.mainLayout.enable(BASE_HEIGHT + random.nextInt() % 4, 3 + random.nextInt() & 0b11);
            }
        }

        if(entity.mainLayout.enable){
            final int fromX,fromZ,toX,toZ;
            if(entity.mainLayout.north==-1){
                if(north!=null) north.mainLayout.copy(entity.mainLayout);
                toZ=z+16;
            }
            else{
                if(north!=null) north.mainLayout.enable=false;
                toZ=z+16-entity.mainLayout.north;
            }
            if(entity.mainLayout.east==-1){
                if(east!=null) east.mainLayout.copy(entity.mainLayout);
                toX=16;
            }
            else{
                if(east!=null) east.mainLayout.enable=false;
                toX=16-entity.mainLayout.east;
            }

            if(entity.mainLayout.south==-1){
                if(south!=null) south.mainLayout.copy(entity.mainLayout);
                fromZ=z;
            }
            else{
                if(south!=null) south.mainLayout.enable=false;
                fromZ=z+entity.mainLayout.south;
            }
            if(entity.mainLayout.west==-1){
                if(west!=null) west.mainLayout.copy(entity.mainLayout);
                fromX=x;
            }
            else{
                if(west!=null) west.mainLayout.enable=false;
                fromX=x+entity.mainLayout.west;
            }
            fill(level,fromX,entity.mainLayout.ground,fromZ,toX,entity.mainLayout.ground+entity.mainLayout.height,toZ);
        }

        return true;
    }
    public BlockPos getDataBlockPos(int x,int z){
        return new BlockPos(x,LEVEL1_HEIGHT,z);
    }

    public static void fill(WorldGenLevel level,int x,int y,int z,final int toX,final int toY,final int toZ){
        BlockState air= Blocks.AIR.defaultBlockState();
        for(;x<toX;x++){
            for(;z<toZ;z++){
                for(;y<toY;y++){
                    level.setBlock(new BlockPos(x,y,z),air,3);
                }
            }
        }
    }
}
