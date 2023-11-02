package yee.pltision.mushroom.data.bgi;

import yee.pltision.mushroom.data.MushroomEffect;
import yee.pltision.mushroom.data.MushroomTrait;
import yee.pltision.mushroom.data.EntityMushroomData;
import yee.pltision.mushroom.data.EntityTraitContext;

import java.io.Serializable;

public class MushroomTraitSrc implements Serializable {
    public final int length;
    public final long bits;

    public MushroomTraitSrc(long bits, int length){
        this.length = length;
        this.bits = bits;
    }

    public MushroomTrait createTrait()throws ClassCastException{
        return new MushroomTrait(this);
    }

    public EntityTraitContext playerTick(MushroomEffect effect, MushroomTrait data, EntityMushroomData player){
        EntityTraitContext context=new EntityTraitContext();
        playerTick(context,data,effect,player);
        return context;
    }
    public void playerTick(EntityTraitContext base, MushroomTrait data, MushroomEffect effect, EntityMushroomData player){
        base.group+= humanGroupPower()*data.count;
        base.toxin+= toxinPower()*data.count;
        base.toxinMul*= Math.pow(toxinPowerMul(data,effect),data.count);
        base.groupMul*= Math.pow(humanGroupMul(data,effect),data.count);
        base.damageMul*= Math.pow(damageMul(data,effect),data.count);
    }

    public double humanGroupPower(){
        return 0;
    }
    public double humanGroupMul(MushroomTrait data,MushroomEffect effect)throws ClassCastException{
        return 1;
    }
    public double toxinPower(){
        return 0;
    }
    public double toxinPowerMul(MushroomTrait data,MushroomEffect effect)throws ClassCastException{
        return 1;
    }
    public double damageMul(MushroomTrait data,MushroomEffect effect)throws ClassCastException{
        return 1;
    }
    public double groundGroup(){
        return 0;
    }

    @Override
    public int hashCode(){
        return Long.hashCode(bits)^length;
    }

    @Override
    public boolean equals(Object o){
        if(o instanceof MushroomTraitSrc){
            MushroomTraitSrc data= (MushroomTraitSrc)o;
            return data.bits==bits&&data.length==length;
        }
        return false;
    }

}
