/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package behaviours;

import agentes.Interface;
import business.Leitura;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;


public class RecebeInfoDoCoordenador extends CyclicBehaviour {
    
    private Interface inter;
    
    public RecebeInfoDoCoordenador(Interface i) {
        this.inter = i;
    }
    
    @Override
    public void action() {
        
        ACLMessage recebida = this.inter.receive();
        
        if (recebida != null) {
            if (recebida.getPerformative() == ACLMessage.INFORM) {
                if (recebida.getContent().matches("sensores:.+")){
                    String[] sensores = recebida.getContent().split(":");
                    for(String ss : sensores[1].split(";"))
                        if(!this.inter.getEstadoSensores().containsKey(ss))
                            this.inter.atualizaEstadoSensor(ss, "offline");


                }
                else if (recebida.getContent().matches("online:.+")) {
                    this.inter.atualizaEstadoSensor(recebida.getContent().split(":")[1], "online");
                    
                } else if (recebida.getContent().matches("onlineLista:.+")) {
                    String[] sensores = recebida.getContent().split(":");
                    for(String ss : sensores[1].split(";"))
                        if(!ss.isEmpty())
                            this.inter.atualizaEstadoSensor(ss, "online");
                    
                } else if (recebida.getContent().matches("temperaturas:.+")) {

                    String[] ss = recebida.getContent().split(":");
                    String[] leituras = ss[1].split(";");
                    Leitura l = null;
                    for (String s : leituras) {
                        if (!s.equals("") && s != null) {
                            String[] leitura = s.split(",");
                            switch (leitura[1]) {
                                case "timeout":
                                    this.inter.atualizaEstadoSensor(leitura[0], "timedout");
                                    break;
                                case "XXXXX":
                                    this.inter.atualizaEstadoSensor(leitura[0], "error");
                                    break;
                                default:
                                    if(leitura[1] != null){

                                        l = new Leitura(Integer.parseInt(leitura[1]));
                                        this.inter.atualizaEstadoSensor(leitura[0], "online");
                                        if (l.isValid()) {
                                            this.inter.addLeitura(leitura[0], l);
                                        }
                                        else {
                                            this.inter.atualizaEstadoSensor(leitura[0], "error");
                                        }
                                    }
                                    break;
                            }

                            LocalDateTime agora = LocalDateTime.now();
                            Leitura ultima = this.inter.getLastLeitura(s);
                            if(ultima != null){
                                //fazer neste if e else a lógica para tentar determinar se um agente foi "killed"
                            }
                            else{

                            }
                        }
                    }


                } else if (recebida.getContent().matches("offline:.+")) {
                    
                } else if (recebida.getContent().matches("offlineLista:.+")) {
                    String[] nomes = recebida.getContent().split(":");
                    String[] lista = nomes[1].split(";");
                    for(String nome : lista){
                        this.inter.atualizaEstadoSensor(nome, "offline");
                    }
                }
                
            } else {
                System.out.println("Interface: Recebi mensagem que não me interessa");
            }
        }
        block();
    }
}
