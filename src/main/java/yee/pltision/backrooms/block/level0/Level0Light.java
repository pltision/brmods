package yee.pltision.backrooms.block.level0;

import net.minecraft.core.Direction;
import net.minecraft.core.FrontAndTop;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.Material;
import yee.pltision.backrooms.block.type.BackroomsLightBlock;

import static net.minecraft.world.level.block.NetherPortalBlock.AXIS;

public class Level0Light extends BackroomsLightBlock {
    public static EnumProperty<Direction.Axis> AXIS= BlockStateProperties.AXIS;
    public Level0Light() {
        super(
                BlockBehaviour.Properties.of(Material.DECORATION).instabreak().lightLevel((p_187435_) -> 15).requiresCorrectToolForDrops().strength(1F, 10.0F).noCollission().sound(SoundType.GLASS)
        );
        this.registerDefaultState(this.stateDefinition.any().setValue(AXIS, Direction.Axis.X));
    }
    public Level0Light(Properties p_49795_) {super(p_49795_);}

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_54935_) {
        p_54935_.add(AXIS);
    }
    public BlockState rotate(BlockState p_55930_, Rotation p_55931_) {
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
