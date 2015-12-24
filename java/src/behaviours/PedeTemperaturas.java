/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package behaviours;

import agentes.Interface;
import business.Leitura;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;

import java.util.ArrayList;

/**
 *
 * @author PedroJosé
 */
public class PedeTemperaturas extends TickerBehaviour {

    private Interface i;
    private long timeout;

    public PedeTemperaturas(Interface i, long t) {
        super(i, t);
        this.i = i;
        this.timeout = t;
    }

    @Override
    public void onTick() {
        ArrayList<String> nabos = new ArrayList<>();
        for(String sensor : this.i.getEstadoSensores().keySet()){
            if(!this.i.getEstadoSensores().get(sensor).equals("offline")){
                nabos.add(sensor);
            }
        }

        if(nabos.size() > 0){
            ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
            AID coord = new AID();
            String mensagem = "temperaturas:";
            coord.setLocalName("coord");

            msg.addReceiver(coord);
            msg.setConversationId("temps" + System.currentTimeMillis());

            int tamanho = this.i.getEstadoSensores().size();

            for(String s : nabos)
                mensagem += s+";";

            mensagem = mensagem.substring(0,mensagem.length()-1);
            msg.setContent(mensagem);

            this.i.send(msg);
        }
    }
}
