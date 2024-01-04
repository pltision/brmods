package yee.pltision.backrooms.block.crate;

import net.minecraft.core.BlockPos;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BarrelBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import yee.pltision.backrooms.block.type.BackroomsHardBlock;

public class CrateBlock extends Block implements EntityBlock {
    public static final BooleanProperty PLAYER_PLACE=BooleanProperty.create("player_place");

    public CrateBlock(Properties p_49795_) {
        super(p_49795_);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_49915_) {
        p_49915_.add(PLAYER_PLACE);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos p_153215_, @NotNull BlockState p_153216_) {
        return new CrateBlockEntity(p_153215_,p_153216_);
    }

    public @NotNull InteractionResult use(@NotNull BlockState p_49069_, Level p_49070_, @NotNull BlockPos p_49071_, @NotNull Player p_49072_, @NotNull InteractionHand p_49073_, @NotNull BlockHitResult p_49074_) {
        if (p_49070_.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            BlockEntity blockentity =  p_49070_.getBlockEntity(p_49071_);
            if (blockentity instanceof CrateBlockEntity crateBlockEntity) {
                p_49072_.openMenu(crateBlockEntity);
                p_49072_.awardStat(Stats.OPEN_BARREL);
                PiglinAi.angerNearbyPiglins(p_49072_, true);
            }

            return InteractionResult.CONSUME;
        }
    }
}
