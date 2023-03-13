
import java.util.Scanner;

public class DuTrungHoa {
    public int NghichDao(int a, int b)
    {
        if(b==0){
            if(a!=1)
                return -404;
        }
        int tmp=b;
        int x1=0, x2=1, y1=1, y2=0;
        int q,r,x,y,d;
        while (b>0){
            q=a/b;
            r=a-q*b;
            x=x2-q*x1;
            y= y2-q*y1;
            
            a=b;
            b=r;
            x2=x1;
            x1=x;
            y2=y1;
            y1=y;
        }
        d=a;
        x=x2;
        y=y2;
        if(d!=1) 
            return -404;
        if(x<0) 
            x=x+tmp;
        return x;
    }
    
    public int TimX(int a1, int a2, int a3, int m1, int m2, int m3){
        int M=m1*m2*m3;
        int M1=m2*m3;
        int M2=m1*m3;
        int M3=m1*m2;
        
        int y1=this.NghichDao(M1, m1);
        int y2=this.NghichDao(M2, m2);
        int y3=this.NghichDao(M3, m3);
        
        int x=a1*M1*y1 + a2*M2*y2 + a3*M3*y3;
        int X=x%M;
        
        System.out.print("x= "+ X +" + k*"+M);
        
        return 0;
    }

    public static void main(String[] args) {
        int a1, a2, a3, m1, m2, m3;
	    Scanner sc = new Scanner(System.in);
        System.out.print("a1= ");
        a1= sc.nextInt();
        System.out.print("a2= ");
        a2= sc.nextInt();
        System.out.print("a3= ");
        a3= sc.nextInt();
        
        System.out.print("m1= ");
        m1= sc.nextInt();
        System.out.print("m2= ");
        m2= sc.nextInt();
        System.out.print("m3= ");
        m3= sc.nextInt();
        
        DuTrungHoa e= new DuTrungHoa();
        e.TimX(a1, a2, a3, m1, m2, m3);
    }
}
