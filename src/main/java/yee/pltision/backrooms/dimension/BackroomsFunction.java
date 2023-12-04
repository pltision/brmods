package yee.pltision.backrooms.dimension;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.*;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.level.levelgen.*;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import yee.pltision.Util;
import yee.pltision.backrooms.dimension.densityfunctioncontext.AbsoluteYFunctionContext;
import yee.pltision.backrooms.dimension.densityfunctioncontext.CFunctionContext;
import yee.pltision.backrooms.dimension.densityfunctioncontext.DeviationableFunctionContext;
import yee.pltision.rectraft.RectRaft;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Stack;
import java.util.function.Function;

@SuppressWarnings("EnhancedSwitchMigration")
@Mod.EventBusSubscriber(modid = Util.MODID)
public class BackroomsFunction {

    Codec<Double> NOISE_VALUE_CODEC = Codec.doubleRange(-1000000.0D, 1000000.0D);

    public static final int BASE_HEIGHT=49;
    public static final int LEVEL1_TOP=95;
    public static final int LEVEL1_MINABLE_TOP =93;
    public static final int LEVEL1_MINABLE_BOTTOM =-31;
    public static final int LEVEL1_HEIGHT=6;

    public BackroomsFunction() throws IOException {
    }

    static <A, O> Codec<O> singleArgumentCodec(Codec<A> p_208276_, Function<A, O> p_208277_, Function<O, A> p_208278_) {
        return p_208276_.fieldOf("argument").xmap(p_208277_, p_208278_).codec();
    }

    public record Level0GraphFunction(DensityFunction graph, DensityFunction turn) implements DensityFunction.SimpleFunction{

        public static final Codec<Level0GraphFunction> CODEC = RecordCodecBuilder.create((p_208359_) ->
                p_208359_.group(
                        DensityFunction.HOLDER_HELPER_CODEC.fieldOf("turn").forGetter(Level0GraphFunction::turn),
                        DensityFunction.HOLDER_HELPER_CODEC.fieldOf("graph").forGetter(Level0GraphFunction::graph)
                ).apply(p_208359_, Level0GraphFunction::new));

        @Override
        public double compute(@NotNull FunctionContext c) {
            FunctionContext context=new FunctionContext(){

                @Override
                public int blockX() {
                    return c.blockX()>>1;
                }

                @Override
                public int blockY() {
                    return BASE_HEIGHT;
                }

                @Override
                public int blockZ() {
                    return c.blockZ()>>1;
                }
            };


            return LEVEL0_BMPS[( ( LEVEL0_BMPS.length + (int)(graph.compute(context)*(LEVEL0_BMPS.length-3.1415926535)) /*+ ((int)(graph.compute(context)/2)<<2)*/ ) &0xffffff)% LEVEL0_BMPS.length]
                    .getPoint(c.blockX()&0b11111, c.blockZ()&0b11111, (byte) turn.compute(c))&0xff;
        }

        @Override
        public double minValue() {
            return 0;
        }

        @Override
        public double maxValue() {
            return 0xff;

        }

        @Override
        public @NotNull Codec<? extends DensityFunction> codec() {
            return CODEC;
        }
    }

    public record Level0BlockFunction(DensityFunction graph,DensityFunction type) implements DensityFunction.SimpleFunction
    {
        public static byte getDoorFrameType(double functionReturn){
            if(functionReturn>=1&&functionReturn<4) return 1;
            if(functionReturn>=4&&functionReturn<8) return 2;
            return 0;
        }
        public static final Codec<Level0BlockFunction> CODEC = RecordCodecBuilder.create((p_208359_) ->
                p_208359_.group(
                        DensityFunction.HOLDER_HELPER_CODEC.fieldOf("graph").forGetter(Level0BlockFunction::graph),
                        DensityFunction.HOLDER_HELPER_CODEC.fieldOf("block_type").forGetter(Level0BlockFunction::type)
                ).apply(p_208359_, Level0BlockFunction::new));

