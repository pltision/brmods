package yee.pltision.mushroom.data;

import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import yee.pltision.mushroom.visual.EffectBar;
import yee.pltision.mushroom.visual.bar.BarBlock;
import yee.pltision.mushroom.data.bgi.BgiData;
import yee.pltision.mushroom.data.bgi.MushroomTraitSrc;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 算是底层接口罢, 这个没有实现ai
 */
public class MushroomEffect implements BarBlock, Serializable {
    public long type;
    public double value;
    public double deep=0;
    public int sleep=0;

    public ArrayList<MushroomTrait> traits;
    public void damage(double value){}
    public boolean tick(EntityMushroomData entityData,@Nullable Entity entity){

        if(value<=0) return true;
        defaultGroup(entityData,0.4,0.5+deep,0.5,1,36,16,1);
        if(deep<10&&deep>-10) deep+=(0.75+(value-deep)/value)*Math.min(value*0.01,0.01);
        deep-=entityData.damage()*0.01;
        return false;
    }

    public final void defaultGroup(EntityMushroomData player, double centerValue, double nValueBuff, double pValueBuff
            , double deepMul, double tpStart, double tpRange, double tpMul){
        EntityTraitContext context=new EntityTraitContext();
        for(MushroomTrait trait:traits){
            trait.src.playerTick(context,trait,this,player);
        }
        double sum=player.getSum();
        double centered=value-centerValue*(1.5-sum);
        double valueBuff = centered>0 ? centered*pValueBuff : -centered*nValueBuff ;
        value=Math.min(1-sum+value,value+ (2-sum*2)*context.group*(1+valueBuff)*(1+deep*deepMul)*context.groupMul*
                ( (player.temperature>tpStart ? (tpRange-(player.temperature-tpStart))/tpRange : 1) *tpMul+(1-tpMul))
        );
        //System.out.println(( (player.temperature>tpStart ? (tpRange-(player.temperature-tpStart))/tpRange : 1) *tpMul+(1-tpMul)));
        player.toxin+=value* context.toxin*context.toxinMul;
        value-=player.damage()*context.damageMul;

    }

    public MushroomEffect(long type, double value){
        this.type=type;
        this.value=value;
        traits=setupTraits(type);
    }

    public MushroomEffect(long type, double value,double deep) {
        this(type, value);
        this.deep=deep;
    }

    /**
     * 找出type中包含了多少性狀。
     */
    public static @NotNull Map<MushroomTraitSrc,MushroomTrait> getTraitsMap(long type){
        HashMap<MushroomTraitSrc,MushroomTrait> traits=new HashMap<>();

        for(int i=64;i>0;i--){
            for(MushroomTraitSrc traitData: BgiData.MUSHROOM){
                if(traitData.length>i) continue;
                if((type&(-1L>>>(64-traitData.length)))==traitData.bits){
                    MushroomTrait trait=traits.get(traitData);
                    if(trait==null){
                        traits.put(traitData,traitData.createTrait());
                    }
                    else trait.count++;
                }

            }
            type>>>=1;
        }
        return traits;
    }

    public static @NotNull ArrayList<MushroomTrait> setupTraits(final long type){
        Map<MushroomTraitSrc,MushroomTrait> traits=getTraitsMap(type);
        ArrayList<MushroomTrait> traitList=new ArrayList<>();
        traits.forEach((data,trait)-> traitList.add(trait));
        System.out.println(traitList);
        return traitList;
    }

    @Override
    public int hashCode(){
        return Long.hashCode(type);
    }

    @Override
    public boolean equals(Object o){
        return o instanceof MushroomEffect &&((MushroomEffect)o).type==type;
    }

    @Override
    public double width() {
        return value;
    }

    @Override
    public Color color() {
        return EffectBar.getColor(type);
    }

    public String getData(){
        StringBuilder builder=new StringBuilder();
        double group=0;
        double toxin=0;
        double groupMul=1;
        double toxinMul=1;
        double damageMul=1;
        for(MushroomTrait trait:traits){
            group+= trait.src.humanGroupPower()*trait.count;
            toxin+= trait.src.toxinPower()*trait.count;
            toxinMul*= Math.pow(trait.src.toxinPowerMul(trait,this),trait.count);
            groupMul*= Math.pow(trait.src.humanGroupMul(trait,this),trait.count);
            damageMul*= Math.pow(trait.src.damageMul(trait,this),trait.count);

        }
        builder.append("group:").append(group);
        builder.append(" groupMul:").append(groupMul);
        builder.append(" toxin:").append(toxin);
        builder.append(" toxinMul:").append(toxinMul);
        builder.append(" damageMul:").append(damageMul);
        builder.append(" deep:").append(deep);
        return builder.toString();
    }
}
