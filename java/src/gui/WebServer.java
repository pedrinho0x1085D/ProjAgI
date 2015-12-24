package gui;

import agentes.Interface;
import behaviours.PedeListaAgentes;
import behaviours.PedeOffline;
import behaviours.PedeOnline;
import business.TriggerListaSensores;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import jade.core.AID;
import jade.wrapper.AgentContainer;
import jade.domain.FIPAAgentManagement.AMSAgentDescription;

import java.util.ArrayList;

import static spark.Spark.get;
import static spark.Spark.post;
import static spark.SparkBase.port;


public class WebServer {
    private Interface iface;

    public WebServer(Interface i){
        this.iface = i;
    }

    public void start(){
        ObjectWriter jsonWriter = new ObjectMapper().writer();

        port(9090);

        get("/estadoSensores", (request, response) -> {
            return jsonWriter.writeValueAsString(iface.getEstadoSensores());
        });

        get("/mapaLeituras", (request, response) -> {
            return jsonWriter.writeValueAsString(iface.getMapaLeituras());
        });

        get("/online", (req,res) -> {
            new PedeOnline(this.iface).action();
            return "{\"status\" : \"ok\"}";
        });

        get("/offline", (req,res) -> {
            new PedeOffline(this.iface).action();
            return "{\"status\" : \"ok\"}";
        });

        get("/botaOnline", (request, response) -> {
            String[] cenas = request.queryParams("nomes").split(",");

            ArrayList<String> temp = new ArrayList<>();

            for(String s : cenas) {
                if (!this.iface.getEstadoSensores().containsKey(s)){
                    this.iface.getSensoresContainer().createNewAgent(s,"agentes.Sensor", new Object[] {}).start();
                    TriggerListaSensores tls = new TriggerListaSensores(this.iface, s);
                    tls.start();
                }
                else {
                    temp.add(s);
                }
            }
            String fim[] = new String[temp.size()];
            for(int i = 0; i<temp.size();i++)
                fim[i] = temp.get(i);

            new PedeOnline(this.iface,fim).action();

            return "{\"status\" : \"ok\"}";
        });

        get("/botaOffline", (request, response) -> {
            String[] cenas = request.queryParams("nomes").split(",");
            ArrayList<String> temp = new ArrayList<>();
            for(String s : cenas) {
                if (this.iface.getEstadoSensores().containsKey(s)) {
                    temp.add(s);
                }
                else {
                    this.iface.getSensoresContainer().createNewAgent(s,"Agents.Sensor", new Object[] {}).start();
                    TriggerListaSensores tls = new TriggerListaSensores(this.iface);
                    tls.start();
                }
            }

            String offline[] = new String[temp.size()];
            for(int i = 0; i<temp.size(); i++)
                offline[i] = temp.get(i);

            new PedeOffline(this.iface, offline).action();
            return "{\"status\" : \"ok\"}";
        });

        get("/poeAgente", (req, res) -> {
            AgentContainer ac = this.iface.getSensoresContainer();
            String coiz = req.queryParams("nomeSensor");
            if(this.iface.getEstadoSensores().containsKey(coiz)){
                return "{\"status\" : \"fail\"}";
            }
            else {
                ac.createNewAgent(coiz, "Agents.Sensor", new Object[] {  }).start();
                TriggerListaSensores tls = new TriggerListaSensores(this.iface);
                tls.start();
                return "{\"status\" : \"ok\"}";
            }

        });

    }
}
