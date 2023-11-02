package yee.pltision.mushroom.visual;

import yee.pltision.mushroom.visual.bar.Bar;
import yee.pltision.mushroom.data.MushroomEffect;
import yee.pltision.mushroom.data.EntityMushroomData;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class VisualFrame extends Panel {

    BufferedImage BROWN_MUSHROOM;
    BufferedImage RED_MUSHROOM;

    {
        try {
            RED_MUSHROOM = ImageIO.read(new File("mushroom/red_mushroom.png"));
            BROWN_MUSHROOM = ImageIO.read(new File("mushroom/brown_mushroom.png"));
        } catch (IOException e) {
//            throw new RuntimeException(e);
        }
    }

    public EntityMushroomData player;

    int barX=10,barY=10,barWidth=960,barHeight=40;
    int width=1000,height=400;
    int[] baseImage;
    BufferedImage image;

    Bar effectBar;
    VisualFrame(EntityMushroomData player){
        this.player=player;
        effectBar=new EffectBar(player);
        image=new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
        baseImage=new int[width*height];
        Arrays.fill(baseImage,0xffffffff);
    }

    public void paint(Graphics g){
        super.paint(g);
        this.draw();
    }

    public void draw(){
        if(player==null) return;

        final int INFO_HEIGHT=height-barX*2+barHeight;

        image.setRGB(0,0,width,height,baseImage,0,width);
        Graphics2D graphics= (Graphics2D) image.getGraphics();

        graphics.setColor(Color.GRAY);
        //graphics.setBackground(Color.GRAY);
        graphics.fillRect(barX,barY,barWidth,barHeight);

        int atX=barX;
        int i=0;
        for(MushroomEffect effect:player.effects.values()){
            Color effectColor=effect.color();
            graphics.setColor(effectColor);
            graphics.fillRect(atX,barY, (int) (effect.width()* barWidth),barHeight);
            atX+=Math.max((int) (effect.width()* barWidth),0);

            //graphics.setColor(Color.black);
            int size=INFO_HEIGHT/32;
            int y=(i%size)*32+(barX*2+barHeight);
            int x=(i/size)*100+20;

            //System.out.println(x+" "+y);
            switch ((int) (effect.type>>>58L)){
                case 0b011101:
                case 0b110101:
                    graphics.drawImage(RED_MUSHROOM,x,y,null);
                    break;
                case 0b011010:
                    graphics.drawImage(BROWN_MUSHROOM,x,y,null);
            }
            graphics.fillRect(x+14,y+2,14,14);
            graphics.setColor(Color.black);
            graphics.drawString(toFullBinaryString(effect.type).append(" : ").append(effect.value).toString(),x+32,y+14);
            graphics.drawString(effect.getData(),x+32,y+30);

            i++;
        }
        getGraphics().drawImage(image,0,0,null);
    }

    public static StringBuilder toFullBinaryString(final long num){
        StringBuilder builder=new StringBuilder();
        for(int i=63;i>=0;i--){
            builder.append(((num>>>i)&1)==0?'0':'1');
        }
        return builder;
    }
}
