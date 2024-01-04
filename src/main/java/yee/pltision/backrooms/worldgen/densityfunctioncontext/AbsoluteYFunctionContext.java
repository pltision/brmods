package yee.pltision.backrooms.worldgen.densityfunctioncontext;

import net.minecraft.world.level.levelgen.DensityFunction;

public class AbsoluteYFunctionContext extends DeviationableFunctionContext{

    public AbsoluteYFunctionContext(int startX, int startY, int startZ) {
        super(startX, startY, startZ);
    }

    public AbsoluteYFunctionContext(DensityFunction.FunctionContext context, int absoluteY) {
        super(context.blockX(), absoluteY, context.blockZ());
    }

    public AbsoluteYFunctionContext(DensityFunction.FunctionContext context) {
        super(context);
    }
}