        @Override
        public double compute(@NotNull FunctionContext context) {
            if(context.blockY()<=BASE_HEIGHT) return 1;
            if(context.blockY()>BASE_HEIGHT+5) return 1;
            switch ((int) graph.compute(context))
            {
                case 0: return 1;
                case 0xCF:{
                    AverageTypeComputer computer=new AverageTypeComputer();
                    searchType(context,this.graph,type,computer);

                    double average;
                    if(computer.outedOfBounds) average=0;
                    else average= computer.average();

                    switch (getDoorFrameType(average)){
                        case 1:
                            if(context.blockY()>BASE_HEIGHT+2&&context.blockY()<=BASE_HEIGHT+4){
                                return 0;
                            }
                            return 1;
                        case 2:
                            if(context.blockY()>BASE_HEIGHT+3){
                                return 0;
                            }
                            return 1;
                        default:
                            if(context.blockY()<=BASE_HEIGHT+2){
                                return 0;
                            }
                            return 1;
                    }
                }
                default:
                    return 0;
            }
            //return graph.compute(context)==0?1:0;
        }

        @Override
        public double minValue() {
            return 0;
        }

        @Override
        public double maxValue() {
            return 1;
        }

        @Override
        public @NotNull Codec<? extends DensityFunction> codec() {
            return CODEC;
        }
    }

    public record Level0ErosionFunction(DensityFunction noise) implements DensityFunction.SimpleFunction{

        public static final Codec<TestFunction> CODEC= DensityFunctions.DIRECT_CODEC.fieldOf("noise").xmap(TestFunction::new, TestFunction::noise).codec();

        @Override
        public double compute(@NotNull FunctionContext c) {

            int x=(c.blockX()&0xfffffff0)>>2,z=(c.blockZ()&0xfffffff0)>>2;
            AbsoluteYFunctionContext context=new AbsoluteYFunctionContext(x,BASE_HEIGHT,z);

            int xSum=1,zSum=1;
            final int xMod,zMod;


            if (noise.compute(context) < -1) {
                //计算xz轴正负范围
                {
                    for (int i = 0; i < 100; i++) {
                        context.dx += 4;
                        if (noise.compute(context) > -1) {
                            break;
                        }
                        xSum++;
                    }
                    context.dx = 0;
                    for (int i = 0; i < 100; i++) {
                        context.dx -= 4;
                        if (noise.compute(context) > -1) {
                            break;
                        }
                        xSum++;
                    }
                    context.dx = 0;
                    for (int i = 0; i < 100; i++) {
                        context.dz += 4;
                        if (noise.compute(context) > -1) {
                            break;
                        }
                        zSum++;
                    }
                    context.dz = 0;
                    for (int i = 0; i < 100; i++) {
                        context.dz -= 4;
                        if (noise.compute(context) > -1) {
                            break;
                        }
                        zSum++;
                    }
                    context.dz = 0;
                }
                xMod=0b111;
                zMod=0b1111;
            }
            else if(noise.compute(context) > 1){
                {
                    for (int i = 0; i < 100; i++) {
                        context.dx += 4;
                        if (noise.compute(context) < 1) {
                            break;
                        }
                        xSum++;
                    }
                    context.dx = 0;
                    for (int i = 0; i < 100; i++) {
                        context.dx -= 4;
                        if (noise.compute(context) < 1) {
                            break;
                        }
                        xSum++;
                    }
                    context.dx = 0;
                    for (int i = 0; i < 100; i++) {
                        context.dz += 4;
                        if (noise.compute(context) < 1) {
                            break;
                        }
                        zSum++;
                    }
                    context.dz = 0;
                    for (int i = 0; i < 100; i++) {
                        context.dz -= 4;
                        if (noise.compute(context) < 1) {
                            break;
                        }
                        zSum++;
                    }
                    context.dz = 0;
                }
                xMod=0b1111;
                zMod=0b111;
            }
            else return 0;

            //if((x&0b11000)==0) return 0.11;

            if((z&0b11100)==0&&(xSum&xMod)==0){
                //if(((xSum^92136)&0b1111111)==0) return 0.20;
                if((x&0b11100)==0&&(zSum&zMod)==0) return 0.12;
                if((c.blockZ()&0b1111)>=8) return 0;
                return 0.10;
            }
            if((x&0b11100)==0&&(zSum&zMod)==0){
                //if(((zSum^145436)&0b1111111)==0) return 0.21;
                if((c.blockX()&0b1111)>=8) return 0;
                return 0.11;
            }

            return 0;
        }

