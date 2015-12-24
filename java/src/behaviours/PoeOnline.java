/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package behaviours;

import agentes.Coordenador;
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
    private String[] escolhidos = null;

    public PoeOnline(Coordenador c) {
        this.c = c;
    }
    
    public PoeOnline(Coordenador c, String[] localNames){
        this.c = c;
        this.escolhidos = localNames;
    }
    

    @Override
    public void action() {
        
        if(escolhidos == null){
            //ligar todos
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
                    
                    String answer = "onlineLista:";
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
            //ligar os que estão na lista
            ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
            msg.setConversationId("" + System.currentTimeMillis());
            msg.setContent("online");
            
            for(String s : this.escolhidos){
                AID sensor = new AID();
                sensor.setLocalName(s);
                msg.addReceiver(sensor);
            }
            this.c.send(msg);
            
            int conta = 0;
            for(String s : this.escolhidos){
                ACLMessage resposta = this.c.blockingReceive();
            
                if(resposta != null){
                    conta++;
                }
            }
            
            if(conta == this.escolhidos.length){
                ACLMessage nova = new ACLMessage(ACLMessage.INFORM);
                AID iefe = new AID();
                iefe.setLocalName("interface");
                nova.addReceiver(iefe);
                nova.setConversationId("" + System.currentTimeMillis());
                
                if(conta == 1){
                    nova.setContent("online:"+this.escolhidos[0]);
                }
                else{
                    String content = "onlineLista:";
                    int j = 0;
                    for(String zz : this.escolhidos){
                        if(j == this.escolhidos.length-1)
                            content += zz;
                        else
                            content += zz+";";
                        j++;
                    }
                    nova.setContent(content);
                }
                this.c.send(nova);
            }
            
            

            
            
        }

    }

}
