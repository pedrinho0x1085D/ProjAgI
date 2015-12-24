package behaviours;

import agentes.Interface;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

public class PedeOnline extends OneShotBehaviour {

    private Interface inter;
    private String[] listinha = null;

    public PedeOnline(Interface i) {
        super(i);
        this.inter = i;
    }

    public PedeOnline(Interface i, String[] lista) {
        super(i);
        this.inter = i;
        this.listinha = lista;
    }

    @Override
    public void action() {

        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        AID receiver = new AID();
        receiver.setLocalName("coord");
        msg.setConversationId("" + System.currentTimeMillis());
        msg.addReceiver(receiver);
        String content = "";

        if (this.listinha == null) {
            content = "online";
            for(String s : this.inter.getEstadoSensores().keySet())
                this.inter.atualizaEstadoSensor(s,"online");
        } else {
            content += "online:";
            int i = 0;
            for (String s : this.listinha) {
                if (i == this.listinha.length - 1) {
                    content += s;
                } else {
                    content += s + ";";
                }
                this.inter.atualizaEstadoSensor(s,"online");
                i++;
            }
        }

        msg.setContent(content);
        this.inter.send(msg);

    }

}