        @Override
        public double minValue() {
            return 0;
        }

        @Override
        public double maxValue() {
            return 2;
        }

        @Override
        public @NotNull Codec<? extends DensityFunction> codec() {
            return CODEC;
        }
    }

    public record Noise(Holder<NormalNoise.NoiseParameters> noiseData, @javax.annotation.Nullable NormalNoise noise, double xzScale, double yScale) implements DensityFunction.SimpleFunction {
        public static final MapCodec<Noise> DATA_CODEC = RecordCodecBuilder.mapCodec((p_208798_) -> p_208798_.group(
                NormalNoise.NoiseParameters.CODEC.fieldOf("noise").forGetter(Noise::noiseData),
                Codec.DOUBLE.fieldOf("xz_scale").forGetter(Noise::xzScale),
                Codec.DOUBLE.fieldOf("y_scale").forGetter(Noise::yScale)).apply(p_208798_, Noise::createUnseeded));
        public static final Codec<Noise> CODEC = DATA_CODEC.codec();

        public static Noise createUnseeded(Holder<NormalNoise.NoiseParameters> p_208802_, @Deprecated double p_208803_, double p_208804_) {
            return new Noise(p_208802_, NormalNoise.create(new LegacyRandomSource(seed),p_208802_.value()) , p_208803_, p_208804_);
        }

        public double compute(DensityFunction.@NotNull FunctionContext p_208800_) {
            return this.noise == null ? 0.0D : this.noise.getValue((double)p_208800_.blockX() * this.xzScale, (double)p_208800_.blockY() * this.yScale, (double)p_208800_.blockZ() * this.xzScale);
        }

        public double minValue() {
            return -this.maxValue();
        }

        public double maxValue() {
            return this.noise == null ? 2.0D : this.noise.maxValue();
        }

        public @NotNull Codec<? extends DensityFunction> codec() {
            return CODEC;
        }
    }

    public record RemoveShortSpace(DensityFunction noise, int min)implements DensityFunction.SimpleFunction{
        public static final MapCodec<RemoveShortSpace> DATA_CODEC = RecordCodecBuilder.mapCodec((p_208798_) -> p_208798_.group(
                        DensityFunction.HOLDER_HELPER_CODEC.fieldOf("noise").forGetter(RemoveShortSpace::noise),
                        Codec.INT.fieldOf("min").forGetter(RemoveShortSpace::min)
                        //Codec.INT.fieldOf("max").forGetter(RemoveShortSpace::max)
                ).apply(p_208798_, RemoveShortSpace::new));
        public static final Codec<RemoveShortSpace> CODEC = DATA_CODEC.codec();

        @Override
        public double compute(@NotNull FunctionContext c) {
            if(noise.compute(c)>0) return 1;
            int sum=1;
            DeviationableFunctionContext context=new DeviationableFunctionContext(c);

            for(int i=min;i>0;i--){
                context.dy++;
                if(noise.compute(context)>0) break;
                sum++;
            }
            context.dy=0;
            for(int i=min;i>0;i--){
                context.dy--;
                if(noise.compute(context)>0) break;
                sum++;
            }
            if(sum>min) return 0;

            return 1;
        }

        @Override
        public double minValue() {
            return 0;
        }

        @Override
        public double maxValue() {
            return 1;
        }

        @Override
        public @NotNull Codec<? extends DensityFunction> codec() {
            return CODEC;
        }
    }


    public record TestFunction(DensityFunction noise) implements DensityFunction.SimpleFunction {

