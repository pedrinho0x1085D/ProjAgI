/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Behaviours;

import Agents.Coordenador;
import jade.core.AID;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author PedroJosé
 */
public class PedeLeiturasBehaviour extends TickerBehaviour {

    private Coordenador c;
    private long timeout;

    public PedeLeiturasBehaviour(Coordenador c, long timeout) {
        super(c, timeout);
        this.c = c;
        this.timeout = timeout;
    }

    @Override
    protected void onTick() {
        ArrayList<AID> sensores = c.getAtual();
        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        ACLMessage resp = null;
        msg.setConversationId("" + System.currentTimeMillis());
        msg.setContent("value");
        for (AID a : sensores) {
            msg.addReceiver(a);
        }
        this.c.send(msg);
        for (int i = 0; i < sensores.size(); i++) {
            resp = this.c.blockingReceive();
            if (resp != null) {
                System.out.println(resp.getSender().getLocalName()+" "+resp.getContent());
            }
        }
    }

}
