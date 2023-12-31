package yee.pltision.backrooms.dimension.feature.level1;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.AirBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import org.jetbrains.annotations.NotNull;
import yee.pltision.backrooms.block.BrBlocks;
import yee.pltision.backrooms.block.pipe.AxisPipe;
import yee.pltision.backrooms.block.pipe.AxisPipeBlock;
import yee.pltision.backrooms.block.pipe.CrossPipe;

import java.util.List;
import java.util.Random;

import static yee.pltision.backrooms.block.pipe.CrossPipe.getPropertyFromDirection;
import static yee.pltision.backrooms.block.pipe.Pipe.stackUpdate;

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
        //生成管道
        {
            BlockPos pos = new BlockPos(
                    rx + random.nextInt() % 3,
                    ry + random.nextInt() % 2 - 1,
                    rz + random.nextInt() % 3
            );
            int t;
            if((t=(pos.getX()&0b1111))!=0&&t!=15)
            if (level.getBlockState(pos.above()).getBlock() instanceof LiquidBlock){

                BlockState axisPipe=BrBlocks.Pipes.IRON_PIPE_AXIS.get().defaultBlockState().setValue(AxisPipe.AXIS, Direction.Axis.Y)
                        .setValue(AxisPipeBlock.POSITIVE,false).setValue(AxisPipeBlock.NEGATIVE,false);
                BlockState crossPipe=BrBlocks.Pipes.IRON_PIPE_CROSS.get().defaultBlockState();

                level.setBlock(pos,axisPipe.setValue(AxisPipeBlock.POSITIVE,true),3);
//                axisPipe.getBlock().updateShape(axisPipe.setValue(AxisPipeBlock.POSITIVE,true), Direction.UP,level.getBlockState(pos.above()),level,pos,pos.above());
                BlockPos start=pos;

                Direction from=Direction.UP;
                Direction to=Direction.DOWN;
                int r= (random.nextInt()&0b111)+32;
                pos=placePipes(r,from,to,pos.below(),true,level,axisPipe,crossPipe);


                from=to.getOpposite();
                to=Direction.getRandom(random);
                r= (random.nextInt()&0b1111)+1;
                pos=placePipes(r,from,to,pos,false,level,axisPipe,crossPipe);
                if(pos==null) {
                    stackUpdate(level,start);
                    return true;
                }

                from=to.getOpposite();
                to=Direction.getRandom(random);
                r= (random.nextInt()&0b111)+16;
                pos=placePipes(r,from,to,pos,false,level,axisPipe,crossPipe);
                if(pos==null) {
                    stackUpdate(level,start);
                    return true;
                }

                placePipes(0,to=to.getOpposite(),to,pos,false,level,axisPipe,crossPipe);
                stackUpdate(level,start);

            }
        }

        return true;
    }

    public static BlockPos placePipes(int range, Direction from, Direction to,BlockPos pos,boolean noAir, LevelAccessor level, BlockState axisPipe, BlockState crossPipe){
        axisPipe=axisPipe.setValue(AxisPipeBlock.AXIS,to.getAxis());
        if(to==from) {
            if (from.getAxisDirection() == Direction.AxisDirection.POSITIVE) {
                level.setBlock(pos.relative(from), axisPipe.setValue(AxisPipeBlock.NEGATIVE, true), 3);
            } else {
                level.setBlock(pos.relative(from), axisPipe.setValue(AxisPipeBlock.POSITIVE, true), 3);
            }
            return null;
        }
        if(to==from.getOpposite()){
            level.setBlock(pos,axisPipe,3);
        }
        else{
            level.setBlock(pos,crossPipe.setValue(getPropertyFromDirection(from),true).setValue(getPropertyFromDirection(to),true),3);
        }
        for(int i=0;i<range;i++){
            pos=pos.relative(to);
            BlockState state=level.getBlockState(pos);
            if(noAir&&state.getBlock() instanceof AirBlock) return pos;
            level.setBlock(pos,axisPipe,3);
        }
        return pos.relative(to);
    }


}