        public static final Codec<TestFunction> CODEC = DensityFunctions.DIRECT_CODEC.fieldOf("noise").xmap(TestFunction::new, TestFunction::noise).codec();

        @Override
        public double compute(@NotNull FunctionContext p_208223_) {
            return noise.compute(p_208223_);
        }

        @Override
        public double minValue() {
            return -100;
        }

        @Override
        public double maxValue() {
            return 100;
        }

        @Override
        public @NotNull Codec<? extends DensityFunction> codec() {
            return CODEC;
        }
    }

    public record ConstantBlockAverageFunction(DensityFunction argument)implements DensityFunction.SimpleFunction{
        public static final Codec<ConstantBlockAverageFunction> CODEC = DensityFunctions.DIRECT_CODEC.fieldOf("argument").xmap(ConstantBlockAverageFunction::new, ConstantBlockAverageFunction::argument).codec();

        @Override
        public double compute(@NotNull FunctionContext c) {
            int x=c.blockX()&0xFFFFFFFC,z=c.blockZ()&0xFFFFFFFC;

            AbsoluteYFunctionContext context=new AbsoluteYFunctionContext(x,BASE_HEIGHT,z);

            double average=0;

            context.dx=0;
            for(int i=0;i<4;i++){
                context.dz=0;
                for(int j=0;j<4;j++){
                    average+= argument().compute(context);
                    context.dz++;
                }
                context.dx++;
            }

            return average/16;
        }

        @Override
        public double minValue() {
            return argument.minValue();
        }

        @Override
        public double maxValue() {
            return argument.maxValue();
        }

        @Override
        public @NotNull Codec<? extends DensityFunction> codec() {
            return CODEC;
        }
    }

    public record MinSquareFunction(DensityFunction noise)implements DensityFunction.SimpleFunction{
        public static final Codec<MinSquareFunction> CODEC = DensityFunction.HOLDER_HELPER_CODEC.fieldOf("noise").xmap(MinSquareFunction::new, MinSquareFunction::noise).codec();

        @Override
        public double compute(@NotNull FunctionContext c) {
            return Math.min(
                    noise.compute(new DeviationableFunctionContext(c.blockX(),c.blockY(),0)),
                    noise.compute(new DeviationableFunctionContext(0,c.blockY(),c.blockZ()))
            );
        }

        @Override
        public double minValue() {
            return noise.minValue();
        }

        @Override
        public double maxValue() {
            return noise.maxValue();
        }

        @Override
        public @NotNull Codec<? extends DensityFunction> codec() {
            return CODEC;
        }
    }

    public record Level1BlockFunction(DensityFunction noise)implements DensityFunction.SimpleFunction{
        public static final int MAIN_LAYOUT_START=60,MAIN_LAYOUT_END=90;

        public static final Codec<Level1BlockFunction> CODEC = RecordCodecBuilder.create((p_208359_) ->
                p_208359_.group(
                        DensityFunction.HOLDER_HELPER_CODEC.fieldOf("noise").forGetter(Level1BlockFunction::noise)
                ).apply(p_208359_, Level1BlockFunction::new));

        @Override
        public double compute(@NotNull FunctionContext c) {
            if(c.blockY()<MAIN_LAYOUT_START||c.blockY()>=MAIN_LAYOUT_END)return 1;
            return noise.compute(c)>0?1:0;
        }

        @Override
        public double minValue() {
            return 0;
        }

        @Override
        public double maxValue() {
            return 1;
        }

        @Override
        public @NotNull Codec<? extends DensityFunction> codec() {
            return CODEC;
        }
    }
    public record MoveFunction(DensityFunction move,DensityFunction dx,DensityFunction dy,DensityFunction dz)implements DensityFunction.SimpleFunction{
        public static final MapCodec<MoveFunction> DATA_CODEC = RecordCodecBuilder.mapCodec((p_208798_) -> p_208798_.group(
                        DensityFunction.HOLDER_HELPER_CODEC.fieldOf("move").forGetter(MoveFunction::move),
                        DensityFunction.HOLDER_HELPER_CODEC.fieldOf("dx").forGetter(MoveFunction::dx),
                        DensityFunction.HOLDER_HELPER_CODEC.fieldOf("dy").forGetter(MoveFunction::dy),
                        DensityFunction.HOLDER_HELPER_CODEC.fieldOf("dz").forGetter(MoveFunction::dz))
                .apply(p_208798_, MoveFunction::new));
        public static final Codec<MoveFunction> CODEC = DATA_CODEC.codec();

