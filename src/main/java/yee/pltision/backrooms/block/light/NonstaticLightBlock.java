package yee.pltision.backrooms.block.light;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import org.jetbrains.annotations.NotNull;
import yee.pltision.backrooms.block.type.BackroomsLightBlock;

import java.util.Random;

public class NonstaticLightBlock extends BackroomsLightBlock implements NonstaticLight {
    final int beSteady,beUnsteady,flashTick;
    public NonstaticLightBlock(Properties p_49795_,int beSteady,int beUnsteady,int flashTick) {
        super(p_49795_);
        this.beSteady=beSteady;
        this.beUnsteady=beUnsteady;
        this.flashTick=flashTick;
    }

    @Override
    public void neighborChanged(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Block p_60512_, @NotNull BlockPos p_60513_, boolean p_60514_) {
        if(!state.getValue(STEADY))
            level.scheduleTick(pos,this,flashTick);
    }

    @Override
    public void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        NonstaticLight.super.createBlockStateDefinition(builder);
    }

    @Override
    public void randomTick(@NotNull BlockState state, @NotNull ServerLevel level, @NotNull BlockPos pos, @NotNull Random random) {
        if(state.getValue(STEADY)){
            if((random.nextInt()&0xffff)<beUnsteady){
                level.setBlock(pos,state.setValue(STEADY,false),3);
                level.scheduleTick(pos,this,flashTick);
            }
        }

    }

    @Override
    public void tick(BlockState state, @NotNull ServerLevel level, @NotNull BlockPos pos, @NotNull Random random) {
        if(!state.getValue(STEADY)){
//            int t;
//            if((t=(random.nextInt()&0xffff))<beSteady){
            if((random.nextInt()&0xffff)<beSteady){
                level.setBlock(pos,state.setValue(STEADY,true).setValue(LIGHTING,state.getValue(DEFAULT_STATE)),3);
            }
            else{
//                System.out.println(t+" "+beSteady);
                boolean lighting=state.getValue(LIGHTING);
                if(random.nextBoolean()){
                    level.setBlock(pos,state.setValue(LIGHTING,!lighting),3);
                }
                level.scheduleTick(pos,this,flashTick);
            }
        }

    }
}
