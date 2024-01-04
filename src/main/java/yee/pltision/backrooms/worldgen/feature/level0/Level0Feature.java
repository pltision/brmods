package yee.pltision.backrooms.worldgen.feature.level0;

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
import yee.pltision.backrooms.worldgen.BackroomsFunction;
import yee.pltision.backrooms.worldgen.densityfunctioncontext.AbsoluteYFunctionContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

import static yee.pltision.backrooms.worldgen.BackroomsFunction.BASE_HEIGHT;
import static yee.pltision.backrooms.worldgen.BackroomsFunction.searchType;

@SuppressWarnings("ALL")
public class Level0Feature extends Feature<NoneFeatureConfiguration> {
    public Level0Feature() {
        super(NoneFeatureConfiguration.CODEC);
    }

    public static Holder<PlacedFeature> PLACED_FEATURE;

    public static @NotNull Feature<?> feature(){
        //System.out.println("feature被调用");
        Level0Feature feature= new Level0Feature();
        Holder<ConfiguredFeature<NoneFeatureConfiguration,?>> configuredFeature=FeatureUtils.register("level0_feature",feature, FeatureConfiguration.NONE);
        PLACED_FEATURE= PlacementUtils.register("level0_feature",
                configuredFeature
                , List.of());
        return feature;
    }
    public static Holder<PlacedFeature> placedFeature(){
        return PLACED_FEATURE;
    }

    public interface ShelfTypes{
        int WOODEN_SHELF=0, BOOK_SHELF=1;
        int NORMAL=0, TOP=1, TOP_ITEM=2;
    }
    public record ShelfInti(Supplier<BlockState> block, int weight,int shelfType,int placeType){}
    public record BlockRange(BlockState block,int from,int to){}
    public static class ShelfType{
        public ArrayList<BlockRange> shelfList;
        public ArrayList<BlockRange>topShelfList;
        public ArrayList<BlockRange> topItemList;
        public int normal=0,top=0,topItem=0;

        public ShelfType(ArrayList<BlockRange>shelfList,ArrayList<BlockRange>topShelfList,ArrayList<BlockRange> topItemList){
            this.shelfList=shelfList;
            this.topShelfList=topShelfList;
            this.topItemList = topItemList;
        }

        public void addBlock(ShelfInti inti){
            switch (inti.placeType){
                case ShelfTypes.NORMAL:
                    if(shelfList!=null)
                        shelfList.add(new BlockRange(inti.block.get(),normal,normal+=inti.weight));
                    break;
                case ShelfTypes.TOP:
                    if(topShelfList!=null)
                        topShelfList.add(new BlockRange(inti.block.get(),top,top+=inti.weight));
                    break;
                case ShelfTypes.TOP_ITEM:
                    if(topItemList!=null)
                        topItemList.add(new BlockRange(inti.block.get(),topItem,topItem+=inti.weight));
                    break;
            }
        }
    }

    public static BlockState CANNED_CARROTS_SHELF;
    public static List<ShelfInti> SHELF_INTI_LIST=List.of(
            new ShelfInti(()-> BrBlocks.WoodenShelves.SHELF.get().defaultBlockState(),
                    16,ShelfTypes.TOP,ShelfTypes.WOODEN_SHELF),
            new ShelfInti(()-> BrBlocks.WoodenShelves.SHELF.get().defaultBlockState(),
                    64,ShelfTypes.NORMAL,ShelfTypes.WOODEN_SHELF),

            new ShelfInti(()-> CANNED_CARROTS_SHELF.setValue(StackableShelfBlock.STACK,1),
                    4,ShelfTypes.NORMAL,ShelfTypes.WOODEN_SHELF),
            new ShelfInti(()-> CANNED_CARROTS_SHELF.setValue(StackableShelfBlock.STACK,2),
                    3,ShelfTypes.NORMAL,ShelfTypes.WOODEN_SHELF),
            new ShelfInti(()-> CANNED_CARROTS_SHELF.setValue(StackableShelfBlock.STACK,3),
                    1,ShelfTypes.NORMAL,ShelfTypes.WOODEN_SHELF),
            new ShelfInti(()-> CANNED_CARROTS_SHELF.setValue(StackableShelfBlock.STACK,4),
                    1,ShelfTypes.NORMAL,ShelfTypes.WOODEN_SHELF)

    );

