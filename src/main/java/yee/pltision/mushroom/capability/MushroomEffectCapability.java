package yee.pltision.mushroom.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import yee.pltision.Util;
import yee.pltision.mushroom.data.EntityMushroomData;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

@Mod.EventBusSubscriber
public class MushroomEffectCapability {

    public static final Capability<EntityMushroomDataGetter> CAPABILITY = CapabilityManager.get(new CapabilityToken<>(){});


    @SubscribeEvent
    public static void registerCaps(RegisterCapabilitiesEvent event) {
        event.register(EntityMushroomDataGetter.class);
    }

    static class CEntityMushroomDataGetter implements EntityMushroomDataGetter {
        EntityMushroomData data;

        @Override
        public void tick(Player player) {
            data.tick(player);
        }

        @Override
        public EntityMushroomData getEntityData() {

            return data;
        }

        @Override
        public void deserializeNBT(CompoundTag nbt) {
            EntityMushroomDataGetter.deserializeNBT(data,nbt);
        }

        public CEntityMushroomDataGetter(){
            data=new EntityMushroomData();
        }
    }

    /**
     * 用于接收从调试器传回的信息
     */
    public static class DebugHelper{
        public static EntityMushroomData data;

        /**
         * 若需要debug, 在playerTick中插入这个即可。
         * data的线程安全问题不保证, 若出现同步修改异常属正常现象
         */
        public static void debug(EntityMushroomData data){
            DebugHelper.data=data;
            ByteArrayOutputStream bos=new ByteArrayOutputStream(1024*2);
            try {
                new ObjectOutputStream(bos).writeObject(data);
                //System.out.println(data);
                byte[] out=bos.toByteArray();
                DatagramPacket packet=new DatagramPacket(out,0,out.length,InetAddress.getByName("127.0.0.1"),Util.MUSHROOM_DEBUG_DEBUGGER_PORT);
                DatagramSocket socket=new DatagramSocket();
                socket.send(packet);
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public static void setupListener(){
            new Thread(()->{
                try {
                    byte[] buf=new byte[1024*2];
                    DatagramPacket packet=new DatagramPacket(buf,buf.length);
                    InetAddress address=InetAddress.getByName("127.0.0.1");

                    DatagramSocket socket=new DatagramSocket(Util.MUSHROOM_DEBUG_SERVER_PORT, address);

                    while (true){
                        socket.receive(packet);

                        ByteArrayInputStream bis=new ByteArrayInputStream(buf);
                        Object obj=new ObjectInputStream(bis).readObject();

                        if(obj instanceof DebugHelper.Action action){
                            if(data!=null) {
                                action.todo(data);
                            }
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }
        public interface Action extends Serializable{
            void todo(EntityMushroomData data);

            static void sendAction(Action action){
                ByteArrayOutputStream bos=new ByteArrayOutputStream(1024*2);
                try {
                    new ObjectOutputStream(bos).writeObject(action);
                    byte[] out=bos.toByteArray();
                    DatagramPacket packet=new DatagramPacket(out,0,out.length,InetAddress.getByName("127.0.0.1"),Util.MUSHROOM_DEBUG_SERVER_PORT);
                    DatagramSocket socket=new DatagramSocket();
                    socket.send(packet);
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @SubscribeEvent
    public static void tick(TickEvent.PlayerTickEvent event){
        event.player.getCapability(CAPABILITY).ifPresent(cap->{
            DebugHelper.debug(cap.getEntityData());

            //new PlayerVisual(cap.getEntityData()).start();
            cap.tick(event.player);
        });
    }

    @SubscribeEvent
    public static void registerCapability(AttachCapabilitiesEvent<Entity> event){
        //System.out.println("Capability");
        if(event.getObject() instanceof Player){
            event.addCapability(new ResourceLocation(Util.MODID,"mushroom"), new MushroomEffectCapabilityProvider());
        }
    }


    @SubscribeEvent
    public static void onPlayerCloned(PlayerEvent.Clone event) {
        if (!event.isWasDeath()) {
            LazyOptional<EntityMushroomDataGetter> oldSpeedCap = event.getOriginal().getCapability(CAPABILITY);
            LazyOptional<EntityMushroomDataGetter> newSpeedCap = event.getPlayer().getCapability(CAPABILITY);
            if (oldSpeedCap.isPresent() && newSpeedCap.isPresent()) {
                newSpeedCap.ifPresent((newCap) -> {
                    oldSpeedCap.ifPresent((oldCap) -> {
                        newCap.deserializeNBT(oldCap.serializeNBT());
                    });
                });
            }
        }
    }

    @SubscribeEvent
    public static void save(PlayerEvent.SaveToFile event){
        File save=new File(event.getPlayerDirectory().getPath()+"/mushroom_data.dat");
        event.getPlayer().getCapability(CAPABILITY).ifPresent(cap->{
            try {
                NbtIo.write(cap.serializeNBT(),save);
            } catch (IOException e) {
                Util.LOGGER.error(
                        "MushroomEffect: Cannot save player mushroom effect data to file:\n" + e
                );
            }
        });
    }

    @SubscribeEvent
    public static void load(PlayerEvent.LoadFromFile event){
        File save=new File(event.getPlayerDirectory().getPath()+"/mushroom_data.dat");
        event.getPlayer().getCapability(CAPABILITY).ifPresent(cap->{
            if(save.isFile()){
                try {
                    cap.deserializeNBT(NbtIo.read(save));
                } catch (IOException e) {
                    Util.LOGGER.warn(
                            "MushroomEffect: Cannot read player mushroom effect data in file:\n" + e
                    );
                }
            }
            else{
                Util.LOGGER.info(
                        "MushroomEffect: Cannot found player mushroom effect data in "+save.getPath()+" . Maybe it never been created, or it been deleted."
                );
            }
        });
    }


}
