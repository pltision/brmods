package yee.pltision.mushroom.mc;

import net.minecraft.client.renderer.EffectInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import yee.pltision.Util;

import java.util.Random;

@Mod.EventBusSubscriber
public class MushroomMobEffects {
    public static final Random EFFECT_ID_RANDOM=new Random(817098);
    public static final DeferredRegister<MobEffect> REGISTER=DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, Util.MODID);

    public static final RegistryObject<MobEffect> MUSHROOM_POTION=REGISTER.register(
            "mushroom_potion",()-> new MobEffect(MobEffectCategory.HARMFUL,EFFECT_ID_RANDOM.nextInt()){
                public boolean isDurationEffectTick(int p_19631_, int p_19632_) {
                    return true;
                }
                public void applyEffectTick(@NotNull LivingEntity entity, int level) {
//                    System.out.println(entity.tickCount);
                    if((((entity.tickCount>>4)+entity.hashCode())&Integer.MAX_VALUE)%Math.max(1,(63-((level-1)<<4)<<2))==0){
                        if(entity.getHealth()>10) entity.hurt(DamageSource.MAGIC, 0.7F);
                        else entity.hurt(DamageSource.MAGIC, 0.4f);

                    }
                }
            }
    );

}
