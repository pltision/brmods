package yee.pltision.backrooms.block.farmland;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import static yee.pltision.Util.RANDOM;


public interface IFarmland {

    ResourceKey<Biome> LEVEL1_SQUARE=ResourceKey.create(Registry.BIOME_REGISTRY, new ResourceLocation("backrooms:level1/square"));
    ResourceKey<Biome> LEVEL1_UNKNOWN=ResourceKey.create(Registry.BIOME_REGISTRY, new ResourceLocation("backrooms:level1/unknown"));

    HashMap<ResourceKey<Biome>,Double> BIOME_NUTRIENT_MAP=new HashMap<>(Map.of(
            LEVEL1_SQUARE,0.4,
            LEVEL1_UNKNOWN,0.4
    ));

    HashMap<ResourceKey<Biome>,Double> BIOME_DRY_MAP=new HashMap<>(Map.of(
            LEVEL1_SQUARE,1.2,
            LEVEL1_UNKNOWN,1.2
    ));

    /**
     * @return 表面的湿润程度，若为1则表面布满水。
     */
    double getDry(BlockState state, LevelReader level, BlockPos pos);
    default double getDry(LevelReader level, BlockPos pos){
        return getDry(level.getBlockState(pos),level,pos);
    }

    /**
     * @return  群系湿润系数，若未定则返回1，以平原为1记。
     * <ul>
     *     <li>level1_square: 1.2</li>
     *     <li>level1_unknown: 1.2</li>
     * </ul>
     */
    static double biomeDryFactor(@NotNull LevelReader level, BlockPos pos){
        Double value= BIOME_DRY_MAP.get(level.getBiome(pos).unwrapKey().get());
        return value==null?1:value;
    }

    static double randomComputeDry(double base,LevelReader level,BlockPos pos){
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
            Block block= level.getBlockState(new BlockPos(
                    x+RANDOM.nextInt()%5,
                    y+x+RANDOM.nextInt()%3,
                    z+RANDOM.nextInt()%3)
                    ).getBlock();
            if(block instanceof IWaterBlock water)
                sum+= water.getWater();
            else if(block instanceof LiquidBlock liquid && liquid.getFluid()== Fluids.WATER)
                sum+=1;

        }
        return base*Math.log(sum);
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

    /**
     * @return  群系营养系数，若未定则返回0.5，以平原为1记。
     * <ul>
     *     <li>level1_square: 0.4</li>
     *     <li>level1_unknown: 0.4</li>
     * </ul>
     */
    static double biomeNutrientFactor(@NotNull LevelReader level, BlockPos pos){
        Double value= BIOME_NUTRIENT_MAP.get(level.getBiome(pos).unwrapKey().get());
        return value==null?0.5:value;
    }

    default double nutrientFactor(@NotNull LevelReader level, BlockPos pos){
        return nutrientFactor(level.getBlockState(pos),level,pos);
    }

    default void beDry(BlockState state,@NotNull LevelReader level, BlockPos pos){}

}
