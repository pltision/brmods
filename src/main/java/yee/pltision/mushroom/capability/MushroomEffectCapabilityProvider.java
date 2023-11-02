package yee.pltision.mushroom.capability;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MushroomEffectCapabilityProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    public static final LazyOptional<EntityMushroomDataGetter> optional =LazyOptional.of(MushroomEffectCapability.CEntityMushroomDataGetter::new);

    @Nullable EntityMushroomDataGetter getter;

    @NotNull EntityMushroomDataGetter getGetterOrCreate(){
        return getter==null?getter=new MushroomEffectCapability.CEntityMushroomDataGetter():getter;
    }

    @Override
    public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, Direction side) {
        return optional.cast();
    }

    @Override
    public CompoundTag serializeNBT() {
        return getGetterOrCreate().serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        getGetterOrCreate().deserializeNBT(nbt);
    }
}
