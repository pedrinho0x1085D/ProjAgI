/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package behaviours;

import agentes.Coordenador;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import java.util.ArrayList;

/**
 *
 * @author PedroJosé
 */
public class PedeLeiturasBehaviour extends OneShotBehaviour {

    private Coordenador c;
    private String[] listaSensores;

    public PedeLeiturasBehaviour(Coordenador c, String[] ls) {
        super(c);
        this.c = c;
        this.listaSensores = ls;
    }

    @Override
    public void action() {
        String conteudo = "temperaturas:";
        ArrayList<String> verificados = new ArrayList<>();
        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        ACLMessage resp = null;
        msg.setConversationId("" + System.currentTimeMillis());
        msg.setContent("value");
        for (String sensor : this.listaSensores) {
            AID sens = new AID();
            sens.setLocalName(sensor);
            msg.addReceiver(sens);
        }
        this.c.send(msg);

        for (int i = 0; i < this.listaSensores.length; i++) {
            resp = this.c.blockingReceive(3000);
            if (resp != null && resp.getPerformative() == ACLMessage.INFORM) {
                if(resp.getContent()!= null) {
                    System.out.println(resp.getSender().getLocalName() + " -> " + resp.getContent());
                    verificados.add(resp.getSender().getLocalName());
                    conteudo += resp.getSender().getLocalName() + "," + resp.getContent() + ";";
                }
            }
        }

        if (this.listaSensores.length > verificados.size()) {
            for (String id : this.listaSensores) {
                if (!(verificados.contains(id))) {
                    System.out.println(id+" -> timeout");
                    conteudo += id + "," + "timeout;";
                }
            }
        }
        conteudo = conteudo.substring(0,conteudo.length()-1);

        ACLMessage nova = new ACLMessage(ACLMessage.INFORM);
        AID iefe = new AID();
        iefe.setLocalName("interface");
        nova.addReceiver(iefe);
        nova.setConversationId("" + System.currentTimeMillis());
        nova.setContent(conteudo);

        this.c.send(nova);

    }

}
