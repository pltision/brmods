package yee.pltision.backrooms.block.pipe;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

import static yee.pltision.backrooms.block.pipe.ConnectState.*;

@Deprecated
public interface ComplexPipe extends Pipe{
    EnumProperty<ConnectState> EAST=EnumProperty.create("east",ConnectState.class),
                                WEST=EnumProperty.create("west",ConnectState.class),
                                NORTH=EnumProperty.create("north",ConnectState.class),
                                SOUTH=EnumProperty.create("south",ConnectState.class),
                                UP=EnumProperty.create("up",ConnectState.class),
                                DOWN=EnumProperty.create("down",ConnectState.class);
//    EnumProperty<PipeLiquidState> LIQUID=Pipe.LIQUID;
//    DirectionProperty FROM = Pipe.FROM;


    default void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        Pipe.super.createBlockStateDefinition(builder);
        builder.add(EAST);
        builder.add(WEST);
        builder.add(NORTH);
        builder.add(SOUTH);
        builder.add(UP);
        builder.add(DOWN);
//        builder.add(LIQUID);
//        builder.add(FROM);
    }
    static BlockState toDefaultState(BlockState state){
        return state.setValue(EAST, NONE)
                .setValue(WEST, NONE)
                .setValue(NORTH, NONE)
                .setValue(SOUTH, NONE)
                .setValue(UP, NONE)
                .setValue(DOWN, NONE)
                .setValue(LIQUID,PipeLiquidState.NONE);
    }
    static BlockState updateLiquid(BlockState state,LevelAccessor level,BlockPos pos){

        if(state.getValue(LIQUID) != PipeLiquidState.NONE){
            BlockState t=level.getBlockState(pos.relative(state.getValue(FROM)));
            if(t.getBlock()instanceof ComplexPipe){
                if(t.getValue(LEVEL)!=0) {
                    return state.setValue(LIQUID, t.getValue(LIQUID)).setValue(LEVEL, t.getValue(LEVEL) - 1);
                }
            }
            else{
                int l=state.getValue(LIQUID).sourceFunction.sourceLevel(t,level,pos);
                if(l>0) state.setValue(LEVEL,l);
            }
            /*else if(t.getBlock()instanceof LiquidBlock liquid&&
                    !(t.getBlock() instanceof FakeLiquidBlock)*//*&&
                    liquid.getFluid().isSame(state.getValue(LIQUID).fluid)*//*){
                int l=t.getValue(LiquidBlock.LEVEL);
                if(l<1)return state.setValue(LEVEL,(9<<1)-2);
                l=(l<<1)-2;
                if(l>0) return state.setValue(LEVEL, l);
            }*/
            return state.setValue(LIQUID,PipeLiquidState.NONE);
        }
        for(Direction direction:Direction.values()) {
            BlockState t = tryState(state, level, pos, direction);
            if (t != null) return t;
        }
        return state;

    }
    static BlockState tryState(BlockState state,LevelAccessor level,BlockPos pos,Direction face){
        BlockState t;
        if(state.getValue(getPropertyFromDirection(face))==CONNECT){
            t=level.getBlockState(pos.relative(face));
            if(t.getBlock() instanceof ComplexPipe){
                if (t.getValue(LIQUID)!=PipeLiquidState.NONE&&
                        t.getValue(FROM)!=face.getOpposite()&&
                        t.getValue(getPropertyFromDirection(face.getOpposite()))==CONNECT){
                    if((t.getValue(LIQUID)!=PipeLiquidState.NONE||state.getValue(FROM)==face))
                        return state.setValue(LIQUID,t.getValue(LIQUID)).setValue(FROM,face);
                }

            }
        }
        else if(state.getValue(getPropertyFromDirection(face))==OPEN){
            t=level.getBlockState(pos.relative(face));
            for(PipeLiquidState liquidState:PipeLiquidState.values()){
                int l=liquidState.sourceFunction.sourceLevel(t,level,pos);
                if(l>0) return state.setValue(LEVEL,l).setValue(LIQUID,liquidState);
            }
            /*if(t.getBlock() instanceof LiquidBlock liquid){
                int l=t.getValue(LiquidBlock.LEVEL);
                if(l<1)return state.setValue(LIQUID,PipeLiquidState.WATER).setValue(FROM,face).setValue(LEVEL,(9<<1)-2);
                l=(l<<1)-2;
                if(l>0) return state.setValue(LIQUID,PipeLiquidState.WATER).setValue(FROM,face).setValue(LEVEL,l);
            }*/
        }
        return null;
    }
    default void updateNeighbors(BlockState state, Level level,BlockPos pos){
        Direction from=state.getValue(FROM);
//        PipeLiquidState liquid=state.getValue(LIQUID);
        for(Direction direction:Direction.values()) {
            BlockState update=level.getBlockState(pos.relative(direction));
            if(direction!=from &&
                    state.getValue(getPropertyFromDirection(from))==CONNECT &&
                    update.getBlock()instanceof ComplexPipe &&
                    update.getValue(FROM)!=direction.getOpposite()){

                    level.scheduleTick(pos.relative(direction),update.getBlock(),3);
            }
        }
    }
    default void placeLiquid(BlockState state, Level level, BlockPos pos){
        if(state.getValue(LIQUID)!=PipeLiquidState.NONE){
            int l=state.getValue(LEVEL)>>1;
            if(l>0){
                PipeLiquidState liquid=state.getValue(LIQUID);
                for(Direction direction:Direction.values()){
                    if(state.getValue(getPropertyFromDirection(direction))==OPEN) {
                        BlockPos place = pos.relative(direction);
                        liquid.placeFunction.place(l, direction, state, level, place);
                    }
                }
            }
        }
        /*BlockState liquid=null;
        switch (state.getValue(LIQUID)){
            case NONE -> {
                //liquid=null;
            }
            case WATER -> {
                int l=state.getValue(LEVEL)>>>1;
                if(l!=0)
                    liquid= BrBlocks.Pipes.FAKE_LIQUID.get().defaultBlockState().setValue(LIQUID,PipeLiquidState.WATER).setValue(LiquidBlock.LEVEL,l);

            }
        }

        if(liquid!=null){
            for(Direction direction:Direction.values()){
                BlockPos place= pos.relative(direction);
                BlockState replace=level.getBlockState(place);
                if(state.getValue(getPropertyFromDirection(direction))==OPEN&&
                        (
                                (replace.getBlock()instanceof AirBlock&&!(replace.getBlock()instanceof FakeLiquidBlock))||
                                        (replace.getBlock()instanceof LiquidBlock liquidBlock&&
                                                liquidBlock.getFluid().defaultFluidState()== liquid.getFluidState()&&
                                                (replace.getValue(LiquidBlock.LEVEL) >0))
                        )
                ){
                    level.setBlock(place,liquid,3);

                }
            }
        }*/
    }

    default void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos changePos, boolean p_54714_) {
        level.scheduleTick(pos,state.getBlock(),3);
        //tryPlaceLiquid(state,level,pos);
    }
    default BlockState updateShape(BlockState state, Direction direction, BlockState change, LevelAccessor level, BlockPos pos, BlockPos p_48926_) {
        EnumProperty<ConnectState> property= getPropertyFromDirection(direction);
        if(state.getValue(property)!=NONE){
            if(change.getBlock() instanceof ComplexPipe){
                if(change.getValue(getPropertyFromDirection(direction.getOpposite()))!=NONE)
                    state= state.setValue(property,CONNECT);
            }
            else{
                state= state.setValue(property,OPEN);
            }
        }
        return state;
    }
    default void tick(BlockState state, ServerLevel level, BlockPos pos, Random random) {
        BlockState t= updateLiquid(state,level,pos);
        updateNeighbors(t,level,pos);
        placeLiquid(t,level,pos);
        if(t!=state) level.setBlock(pos,t,3);
    }
    static EnumProperty<ConnectState> getPropertyFromDirection(Direction direction){
        switch (direction){
            case EAST -> {
                return EAST;
            }
            case WEST -> {
                return WEST;
            }
            case NORTH -> {
                return NORTH;
            }
            case SOUTH -> {
                return SOUTH;
            }
            case UP -> {
                return UP;
            }
            case DOWN -> {
                return DOWN;
            }
        }
        return EAST;
    }

    @Override
    default boolean isConnected(BlockState state, Direction face){
        ConnectState connectState= state.getValue(getPropertyFromDirection(face));
        return connectState==CONNECT||connectState==OPEN;
    }
    @Override
    default boolean isOpen(BlockState state, Direction face){
        ConnectState connectState= state.getValue(getPropertyFromDirection(face));
        return connectState==OPEN;
    }
}
