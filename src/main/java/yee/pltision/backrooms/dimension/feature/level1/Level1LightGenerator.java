package yee.pltision.backrooms.dimension.feature.level1;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.AirBlock;
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
import org.jetbrains.annotations.Nullable;
import yee.pltision.backrooms.block.BrBlocks;
import yee.pltision.backrooms.block.level1.WallLight;
import yee.pltision.backrooms.dimension.densityfunctioncontext.CFunctionContext;
import yee.pltision.backrooms.dimension.densityfunctioncontext.DeviationableFunctionContext;

import java.util.LinkedList;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Supplier;

import static java.lang.Math.min;
import static yee.pltision.backrooms.dimension.BackroomsFunction.*;
import static yee.pltision.backrooms.dimension.feature.level1.Level1SquareGenerator.fill;

public class Level1LightGenerator extends Feature<NoneFeatureConfiguration> {
    public Level1LightGenerator() {
        super(NoneFeatureConfiguration.CODEC);
    }

    public static Holder<PlacedFeature> PLACED_FEATURE;

    public static @NotNull Feature<?> feature(){
        //System.out.println("feature被调用");
        Level1LightGenerator feature= new Level1LightGenerator();
        Holder<ConfiguredFeature<NoneFeatureConfiguration,?>> configuredFeature=FeatureUtils.register("level1/light_generator",feature, FeatureConfiguration.NONE);
        PLACED_FEATURE= PlacementUtils.register("level1/light_generator",
                configuredFeature
                , List.of());
        return feature;
    }
    public static Holder<PlacedFeature> placedFeature(){
        return PLACED_FEATURE;
    }

    public static Supplier<BlockState> wallLightBlock= ()->BrBlocks.Normal.WALL_LIGHT.get().defaultBlockState();

//    public static @Nullable BlockState getLight(BlockState lightDefault, LevelReader level, BlockPos pos){
//        if(!(level.getBlockState(pos).getBlock() instanceof AirBlock)) return null;
//        if()
//    }

    @Override
    public boolean place(@NotNull FeaturePlaceContext<NoneFeatureConfiguration> context)
    {
        if(MAIN_LAYOUT_LIGHT==null)return false;

        BlockPos origin=context.origin();
        int x=origin.getX(),z=origin.getZ();
        boolean xLineLight=MAIN_LAYOUT_LIGHT.compute(new CFunctionContext(0,0,z))>0;
        boolean zLineLight=MAIN_LAYOUT_LIGHT.compute(new CFunctionContext(x,0,0))>0;

        WorldGenLevel level=context.level();
        int[] genY= getY(level,x+8,z+8);

        BlockState lightDefault=wallLightBlock.get();

        for(int y:genY) {
            //xz轴探测4格是否为空气, 是空气就选择生成一个方向的灯并跳出循环
            //if (!level.getBlockState(new BlockPos(x, z, y)).getMaterial().isSolid()) continue;
//            System.out.println(new BlockPos(x,y,z));
            if(xLineLight){
                for (int i = 1; i < 5; i++) {
                    BlockPos pos=new BlockPos(x, y,z+i);
                    BlockState state=level.getBlockState(pos);
                    if (state.getBlock()instanceof AirBlock){
                        if(level.getBlockState(new BlockPos(x, y,z+i-1)).getMaterial().isSolid()){
                            level.setBlock(pos,lightDefault.setValue(WallLight.FACING, Direction.SOUTH),3);
                        }
                    }
                }
                for (int i = 1; i < 5; i++) {
                    BlockPos pos=new BlockPos(x, y,z-i);
                    BlockState state=level.getBlockState(pos);
                    if (state.getBlock()instanceof AirBlock){
                        if(level.getBlockState(new BlockPos(x, y,z-i+1)).getMaterial().isSolid()){
                            level.setBlock(pos,lightDefault.setValue(WallLight.FACING, Direction.NORTH),3);
                        }
                    }
                }
            }
            if(zLineLight){
                for (int i = 1; i < 5; i++) {
                    BlockPos pos=new BlockPos(x+i,y, z);
                    BlockState state=level.getBlockState(pos);
                    if (state.getBlock()instanceof AirBlock){
                        if(level.getBlockState(new BlockPos(x+i-1, y,z)).getMaterial().isSolid()){
                            level.setBlock(pos,lightDefault.setValue(WallLight.FACING, Direction.EAST),3);
                        }
                    }
                }
                for (int i = 1; i < 5; i++) {
                    BlockPos pos=new BlockPos(x-i,y, z);
                    BlockState state=level.getBlockState(pos);
                    if (state.getBlock()instanceof AirBlock){
                        if(level.getBlockState(new BlockPos(x-i+1, y,z)).getMaterial().isSolid()){
                            level.setBlock(pos,lightDefault.setValue(WallLight.FACING, Direction.WEST),3);
                        }
                    }
                }
            }
        }

        return false;
    }


    public static int[] getY(LevelReader reader,int x,int z){
        LinkedList<Integer> list=new LinkedList<>();

        int sum=0;
        for(int i=Level1BlockFunction.MAIN_LAYOUT_START;i<Level1BlockFunction.MAIN_LAYOUT_END;i++){
            if(reader.getBlockState(new BlockPos(x,i,z)).getBlock() instanceof AirBlock){
                sum++;
            }
            else if(sum!=0){
                 list.add(i - (sum-Math.min(sum-2,3)));
                 sum=0;
            }
        }
        int[] genY=new int[list.size()];
        int i=0;
        for(Integer num:list){
            genY[i]=num;
            i++;
        }
        return genY;
    }
///execute in backrooms:level1 run tp @s 245.05 56.20 1275.88 -86.22 37.50
}
