package yee.pltision.mushroom.data;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;
import yee.pltision.Util;
import yee.pltision.mushroom.mc.MushroomMobEffects;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class EntityMushroomData implements Serializable {
    public HashMap<Long,MushroomEffect> effects;
    public double dry=0.1;
    public final double defaultTemplate=36.5;
    public double temperature=defaultTemplate;
    final public double immunity=0.0035;
    public double toxin=0;
    int sleep;


    public EntityMushroomData(){
        effects=new HashMap<>();
    }

    public void tick(@Nullable LivingEntity entity){
        if(sleep>0)
        {
            sleep--;
        }
        else
        {
            sleep=10+ (Util.RANDOM.nextInt()&0b11);
            if(toxin>0){
                toxin-=0.02;
                toxin*=0.90;
                temperature+=toxin*0.02;
                //temperature-=0.01;
            }
            temperature-=(temperature-defaultTemplate)*0.01;
            //if(toxin>1) System.out.println("yee");

            effects.values().removeIf(effect -> effect.tick(this, entity));
        }

        if(entity!=null){
            entityAction(entity);
        }
    }

    //对实体操作
    public void entityAction(LivingEntity entity){
        if(toxin>4) entity.addEffect(new MobEffectInstance(MushroomMobEffects.MUSHROOM_POTION.get(),200,5,false,true,false));
        else if(toxin>3) entity.addEffect(new MobEffectInstance(MushroomMobEffects.MUSHROOM_POTION.get(),200,4,false,true,false));
        else if(toxin>2.5) entity.addEffect(new MobEffectInstance(MushroomMobEffects.MUSHROOM_POTION.get(),200,3,false,true,false));
        else if(toxin>2) entity.addEffect(new MobEffectInstance(MushroomMobEffects.MUSHROOM_POTION.get(),200,2,false,true,false));
        else if(toxin>1.5) entity.addEffect(new MobEffectInstance(MushroomMobEffects.MUSHROOM_POTION.get(),200,1,false,true,false));
        else if(toxin>1) entity.addEffect(new MobEffectInstance(MushroomMobEffects.MUSHROOM_POTION.get(),200,0,false,true,false));
    }

    public double damage(){
        return Math.max(0,(immunity-toxin*(0.02/100))/100);
    }

    public double getSum(){
        var ref = new Object() {
            double sum = 0;
        };
        effects.forEach((key,value)-> ref.sum +=Math.max(value.value,0));
        //System.out.println(ref.sum);
        return ref.sum;
    }

    public void addEffect(long type,double value){
        MushroomEffect effect;
        if((effect=effects.get(type))!=null){
            effect.value+=value;
        }
        else {
            effects.put(type,new MushroomEffect(type,value));
        }
    }
    public void addEffect(MushroomEffect add){
        MushroomEffect effect;
        if((effect=effects.get(add.type))!=null){
            effect.value+=add.value;
        }
        else {
            effects.put(add.type,add);
        }
    }

}
