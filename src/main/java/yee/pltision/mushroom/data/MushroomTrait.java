package yee.pltision.mushroom.data;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;
import yee.pltision.mushroom.data.bgi.MushroomTraitSrc;

import java.io.Serializable;

public class MushroomTrait implements INBTSerializable<CompoundTag>, Serializable {
    public MushroomTraitSrc src;
    public int count=1;
    public MushroomTrait(MushroomTraitSrc src){
        this.src = src;
    }
    public void countPlus(){
        count++;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag=new CompoundTag();
        CompoundTag src=new CompoundTag();
        src.putInt("length",this.src.length);
        src.putLong("bits",this.src.bits);
        tag.put("src",src);
        tag.putInt("count",count);
        return tag;
    }

    /**
     * 已根据nbt生成对应的类型和源以及计数, 只需要负责初始化即可
     */
    @Override
    public void deserializeNBT(CompoundTag nbt) {}
}
