

package behaviours;

import agentes.Interface;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;


public class PedeOffline extends OneShotBehaviour {
    
    private Interface iface;
    private String[] escolhidos;

    public PedeOffline(Interface i){
        this.iface = i;
        this.escolhidos = null;
    }
    public PedeOffline(Interface i, String[] esc){
        super(i);
        this.iface = i;
        this.escolhidos = esc;
    }
    
    @Override
    public void action(){
        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        
        AID receiver = new AID();
        receiver.setLocalName("coord");
        msg.setConversationId(""+System.currentTimeMillis());
        msg.addReceiver(receiver);

        String content;
        if(escolhidos==null){
            content = "offline";
        }
        else{
            content = "offline:";
            int i = 0;
            for(String sensor : escolhidos){
                if(i == escolhidos.length-1)
                    content += sensor;
                else
                    content += sensor + ";";
                i++;
            }
        }

        for(String s : escolhidos)
            this.iface.atualizaEstadoSensor(s,"offline");

        msg.setContent(content);
        this.iface.send(msg);
    }
}
