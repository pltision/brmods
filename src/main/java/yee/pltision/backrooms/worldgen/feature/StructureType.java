package yee.pltision.backrooms.worldgen.feature;

import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.JigsawFeature;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import yee.pltision.Util;


public class StructureType extends JigsawFeature {

    public StructureType(int height,GenerationStep.Decoration step) {
        super(JigsawConfiguration.CODEC, height, true, true, (p_197185_) -> true);
        this.step=step;
    }
    GenerationStep.Decoration step;
    @Override
    public GenerationStep.@NotNull Decoration step() {
        return step;
    }

    public static final DeferredRegister<StructureFeature<?>> REGISTER =DeferredRegister.create(ForgeRegistries.STRUCTURE_FEATURES, Util.MODID);

    public static final RegistryObject<StructureFeature<?>> LEVEL0_STRUCTURE=REGISTER.register("level0_structure",()->new StructureType(50, GenerationStep.Decoration.UNDERGROUND_STRUCTURES));

}
