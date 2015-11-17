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
public class PoeOffline extends OneShotBehaviour {

    private Coordenador c;
    private String escolhido;

    public PoeOffline(Coordenador c) {
        this.c = c;
        this.escolhido = "";
    }
    
    public PoeOffline(Coordenador c, String localName){
        this.c = c;
        this.escolhido = localName;
    }
    

    @Override
    public void action() {
        
        if("".equals(this.escolhido)){
            //ligar todos
            DFAgentDescription[] result = null;
            DFAgentDescription dfd = new DFAgentDescription();
            ServiceDescription sd = new ServiceDescription();
            sd.setType("sensor");
            dfd.addServices(sd);

            ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
            ACLMessage resp=null;
            msg.setConversationId("" + System.currentTimeMillis());
            msg.setContent("offline");

            ArrayList<AID> atual = new ArrayList<>();
            try {
                result = DFService.search(c, dfd);
                if (result.length > 0) {
                    for (DFAgentDescription d : result) {
                        atual.add(d.getName());
                        msg.addReceiver(d.getName());
                    }
                    this.c.send(msg);
                    
                    String answer = "offlineLista:";
                    for(int i=0; i<result.length; i++){
                        resp=this.c.blockingReceive();
                        if(resp!=null){
                            System.out.println("Mensagem recebida de "+resp.getSender().getLocalName()+" "+resp.getPerformative());
                            if(i==result.length-1)
                                answer += resp.getSender().getLocalName();
                            else
                                answer += resp.getSender().getLocalName()+";";
                        }
                    }
                    
                    ACLMessage nova = new ACLMessage(ACLMessage.INFORM);
                    AID iefe = new AID();
                    iefe.setLocalName("interface");
                    nova.addReceiver(iefe);
                    nova.setConversationId("" + System.currentTimeMillis());
                    nova.setContent(answer);

                    this.c.send(nova);
                    
                    
                }
            } catch (FIPAException ex) {
                Logger.getLogger(PedeLeiturasBehaviour.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else {
            //ligar um em particular
            ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
            msg.setConversationId("" + System.currentTimeMillis());
            msg.setContent("offline");
            AID sensor = new AID();
            sensor.setLocalName(this.escolhido);
            msg.addReceiver(sensor);
            
            this.c.send(msg);
            
            ACLMessage resposta = this.c.blockingReceive();
            
            if(resposta != null){
                ACLMessage nova = new ACLMessage(ACLMessage.INFORM);
                AID iefe = new AID();
                iefe.setLocalName("interface");
                nova.addReceiver(iefe);
                nova.setConversationId("" + System.currentTimeMillis());
                nova.setContent("offline:"+this.escolhido);
                
                this.c.send(nova);
            }
        }

    }

}
