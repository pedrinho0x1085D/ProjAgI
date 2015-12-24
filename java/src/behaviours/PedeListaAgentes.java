package behaviours;

import agentes.Interface;
import gui.WebServer;
import jade.core.AID;
import jade.wrapper.AgentContainer;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

import java.io.IOException;
import business.LeFicheiros;


public class PedeListaAgentes extends OneShotBehaviour {

    private Interface iface;

    public PedeListaAgentes(Interface i) {
        this.iface = i;
    }

    @Override
    public void action() {

        if (!this.iface.hasStarted()) {
            WebServer novo = new WebServer(this.iface);
            novo.start();

            AgentContainer ac = (AgentContainer)this.iface.getArguments()[0];
            this.iface.setSensoresContainer(ac);
            this.iface.justStarted();

            LeFicheiros lf = new LeFicheiros(this.iface);
            lf.start();
        }
        block(5000);
        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        AID coord = new AID();
        coord.setLocalName("coord");

        msg.addReceiver(coord);
        msg.setConversationId("" + System.currentTimeMillis());

        msg.setContent("listaAgentes");

        this.iface.send(msg);


    }
}