        @Override
        public double compute(@NotNull FunctionContext c) {
            return move.compute(new CFunctionContext( (int) dx.compute(c)+c.blockX(), (int) dy.compute(c)+c.blockY(), (int) dz.compute(c)+c.blockZ()));
        }

        @Override
        public double minValue() {
            return move.minValue();
        }

        @Override
        public double maxValue() {
            return move.maxValue();
        }

        @Override
        public @NotNull Codec<? extends DensityFunction> codec() {
            return CODEC;
        }
    }

    public static abstract class CopyFunction implements DensityFunction.SimpleFunction{
        DensityFunction argument;

        protected CopyFunction(DensityFunction argument){
            this.argument=argument;
        }

        DensityFunction argument(){
            return this.argument;
        }

        @Override
        public double compute(@NotNull FunctionContext p_208223_) {
            return argument.compute(p_208223_);
        }

        @Override
        public double minValue() {
            return argument.minValue();
        }

        @Override
        public double maxValue() {
            return argument.maxValue();
        }
    }

    public static class XFunction extends CopyFunction{

        public static final Codec<XFunction> CODEC = RecordCodecBuilder.create((p_208359_) ->
                p_208359_.group(
                        DensityFunction.HOLDER_HELPER_CODEC.fieldOf("argument").forGetter(XFunction::argument)
                ).apply(p_208359_, XFunction::new));

        public XFunction(DensityFunction argument) {
            super(argument);
        }

        @Override
        public double compute(@NotNull FunctionContext p_208223_) {
            return argument.compute(new CFunctionContext(p_208223_.blockX(),0,0));
        }

        @Override
        public @NotNull Codec<? extends DensityFunction> codec() {
            return CODEC;
        }
    }
    public static class YFunction extends CopyFunction{

        public static final Codec<YFunction> CODEC = RecordCodecBuilder.create((p_208359_) ->
                p_208359_.group(
                        DensityFunction.HOLDER_HELPER_CODEC.fieldOf("argument").forGetter(YFunction::argument)
                ).apply(p_208359_, YFunction::new));

        public YFunction(DensityFunction argument) {
            super(argument);
        }

        @Override
        public double compute(@NotNull FunctionContext p_208223_) {
            return argument.compute(new CFunctionContext(0,p_208223_.blockY(),0));
        }

        @Override
        public @NotNull Codec<? extends DensityFunction> codec() {
            return CODEC;
        }
    }
    public static class ZFunction extends CopyFunction{

        public static final Codec<ZFunction> CODEC = RecordCodecBuilder.create((p_208359_) ->
                p_208359_.group(
                        DensityFunction.HOLDER_HELPER_CODEC.fieldOf("argument").forGetter(ZFunction::argument)
                ).apply(p_208359_, ZFunction::new));

        public ZFunction(DensityFunction argument) {
            super(argument);
        }

        @Override
        public double compute(@NotNull FunctionContext p_208223_) {
            return argument.compute(new CFunctionContext(0,0,p_208223_.blockZ()));
        }

        @Override
        public @NotNull Codec<? extends DensityFunction> codec() {
            return CODEC;
        }
    }
    public static class IntFunction extends CopyFunction{

        public static final Codec<IntFunction> CODEC = RecordCodecBuilder.create((p_208359_) ->
                p_208359_.group(
                        DensityFunction.HOLDER_HELPER_CODEC.fieldOf("argument").forGetter(IntFunction::argument)
                ).apply(p_208359_, IntFunction::new));

        public IntFunction(DensityFunction argument) {
            super(argument);
        }

