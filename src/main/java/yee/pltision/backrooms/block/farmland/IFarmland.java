package yee.pltision.backrooms.block.farmland;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import static yee.pltision.Util.RANDOM;


public interface IFarmland {

    ResourceKey<Biome> LEVEL1_SQUARE=ResourceKey.create(Registry.BIOME_REGISTRY, new ResourceLocation("backrooms:level1/square"));
    ResourceKey<Biome> LEVEL1_UNKNOWN=ResourceKey.create(Registry.BIOME_REGISTRY, new ResourceLocation("backrooms:level1/unknown"));

    HashMap<ResourceKey<Biome>,Double> BIOME_NUTRIENT_MAP=new HashMap<>(Map.of(
            LEVEL1_SQUARE,0.6,
            LEVEL1_UNKNOWN,0.6
    ));

    HashMap<ResourceKey<Biome>,Double> BIOME_WET_MAP=new HashMap<>(Map.of(
            LEVEL1_SQUARE,1.2,
            LEVEL1_UNKNOWN,1.2
    ));

    /**
     * @return 表面的湿润程度，若为1则表面布满水。
     */
    double getWet(BlockState state, LevelReader level, BlockPos pos);
    default double getWet(LevelReader level, BlockPos pos){
        return getWet(level.getBlockState(pos),level,pos);
    }

    /**
     * @return  群系湿润系数，若未定则返回1，以平原为1记。
     * <ul>
     *     <li>level1_square: 1.2</li>
     *     <li>level1_unknown: 1.2</li>
     * </ul>
     */
    static double biomeWetFactor(@NotNull LevelReader level, BlockPos pos){
        Double value= BIOME_WET_MAP.get(level.getBiome(pos).unwrapKey().get());
        return value==null?1:value;
    }

    /*
        含水量计算公式: \frac{b}{\ln\left(c+1\right)}\cdot\ln(x+1)
        return=base/ln(c+1)*ln(sum+1)
        其中ln(c+1)提前计算出来, c=(1/8)*随机总数
        x是计算所有方块的含水量的和, 计算c个方块的含水量为1时计算出的含水量可达b
     */
    /**
     * @return 从传入的坐标开始第一点，若其上面不是实体方块则有一点搜寻到其上方的第一个实体方块作为第二个点，在这两个点周围2格随机，若两个点则检测一倍的随机。最多检测10格的方块。
     * 用于检测水滴下来的湿度，用于生成渗水石。
     */
    static double randomUpToDownWet(double base, LevelReader level, BlockPos pos){
        final double P1_RANDOM_FACTOR=(3*5*3)/8D/10D;
        final double P2_RANDOM_FACTOR=(5*5*5)/8D/10D;
        int x=pos.getX(),y=pos.getY(),z=pos.getZ();
        double sum=0;

        if(
                level.getBlockState(pos.east()).getMaterial().isSolid()&&
                        level.getBlockState(pos.west()).getMaterial().isSolid()&&
                        level.getBlockState(pos.south()).getMaterial().isSolid()&&
                        level.getBlockState(pos.north()).getMaterial().isSolid()&&
                        level.getBlockState(pos.below()).getMaterial().isSolid()
        )base+=0.05;

        for(int i=0;i<10;i++){  //第一个点
            sum+= getBlockWet(level.getBlockState(new BlockPos(
                    x+RANDOM.nextInt()%2,
                    y+RANDOM.nextInt()%3+1,
                    z+RANDOM.nextInt()%2)
            ));
        }
        if(!level.getBlockState(pos.above()).getMaterial().isSolid()){
            int i=1;
            for(;i<11;i++){
                if(level.getBlockState(new BlockPos(x,y+i,z)).getMaterial().isSolid())
                    break;
            }
            if(i!=11){
                //第二点
                //sum*=1/2D;//稀释第一个点
                y+=i;
                for(i=0;i<10;i++){  //第二点的随机
                    sum+= getBlockWet(level.getBlockState(new BlockPos(
                            x+RANDOM.nextInt()%3,
                            y+RANDOM.nextInt()%3,
                            z+RANDOM.nextInt()%3)
                    ));
                }
                return base/Math.log(20/8D+1)*Math.log(sum+1)* biomeWetFactor(level,pos);
            }
        }

        return base/Math.log(10/8D+1)*Math.log(sum+1)* biomeWetFactor(level,pos);
    }

