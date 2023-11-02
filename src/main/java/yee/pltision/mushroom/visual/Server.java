package yee.pltision.mushroom.visual;

import yee.pltision.mushroom.data.EntityMushroomData;

public class Server extends Thread{
    public final EntityMushroomData player;

    public int ticks;
    public boolean stop;

    public Server(EntityMushroomData player){
        this.player=player;
        if(player.toxin>0) player.toxin-=0.02;
    }

    public void tick(){
        player.tick(null);
        ticks++;
    }

}
