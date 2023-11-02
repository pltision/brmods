import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Scanner;

public class 二分查找 {
    public static void main(String[] args) {
        Range[] ranges=new Range[]{
                new Range(0,0,5),
                new Range(1,5,8),
                new Range(2,8,19),
                new Range(3,19,20),
        };
        Scanner scanner=new Scanner(System.in);

        for (Range range : ranges) System.out.println(range + " " + range.min());
        while(true){
            int max= ranges.length, min=0,ans=-1,find=scanner.nextInt();
            while(min<max){
                int mid=(max+min)>>1;
                System.out.println(min+" "+max+" "+mid+" "+ans);
                if(ranges[mid].min()<=find){
                    ans=mid;
                    min=mid+1;
                }
                else max=mid;
                //System.out.println(mid+" "+(mid-1));
            }
            if(ans!=-1&&ranges[ans].max()<=find) ans=-2;
            System.out.println(ans);
        }
    }


}
record Range(int data,int min,int max){

    /*@Contract(pure = true)
    @Override
    public int compareTo(@NotNull Range o) {
        return min-o.min;
    }*/
}