package yee.pltision.backrooms.worldgen.densityfunctioncontext;

import net.minecraft.world.level.levelgen.DensityFunction;

public class DeviationableFunctionContext implements DensityFunction.FunctionContext{
    public final int sx,sy,sz;
    public int dx,dy,dz;

    public DeviationableFunctionContext(int startX, int startY, int startZ) {
        sx = startX;
        sy = startY;
        sz = startZ;
    }

    public DeviationableFunctionContext(DensityFunction.FunctionContext context){
        sx=context.blockX();
        sy=context.blockY();
        sz=context.blockZ();
    }

    @Override
    public int blockX() {
        return sx+dx;
    }

    @Override
    public int blockY() {
        return sy+dy;
    }

    @Override
    public int blockZ() {
        return sz+dz;
    }
}
