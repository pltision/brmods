package yee.pltision.backrooms.worldgen.densityfunctioncontext;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.DensityFunction;

public class CFunctionContext implements DensityFunction.FunctionContext {
    public int x,y,z;

    public void setPos(BlockPos pos){
        x=pos.getX();
        y=pos.getY();
        z=pos.getZ();
    }

    public CFunctionContext(){}

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
