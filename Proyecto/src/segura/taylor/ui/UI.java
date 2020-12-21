package segura.taylor.ui;

import java.io.PrintStream;
import java.util.Scanner;

public class UI {
    //Variables
    private Scanner input = new Scanner(System.in).useDelimiter("\\n");
    private PrintStream output = new PrintStream(System.out);

    //Metodos

    //***Imprimir***
    public void imprimir(String texto){
        output.print(texto);
    }
    public  void imprimirLinea(String texto){
        output.println(texto);
    }

    //***Leer***
    public String leerLinea(){
        return input.next();
    }
    public int leerEntero(){
        return input.nextInt();
    }
    public double leerDouble(){
        return input.nextDouble();
    }
    public boolean leerBoolean(){
        return input.nextBoolean();
    }
}
