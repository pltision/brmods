package yee.pltision.backrooms.worldgen.feature.level1;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.AirBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import org.jetbrains.annotations.NotNull;
import yee.pltision.backrooms.block.BrBlocks;
import yee.pltision.backrooms.block.crate.CrateBlockEntity;
import yee.pltision.backrooms.worldgen.BackroomsFunction;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Level1CrateFeature extends Feature<NoneFeatureConfiguration> {
    public Level1CrateFeature() {
        super(NoneFeatureConfiguration.CODEC);
    }

    public static Holder<PlacedFeature> PLACED_FEATURE;

    public static @NotNull Feature<?> feature(){
        Level1CrateFeature feature= new Level1CrateFeature();
        Holder<ConfiguredFeature<NoneFeatureConfiguration,?>> configuredFeature=FeatureUtils.register("level1/crate_feature",feature, FeatureConfiguration.NONE);
        PLACED_FEATURE= PlacementUtils.register("level1/crate_feature",
                configuredFeature
                , List.of());
        return feature;
    }
    public static Holder<PlacedFeature> placedFeature(){
        return PLACED_FEATURE;
    }

    public static ArrayList<ResourceLocation> LOOT_TABLES=new ArrayList<>();

    @Override
    public boolean place(@NotNull FeaturePlaceContext<NoneFeatureConfiguration> context)
    {
        int x=context.origin().getX();
        int z=context.origin().getZ();
        Random random=context.random();
        LevelAccessor level=context.level();

        for(int i=0;i<5;i++){
            tryAt(level,
                    new BlockPos(
                            (x+(random.nextInt()&0b1111)),
                            random.nextInt(0,BackroomsFunction.Level1BlockFunction.MAIN_LAYOUT_START-10),
                            (z+(random.nextInt()&0b1111))
                    ),
            random);
        }
        tryAt(level,
                new BlockPos(
                        (x+(random.nextInt()&0b1111)),
                        random.nextInt(BackroomsFunction.Level1BlockFunction.MAIN_LAYOUT_START,BackroomsFunction.Level1BlockFunction.MAIN_LAYOUT_END),
                        (z+(random.nextInt()&0b1111))
                ),
                random);

        return true;
    }

    public static void tryAt(LevelAccessor level, BlockPos pos,Random random){
        if(level.getBlockState(pos).getMaterial().isSolid()){
            for(int i=0;i<10;i++) {
                pos = pos.above();
                if(pos.getY()>BackroomsFunction.LEVEL1_MINABLE_TOP) return;
                if (!level.getBlockState(pos).getMaterial().isSolid()) {
                    break;
                }
            }
        }
        else{
            pos=pos.below();
            for(int i=0;i<10;i++){
                if(pos.getY()<BackroomsFunction.LEVEL1_MINABLE_BOTTOM) return;
                if(level.getBlockState(pos).getMaterial().isSolid()){
                    break;
                }
                pos=pos.below();
            }
        }
//        System.out.println(pos);
        if(level.getBlockState(pos).getBlock()instanceof AirBlock){ //非空气直接生成失败
            double weight=random.nextDouble(0,10)*getPosWeight(level,pos);
            BlockState crate= BrBlocks.LootBlock.CRATE.get().defaultBlockState();
            while(weight>1){
                weight/=2;
                CrateConfig config=new CrateConfig(LOOT_TABLES.size()==0?
                                        new ResourceLocation("backrooms:chest/crate"):
                                        LOOT_TABLES.get(random.nextInt(LOOT_TABLES.size())), crate);
                int fx=pos.getX()+ random.nextInt()%3,fz=pos.getZ()+ random.nextInt()%3;
                int xs=random.nextInt(1,Math.max(3,(int)weight)),zs=random.nextInt(1,Math.max(3,(int)weight));
                int height=Math.max((random.nextInt()&3)+1-((xs*zs)/10),1);
                if((xs*zs)>6) height=Math.max(2,height);
                fill(pos.getY(),fx-(xs>>1),fz-(zs>>1),fx+xs,fz+zs,height,level,config,random);
            }
        }
    }

    public static void fill(int y,int fx,int fz,int tx,int tz,int height,LevelAccessor level,CrateConfig config,Random random){
        for(int x=fx;x<tx;x++){
            for(int z=fz;z<tz;z++){
                BlockPos pos=new BlockPos(x,y,z);
                if(!level.getBlockState(pos.below()).getMaterial().isSolid()){
                    if(level.getBlockState(pos).getBlock()instanceof AirBlock){
                        pos=pos.below();
                        if (!level.getBlockState(pos.below()).getMaterial().isSolid())
                            continue;
                    }
                    else continue;
                }
                for(int i=0;i<height;i++){
                    if(level.getBlockState(pos).getBlock()instanceof AirBlock)
                        placeCrate(config,random,level,pos);
                    else break;
                    pos= pos.above();
                }

            }
        }
    }
    public static void placeCrate(CrateConfig config,Random random,LevelAccessor level,BlockPos pos){
        level.setBlock(pos,config.crate,3);
        BlockEntity entity=level.getBlockEntity(pos);
        if(entity instanceof CrateBlockEntity crate){
            crate.setLootTable(config.lootTable,random.nextLong());
        }
    }

    public static double getPosWeight(LevelReader level,BlockPos pos){
        Direction[] dirs= new Direction[]{Direction.NORTH,Direction.EAST,Direction.SOUTH,Direction.WEST};

        boolean[] hasEdge=new boolean[4];   //false为未找到边界, true为找到边界
        int edges=0;

        for(int i=0;i<4;i++){
            Direction dir=dirs[i];
            BlockPos check=pos;
            for(int j=0;j<4;j++){
                check=check.relative(dir);
                if(!(level.getBlockState(check).getBlock()instanceof AirBlock)){
                    if(checkOblique(level,pos,dir,dir.getClockWise())&&checkOblique(level,pos,dir,dir.getCounterClockWise())){
                        hasEdge[i]=true;
                        edges++;
                        break;
                    }
                }
            }
        }

        switch (edges){
            case 1:
            case 4:
                return 1;
            case 3:
                return 0.5;
            case 2:{
                if((hasEdge[0]&&hasEdge[2])||(hasEdge[1]&&hasEdge[3])){ //两边是墙为走廊
                    return 0.11;
                }
                else return 1;
            }
            default:
                return 0.11;
        }

    }

    public static boolean checkOblique(LevelReader level,BlockPos pos,Direction a,Direction b){
        BlockPos check=pos;
        for(int i=0;i<3;i++){
            check=check.relative(a).relative(b);
            if(!(level.getBlockState(check).getBlock()instanceof AirBlock)){
                return true;
            }
        }
        return false;
    }

    record CrateConfig(ResourceLocation lootTable,BlockState crate){}
}
