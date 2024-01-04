package yee.pltision.backrooms.block.light;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class NonstaticAndRandomNoPowerLightBlock extends NonstaticLightBlock implements RandomNoPowerLight{
    double beNoPower,bePower;
    double flashWhenTurnOn,flashWhenTurnOff;
    public NonstaticAndRandomNoPowerLightBlock(Properties p_49795_, double beSteady, double beUnsteady, double beNoPower,double bePower, int flashTick,double flashWhenTurnOn,double flashWhenTurnOff) {
        super(p_49795_, beSteady, beUnsteady, flashTick);
        this.beNoPower=beNoPower;
        this.bePower=bePower;
        this.flashWhenTurnOn=flashWhenTurnOn;
        this.flashWhenTurnOff=flashWhenTurnOff;
        registerDefaultState(defaultBlockState().setValue(NO_POWER,false));
    }
    @Override
    public void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        RandomNoPowerLight.super.createBlockStateDefinition(builder);
    }

    @Override
    public void neighborChanged(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Block p_60512_, @NotNull BlockPos p_60513_, boolean p_60514_) {
        if(!state.getValue(NO_POWER))
            level.setBlockAndUpdate(pos,state.setValue(LIGHTING,false));
        else
            super.neighborChanged(state, level, pos, p_60512_, p_60513_, p_60514_);
    }

    @Override
    public void tick(BlockState state, @NotNull ServerLevel level, @NotNull BlockPos pos, @NotNull Random random) {
        if(!state.getValue(STEADY)){
            if(random.nextDouble()<beSteady){
                //覆盖基类方法，让闪烁停止时恢复到没电或有电时光源的状态
                state=state.setValue(STEADY,true);
                if(state.getValue(NO_POWER))
                    state= state.setValue(LIGHTING,false);
                else
                    state= state.setValue(LIGHTING,state.getValue(DEFAULT_STATE));
                level.setBlock(pos,state,3);
            }
            else{
                boolean lighting=state.getValue(LIGHTING);
                if(random.nextBoolean()){
                    level.setBlock(pos,state.setValue(LIGHTING,!lighting),3);
                }
                level.scheduleTick(pos,this,flashTick);
            }
        }

    }

    @Override
    public void randomTick(@NotNull BlockState state, @NotNull ServerLevel level, @NotNull BlockPos pos, @NotNull Random random) {
        if(state.getValue(NO_POWER)) {
            if (random.nextDouble() < bePower) {
                state=state.setValue(NO_POWER, false).setValue(LIGHTING, state.getValue(DEFAULT_STATE));
                if(random.nextDouble()<flashWhenTurnOn){
                    state=state.setValue(STEADY, false);
                    level.scheduleTick(pos,this,flashTick);
                }
                level.setBlock(pos,state , 3);
            }
        }
        else{
            if(random.nextDouble()<beNoPower) {
                state= state.setValue(NO_POWER, true).setValue(LIGHTING, false);
                if(random.nextDouble()<flashWhenTurnOff){
                    state=state.setValue(STEADY, false);
                    level.scheduleTick(pos,this,flashTick);
                }
                level.setBlock(pos, state, 3);
            }
            else super.randomTick(state, level, pos, random);
        }
    }
}
