package yee.pltision.backrooms.worldgen.feature.level0;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import org.jetbrains.annotations.NotNull;
import yee.pltision.backrooms.Backrooms;
import yee.pltision.backrooms.block.BrBlocks;
import yee.pltision.backrooms.block.Chara;
import yee.pltision.backrooms.block.Table;
import yee.pltision.backrooms.worldgen.densityfunctioncontext.CFunctionContext;
import yee.pltision.backrooms.worldgen.feature.level1.Level1ColumnFeature;

import java.util.List;
import java.util.Random;

import static yee.pltision.backrooms.worldgen.BackroomsFunction.LEVEL0_GRAPH;

public class Level0TableRoom extends Feature<NoneFeatureConfiguration>{
    public Level0TableRoom() {
        super(NoneFeatureConfiguration.CODEC);
    }

    public static Holder<PlacedFeature> PLACED_FEATURE;
    public static @NotNull Feature<?> feature(){
        Level0TableRoom feature= new Level0TableRoom();
        Holder<ConfiguredFeature<NoneFeatureConfiguration,?>> configuredFeature= FeatureUtils.register("level0/table_room",feature, FeatureConfiguration.NONE);
        PLACED_FEATURE= PlacementUtils.register("level0/table_room",
                configuredFeature
                , List.of());
        return feature;
    }
    public static Holder<PlacedFeature> placedFeature(){
        return PLACED_FEATURE;
    }

    @Override
    public boolean place(@NotNull FeaturePlaceContext<NoneFeatureConfiguration> context) {
        BlockPos origin=context.origin();
        Random r=context.random();

        if(((origin.getX()&0b1_0000)|(origin.getZ()&0b1_0000)|r.nextInt(4))!=0) return false;

        if(LEVEL0_GRAPH==null)return false;

//        System.out.println(origin);

        WorldGenLevel level=context.level();

        int x=origin.getX()+ r.nextInt(16),z= origin.getZ()+ r.nextInt(16);
        int topY=54,floorY=50;

        int[] wall=new int[4];

        wall[0]=wall[2]=r.nextInt(12,20);
        wall[1]=wall[3]=r.nextInt(12,20);

        Direction dir=Direction.EAST;
        BlockPos it=new BlockPos(x-r.nextInt(4,8),floorY,z-r.nextInt(4,8));

        BlockState AIR= Blocks.AIR.defaultBlockState(),WALL= BrBlocks.Level0.WALL.get().defaultBlockState(),
                CHARA=BrBlocks.WOODEN_CHARA.get().defaultBlockState(),TABLE=BrBlocks.WOODEN_TABLE.get().defaultBlockState();

        Level1ColumnFeature.fill(level,AIR,it.getX()+1,floorY,it.getZ(),it.getX()+wall[0],topY+1,it.getZ()+wall[1]);

        boolean placeChara=r.nextBoolean();
        boolean placeWall=r.nextBoolean(),touchedWall=true;

        CFunctionContext functionContext=new CFunctionContext();

        //放置围墙
        for(int i=0;i<4;i++){
            for(int j=0,t=wall[i];j<t;j++){
                functionContext.setPos(it);
                if(LEVEL0_GRAPH.compute(functionContext)==0xff){
                    touchedWall=false;
                    if(placeWall){
                        Level1ColumnFeature.fill(level,WALL,it.getX(),floorY,it.getZ(),it.getX()+1,topY+1,it.getZ()+1);
                        if(placeChara){
                            if(r.nextInt(5)==0)
                                level.setBlock(it.relative(dir.getClockWise()),CHARA.setValue(Chara.FACING,dir.getClockWise()),3);
                        }
                        BlockState update=level.getBlockState(it.relative(dir.getCounterClockWise()));
                        update.getBlock().updateShape(update,dir.getClockWise(),WALL,level,it.relative(dir.getCounterClockWise()),it);
                    }
                }
                else{
                    if(!touchedWall){
                        touchedWall=true;
                        placeWall=r.nextBoolean();
                    }
                }
                it=it.relative(dir);
            }
            dir=dir.getClockWise();
        }

        if(r.nextBoolean()){    //放置桌子
            for(int i=-1;i<2;i++)
                for(int j=-1;j<2;j++)
                    level.setBlock(new BlockPos(x+i,floorY,z+j),TABLE,3);
            for(int i=-1;i<2;i++)
                for(int j=-1;j<2;j++){
                    BlockPos pos=new BlockPos(x+i,floorY,z+j);
                    level.setBlock(pos,Table.getState(level,pos),3);
                }

            it=new BlockPos(x-2,floorY,z-2);
            for(int i=0;i<4;i++){
                CHARA= CHARA.setValue(Chara.FACING,dir.getClockWise());
                for(int j=0;j<3;j++){
                    it=it.relative(dir);
                    if(r.nextInt(3)==0) {
                        level.setBlock(it,CHARA,3);
                    }
                }
                it=it.relative(dir);
                dir=dir.getClockWise();
            }
        }

        return true;
    }
}
