/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Behaviours;

import Agents.Interface;
import Business.Leitura;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;

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
        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        AID coord = new AID();
        String mensagem = "temperaturas:";
        coord.setLocalName("coord");

        msg.addReceiver(coord);
        msg.setConversationId("" + System.currentTimeMillis());
        for (int j = 0; j < this.i.getAtual().size(); j++) {
            if (j == this.i.getAtual().size() - 1) {
                mensagem += this.i.getAtual().get(j);
            } else {
                mensagem += this.i.getAtual().get(j) + ";";
            }
        }
        msg.setContent(mensagem);

        this.i.send(msg);

    }
}
