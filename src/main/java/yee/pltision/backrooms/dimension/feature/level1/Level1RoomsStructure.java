package yee.pltision.backrooms.dimension.feature.level1;

import net.minecraft.core.BlockPos;
import net.minecraft.core.FrontAndTop;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.AirBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.JigsawBlock;
import net.minecraft.world.level.block.entity.JigsawBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.accesstransformer.generated.AtParser;
import org.jetbrains.annotations.NotNull;
import yee.pltision.backrooms.block.BrBlocks;
import yee.pltision.backrooms.dimension.BackroomsFunction;
import yee.pltision.backrooms.dimension.densityfunctioncontext.CFunctionContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static yee.pltision.backrooms.dimension.BackroomsFunction.COLUMN_NOISE;
import static yee.pltision.backrooms.dimension.BackroomsFunction.LEVEL1_MINABLE_TOP;
import static yee.pltision.backrooms.dimension.feature.level1.Level1SquareGenerator.fill;


public class Level1RoomsStructure extends Feature<NoneFeatureConfiguration> {
    public Level1RoomsStructure() {
        super(NoneFeatureConfiguration.CODEC);
    }

    public static Holder<PlacedFeature> PLACED_FEATURE;

    public static @NotNull Feature<?> feature(){
        //System.out.println("feature被调用");
        Level1RoomsStructure feature= new Level1RoomsStructure();
        Holder<ConfiguredFeature<NoneFeatureConfiguration,?>> configuredFeature=FeatureUtils.register("level1/rooms",feature, FeatureConfiguration.NONE);
        PLACED_FEATURE= PlacementUtils.register("level1/rooms",
                configuredFeature
                , List.of());
        return feature;
    }
    public static Holder<PlacedFeature> placedFeature(){
        return PLACED_FEATURE;
    }



    @Override
    public boolean place(@NotNull FeaturePlaceContext<NoneFeatureConfiguration> c)
    {
        BlockPos origin= c.origin();
        int x=origin.getX()+8,y= BackroomsFunction.Level1BlockFunction.MAIN_LAYOUT_START,z=c.origin().getZ()+8;

        WorldGenLevel level=c.level();

        BlockState concrete=BrBlocks.Normal.CONCRETE.get().defaultBlockState();

        for(;y<BackroomsFunction.Level1BlockFunction.MAIN_LAYOUT_END;y++){
            BlockState block=level.getBlockState(new BlockPos(x,y,z));
            if(block.getBlock() instanceof AirBlock){
                break;
            }
            if(block != concrete) return false;
        }
        if(y == BackroomsFunction.Level1BlockFunction.MAIN_LAYOUT_END)return false;

        Random random=new Random();

        if((random.nextInt()&0x1)==0) {
            if (!(level.getBlockState(new BlockPos(x - 16, y, z)).getBlock() instanceof AirBlock)) {
                System.out.println("yee");
                int i = 15;
                for (; i > 0; i--) {
                    if (level.getBlockState(new BlockPos(x - i, y, z)).getBlock() instanceof AirBlock)
                        break;
                }
                BlockPos pos=new BlockPos(x - i, y, z);
                level.setBlock(pos,Blocks.JIGSAW.defaultBlockState().setValue(JigsawBlock.ORIENTATION, FrontAndTop.WEST_UP),3);
                JigsawBlockEntity jigsaw= (JigsawBlockEntity) level.getBlockEntity(pos);
                jigsaw.setPool(new ResourceLocation("backrooms", "level1/rooms/cross"));
                jigsaw.setName(new ResourceLocation("3xdoor"));
                jigsaw.setTarget(new ResourceLocation("3xdoor"));
                try {
                    jigsaw.generate(level.getLevel(), 3, false);
                }catch (Exception e){
                    e.printStackTrace();
                }
                if(level.getBlockState(pos).getBlock()instanceof JigsawBlock)
                    level.setBlock(pos,Blocks.AIR.defaultBlockState(), 3);
                System.out.println(pos);
            }
        }

        return true;
    }

}