    double randomPlaneWet_CEXP=Math.log(20/8D+1);
    static double randomPlaneWet(double base, LevelReader level, BlockPos pos){
        int x=pos.getX(),y=pos.getY(),z=pos.getZ();
        double sum=0;

        if(
                level.getBlockState(pos.east()).getMaterial().isSolid()&&
                level.getBlockState(pos.west()).getMaterial().isSolid()&&
                level.getBlockState(pos.south()).getMaterial().isSolid()&&
                level.getBlockState(pos.north()).getMaterial().isSolid()&&
                level.getBlockState(pos.below()).getMaterial().isSolid()
        )base+=0.05;

        for(int i=0;i<10;i++){
            sum+= getBlockWet(level.getBlockState(new BlockPos(
                    x+RANDOM.nextInt()%3,
                    y+RANDOM.nextInt()%2,
                    z+RANDOM.nextInt()%3)
            ));
        }
        for(int i=0;i<10;i++){
            sum+= getBlockWet(level.getBlockState(new BlockPos(
                    x+RANDOM.nextInt()%5,
                    y,
                    z+RANDOM.nextInt()%5)
            ));
        }
        return base/randomPlaneWet_CEXP*Math.log(sum+1)* biomeWetFactor(level,pos);
    }
    static double randomCubeWet(double base, LevelReader level, BlockPos pos){
        final double RANDOM_FACTOR=(5*5*5)/8D/20D;
        int x=pos.getX(),y=pos.getY(),z=pos.getZ();
        double sum=0;

        if(
                level.getBlockState(pos.east()).getMaterial().isSolid()&&
                        level.getBlockState(pos.west()).getMaterial().isSolid()&&
                        level.getBlockState(pos.south()).getMaterial().isSolid()&&
                        level.getBlockState(pos.north()).getMaterial().isSolid()&&
                        level.getBlockState(pos.below()).getMaterial().isSolid()
        )base+=0.05;

        for(int i=0;i<20;i++){
            sum+= getBlockWet(level.getBlockState(new BlockPos(
                    x+RANDOM.nextInt()%3,
                    y+RANDOM.nextInt()%3,
                    z+RANDOM.nextInt()%3)
            ))*RANDOM_FACTOR;
        }
        return base*Math.log(sum+1)* biomeWetFactor(level,pos);
    }
    static double getBlockWet(BlockState state){
        Block block= state.getBlock();
        if(block instanceof IWaterBlock water)
            return water.getWater();
        else if(block instanceof LiquidBlock liquid && liquid.getFluid()== Fluids.WATER)
            return 1;
        else if(block instanceof SimpleWaterloggedBlock && state.getValue(BlockStateProperties.WATERLOGGED))
            return 1;
        else return 0;
    }

    /**
     * @return 表面的岩石完整度和硬度，以泥土记作0.5为标准。主要地决定了植物扎根的难度。
     * <p>原版方块并未实现，仅作参考。</p>
     * <ul>
     *     <li>水: 0</li>
     *     <li>沙子: 0.3</li>
     *     <li>泥土: 0.5</li>
     *     <li>沙砾: 0.6</li>
     *     <li>圆石: 0.7</li>
     *     <li>石头: 0.8</li>
     *     <li>完整混凝土: 0.8</li>
     *     <li>黑曜石: 0.9</li>
     * </ul>
     */
    double getRocky(BlockState state, LevelReader level, BlockPos pos);
    default double getRocky(LevelReader level, BlockPos pos){
        return getRocky(level.getBlockState(pos),level,pos);
    }

    /**
     * @return 土地的营养系数，0为完全没营养，我想不出没营养的东西，空气？此外，群系也决定营养。
     * <p>为了可扩展性，将其作为系数。</p>
     * <p>方块系数，若在范围外则乘0.5，以泥土为1记。</p>
     * <ul>
     *     <li>水: 0.2</li>
     *     <li>沙子: 0.5</li>
     *     <li>石头: 0.5</li>
     *     <li>混凝土: 0.5</li>
     *     <li>圆石: 0.5</li>
     *     <li>沙砾: 0.6</li>
     *     <li>砂土: 0.8</li>
     *     <li>草方块: 0.9</li>
     *     <li>泥土: 1</li>
     *     <li>耕地: 1.1</li>
     * </ul>
     * <p>大部分植物在营养低于0.5时无法存活。</p>
     */
    double nutrientFactor(BlockState state, @NotNull LevelReader level, BlockPos pos);
    static double getNutrient(@NotNull LevelReader level, BlockPos pos){
        BlockState state=level.getBlockState(pos);
        if(level.getBlockState(pos).getBlock() instanceof IFarmland farmlandBlock)
            return farmlandBlock.nutrientFactor(state,level,pos);
        else return 0.3;
    }

    /**
     * @return  群系营养系数，若未定则返回0.7，以平原为1记。
     * <ul>
     *     <li>level1_square: 0.6</li>
     *     <li>level1_unknown: 0.6</li>
     * </ul>
     */
    static double biomeNutrientFactor(@NotNull LevelReader level, BlockPos pos){
        Double value= BIOME_NUTRIENT_MAP.get(level.getBiome(pos).unwrapKey().get());
        return value==null?0.5:value;
    }

    default double nutrientFactor(@NotNull LevelReader level, BlockPos pos){
        return nutrientFactor(level.getBlockState(pos),level,pos);
    }

    default BlockState beWet(BlockState state, @NotNull LevelReader level, BlockPos pos){return state;}

}
