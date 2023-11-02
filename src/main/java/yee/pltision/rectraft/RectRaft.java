package yee.pltision.rectraft;

import org.jetbrains.annotations.NotNull;

import java.awt.image.BufferedImage;

public class RectRaft {
    byte @NotNull [] image;
    int width,height;

    public RectRaft(int width,int height){
        image=new byte[width*height];
        this.width=width;
        this.height=height;
    }

    public RectRaft(byte@NotNull [] image, int width, int height){
        this.image=image;
        this.width=width;
        this.height=height;
    }

    public static RectRaft createWithImage(@NotNull BufferedImage image)
    {
        int width=image.getWidth(), height=image.getHeight(),length=width*height;

        int[]colors=new int[length];
        image.getRGB(0,0,width,height,colors,0,width);


        byte[]bmp=new byte[length];

        for(int i=0;i<length;i++){
            bmp[i]=(byte)(
                    ( ((colors[i]>>16)&0xff) + ((colors[i]>>8)&0xff) + (colors[i]&0xff) ) /3
            );
        }

        return new RectRaft(bmp,width,height);

    }


    @SuppressWarnings("SuspiciousNameCombination")
    public byte getPoint(int x, int y, int turn){
        switch (turn&0b11){
            case 0:
                return getPoint(x,y);
            case 1:
                return getPoint(y,height-x-1);
            case 2:
                return getPoint(width-x-1,height-y-1);
            case 3:
                return getPoint(width-y-1,x);
        }
        throw new RuntimeException();
    }

    public byte getPoint(int x,int y){
        return image[y*width+x];
    }

}
