package yee.pltision.backrooms.dimension;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import yee.pltision.Util;
import yee.pltision.backrooms.dimension.densityfunctioncontext.DeviationableFunctionContext;
import yee.pltision.rectraft.GeneratorTest;
import yee.pltision.rectraft.RectRaft;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Stack;
import java.util.function.Function;

@Mod.EventBusSubscriber(modid = Util.MODID)
public class BackroomsFunction {

    Codec<Double> NOISE_VALUE_CODEC = Codec.doubleRange(-1000000.0D, 1000000.0D);

    public static final int BASE_HEIGHT=49;

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


            return GeneratorTest.RAFT[( ( GeneratorTest.RAFT.length + (int)(graph.compute(context)*(GeneratorTest.RAFT.length-3.1415926535)) /*+ ((int)(graph.compute(context)/2)<<2)*/ ) &0xffffff)%GeneratorTest.RAFT.length]
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
            public int deviation;
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


    @SubscribeEvent
    public static void worldLoading(ServerAboutToStartEvent event){
        //System.out.println("worldLoading被执行");
        MinecraftServer server=event.getServer();
        if(server==null) return;
        ResourceManager manager= server.getServerResources().resourceManager();
        LEVEL0_GRAPH =null;
        LEVEL0_TYPE_NOISE =null;
        try {
            //event.getWorld().getServer().getServerResources()
            LEVEL0_GRAPH=readFunction(manager,new ResourceLocation(Util.MODID,"worldgen/density_function/level0_graph_function.json"));
            LEVEL0_TYPE_NOISE=readFunction(manager,new ResourceLocation(Util.MODID,"worldgen/density_function/level0_type_noise.json"));

        } catch (IOException|ClassCastException e) {
            e.printStackTrace();
        }

        //loadBmps(manager);

        new ResourceLocation(Util.MODID,"level0");
    }

    public static void loadBmps(ResourceManager manager){
        try {
            Scanner scanner = new Scanner(manager.getResource(new ResourceLocation(Util.MODID,"worldgen/levelbpms/level0/length.txt")).getInputStream());
            int length = scanner.nextInt();
            GeneratorTest.RAFT=new RectRaft[length];
            for (int i = 0; i < length; i++) {
                GeneratorTest.RAFT[i] = RectRaft.createWithImage(ImageIO.read(manager.getResource(new ResourceLocation(Util.MODID,"worldgen/levelbpms/level0/level0_"+i+".png")).getInputStream()));
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static DensityFunction readFunction(ResourceManager manager,ResourceLocation location) throws IOException {
        InputStream inputStream= manager.getResource(location).getInputStream();
        String json= IOUtils.toString(inputStream, StandardCharsets.UTF_8);
        JsonObject jsonObject= JsonParser.parseString(json).getAsJsonObject();
        //System.out.println(jsonObject);

        Dynamic<JsonElement> dynamic=new Dynamic<>(JsonOps.INSTANCE,jsonObject);
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

        ResourceKey<DensityFunction> level0FunctionKey=ResourceKey.create(
                Registry.DENSITY_FUNCTION_REGISTRY,
                new ResourceLocation(Util.MODID,"level0")
        );
    }
}
