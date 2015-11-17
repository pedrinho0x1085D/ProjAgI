/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Behaviours;

import Agents.Coordenador;
import Business.Leitura;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import java.util.ArrayList;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

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
            if (resp != null) {
                verificados.add(resp.getSender().getLocalName());
                System.out.println(resp.getSender().getLocalName() + " " + resp.getContent());
                if (resp.getContent().matches("[0-9]+")) {
                    conteudo += resp.getSender().getLocalName() + "," + resp.getContent() + ";";
                }

            }
        }

        if (this.listaSensores.length > verificados.size()) {
            for (String id : this.listaSensores) {
                if (!(verificados.contains(id))) {
                    conteudo += id + "," + "timeout;";
                }
            }
        }
        ACLMessage nova = new ACLMessage(ACLMessage.INFORM);
        AID iefe = new AID();
        iefe.setLocalName("interface");
        nova.addReceiver(iefe);
        nova.setConversationId("" + System.currentTimeMillis());
        nova.setContent(conteudo);

        this.c.send(nova);

    }

}
