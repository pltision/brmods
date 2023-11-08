package yee.pltision.mushroom.visual;

import yee.pltision.mushroom.data.MushroomEffect;
import yee.pltision.mushroom.data.EntityMushroomData;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class MushroomVisual {
    static public EntityMushroomData player=new EntityMushroomData();
    static public Server server=new Server(player);
    static public VisualFrame visual=new VisualFrame(player);


    public static void main(String[] args) {
        Frame frame=new Frame();
        frame.setBounds(100,100,1000,500);

        System.out.println(LinkedList.class.hashCode());
        System.out.println(Queue.class.hashCode());

        //1010110

        Random random =new Random(11243);

        final Action[] action = {null};

        player.addEffect(new MushroomEffect(0b0111010000000001011010101101010101100000000000000000000000000000L,0.2));
//        player.effects.add(new MushroomEffect(0b0111010000000000000000001010110101011010101101010110000000000000L,0.1,0));
//        player.effects.add(new MushroomEffect(0b0111010000000000000000001010110000000000000000000000101011101101L,0.4,0.4));

//        player.effects.add(new MushroomEffect(0b0111010001010110101011010101101010110101011010101110101100000000L,0.1,0));
//        player.effects.add(new MushroomEffect(0b0111010001010110000000000000000000000000000000000000000000000000L,0.1,0));

/*        player.addEffect(new MushroomEffect(random.nextLong(),0.01,0));
        player.addEffect(new MushroomEffect(random.nextLong(),0.01,0));
        player.addEffect(new MushroomEffect(random.nextLong(),0.01,0));
        player.addEffect(new MushroomEffect(random.nextLong(),0.01,0));
        player.addEffect(new MushroomEffect(random.nextLong(),0.01,0));
        player.addEffect(new MushroomEffect(random.nextLong(),0.01,0));
        player.addEffect(new MushroomEffect(random.nextLong(),0.01,0));
        player.addEffect(new MushroomEffect(random.nextLong(),0.01,0));
        player.addEffect(new MushroomEffect(random.nextLong(),0.01,0));*/

        BorderLayout layout=new BorderLayout();
        layout.setHgap(80);
        frame.setLayout(layout);

        //顶部玩家信息
        Panel topPanel=new Panel();
        topPanel.setLayout(new GridLayout(1,3));

        TextField time=new TextField();
        time.setEditable(false);
        topPanel.add(time);

        TextField toxin=new TextField();
        toxin.setEditable(false);
        topPanel.add(toxin);

        TextField temperature=new TextField();
        temperature.setEditable(false);
        topPanel.add(temperature);

        TextField reload=new TextField();
        topPanel.add(reload);

        frame.add(topPanel,BorderLayout.NORTH);

        //底部控制按钮
        Panel bottomPlane=new Panel(new FlowLayout());

        Button nextButton=new Button("Next");
        nextButton.addMouseListener(
                new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        action[0] = player->{
                            server=new Server(MushroomVisual.player=new EntityMushroomData());
                            visual.player=MushroomVisual.player;
                            for(int i=0;i<4;i++)
                                MushroomVisual.player.addEffect(random.nextLong(),0.1);

                        };
                    }
                }
        );
        bottomPlane.add(nextButton);

        frame.add(nextButton,BorderLayout.SOUTH);

        frame.add(visual,BorderLayout.CENTER);

        frame.setVisible(true);


        int reloadTick=1;
        reload.setText("1");
        while(true){
            server.tick();

            if(server.ticks%reloadTick==0){
                if(action[0]!=null){
                    action[0].todo(player);
                    action[0]=null;
                }
                visual.draw();
                int ticks=server.ticks;
                time.setText("Ticks: "+ticks+"  "+ticks/60/20+":"+(ticks/20)%60);
                temperature.setText("temperature: "+player.temperature);
                toxin.setText("toxin: "+player.toxin);
            }

            if(server.ticks%200==0){
                reloadTick=1;
                try{
                    reloadTick= Integer.parseInt(reload.getText());
                    if(reloadTick<1){
                        reloadTick=1;
                        reload.setText("1");
                    }
                }
                catch (NumberFormatException e){
                    reload.setText("1");
                }
            }

            //Thread.sleep(10);
        }
    }
}

interface Action{
    void todo(EntityMushroomData player);
}
