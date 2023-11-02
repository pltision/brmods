package yee.pltision.mushroom.data;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;
import yee.pltision.Util;

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
    final public double immunity=0.2/100;
    public double toxin=0;
    int sleep;


    public EntityMushroomData(){
        effects=new HashMap<>();
    }

    public void tick(@Nullable Entity entity){
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
                temperature+=toxin*0.04;
                //temperature-=0.01;
            }
            temperature-=(temperature-defaultTemplate)*0.01;
            //if(toxin>1) System.out.println("yee");

            effects.values().removeIf(effect -> effect.tick(this, entity));
        }

    }
    public double damage(){
        return Math.max(0,(immunity-toxin*(0.08/100))/100);
    }

    public double getSum(){
        var ref = new Object() {
            double sum = 0;
        };
        effects.forEach((key,value)-> ref.sum +=Math.max(value.value,0));
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
