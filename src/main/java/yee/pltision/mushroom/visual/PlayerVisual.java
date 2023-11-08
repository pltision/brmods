package yee.pltision.mushroom.visual;

import yee.pltision.Util;
import yee.pltision.mushroom.capability.MushroomEffectCapability;
import yee.pltision.mushroom.data.EntityMushroomData;
import yee.pltision.mushroom.data.MushroomEffect;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.*;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import static yee.pltision.mushroom.capability.MushroomEffectCapability.DebugHelper.Action.sendAction;

public class PlayerVisual extends Thread{
    public static void main(String[] args) {
        new PlayerVisual(null).run();
    }

    interface Action{
        void todo(PlayerVisual visual);
    }

    public PlayerVisual(EntityMushroomData data){
        this.data=data;
        visual=new VisualFrame(data);
    }

    public EntityMushroomData data;
    public VisualFrame visual;
    public boolean close=false;


    public void run() {
        Frame frame=new Frame();
        frame.setBounds(100,100,1000,500);

        //1010110

        Random random =new Random(11243);

        final LinkedList<Action> actions = new LinkedList<>();


        BorderLayout layout=new BorderLayout();
        layout.setHgap(80);
        frame.setLayout(layout);

        //顶部玩家信息
        Panel topPanel=new Panel();
        topPanel.setLayout(new GridLayout(1,3));


        TextField toxin=new TextField();
        toxin.setEditable(false);
        topPanel.add(toxin);

        TextField temperature=new TextField();
        temperature.setEditable(false);
        topPanel.add(temperature);

        frame.add(topPanel,BorderLayout.NORTH);

        //底部控制按钮
        Panel bottomPlane=new Panel(new FlowLayout());

        GridBagLayout effectLayout=new GridBagLayout();
        Panel effectActionPlane=new Panel();
        GridBagConstraints con=new GridBagConstraints();
        con.fill=GridBagConstraints.BOTH;

        TextField effectType=new TextField("0000000000000000000000000000000000000000000000000000000000000000");
        TextField effectValue=new TextField("0.01");
        Button addButton=new Button("Add");
        addButton.addMouseListener(new MouseAdapter() {
            //final EntityMushroomData finalData= data;
            @Override
            public void mouseClicked(MouseEvent e) {
                long type=0;
                try {
                    type = Long.parseLong(effectType.getText(),2);
                }
                catch ( NumberFormatException exception){
                    effectType.setText("0");
                }
                double value = 0.01;
                try {
                    value = Double.parseDouble(effectValue.getText());
                }
                catch ( NumberFormatException exception){
                    effectValue.setText("0.01");
                }
                long finalType = type;
                double finalValue = value;
                sendAction(player -> player.addEffect(finalType, finalValue));
            }
        });
        Button randomButton=new Button("Random");
        randomButton.addMouseListener(new MouseAdapter() {
            //final EntityMushroomData finalData= data;
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getButton()==MouseEvent.BUTTON1){
                    double value = 0.01;
                    try {
                        value = Double.parseDouble(effectValue.getText());
                    }
                    catch ( NumberFormatException exception){
                        effectValue.setText("0.01");
                    }

                    double finalValue = value;
                    sendAction(player -> player.addEffect(random.nextLong(), finalValue));
                }
                else{
                    effectType.setText(VisualFrame.toFullBinaryString(random.nextLong()).toString());
                }

            }
        });
        Button clearButton=new Button("Clear");
        clearButton.addMouseListener(new MouseAdapter() {
            //final EntityMushroomData finalData= data;
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getButton()==MouseEvent.BUTTON2) {
                    sendAction(player -> player.effects.clear());
                }
            }
        });
        {
            con.weighty = 1;
            con.weightx = 1;
            effectActionPlane.add(addButton, con);
            con.weighty = 1;
            con.weightx = 10;
            effectActionPlane.add(effectType, con);
            con.weighty = 1;
            con.weightx = 1;
            effectActionPlane.add(effectValue, con);
            con.weighty = 1;
            con.weightx = 1;
            effectActionPlane.add(randomButton, con);
            con.weighty = 1;
            con.weightx = 1;
            effectActionPlane.add(clearButton, con);
        }
        bottomPlane.add(effectActionPlane);

        frame.addWindowListener(
                new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        frame.dispose();
                        close=true;
                    }
                }
        );

        frame.add(bottomPlane,BorderLayout.SOUTH);

        frame.add(visual,BorderLayout.CENTER);

        frame.setVisible(true);

        byte[] buf=new byte[1024*2];
        DatagramPacket packet=new DatagramPacket(buf,buf.length);
        InetAddress address=null;
        DatagramSocket socket=null;
        try {
            address=InetAddress.getByName("127.0.0.1");
            socket=new DatagramSocket(Util.MUSHROOM_DEBUG_DEBUGGER_PORT, address);

        } catch (IOException e) {
            e.printStackTrace();
        }

        while(true){
            if(close) return;

            try {
                socket.receive(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
            ByteArrayInputStream bis=new ByteArrayInputStream(buf);
            Object obj=null;
            try {
                obj=new ObjectInputStream(bis).readObject();
            } catch (Exception e) {
                continue;
            }
            if(obj instanceof EntityMushroomData data){
                this.data=data;
                visual.player=data;
            }
            else if(obj instanceof Action action){
                actions.push(action);
            }

            if(this.data==null) continue;


            temperature.setText("temperature: "+ data.temperature);
            toxin.setText("toxin: "+ data.toxin);
            visual.draw();

            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