        @Override
        public double compute(@NotNull FunctionContext p_208223_) {
            return (int)argument.compute(p_208223_);
        }

        @Override
        public @NotNull Codec<? extends DensityFunction> codec() {
            return CODEC;
        }
    }

    public interface TypeComputer{
        void next(double type);

        void outedOfBounds();
    }
    public static class AverageTypeComputer implements BackroomsFunction.TypeComputer {
        public double sum;
        public int count;
        public boolean outedOfBounds;

        public double average(){
            return count==0?0:sum/count;
        }

        @Override
        public void next(double type) {
            sum+=type;
            count++;
        }

        @Override
        public void outedOfBounds() {
            outedOfBounds=true;
        }
    }

    /**
     * 使用广度优先搜索遍历相邻且blockGraph获取的值相同的方块。
     * 使用方块坐标从typeGraph中获取值, 并将值传给computer.next()。
     * 若超出遍历方块上限则调用computer.outedOfBounds。
     */
    public static void searchType(@NotNull DensityFunction.FunctionContext start, @NotNull DensityFunction blockGraph, DensityFunction typeGraph, TypeComputer computer){
        final int MAX_SEARCH=100;

        class SearchLinker{
            public final SearchLinker[] neighbor;//右下左上
            public int created;
            public int searched;
            public final int deviation;
            public SearchLinker(){
                deviation=-1;//根节点
                neighbor=new SearchLinker[4];
            }
            public SearchLinker(int deviation){
                this.deviation=deviation;
                neighbor=new SearchLinker[4];
            }

            public String toString(){
                return "{created="+created+", searched="+searched+", deviation="+deviation+"}";
            }
        }

        HashSet<Long> searchedPoints=new HashSet<>();

        Stack<SearchLinker> stack=new Stack<>();
        stack.push(new SearchLinker());

        double startBlock=blockGraph.compute(start);
        DeviationableFunctionContext context=new DeviationableFunctionContext(start);

        int searchedCount=0;
        while(true){
            //System.out.println(stack);
            if(stack.size()==0) return;
            if(searchedCount>MAX_SEARCH) {
                computer.outedOfBounds();
                return;
            }
            SearchLinker top=stack.peek();
            switch (top.created){
                case 0:
                    context.dx++;
                    if(!searchedPoints.contains( (( context.dx&0xffffffffL )<<32) | (context.dz&0xffffffffL) ) && blockGraph.compute(context)==startBlock){
                        searchedPoints.add( (( context.dx&0xffffffffL )<<32) | (context.dz&0xffffffffL) );
                        computer.next(typeGraph.compute(context));
                        top.neighbor[0]=new SearchLinker(0);
                        searchedCount++;
                    }
                    context.dx--;
                    top.created++;
                    continue;
                case 1:
                    context.dz++;
                    if(!searchedPoints.contains( (( context.dx&0xffffffffL )<<32) | (context.dz&0xffffffffL) ) && blockGraph.compute(context)==startBlock){
                        searchedPoints.add( (( context.dx&0xffffffffL )<<32) | (context.dz&0xffffffffL) );
                        computer.next(typeGraph.compute(context));
                        top.neighbor[1]=new SearchLinker(1);
                        searchedCount++;
                    }
                    context.dz--;
                    top.created++;
                    continue;
                case 2:
                    context.dx--;
                    if(!searchedPoints.contains( (( context.dx&0xffffffffL )<<32) | (context.dz&0xffffffffL) ) && blockGraph.compute(context)==startBlock){
                        searchedPoints.add( (( context.dx&0xffffffffL )<<32) | (context.dz&0xffffffffL) );
                        computer.next(typeGraph.compute(context));
                        top.neighbor[2]=new SearchLinker(0);
                        searchedCount++;
                    }
                    context.dx++;
                    top.created++;
                    continue;
                case 3:
                    context.dz--;
                    if(!searchedPoints.contains( (( context.dx&0xffffffffL )<<32) | (context.dz&0xffffffffL) ) && blockGraph.compute(context)==startBlock){
                        searchedPoints.add( (( ((long)context.dx)&0xffffffffL )<<32) | (((long)context.dz)&0xffffffffL) );
                        computer.next(typeGraph.compute(context));
                        top.neighbor[3]=new SearchLinker(0);
                        searchedCount++;
                    }
                    context.dz++;
                    top.created++;
                    continue;
            }
            switch (top.searched){
                case 0:
                    if(top.neighbor[0]!=null){
                        context.dx++;
                        stack.push(top.neighbor[0]);
                    }
                    top.searched++;
                    continue;
                case 1:
                    if(top.neighbor[1]!=null){
                        context.dz++;
                        stack.push(top.neighbor[1]);
                    }
                    top.searched++;
                    continue;
                case 2:
                    if(top.neighbor[2]!=null){
                        context.dx--;
                        stack.push(top.neighbor[2]);
                    }
                    top.searched++;
                    continue;
                case 3:
                    if(top.neighbor[3]!=null){
                        context.dz--;
                        stack.push(top.neighbor[3]);
                    }
                    top.searched++;
                    continue;
            }
            //这个节点搜索完了, 弹栈
            switch (top.deviation){
                case 0:
                    context.dx--;
                    stack.pop();
                    break;
                case 1:
                    context.dz--;
                    stack.pop();
                    break;
                case 2:
                    context.dx++;
                    stack.pop();
                    break;
                case 3:
                    context.dz++;
                    stack.pop();
                    break;
                default:
                    stack.pop();
            }
        }


    }

