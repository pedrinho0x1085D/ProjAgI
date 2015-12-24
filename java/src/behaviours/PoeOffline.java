
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


public class PoeOffline extends OneShotBehaviour {

    private Coordenador c;
    private String[] escolhidos = null;

    public PoeOffline(Coordenador c) {
        this.c = c;
    }
    
    public PoeOffline(Coordenador c, String[] localNames){
        this.c = c;
        this.escolhidos = localNames;
    }
    

    @Override
    public void action() {
        if(this.escolhidos != null){
            ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
            msg.setConversationId("" + System.currentTimeMillis());
            msg.setContent("offline");
            
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
                nova.setConversationId(""+System.currentTimeMillis());
                
                if(conta==1){
                    nova.setContent("offline:"+this.escolhidos[0]);
                }
                else{
                    String content = "offlineLista:";
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
        else{
            DFAgentDescription[] result = null;
            DFAgentDescription dfd = new DFAgentDescription();
            ServiceDescription sd = new ServiceDescription();
            sd.setType("sensor");
            dfd.addServices(sd);
            try {
                result = DFService.search(this.c, dfd);
            } catch (FIPAException e) {
                e.printStackTrace();
            }
            ArrayList<String> temp = new ArrayList<>();
            for(DFAgentDescription r : result)
                temp.add(r.getName().getLocalName());

            ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
            msg.setConversationId("" + System.currentTimeMillis());
            msg.setContent("offline");

            for(String s : temp){
                AID sensor = new AID();
                sensor.setLocalName(s);
                msg.addReceiver(sensor);
            }
            this.c.send(msg);

            int conta = 0;
            for(String s : temp){
                ACLMessage resposta = this.c.blockingReceive();

                if(resposta != null){
                    conta++;
                }
            }

            if(conta == temp.size()){
                ACLMessage nova = new ACLMessage(ACLMessage.INFORM);
                AID iefe = new AID();
                iefe.setLocalName("interface");
                nova.addReceiver(iefe);
                nova.setConversationId(""+System.currentTimeMillis());

                String answer = "offlineLista:";
                for(String s : temp)
                    answer += s+";";
                answer = answer.substring(0,answer.length()-1);
                nova.setContent(answer);

                this.c.send(nova);
            }
        }
    }

}
