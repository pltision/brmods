package yee.pltision.rectraft;

import java.util.Random;
import java.util.TreeSet;

public class Noise {
    public static void main(String[] args) {
        TreeSet<Integer> set=new TreeSet<>();
        Random random=new Random();
        for(int i=0;i<100000;i++){
            set.add((int) randomNoise(random.nextInt(),random.nextInt(),random.nextInt()));
        }
        System.out.println(set);
    }

    /**
     * @return 返回-5~5, 概率我也不清楚喵, 可以用scratch模拟一下
     */
    public static byte randomNoise(int x,int z,int seed){
        if(seed==0) seed=1;
        /*if(x==0) x=z%16;
        if(z==0) z=x%16;*/

        int a=(z*(x*11))-((z%13)*(x*seed));
        int b=((z%11)+((x%5)*x))%7;
        return (byte) (b==0?0:a%b);
    }
}