    public static Codec<? extends DensityFunction> register(Registry<Codec<? extends DensityFunction>> p_208345_, String p_208346_, Codec<? extends DensityFunction> p_208347_) {
        return Registry.register(p_208345_, p_208346_, p_208347_);
    }

    public static final ResourceKey<DensityFunction> level0FunctionKey=ResourceKey.create(
            Registry.DENSITY_FUNCTION_REGISTRY,
            new ResourceLocation(Util.MODID,"level0")
    );

    @Nullable
    public static DensityFunction LEVEL0_GRAPH;
    public static DensityFunction LEVEL0_TYPE_NOISE;
    public static DensityFunction TEST_NOISE;
    public static DensityFunction LEVEL0_CORRIDOR_NOISE;
    public static DensityFunction COLUMN_NOISE;
    public static DensityFunction STREET_NOISE;
    public static DensityFunction MAIN_LAYOUT_LIGHT;
    public static DensityFunction LEVEL1_BLOCKS;

    public static long seed;


    @SubscribeEvent
    public static void serverStarting(ServerAboutToStartEvent event){
        //System.out.println("worldLoading被执行");
        seed= event.getServer().getWorldData().worldGenSettings().seed();
        MinecraftServer server=event.getServer();
        if(server==null) return;
        ResourceManager manager= server.getServerResources().resourceManager();
        LEVEL0_GRAPH =null;
        LEVEL0_TYPE_NOISE =null;
        try {
            //event.getWorld().getServer().getServerResources()
            LEVEL0_GRAPH=readFunction(manager,new ResourceLocation(Util.MODID,"worldgen/density_function/level0_graph_function.json"));
            LEVEL0_TYPE_NOISE=readFunction(manager,new ResourceLocation(Util.MODID,"worldgen/density_function/level0_type_noise.json"));
            TEST_NOISE=readFunction(manager,new ResourceLocation(Util.MODID,
                    "worldgen/density_function/test_noise.json"));
            //System.out.println("TEST_NOISE: "+TEST_NOISE);
            LEVEL0_CORRIDOR_NOISE=readFunction(manager,new ResourceLocation(Util.MODID,
                    "worldgen/density_function/level0_corridor.json"));
            COLUMN_NOISE=readFunction(manager,new ResourceLocation(Util.MODID,
                    "worldgen/density_function/level1/xz_noise.json"));
            STREET_NOISE=readFunction(manager,new ResourceLocation(Util.MODID,
                    "worldgen/density_function/level1/street.json"));
            MAIN_LAYOUT_LIGHT=readFunction(manager,new ResourceLocation(Util.MODID,
                    "worldgen/density_function/level1/light_noise.json"));
            LEVEL1_BLOCKS=readFunction(manager,new ResourceLocation(Util.MODID,
                    "worldgen/density_function/level1/blocks.json"));

        } catch (IOException|ClassCastException e) {
            e.printStackTrace();
        }

        //new DensityFunctions.Noise(null,null,null,null);
        loadBmps(manager);

        //new ResourceLocation(Util.MODID,"level0");
    }

