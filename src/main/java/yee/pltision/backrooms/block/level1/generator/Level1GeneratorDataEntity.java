package yee.pltision.backrooms.block.level1.generator;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import yee.pltision.backrooms.block.BrBlocks;

@Mod.EventBusSubscriber
public class Level1GeneratorDataEntity extends BlockEntity {
    @SuppressWarnings("ConstantConditions")
    public static final RegistryObject<BlockEntityType<Level1GeneratorDataEntity>> MYCELIUM_BLOCK_ENTITY_TYPE =
            BrBlocks.BLOCK_ENTITY_REGISTER.register("level1_generator_data_entity", () ->
                    BlockEntityType.Builder.of( Level1GeneratorDataEntity::new, BrBlocks.Level1.GENERATOR_DATA_BLOCK.get()).build(null)
            );

    public boolean generated=false;

    public LayoutSquareData mainLayout;

    public Level1GeneratorDataEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(MYCELIUM_BLOCK_ENTITY_TYPE.get(), p_155229_, p_155230_);
        mainLayout=new LayoutSquareData();
    }

    @Override
    public void saveAdditional(@NotNull CompoundTag tag){
        CompoundTag mainLayoutTag=new CompoundTag();
        mainLayout.save(mainLayoutTag);
        tag.put("mainLayout",mainLayoutTag);
        tag.putBoolean("generated",generated);
    }

    @Override
    public void load(@NotNull CompoundTag tag){
        mainLayout.load(tag.getCompound("mainLayout"));
        generated=tag.getBoolean("generated");
    }

    public static class LayoutSquareData {
        /**
         * 是否生成这一层
         */
        public boolean enable =false;

        /**
         * 从y=ground开始填充空气
         */
        public int ground;

        /**
         * 从y<ground+height停止填充空气
         */
        public int height;

        /**
         * 类型, 生成时决定
         */
        int type;

        /**
         * 生成柱子的类型
         */
        public int column;

        /**
         * 表示空间要被从区块边界开始裁剪几格
         * 当值为-1时则为连接那一边的区块
         */
        public byte north=-1,south=-1,west=-1,east=-1;

        public void enable(int ground,int height){
            enable=true;
            this.ground=ground;
            this.height=height;
        }

        public void enable(int ground, int height, int column){
            enable=true;
            this.ground=ground;
            this.height=height;
            this.column=column;
        }

        public void save(@NotNull CompoundTag tag){
            tag.putBoolean("enable", enable);
            tag.putInt("ground",ground);
            tag.putInt("height",height);
            tag.putInt("type",type);
            tag.putInt("column",column);
            tag.putByte("north",north);
            tag.putByte("south",south);
            tag.putByte("west",west);
            tag.putByte("east",east);
        }
        public void load(@NotNull CompoundTag tag){
            enable =tag.getBoolean("enable");
            ground=tag.getInt("ground");
            height=tag.getInt("height");
            type=tag.getInt("type");
            column=tag.getInt("column");
            north=tag.getByte("north");
            south=tag.getByte("south");
            west=tag.getByte("west");
            east=tag.getByte("east");
        }

        public void copy(LayoutSquareData data){
            enable=data.enable;
            ground=data.ground;
            height=data.height;
            type=  data.type;
            column=data.column;
            north= data.north;
            south= data.south;
            west=  data.west;
            east=  data.east;
        }

    }

}
