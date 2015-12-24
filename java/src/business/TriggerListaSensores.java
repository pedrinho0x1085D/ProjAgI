package business;

import agentes.Interface;
import behaviours.PedeListaAgentes;
import behaviours.PedeOnline;


public class TriggerListaSensores extends Thread {

    private Interface iface;
    private String sensor = null;

    public TriggerListaSensores(Interface i){
        this.iface = i;
    }
    public TriggerListaSensores(Interface i, String s){
        this.iface = i;
        this.sensor = s;
    }

    public void run(){
        try {
            sleep(3000);
            new PedeListaAgentes(this.iface).action();
            if(sensor != null){
                String sens[] = new String[1];
                sens[0] = this.sensor;
                sleep(2000);
                new PedeOnline(this.iface, sens).action();
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
