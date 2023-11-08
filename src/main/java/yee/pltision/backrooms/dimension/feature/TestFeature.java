package yee.pltision.backrooms.dimension.feature;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.AirBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.LadderBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoorHingeSide;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import org.jetbrains.annotations.NotNull;
import yee.pltision.backrooms.block.BrBlocks;
import yee.pltision.backrooms.block.Chara;
import yee.pltision.backrooms.block.Table;
import yee.pltision.backrooms.block.lootblock.StackableShelfBlock;
import yee.pltision.backrooms.dimension.BackroomsFunction;
import yee.pltision.backrooms.dimension.densityfunctioncontext.AbsoluteYFunctionContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

import static yee.pltision.backrooms.dimension.BackroomsFunction.BASE_HEIGHT;
import static yee.pltision.backrooms.dimension.BackroomsFunction.searchType;

@SuppressWarnings("ALL")
public class TestFeature extends Feature<NoneFeatureConfiguration> {
    public TestFeature() {
        super(NoneFeatureConfiguration.CODEC);
    }

    public static Holder<PlacedFeature> PLACED_FEATURE;

    public static @NotNull Feature<?> feature(){
        //System.out.println("feature被调用");
        TestFeature feature= new TestFeature();
        Holder<ConfiguredFeature<NoneFeatureConfiguration,?>> configuredFeature=FeatureUtils.register("test_feature",feature, FeatureConfiguration.NONE);
        PLACED_FEATURE= PlacementUtils.register("test_feature",
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
        //System.out.println("run");

        if(BackroomsFunction.TEST_NOISE==null) {
            //System.out.println("null");
            return false;
        }
        BlockPos origin= context.origin();
        Random r=context.random();
        WorldGenLevel level=context.level();
        int x= origin.getX(),y= 63,z= origin.getZ();

        AbsoluteYFunctionContext functionContext=new AbsoluteYFunctionContext(x, BASE_HEIGHT,z);

        for(int i=0;i<16;i++){
            for(int j=0;j<16;j++){
                functionContext.dx=i;
                functionContext.dz=j;
                double value=BackroomsFunction.TEST_NOISE.compute(functionContext);

                if(value>1) context.level().setBlock(new BlockPos(x+i,y,z+j),Blocks.REDSTONE_BLOCK.defaultBlockState(),3);
                else if(value>0.5) context.level().setBlock(new BlockPos(x+i,y,z+j),Blocks.GOLD_BLOCK.defaultBlockState(),3);
                else if(value>-0.5) context.level().setBlock(new BlockPos(x+i,y,z+j),Blocks.STONE.defaultBlockState(),3);
                else if(value>-1) context.level().setBlock(new BlockPos(x+i,y,z+j),Blocks.DIAMOND_BLOCK.defaultBlockState(),3);
                else context.level().setBlock(new BlockPos(x+i,y,z+j),Blocks.LAPIS_BLOCK.defaultBlockState(),3);
            }
        }

        //System.out.println("yee");

        return true;
    }
}
