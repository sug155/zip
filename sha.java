import java.util.Scanner;
public class sha{
    public static void main(String [] args){
        int h0=0x12345678;
        int h1=0x1BCDEF11;
        int h2=0x14586322;
        int h3=0x16464633;
        int h4=0x14688788;

        int k0=0x5a829799;
Scanner sc=new Scanner(System.in);
String input=sc.nextLine();
byte[] inputBytes=input.getBytes();
int w[]=new int[16];
for(int i=0;i<inputBytes.length;i++){
    if(i<4){
         w[0] |=(inputBytes[i] & 0xFF) << (24-(i*8));
    }}
    int A=h0,B=h1,C=h2,D=h3,E=h4;
    int ft=(B & C)|(~B & D);
    int temp=Integer.rotateLeft(A,5)+ft+w[0]+k0;

    B=A;
    C=Integer.rotateLeft(A,30);
    D=C;
    E=D;
    A=temp;
    System.out.printf("A:%08x,B:%08x,C:%08x,D:%08x,E:%08x",A,B,C,D,E);

    }
}
