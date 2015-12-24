

package gui;

import agentes.Coordenador;
import agentes.Interface;
import agentes.Sensor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import jade.wrapper.AgentContainer;
import jade.core.Profile;
import jade.core.Runtime;
import jade.core.ProfileImpl;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;
import static spark.Spark.*;

public class Main {
    public static void main(String[] args) throws StaleProxyException {

     
        Runtime runtime = Runtime.instance(); 
        Profile profile = new ProfileImpl(false); 
        profile.setParameter(ProfileImpl.AGENTS, "localhost");
        
        AgentContainer secondaryContainer = runtime.createAgentContainer(profile); 

       
        String[] nomes = new String[]{"sala", "cozinha", "quarto", "garagem"};//, "wc", "wc2", "hall", "sotao", "cave", "quarto_fundo", "quarto_visitas"};
        for (String nome : nomes) {
            secondaryContainer.createNewAgent(nome, "agentes.Sensor", new Object[] { }).start();
        }

        Profile profile2 = new ProfileImpl(false); 
        profile2.setParameter(ProfileImpl.AGENTS, "localhost");
        AgentContainer coordContainer = runtime.createAgentContainer(profile2);
        

        coordContainer.createNewAgent("coord", "agentes.Coordenador", new Object[]{ }).start();



        Profile profile3 = new ProfileImpl(false);
        profile3.setParameter(ProfileImpl.MAIN_HOST, "localhost");
        AgentContainer iface_c = runtime.createAgentContainer(profile3);


        iface_c.createNewAgent("interface", "agentes.Interface", new Object[] {secondaryContainer }).start();
    }
}
