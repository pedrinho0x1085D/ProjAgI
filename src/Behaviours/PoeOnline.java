/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Behaviours;

import Agents.Coordenador;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
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
public class PoeOnline extends OneShotBehaviour {

    private Coordenador c;

    public PoeOnline(Coordenador c) {
        this.c = c;
    }

    @Override
    public void action() {
        DFAgentDescription[] result = null;
        DFAgentDescription dfd = new DFAgentDescription();
        ServiceDescription sd = new ServiceDescription();
        sd.setType("sensor");
        dfd.addServices(sd);
        
        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        ACLMessage resp=null;
        msg.setConversationId("" + System.currentTimeMillis());
        msg.setContent("online");
        
        ArrayList<AID> atual = new ArrayList<>();
        try {
            result = DFService.search(c, dfd);
            if (result.length > 0) {
                for (DFAgentDescription d : result) {
                    atual.add(d.getName());
                    msg.addReceiver(d.getName());
                }
                this.c.send(msg);
                this.c.setAtual(atual);
                for(int i=0; i<result.length; i++){
                    resp=this.c.blockingReceive();
                    if(resp!=null)
                        System.out.println("Mensagem recebida de "+resp.getSender().getLocalName()+" "+resp.getPerformative());
                
                }
            }
        } catch (FIPAException ex) {
            Logger.getLogger(PedeLeiturasBehaviour.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
