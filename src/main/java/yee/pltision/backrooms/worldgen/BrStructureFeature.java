package yee.pltision.backrooms.worldgen;

import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.StructureFeature;

public interface BrStructureFeature{
    default GenerationStep.Decoration step() {
        return GenerationStep.Decoration.SURFACE_STRUCTURES;
    }
    StructureFeature<?> feature();
}
