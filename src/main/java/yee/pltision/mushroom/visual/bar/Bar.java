package yee.pltision.mushroom.visual.bar;

import java.util.stream.Stream;

public interface Bar {
    Stream<? extends BarBlock> getBlocks();

}
