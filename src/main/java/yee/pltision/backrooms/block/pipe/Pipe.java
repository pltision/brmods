package yee.pltision.backrooms.block.pipe;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.jetbrains.annotations.NotNull;

import java.util.Random;
import java.util.Stack;

/**
 * This interface is for Block.
 */
public interface Pipe {
    EnumProperty<PipeLiquidState> LIQUID=EnumProperty.create("liquid",PipeLiquidState.class);
    DirectionProperty FROM = DirectionProperty.create("from", Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST, Direction.UP, Direction.DOWN);
    IntegerProperty LEVEL=IntegerProperty.create("level",0,40);

    default void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        builder.add(LIQUID);
        builder.add(FROM);
        builder.add(LEVEL);
    }

    /**
     * 更新方块的流体状态
     */
    default BlockState updateLiquid(BlockState state, LevelReader level, BlockPos pos) {
        PipeLiquidState stateLiquid=state.getValue(LIQUID);
        Direction from=state.getValue(FROM);
        if(stateLiquid==PipeLiquidState.NONE){  //获取流体
            for(Direction direction:Direction.values()){
                BlockPos checkPos=pos.relative(direction);
                BlockState check=level.getBlockState(checkPos);
                if(isConnected(state,direction)){   //获取管道的流体
                    if (check.getBlock() instanceof Pipe pipe) {
                        if (pipe.isConnected(check, direction.getOpposite())) {
                            if (check.getValue(LEVEL) > 0&&check.getValue(LIQUID)!=PipeLiquidState.NONE)
                                return state.setValue(LIQUID, check.getValue(LIQUID)).setValue(LEVEL, check.getValue(LEVEL) - 1).setValue(FROM, direction);
                        }
                    }
                }
                if(isOpen(state,direction)){
                    for (var liquidType : PipeLiquidState.values()) {   //找到适合的流体
                        int l = (liquidType.sourceFunction.sourceLevel(check, level, checkPos)<< 2) - 1;
                        if (l > 0)
                            return state.setValue(LIQUID, liquidType).setValue(LEVEL, l ).setValue(FROM, direction);
                    }
                }
            }
        }
        else{   //内部已有流体
            BlockState t=level.getBlockState(pos.relative(from));
            if(t.getBlock()instanceof Pipe pipe){   //获取上一个管道的
                if(pipe.isConnected(t,from.getOpposite())){
                    if(t.getValue(LEVEL)>0)
                        return state.setValue(LIQUID,t.getValue(LIQUID)).setValue(LEVEL,t.getValue(LEVEL)-1);
                }
            }
            else{ //获取源头的
                int l=(stateLiquid.sourceFunction.sourceLevel(t,level,pos.relative(from))<<2)-1;
                if(l>0) return state.setValue(LIQUID,stateLiquid).setValue(LEVEL,l);
            }
            return state.setValue(LIQUID,PipeLiquidState.NONE);
        }
        return state;
    }

    default void updateNeighbors(BlockState state, Level level,BlockPos pos){
        Direction from=state.getValue(FROM);
        for(Direction direction:Direction.values()){//遍历每一个面
            if(direction!=from){ //不更新来源
                BlockState check=level.getBlockState(pos.relative(direction));
                if(check.getBlock()instanceof Pipe pipe){
                    if(isConnected(state,direction)&&pipe.isConnected(check,direction.getOpposite())){  //到时候要不写个doUpdate方法判读是否要更新, 不然可能出现回路
                        level.scheduleTick(pos,check.getBlock(),3);
                    }
                }
            }
        }
    }

    /**
     * 尝试在周围放置流体
     */
    default void placeLiquid(BlockState state, LevelAccessor level, BlockPos pos){
        for(var direction:Direction.values()){
            if(isOpen(state,direction)){
                int l=state.getValue(LEVEL)>>2;
                if(l>0) state.getValue(LIQUID).placeFunction.place(l,direction,level.getBlockState(pos.relative(direction)),level,pos.relative(direction));
            }
        }
    }

    default void tick(BlockState state, ServerLevel level, BlockPos pos, Random random) {
        BlockState t= updateLiquid(state,level,pos);
        updateNeighbors(t,level,pos);
        placeLiquid(t,level,pos);
        if(t!=state) level.setBlock(pos,t,3);
    }

    default void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos changePos, boolean p_54714_) {
        level.scheduleTick(pos,state.getBlock(),3);
    }

    /**
     * 用于在世界生成时一次性初始化管道的流体状态以及放置假水
     */
    static void stackUpdate(LevelAccessor level,BlockPos start){
        Stack<BlockPos> stack=new Stack<>();
        stack.push(start);

        int i=0;
        while (i<200&&!stack.isEmpty()){
            i++;
            BlockPos pos=stack.pop();
            BlockState state=level.getBlockState(pos);
            Pipe pipe=(Pipe)state.getBlock();

            BlockState t= pipe.updateLiquid(state,level,pos);
            pipe.placeLiquid(t,level,pos);
            if(t!=state) level.setBlock(pos,t,3);

            Direction from=t.getValue(FROM);
            for(Direction direction:Direction.values()){//遍历每一个面
                if(direction!=from){ //不更新来源
                    BlockState check=level.getBlockState(pos.relative(direction));
                    if(check.getBlock()instanceof Pipe checkPipe){
                        if(pipe.isConnected(t,direction)&&checkPipe.isConnected(check,direction.getOpposite())){
                            stack.push(pos.relative(direction));
                        }
                    }
                }
            }
        }
    }

    boolean isConnected(BlockState state, Direction face);

    boolean isOpen(BlockState state,Direction face);
}
