package yee.pltision.rectraft;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import static yee.pltision.rectraft.Noise.randomNoise;

public class GeneratorTest {
    public static RectRaft[] RAFT;

    static {
        try {
            Scanner scanner = new Scanner(new FileReader("D:/mcmod/forge/brmods/levelbmps/length.txt"));
            int length = scanner.nextInt();
            RAFT=new RectRaft[length];
            for (int i = 0; i < length; i++) {
                RAFT[i] = RectRaft.createWithImage(ImageIO.read(new File("D:/mcmod/forge/brmods/levelbmps/Level0_"+i+".png")));

            }

        } catch (Exception e) {
            //throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        GraphFrame frame=new GraphFrame();
        frame.setLocation(100,100);
        frame.setVisible(true);
        while (true){
            frame.reload();
        }
    }

    public static byte getColor(int x,int y,int seed){
        byte turn= randomNoise(x/32,y/32,seed);
        return RAFT[roomRandom(x/40,y/40,seed+1)].getPoint(x&0b11111,y&0b11111,turn);
    }
    public static byte roomRandom(int x,int y,int seed){
        return (byte) ((randomNoise(x,y,seed)+randomNoise(x+61,y+61,seed))&0b11);
    }
}
class GraphFrame extends Frame {
    public static final int WIDTH=2000,HEIGHT=1200,MOVE=2;
    public int dx=(WIDTH>>1),dy=(HEIGHT>>1);
    public BufferedImage tempImage=new BufferedImage(WIDTH,HEIGHT,BufferedImage.TYPE_INT_ARGB);
    public int[] tempColorArray=new int[WIDTH*HEIGHT];

    public GraphFrame(){
        setSize(WIDTH,HEIGHT);
        addKeyListener(new KeyAdapter() {
            public static final int SPEED=10;
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()){
                    case KeyEvent.VK_UP:
                        dy-=SPEED;
                        break;
                    case KeyEvent.VK_DOWN:
                        dy+=SPEED;
                        break;
                    case KeyEvent.VK_LEFT:
                        dx-=SPEED;
                        break;
                    case KeyEvent.VK_RIGHT:
                        dx+=SPEED;
                        break;
                }
            }
        });
    }

    public void reload(){
        int dx=this.dx,dy=this.dy;
        for(int i=0;i<HEIGHT;i++){
            for(int j=0;j<WIDTH;j++){
                int bmpColor=GeneratorTest.getColor((j>>MOVE)+dx,(i>>MOVE)+dy,1)&0xff;
                //System.out.println(bmpColor);
                tempColorArray[i*WIDTH+j]=0xff000000|(bmpColor<<16)|(bmpColor<<8)|bmpColor;
            }
        }
        tempImage.setRGB(0,0,WIDTH,HEIGHT,tempColorArray,0,WIDTH);
        getGraphics().drawImage(tempImage,0,0,null);
    }

}
