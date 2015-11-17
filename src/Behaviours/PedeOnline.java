/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Behaviours;

import Agents.Interface;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

/**
 *
 * @author PedroJosé
 */
public class PedeOnline extends OneShotBehaviour{
    private Interface inter;
    public PedeOnline(Interface i){
        super(i);
        this.inter=i;
    }
    @Override
    public void action() {
        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        AID receiver=new AID();
        receiver.setLocalName("coord");
        msg.setContent("online");
        msg.setConversationId(""+System.currentTimeMillis());
        msg.addReceiver(receiver);
        this.inter.send(msg);
    }
    
}
