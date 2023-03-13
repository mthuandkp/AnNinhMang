import java.util.*;

public class LuyThuaNhanh{
    public long modexp(long a, long x, long n){
        long r=1;        
        while (x>0){
            if (x%2==1)
                    r=(r*a)%n;
            a=(a*a)%n;
            x/=2;
        }
        return r;
    }

    

    public static void main(String[] args) {
	long a, x, n;
        Scanner scanner = new Scanner(System.in);

        System.out.print("a= ");
        a = scanner.nextLong();
        System.out.print("x= ");
        x = scanner.nextLong();
        System.out.print("n= ");
        n = scanner.nextLong();

        LuyThuaNhanh e=new LuyThuaNhanh();
        
	System.out.print(e.modexp(a,x,n));    
    }
}