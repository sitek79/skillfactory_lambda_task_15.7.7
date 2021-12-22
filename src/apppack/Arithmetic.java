package apppack;

import java.util.Random;

public class Arithmetic {

    // генератор арифметического примера
    String arithSchema1() {
        int a,b,c,d;
        Random random=new Random();
        do {
            a=random.nextInt(100)+1;
            b=random.nextInt(100)+1;
            c=random.nextInt(1000)+1;
            d=random.nextInt(100)+2;
            int result = a + b - c / d;
        }while (c%d!=0||c==d);
        return a+"+"+b+"-"+c+"/"+d;
    }

    // генератор уравнения
    String equatSchema1(){
        int a,b,c,d,r;
        Random random=new Random();
        do {
            a=random.nextInt(100)+1;
            b=random.nextInt(100)+1;
            c=random.nextInt(1000)+1;
            d=random.nextInt(100)+2;
            r=a+b-c/d;
            int result2=d;
        }while (c%d!=0||c==d);
        return a+"+"+b+"-"+c+"/x"+"="+r;
    }
}
