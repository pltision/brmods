package yee.pltision.backrooms.block.mushroom;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.AirBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import yee.pltision.mushroom.MushroomData;
import yee.pltision.mushroom.MushroomGetter;

import java.util.Random;

public class BrMyceliumBlock extends Block implements EntityBlock {

    public static final EnumProperty<MushroomType> TYPE=EnumProperty.create("type", MushroomType.class);

    public BrMyceliumBlock(Properties p_49795_) {
        super(p_49795_);
        MyceliumBlockEntity.ENTITY_BLOCKS.add(this);

    }
    public BlockEntity newBlockEntity(@NotNull BlockPos p_153064_, @NotNull BlockState p_153065_) {
        return new MyceliumBlockEntity(p_153064_, p_153065_);
    }

    public static void group(
            LevelAccessor level, @NotNull BlockPos pos, @Nullable BlockState mushroom, long mushroomData, int count,Random r
    ) {
        int CHECK_LENGTH=10;
        BlockPos check=pos.above();
        int i;
        for(i=0;i<CHECK_LENGTH;i++){
            if(level.getBlockState(check).getBlock() instanceof AirBlock){
                break;
            }
            check= check.above();
        }
        if(i==CHECK_LENGTH) return;

        //System.out.println("yee");
        if(mushroom==null){
            MushroomGetter getter=MushroomData.MUSHROOM_TYPES[(int) (mushroomData >>> (64 - MushroomData.TYPE_LENGTH))];
            if(getter==null) return;
            mushroom=getter.getMushroom();
            if(mushroom==null) return;
        }

        for(i=0;i<count;i++){
            BlockPos place = switch (r.nextInt() & 0b11) {
                case 0 -> check.east();
                case 1 -> check.west();
                default -> check;
            };
            place = switch (r.nextInt() & 0b11) {
                case 0 -> place.south();
                case 1 -> place.north();
                default -> check;
            };

            if(level.getBlockState(place).getBlock() instanceof AirBlock){
                if(!level.getBlockState(place.below()).getMaterial().isSolid()){
                    place=place.below();
                    if(!(level.getBlockState(place).getBlock() instanceof AirBlock) || !level.getBlockState(place.below()).getMaterial().isSolid()){
                        place=null;
                    }
                }
            }
            else{
                place=place.above();
                if(!(level.getBlockState(place).getBlock() instanceof AirBlock) || !level.getBlockState(place.below()).getMaterial().isSolid()){
                    place=null;
                }
            }
            //System.out.println(place);

            if(place!=null){

                level.setBlock(place,mushroom,3);
                if(level.getBlockEntity(place) instanceof MyceliumBlockEntity entity){
                    entity.mushroomData=mushroomData;
                }
            }
        }

    }

    public static void place(
            LevelAccessor level, @NotNull BlockPos pos, @Nullable  BlockState myceliumBlock,
            @Nullable BlockState replace, long mushroomData
    ){
        //BlockPos place=new BlockPos(pos.getX()+random.nextInt()%2,pos.getY()+random.nextInt()%2,pos.getZ()+random.nextInt()%2);

        if(replace==null){
            //System.out.println(mushroomData>>>(64-MushroomData.TYPE_LENGTH));
            if(MushroomData.MUSHROOM_TYPES[(int) (mushroomData>>>(64-MushroomData.TYPE_LENGTH))]!=null) {
                BlockState placeBlock=MushroomData.MUSHROOM_TYPES[(int) (mushroomData>>>(64-MushroomData.TYPE_LENGTH))].getMycelium(level.getBlockState(pos));
                //System.out.println(placeBlock);
                if(placeBlock!=null) {
                    level.setBlock(pos, placeBlock, 3);
                    if(level.getBlockEntity(pos) instanceof MyceliumBlockEntity entity)
                        entity.mushroomData=mushroomData;
                }
            }
        }
        else{
            if(level.getBlockState(pos)==replace){
                if(myceliumBlock==null) {
                    if (MushroomData.MUSHROOM_TYPES[(int) (mushroomData >>> (64 - MushroomData.TYPE_LENGTH))] != null) {
                        BlockState placeBlock = MushroomData.MUSHROOM_TYPES[(int) (mushroomData >>> (64 - MushroomData.TYPE_LENGTH))].getMycelium(replace);
                        if (placeBlock != null) {
                            level.setBlock(pos, placeBlock, 3);
                            if(level.getBlockEntity(pos) instanceof MyceliumBlockEntity entity)
                                entity.mushroomData=mushroomData;
                        }
                    }
                }
                else level.setBlock(pos,myceliumBlock,3);
            }
        }
    }


    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_54935_) {
        p_54935_.add(TYPE);
    }

}
