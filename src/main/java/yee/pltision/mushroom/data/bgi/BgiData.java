package yee.pltision.mushroom.data.bgi;

import yee.pltision.mushroom.data.*;

import java.util.HashSet;
import java.util.List;

/**
 * <p>BGI (Bit Genetic Information) 是模组中数字生命的遗传信息, 其决定了生物的性状。</p>
 * <p>目前, BGI均由64bits的long类型变量储存。其中的6个高位被称作头部 (Head), 头部决定了生物应使用哪种AI类型和外观，其余的部分无需特别为其命名区分，但可以将其称作身体 (Body)。当一个生物没有找到匹配的头部, 通常会被加上debuff使其尽快灭亡或直接生成失败, 它们通常无法提供任何外观属性, 但仍可能致病。</p>
 * <p>整个BGI共同决定着生物的功能性性状, 细微更改生物群体除头部以外的信息通常不会迅速地导致其灭亡。性状的决定机制是，若整个BGI内带有连续的某个性状片段 (由指定长度的bits组成), 这个性状就会在生命体中生效, 在一些AI中可能还有其他决定因素。</p>
 */
public class BgiData  {
    public static MushroomTraitSrc[] TRAIT_SRC_LIST=new MushroomTraitSrc[]{
            new MushroomTraitSrc(0b1011010,7){
                @Override
                public double humanGroupPower() {
                    return 0.02/10000;
                }
                @Override
                public double groundGroup(){
                    return 0.09/10000;
                }
            },
            new MushroomTraitSrc(0b111011,6){
                @Override
                public double humanGroupPower() {
                    return 0.004/10000;
                }
                @Override
                public double groundGroup(){
                    return 0.04/10000;
                }
            },
            new MushroomTraitSrc(0b1010110,7){
                @Override
                public double humanGroupPower() {
                    return 0.06/10000;
                }
                @Override
                public double groundGroup(){
                    return 0.03/10000;
                }
                @Override
                public double toxinPower() {
                    return 0.2;
                }
            },
            new MushroomTraitSrc(0b1010110,7){
                @Override
                public double humanGroupPower() {
                    return 0.02/10000;
                }
                @Override
                public double groundGroup(){
                    return 0.04/10000;
                }
            },
            new MushroomTraitSrc(0b1011,7){
                @Override
                public double humanGroupPower() {
                    return 0.003/10000;
                }
                @Override
                public double groundGroup(){
                    return 0.001/10000;
                }
                @Override
                public double toxinPower() {
                    return 0.04;
                }
            },
            new MushroomTraitSrc(0b1101,7){
                @Override
                public double humanGroupPower() {
                    return 0.001/10000;
                }
                @Override
                public double groundGroup(){
                    return 0.003/10000;
                }
            },
            new MushroomTraitSrc(0b101011101101,12){
                @Override
                public double humanGroupPower() {
                    return 0.01/10000;
                }
                @Override
                public double groundGroup(){
                    return 0.01/10000;
                }
                @Override
                public double toxinPower() {
                    return 0.1;
                }
                @Override
                public PowerMushroomTrait createTrait() throws ClassCastException {
                    return new PowerMushroomTrait(this){
                        @Override
                        public void countPlus() {}
                    };
                }
                public double humanGroupMul(MushroomTrait data,MushroomEffect effect) {
                    PowerMushroomTrait powerData= (PowerMushroomTrait)data;
                    return powerData.start ? powerData.power: 0.3;
                }
                public double toxinPowerMul(MushroomTrait data,MushroomEffect effect){
                    PowerMushroomTrait powerData= (PowerMushroomTrait)data;
                    return powerData.start ? powerData.power*0.5:0.2;
                }
                public double damageMul(MushroomTrait data,MushroomEffect effect){
                    PowerMushroomTrait powerData= (PowerMushroomTrait)data;
                    if(powerData.start)
                        return 1;
                    if(effect.value>0.5) return 1.4;
                    return effect.value-0.2>0? (effect.value-0.2)*4+0.2:0.3;
                }

                @Override
                public void playerTick(EntityTraitContext context, MushroomTrait data, MushroomEffect effect, EntityMushroomData player) {
                    PowerMushroomTrait powerData= (PowerMushroomTrait)data;
                    //System.out.println("yee");
                    if(powerData.power<0.5&&powerData.started) {
                        powerData.started=false;
                        powerData.start=false;
                        powerData.power=-1;
                    }
                    if(powerData.power>=2&&effect.value<0.15&&effect.deep>0.075){
                        powerData.start=true;
                        powerData.started=true;
                    }
                    if(powerData.start){
                         powerData.power-=0.003;
                    }
                    else if(powerData.power<=2.1)powerData.power+=effect.deep* 0.008;
                    super.playerTick(context , data, effect, player);
                }
            }
    };
    public static final HashSet<? extends MushroomTraitSrc> MUSHROOM=new HashSet<>(List.of(TRAIT_SRC_LIST));

}
