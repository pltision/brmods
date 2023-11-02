package yee.pltision.mushroom.data;

public class RedMushroomEffect extends MushroomEffect{
    public RedMushroomEffect(long type, double value) {
        super(type, value);
    }
    public RedMushroomEffect(long type, double value,double deep) {
        super(type, value);
        this.deep=deep;
    }

//    public boolean tick(Player player){
//        defaultGroup(player,0.5,0.5+deep*8,1,2);
//        value-=player.tickDamage();
//        deep+=(value==0?0:(1+(value-deep)/value))*Math.min(value*0.01,0.05);
//        deep-=player.tickDamage()*0.01;
//    }
}
