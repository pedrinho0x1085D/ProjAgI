package business;


import agentes.Interface;

import java.io.IOException;

public class LeFicheiros extends Thread {
    private Interface i;

    public LeFicheiros(Interface ii){
        this.i = ii;
    }

    @Override
    public void run(){
        String path = "C://Users//Vasco//Desktop//leituras//";
        String names[] = {"leiturasManha3Dez2015.txt","leiturasManhaSegunda30Nov2015.txt"}; //, "leiturasManhaSegunda.txt", "leiturasSabadoTarde.txt", "leiturasSexta28Nov2015.txt", "leiturasTercaFeira.txt"};

        for(String name:names){
            String caminho = path+name;
            try {
                this.i.readFile(caminho);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
