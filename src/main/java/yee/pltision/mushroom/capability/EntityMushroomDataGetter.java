package yee.pltision.mushroom.capability;

import net.minecraft.ReportedException;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.INBTSerializable;
import yee.pltision.mushroom.data.EntityMushroomData;
import yee.pltision.mushroom.data.MushroomEffect;
import yee.pltision.mushroom.data.MushroomTrait;
import yee.pltision.mushroom.data.bgi.MushroomTraitSrc;

import java.util.HashMap;
import java.util.Map;

public interface EntityMushroomDataGetter extends INBTSerializable<CompoundTag> {
    void tick(Player player);

    EntityMushroomData getEntityData();

    default CompoundTag serializeNBT(){
        EntityMushroomData data=getEntityData();
        CompoundTag tag=new CompoundTag();
        tag.putDouble("dry",data.dry);
        tag.putDouble("temperature",data.temperature);
        tag.putDouble("toxin",data.toxin);
        ListTag effects=new ListTag();
        for(MushroomEffect effect:data.effects.values()){
            CompoundTag effectTag=new CompoundTag();
            effectTag.putLong("type",effect.type);
            effectTag.putDouble("value",effect.value);
            effectTag.putDouble("deep",effect.deep);
            ListTag traits=new ListTag();
            for(MushroomTrait trait:effect.traits){
                traits.add(trait.serializeNBT());
            }
            effectTag.put("traits",traits);
            effects.add(effectTag);
        }
        tag.put("effects",effects);
        return tag;
    }

    void deserializeNBT(CompoundTag nbt);

    static void deserializeNBT(EntityMushroomData data, CompoundTag nbt){
        //EntityMushroomData data=new EntityMushroomData();
        data.dry=nbt.getDouble("dry");
        data.temperature=nbt.getDouble("temperature");
        data.toxin=nbt.getDouble("toxin");

        for(Tag tryCast_effect:nbt.getList("effects",9)){
            if(tryCast_effect instanceof CompoundTag effectTag){

                MushroomEffect effect=new MushroomEffect(effectTag.getLong("type"),
                        effectTag.getDouble("value"),
                        effectTag.getDouble("deep"));

                Map<MushroomTraitSrc,CompoundTag> traitMap=new HashMap<>();

                for(Tag tryCast_trait:nbt.getList("effects",9)) {

                    if(tryCast_trait instanceof CompoundTag traitTag){
                        try {
                            CompoundTag srcTag = traitTag.getCompound("src");
                            traitMap.put(new MushroomTraitSrc(srcTag.getLong("bits"),srcTag.getInt("length")),srcTag);
                        }
                        catch (ReportedException ignored){}
                    }
                }

                for(MushroomTrait trait:effect.traits){
                    CompoundTag tag= traitMap.remove(trait.src);
                    if(tag!=null){
                        trait.deserializeNBT(tag);
                    }
                }
            }
        }

        //return data;
    }

}