    public static RectRaft[] LEVEL0_BMPS;

    public static void loadBmps(ResourceManager manager){
        try {
            Scanner scanner = new Scanner(manager.getResource(new ResourceLocation(Util.MODID,"worldgen/levelbpms/level0/length.txt")).getInputStream());
            int length = scanner.nextInt();
            LEVEL0_BMPS =new RectRaft[length];
            for (int i = 0; i < length; i++) {
                LEVEL0_BMPS[i] = RectRaft.createWithImage(ImageIO.read(manager.getResource(new ResourceLocation(Util.MODID,"worldgen/levelbpms/level0/level0_"+i+".png")).getInputStream()));
                //System.out.println(i);
            }

        } catch (Exception e) {
            //throw new RuntimeException(e);
            e.printStackTrace();
        }
    }

    public static DensityFunction readFunction(ResourceManager manager,ResourceLocation location) throws IOException {
        InputStream inputStream= manager.getResource(location).getInputStream();
        String json= IOUtils.toString(inputStream, StandardCharsets.UTF_8);
        //JsonObject jsonObject= JsonParser.parseString(json).getAsJsonObject();
        //System.out.println(jsonObject);

        Dynamic<JsonElement> dynamic=new Dynamic<>(JsonOps.INSTANCE,JsonParser.parseString(json));
        DataResult<? extends Pair<DensityFunction, ?>> result= DensityFunction.DIRECT_CODEC.decode(dynamic);

        if(result.result().isPresent()){
                return result.result().get().getFirst();
        }
        System.out.println(result.error());
        return null;
    }


    public static void reg(){
        register(Registry.DENSITY_FUNCTION_TYPES,"level0_graph", Level0GraphFunction.CODEC);
        register(Registry.DENSITY_FUNCTION_TYPES,"level0_block", Level0BlockFunction.CODEC);
        register(Registry.DENSITY_FUNCTION_TYPES,"level0_corridor", Level0ErosionFunction.CODEC);
        register(Registry.DENSITY_FUNCTION_TYPES,"test", TestFunction.CODEC);
        register(Registry.DENSITY_FUNCTION_TYPES,"p_noise", Noise.CODEC);
        register(Registry.DENSITY_FUNCTION_TYPES,"constant_average_block", ConstantBlockAverageFunction.CODEC);
        register(Registry.DENSITY_FUNCTION_TYPES,"min_square", MinSquareFunction.CODEC);
        register(Registry.DENSITY_FUNCTION_TYPES,"level1_block", Level1BlockFunction.CODEC);
        register(Registry.DENSITY_FUNCTION_TYPES,"only_x", XFunction.CODEC);
        register(Registry.DENSITY_FUNCTION_TYPES,"only_y", YFunction.CODEC);
        register(Registry.DENSITY_FUNCTION_TYPES,"only_z", ZFunction.CODEC);
        register(Registry.DENSITY_FUNCTION_TYPES,"move", MoveFunction.CODEC);
        register(Registry.DENSITY_FUNCTION_TYPES,"int", IntFunction.CODEC);
        register(Registry.DENSITY_FUNCTION_TYPES,"remove_short_space", RemoveShortSpace.CODEC);


        ResourceKey<DensityFunction> level0FunctionKey=ResourceKey.create(
                Registry.DENSITY_FUNCTION_REGISTRY,
                new ResourceLocation(Util.MODID,"level0")
        );
    }
}
