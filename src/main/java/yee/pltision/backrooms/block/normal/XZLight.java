package yee.pltision.backrooms.block.normal;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.jetbrains.annotations.NotNull;
import yee.pltision.backrooms.block.type.BackroomsLightBlock;

public class XZLight extends BackroomsLightBlock {
    public static EnumProperty<Direction.Axis> AXIS= BlockStateProperties.HORIZONTAL_AXIS;
    public XZLight(Properties p_49795_) {
        super(p_49795_);
        this.registerDefaultState(this.stateDefinition.any().setValue(AXIS, Direction.Axis.X));
        inti(this);
    }
    protected void inti(XZLight light){}

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_54935_) {
        p_54935_.add(AXIS);
    }
    public BlockState rotate(@NotNull BlockState p_55930_, @NotNull Rotation p_55931_) {
        return rotatePillar(p_55930_, p_55931_);
    }

    /*复制自RotatedPillarBlock*/
    public static BlockState rotatePillar(BlockState p_154377_, Rotation p_154378_) {
        switch(p_154378_) {
            case COUNTERCLOCKWISE_90:
            case CLOCKWISE_90:
                switch(p_154377_.getValue(AXIS)) {
                    case X:
                        return p_154377_.setValue(AXIS, Direction.Axis.Z);
                    case Z:
                        return p_154377_.setValue(AXIS, Direction.Axis.X);
                    default:
                        return p_154377_;
                }
            default:
                return p_154377_;
        }
    }


}
