

package behaviours;

import agentes.Coordenador;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;


public class ListaAgentesBehaviour extends OneShotBehaviour {
    
    private Coordenador cd;

    public ListaAgentesBehaviour(Coordenador c){
        this.cd = c;
    }
    
    @Override
    public void action(){
            DFAgentDescription[] result = null;
            DFAgentDescription dfd = new DFAgentDescription();
            ServiceDescription sd = new ServiceDescription();
            sd.setType("sensor");
            dfd.addServices(sd);
        try {
            result = DFService.search(this.cd, dfd);
        } catch (FIPAException e) {
            e.printStackTrace();
        }

        String conteudo = "sensores:";
            
        int i = 0;
        for(DFAgentDescription dfad : result){
            if(i==result.length-1)
                conteudo += dfad.getName().getLocalName();
            else
                conteudo += dfad.getName().getLocalName() + ";";
            i++;
        }

        System.out.println("Encontrei estes "+conteudo);

        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        AID iefe = new AID();
        iefe.setLocalName("interface");
        msg.addReceiver(iefe);
        msg.setConversationId("sensoresEncontrados" + System.currentTimeMillis());
        msg.setContent(conteudo);

        this.cd.send(msg);
    }
    
}
