package yee.pltision.backrooms.block.light;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.FrontAndTop;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import yee.pltision.backrooms.block.type.BackroomsLightBlock;
import yee.pltision.backrooms.block.type.IBackroomsLightBlock;

public class WallLight extends NonstaticLightBlock {
    public static final DirectionProperty FACING= BlockStateProperties.HORIZONTAL_FACING;

    public static final VoxelShape SOUTH_AABB =
            Block.box(4.0D, 0.0D, 0.0D,
                    12.0D, 16.0D, 4.0D);
    public static final VoxelShape EAST_AABB =
            Block.box(0.0D, 0.0D, 4.0D,
                    4.0D, 16.0D, 12.0D);
    public static final VoxelShape NORTH_AABB =
            Block.box(4.0D, 0.0D, 12.0D,
                    12.0D, 16.0D, 16.0D);
    public static final VoxelShape WEST_AABB =
            Block.box(12.0D, 0.0D, 4.0D,
                    16.0D, 16.0D, 12.0D);

    public WallLight(Properties p_49795_, int beSteady, int beUnsteady, int flashTick) {
        super(p_49795_, beSteady, beUnsteady, flashTick);
    }


    public BlockState rotate(BlockState p_54241_, Rotation p_54242_) {
        return p_54241_.setValue(FACING, p_54242_.rotation().rotate(p_54241_.getValue(FACING)));
    }

    public BlockState mirror(BlockState p_54238_, Mirror p_54239_) {
        return p_54238_.setValue(FACING, p_54239_.rotation().rotate(p_54238_.getValue(FACING)));
    }

    @Override
    public void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> p_54935_) {
        super.createBlockStateDefinition(p_54935_);
        p_54935_.add(BlockStateProperties.HORIZONTAL_FACING);
    }

    public BlockState getStateForPlacement(BlockPlaceContext p_54227_) {
        Direction direction = p_54227_.getClickedFace();
        Direction direction1;
        if (direction == Direction.DOWN||direction == Direction.UP) {
            direction1 = p_54227_.getNearestLookingVerticalDirection().getOpposite();
            if(direction1 == Direction.DOWN||direction1 == Direction.UP) direction1=Direction.NORTH;
        } else {
            direction1 = direction;
        }

        return this.defaultBlockState().setValue(FACING, direction1);
    }

    @Override
    public VoxelShape getShape(BlockState p_60555_, @NotNull BlockGetter p_60556_, @NotNull BlockPos p_60557_, @NotNull CollisionContext p_60558_) {
        switch (p_60555_.getValue(FACING)){
            case EAST -> {
                return EAST_AABB;
            }
            case SOUTH -> {
                return SOUTH_AABB;
            }
            case WEST -> {
                return WEST_AABB;
            }
            default -> {
                return NORTH_AABB;
            }
        }
    }

    @Override
    public boolean canSurvive(BlockState p_60525_, @NotNull LevelReader p_60526_, @NotNull BlockPos p_60527_) {
        BlockPos back;
        switch (p_60525_.getValue(FACING)){
            case EAST :
                back= p_60527_.west();
                break;
            case SOUTH :
                back=p_60527_.north();
                break;
            case WEST :
                back=p_60527_.east();
                break;
            default :
                back=p_60527_.south();
        }
        return p_60526_.getBlockState(back).getMaterial().isSolid();
    }

    public BlockState updateShape(@NotNull BlockState p_57503_, @NotNull Direction p_57504_, @NotNull BlockState p_57505_, @NotNull LevelAccessor p_57506_, @NotNull BlockPos p_57507_, @NotNull BlockPos p_57508_) {
        return this.canSurvive(p_57503_, p_57506_, p_57507_) ? super.updateShape(p_57503_, p_57504_, p_57505_, p_57506_, p_57507_, p_57508_):Blocks.AIR.defaultBlockState();
    }
}
