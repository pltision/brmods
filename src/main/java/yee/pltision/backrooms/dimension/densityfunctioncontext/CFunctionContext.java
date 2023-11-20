package yee.pltision.backrooms.dimension.densityfunctioncontext;

import net.minecraft.world.level.levelgen.DensityFunction;

public class CFunctionContext implements DensityFunction.FunctionContext {
    public final int x,y,z;

    public CFunctionContext(int startX, int startY, int startZ) {
        x = startX;
        y = startY;
        z = startZ;
    }

    public CFunctionContext(DensityFunction.FunctionContext context){
        x=context.blockX();
        y=context.blockY();
        z=context.blockZ();
    }

    @Override
    public int blockX() {
        return x;
    }

    @Override
    public int blockY() {
        return y;
    }

    @Override
    public int blockZ() {
        return z;
    }
}
