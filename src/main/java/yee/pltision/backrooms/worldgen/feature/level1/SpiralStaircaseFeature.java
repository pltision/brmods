package yee.pltision.backrooms.worldgen.feature.level1;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Vec3i;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.AirBlock;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.jetbrains.annotations.NotNull;
import yee.pltision.backrooms.worldgen.BackroomsFunction;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SpiralStaircaseFeature extends Feature<NoneFeatureConfiguration> {
    public SpiralStaircaseFeature() {
        super(NoneFeatureConfiguration.CODEC);
    }

    public static Holder<PlacedFeature> PLACED_FEATURE;

    public static @NotNull Feature<?> feature(){
        //System.out.println("feature被调用");
        SpiralStaircaseFeature feature= new SpiralStaircaseFeature();
        Holder<ConfiguredFeature<NoneFeatureConfiguration,?>> configuredFeature=FeatureUtils.register("c_corridor",feature, FeatureConfiguration.NONE);
        PLACED_FEATURE= PlacementUtils.register("level1/spiral_staircase",
                configuredFeature
                , List.of());
        return feature;
    }
    public static Holder<PlacedFeature> placedFeature(){
        return PLACED_FEATURE;
    }

    public static final ArrayList<ResourceLocation> STARICASES =new ArrayList<>(List.of(
            new ResourceLocation("backrooms:level1/spiral_staircase/b/a")
    ));

    @Override
    public boolean place(@NotNull FeaturePlaceContext<NoneFeatureConfiguration> context)
    {
        BlockPos origin=context.origin();
        int x=origin.getX(),z=origin.getZ();

        Random random=context.random();
        WorldGenLevel level=context.level();



        if(((x&0b111_0000)|(z&0b111_0000)|(random.nextInt()&0b1111))!=0) return false;

        long buttonAndTop= getButtonAndTop(level,x,z);
        if(buttonAndTop==0x8000_0000_8000_0000L)return false;

        System.out.println(new BlockPos(x,0,z));

        StructureTemplate structure= context.level().getLevel().getServer().getStructureManager().getOrCreate(
                STARICASES.get((random.nextInt()&Integer.MAX_VALUE)%STARICASES.size())
        );
        Vec3i structureSize =structure.getSize();
        int dx=structureSize.getX()>>2,dz=structureSize.getZ()>>2,height=structureSize.getY();

        int placing=(int)buttonAndTop,top= (int) (buttonAndTop>>32);

        StructurePlaceSettings structureplacesettings = new StructurePlaceSettings();
        for(;placing<top;placing+=height){
            if((random.nextInt()&0b111)==0)break;
            BlockPos placePos=new BlockPos(x-dx,placing,z-dz);
            structure.placeInWorld(level,placePos,placePos,structureplacesettings,random,18);

        }

        return true;
    }

    /**
     * @return 低32位是底, 高32位是顶, 如果返回0x8000_0000_8000_0000L则无空隙
     */
    long getButtonAndTop(LevelReader level,int x,int z){
        int button= BackroomsFunction.Level1BlockFunction.MAIN_LAYOUT_START;

        for(;button<BackroomsFunction.Level1BlockFunction.MAIN_LAYOUT_END;button++){
            if(level.getBlockState(new BlockPos(x,button,z)).getBlock()instanceof AirBlock) break;
        }
        if(button==BackroomsFunction.Level1BlockFunction.MAIN_LAYOUT_END)return 0x8000_0000_8000_0000L;

        int top=BackroomsFunction.Level1BlockFunction.MAIN_LAYOUT_END;
        for(;top<button;top++){
            if(level.getBlockState(new BlockPos(x,top,z)).getBlock()instanceof AirBlock) break;
        }

        return ((long) top <<32)|button;
    }
}