    public static BlockState getRandomBlock(ArrayList<BlockRange> ranges,int sum,Random random){
        if(ranges==null||ranges.size()==0) return null;
        int find=(random.nextInt()&0x7fffffff)%sum;
        int max= ranges.size(), min=0,ans=-1;
        while(min<max){
            int mid=(max+min)>>1;
            //System.out.println(min+" "+max+" "+mid+" "+ans);
            if(ranges.get(mid).from<=find){
                ans=mid;
                min=mid+1;
            }
            else max=mid;
            //System.out.println(mid+" "+(mid-1));
        }
        if(ans==-1&&ranges.get(ans).to<=find) return null;
        return ranges.get(ans).block;
    }

    public static ShelfType WOODEN_SHELF=new ShelfType(new ArrayList<>(),new ArrayList<>(),new ArrayList<>());
    public static ShelfType BOOK_SHELF=new ShelfType(new ArrayList<>(),new ArrayList<>(),new ArrayList<>());

    @SubscribeEvent
    public static void inti(FMLLoadCompleteEvent event){
        CANNED_CARROTS_SHELF=BrBlocks.WoodenShelves.CANNED_CARROTS.get().defaultBlockState();

        for (ShelfInti inti : SHELF_INTI_LIST) {
            switch (inti.shelfType){
                case ShelfTypes.WOODEN_SHELF :
                    WOODEN_SHELF.addBlock(inti);
                    break;
                case ShelfTypes.BOOK_SHELF:
                    BOOK_SHELF.addBlock(inti);
                    break;
            }
        }
    }




    @Override
    public boolean place(@NotNull FeaturePlaceContext<NoneFeatureConfiguration> context) {
        //System.out.println("try placed.");
        if(BackroomsFunction.LEVEL0_GRAPH==null) return false;
        if(BackroomsFunction.LEVEL0_TYPE_NOISE==null) return false;
        //System.out.println("yee");
        BlockPos origin= context.origin();
        Random r=context.random();
        WorldGenLevel level=context.level();
        int x= origin.getX(),y= origin.getY(),z= origin.getZ();

        AbsoluteYFunctionContext functionContext=new AbsoluteYFunctionContext(x, BASE_HEIGHT,z);

        short[] map=new short[16*16];

        for(int i=0;i<16;i++){
            for(int j=0;j<16;j++){
                functionContext.dx=i;
                functionContext.dz=j;
                short tempBlockType=(short) BackroomsFunction.LEVEL0_GRAPH.compute(functionContext);
                map[(i<<4)+j]=tempBlockType;

                BlockPos onFloor = new BlockPos(x + i, BASE_HEIGHT + 1, z + j);
                switch (tempBlockType){
                    case 0xCF: {
                        //context.level().setBlock(new BlockPos(x+i, BASE_HEIGHT,z+j),Blocks.STONE.defaultBlockState(),3);
                        //System.out.println("yee");
                        BackroomsFunction.AverageTypeComputer computer=new BackroomsFunction.AverageTypeComputer();
                        searchType(functionContext,BackroomsFunction.LEVEL0_GRAPH,BackroomsFunction.LEVEL0_TYPE_NOISE,computer);
                        if (BackroomsFunction.Level0BlockFunction.getDoorFrameType(computer.outedOfBounds?0:computer.average()) == 0) {//是否为底部门框 (目前决定只有通道在地上才生成门
                            //检查x轴是否需要放置门
                            {
                                functionContext.dx--;
                                if (BackroomsFunction.LEVEL0_GRAPH.compute(functionContext) == 0) {//门要被墙包裹
                                    functionContext.dx += 2;
                                    if (BackroomsFunction.LEVEL0_GRAPH.compute(functionContext) == 0xCF) {//检查x+是否仍然是门槛
                                        functionContext.dx++;
                                        if (BackroomsFunction.LEVEL0_GRAPH.compute(functionContext) == 0) {//检查x+是否为墙, 若宽度大于三格则不生成门
                                            int wight = 15;
                                            functionContext.dx = x + i;

                                            //如果门框不是在门槛内部(两边或一边接触空气(或椅子凳子墙等不是门槛的东西(就是不是在走廊中间)))就缩小概率
                                            functionContext.dz++;
                                            if (BackroomsFunction.LEVEL0_GRAPH.compute(functionContext) == 0xCF) {
                                                wight = 29;
                                            }
                                            functionContext.dz -= 2;
                                            if (BackroomsFunction.LEVEL0_GRAPH.compute(functionContext) == 0xCF) {
                                                wight = 29;
                                            }

                                            if ((r.nextInt() & 0b11111) > wight) {//二分之一概率生成门
                                                Direction facing;
                                                DoorHingeSide pSide,nSide;
                                                if (r.nextBoolean()) {//决定门的朝向
                                                    facing = Direction.NORTH;
                                                    pSide=DoorHingeSide.RIGHT;
                                                    nSide=DoorHingeSide.LEFT;
                                                }
                                                else {
                                                    facing = Direction.SOUTH;
                                                    pSide=DoorHingeSide.LEFT;
                                                    nSide=DoorHingeSide.RIGHT;
                                                }

                                                BlockState door = Blocks.OAK_DOOR.defaultBlockState().setValue(DoorBlock.FACING, facing);

                                                door = door.setValue(DoorBlock.HALF, DoubleBlockHalf.LOWER);
                                                level.setBlock(onFloor, door.setValue(DoorBlock.HINGE, nSide), 3);
                                                level.setBlock(new BlockPos(x + i + 1, BASE_HEIGHT + 1, z + j), door.setValue(DoorBlock.HINGE, pSide), 3);

                                                door = door.setValue(DoorBlock.HALF, DoubleBlockHalf.UPPER);
                                                level.setBlock(new BlockPos(x + i, BASE_HEIGHT + 2, z + j), door.setValue(DoorBlock.HINGE, nSide), 3);
                                                level.setBlock(new BlockPos(x + i + 1, BASE_HEIGHT + 2, z + j), door.setValue(DoorBlock.HINGE, pSide), 3);

                                                continue;
                                            }

                                        }
                                    } else if (BackroomsFunction.LEVEL0_GRAPH.compute(functionContext) == 0) {//是否为宽度一格的门槛
                                        int wight = 15;
                                        functionContext.dx = x + i;

                                        //如果门框不是在门槛内部(两边或一边接触空气(或椅子凳子墙等不是门槛的东西(就是不是在走廊中间)))就缩小概率
                                        functionContext.dz++;
                                        if (BackroomsFunction.LEVEL0_GRAPH.compute(functionContext) == 0xCF) {
                                            wight = 29;
                                        }
                                        functionContext.dz -= 2;
                                        if (BackroomsFunction.LEVEL0_GRAPH.compute(functionContext) == 0xCF) {
                                            wight = 29;
                                        }

                                        if ((r.nextInt() & 0b11111) > wight) {//二分之一概率生成门
                                            Direction facing;
                                            if (r.nextBoolean())//决定门的朝向
                                                facing = Direction.NORTH;
                                            else facing = Direction.SOUTH;

                                            DoorHingeSide hinge;
                                            if (r.nextBoolean()) {//决定铰链在左还是右边
                                                hinge = DoorHingeSide.LEFT;
                                            } else hinge = DoorHingeSide.RIGHT;

                                            BlockState door = Blocks.OAK_DOOR.defaultBlockState().setValue(DoorBlock.FACING, facing).setValue(DoorBlock.HINGE, hinge);

                                            door = door.setValue(DoorBlock.HALF, DoubleBlockHalf.LOWER);
                                            level.setBlock(onFloor, door, 3);

                                            door = door.setValue(DoorBlock.HALF, DoubleBlockHalf.UPPER);
                                            level.setBlock(new BlockPos(x + i, BASE_HEIGHT + 2, z + j), door, 3);

                                            continue;
                                        }
                                    }
                                }
                            }

                            //检查z轴是否需要放置门
                            {
                                functionContext.dx=i;
                                functionContext.dz=j;

                                functionContext.dz--;
                                if (BackroomsFunction.LEVEL0_GRAPH.compute(functionContext) == 0) {//门要被墙包裹
                                    functionContext.dz += 2;
                                    if (BackroomsFunction.LEVEL0_GRAPH.compute(functionContext) == 0xCF) {//检查z+是否仍然是门槛
                                        functionContext.dz++;
                                        if (BackroomsFunction.LEVEL0_GRAPH.compute(functionContext) == 0) {//检查z+是否为墙, 若宽度大于三格则不生成门
                                            int wight = 15;
                                            functionContext.dz = z + i;

                                            //如果门框不是在门槛内部(两边或一边接触空气(或椅子凳子墙等不是门槛的东西(就是不是在走廊中间)))就缩小概率
                                            functionContext.dx++;
                                            if (BackroomsFunction.LEVEL0_GRAPH.compute(functionContext) == 0xCF) {
                                                wight = 29;
                                            }
                                            functionContext.dx -= 2;
                                            if (BackroomsFunction.LEVEL0_GRAPH.compute(functionContext) == 0xCF) {
                                                wight = 29;
                                            }

                                            if ((r.nextInt() & 0b11111) > wight) {//二分之一概率生成门
                                                Direction facing;
                                                DoorHingeSide pSide,nSide;
                                                if (r.nextBoolean()) {//决定门的朝向
                                                    facing = Direction.EAST;
                                                    pSide=DoorHingeSide.RIGHT;
                                                    nSide=DoorHingeSide.LEFT;
                                                }
                                                else {
                                                    facing = Direction.WEST;
                                                    pSide=DoorHingeSide.LEFT;
                                                    nSide=DoorHingeSide.RIGHT;
                                                }

                                                BlockState door = Blocks.OAK_DOOR.defaultBlockState().setValue(DoorBlock.FACING, facing);

                                                door = door.setValue(DoorBlock.HALF, DoubleBlockHalf.LOWER);
                                                level.setBlock(onFloor, door.setValue(DoorBlock.HINGE, nSide), 3);
                                                level.setBlock(new BlockPos(x + i , BASE_HEIGHT + 1, z + j+ 1), door.setValue(DoorBlock.HINGE, pSide), 3);

                                                door = door.setValue(DoorBlock.HALF, DoubleBlockHalf.UPPER);
                                                level.setBlock(new BlockPos(x + i, BASE_HEIGHT + 2, z + j), door.setValue(DoorBlock.HINGE, nSide), 3);
                                                level.setBlock(new BlockPos(x + i , BASE_HEIGHT + 2, z + j+ 1), door.setValue(DoorBlock.HINGE, pSide), 3);

                                                continue;
                                            }

                                        }
                                    } else if (BackroomsFunction.LEVEL0_GRAPH.compute(functionContext) == 0) {//是否为宽度一格的门槛
                                        int wight = 15;
                                        functionContext.dz = z + i;

                                        //如果门框不是在门槛内部(两边或一边接触空气(或椅子凳子墙等不是门槛的东西(就是不是在走廊中间)))就缩小概率
                                        functionContext.dx++;
                                        if (BackroomsFunction.LEVEL0_GRAPH.compute(functionContext) == 0xCF) {
                                            wight = 29;
                                        }
                                        functionContext.dx -= 2;
                                        if (BackroomsFunction.LEVEL0_GRAPH.compute(functionContext) == 0xCF) {
                                            wight = 29;
                                        }

                                        if ((r.nextInt() & 0b11111) > wight) {//二分之一概率生成门
                                            Direction facing;
                                            if (r.nextBoolean())//决定门的朝向
                                                facing = Direction.WEST;
                                            else facing = Direction.EAST;

                                            DoorHingeSide hinge;
                                            if (r.nextBoolean()) {//决定铰链在左还是右边
                                                hinge = DoorHingeSide.LEFT;
                                            } else hinge = DoorHingeSide.RIGHT;

                                            BlockState door = Blocks.OAK_DOOR.defaultBlockState().setValue(DoorBlock.FACING, facing).setValue(DoorBlock.HINGE, hinge);

                                            door = door.setValue(DoorBlock.HALF, DoubleBlockHalf.LOWER);
                                            level.setBlock(onFloor, door, 3);

                                            door = door.setValue(DoorBlock.HALF, DoubleBlockHalf.UPPER);
                                            level.setBlock(new BlockPos(x + i, BASE_HEIGHT + 2, z + j), door, 3);

                                            continue;
                                        }
                                    }
                                }
                            }

                        }
                        else{ //生成墙上的梯子之类的
                            int tx,tz;

                            tx=x+i;
                            tz=z+j;
                            for(int k=1;k<=3;k++) {

                                if(!level.getBlockState(new BlockPos(tx, BASE_HEIGHT + k, tz)).getMaterial().isSolid()) continue;

                                BlockPos pos;
                                pos = new BlockPos(tx+1, BASE_HEIGHT + k, tz);
                                if (level.getBlockState(pos).getBlock() instanceof AirBlock)
                                    if ((r.nextInt() & 0b1111) > 12)
                                        level.setBlock(pos, Blocks.LADDER.defaultBlockState().setValue(LadderBlock.FACING, Direction.EAST), 3);

                                pos = new BlockPos(tx-1, BASE_HEIGHT + k, tz);
                                if (level.getBlockState(pos).getBlock() instanceof AirBlock)
                                    if ((r.nextInt() & 0b1111) > 12)
                                        level.setBlock(pos, Blocks.LADDER.defaultBlockState().setValue(LadderBlock.FACING, Direction.WEST), 3);

                                pos = new BlockPos(tx, BASE_HEIGHT + k, tz+1);
                                if (level.getBlockState(pos).getBlock() instanceof AirBlock)
                                    if ((r.nextInt() & 0b1111) > 12) {
                                        level.setBlock(pos, Blocks.LADDER.defaultBlockState().setValue(LadderBlock.FACING, Direction.SOUTH), 3);
                                    }

                                pos = new BlockPos(tx, BASE_HEIGHT + k, tz-1);
                                if (level.getBlockState(pos).getBlock() instanceof AirBlock)
                                    if ((r.nextInt() & 0b1111) > 12) {
                                        level.setBlock(pos, Blocks.LADDER.defaultBlockState().setValue(LadderBlock.FACING, Direction.NORTH), 3);
                                    }
                            }
                        }
                        break;
                    }
                    case 0x7F:{
                        if(level.getBlockState(onFloor).getBlock()instanceof AirBlock)
                            if(!(level.getBlockState(onFloor.below()).getBlock()instanceof AirBlock)){
                                level.setBlock(onFloor, BrBlocks.WOODEN_TABLE.get().defaultBlockState(),3);
                            }
                        break;
                    }
                    case 0xAA:{
                        if(level.getBlockState(onFloor).getBlock()instanceof AirBlock)
                            if(!(level.getBlockState(onFloor.below()).getBlock()instanceof AirBlock)){
                                level.setBlock(onFloor, BrBlocks.WOODEN_CHARA.get().defaultBlockState(),3);
                            }
                        break;
                    }
                    case 0x8A:{
                        BlockState shelf=null,top,item;
                        ShelfType type=null;
                        switch (0){
                            case 0:
                                type=WOODEN_SHELF;
                                break;
                            case 1:
                                type=BOOK_SHELF;
                                break;
                        }
                        top=getRandomBlock(type.topShelfList,type.top,r);
                        item=getRandomBlock(type.topItemList,type.topItem,r);

                        int height=(r.nextInt()&0x7fffffff)%6;
                        if((top!=null&&(r.nextInt()&0b11)==0)||item!=null) {
                            height--;
                            if(item!=null&&(r.nextInt()&0b11)==0) height--;
                            else item=null;
                        }
                        else top=null;

                        BlockPos placing=onFloor;
                        for(int k=0;k<height;k++){
                            shelf=getRandomBlock(type.shelfList,type.normal,r);
                            level.setBlock(placing,shelf,3);
                            placing=placing.above();
                        }
                        if(top!=null){
                            level.setBlock(placing,top,3);
                            placing=placing.above();
                        }
                        if(item!=null){
                            level.setBlock(placing,item,3);
                            //placing=placing.above();
                        }
                        break;
                    }
                }

            }
        }

        //状态更新
        for(int i=0;i<16;i++){
            for(int j=0;j<16;j++){
                BlockPos onFloor = new BlockPos(x + i, BASE_HEIGHT + 1, z + j);
                switch (map[(i<<4)+j]){
                    case 0x7F:{
                        /*if(level.getBlockState(onFloor).getBlock() instanceof Table){
                            int cutLength=(r.nextInt()&0b1111)>>>2;
                            if(cutLength!=0){
                                BlockPos startLine=onFloor;
                                if(r.nextBoolean()){
                                    //裁剪x轴
                                    for(int k=0;k<cutLength;k++){
                                        if(!(level.getBlockState(startLine.west()).getBlock() instanceof Table)){
                                            BlockPos cut=startLine;
                                            for(int l=0;l<6;l++){
                                                if(level.getBlockState(cut).getBlock() instanceof Table){
                                                    level.setBlock(cut,Blocks.AIR.defaultBlockState(),3);
                                                }
                                                else{
                                                    break;
                                                }
                                                cut=cut.east();
                                            }
                                            startLine=startLine.south();
                                        }
                                        else{
                                            break;
                                        }
                                    }
                                }
                                else{
                                    //裁剪z轴
                                    if(!(level.getBlockState(startLine.north()).getBlock() instanceof Table)){
                                        BlockPos cut=startLine;
                                        for(int l=0;l<6;l++){
                                            if(level.getBlockState(cut).getBlock() instanceof Table){
                                                level.setBlock(cut,Blocks.AIR.defaultBlockState(),3);
                                            }
                                            else{
                                                break;
                                            }
                                            cut=cut.south();
                                        }
                                        startLine=startLine.east();
                                    }
                                    else{
                                        break;
                                    }
                                }
                            }
                        }*/

                        level.setBlock(onFloor, Table.getState(level, onFloor),3);
                        break;
                    }
                    case 0xAA:{
                        level.setBlock(onFloor, Chara.getState(level, onFloor,r),3);
                        break;
                    }
                }
            }
        }

        return true;
    }
}
