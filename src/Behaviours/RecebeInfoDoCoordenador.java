/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Behaviours;

import Agents.Interface;
import Business.Leitura;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

/**
 *
 * @author PedroJosé
 */
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
                System.out.println("Interface: Recebi mensagem do Coordenador");
                if (recebida.getContent().matches("online:.+")) {
                    this.inter.addAtual(recebida.getContent().split(":")[1]);
                } else if (recebida.getContent().matches("onlineLista:.+")) {
                    String[] sensores = recebida.getContent().split(":");
                    this.inter.setAtual(sensores[1].split(";"));
                } else if (recebida.getContent().matches("temperaturas:.+")) {
                    String[] ss = recebida.getContent().split(":");
                    String[] leituras = ss[1].split(";");
                    Leitura l = null;
                    for (String s : leituras) {
                        if (!s.isEmpty()) {
                            String[] leitura = s.split(",");
                            if (leitura[1].equals("timeout")) {
                                this.inter.limpaTimeouts();
                                this.inter.addTimeout(leitura[0]);
                            } else {
                                l = new Leitura(Integer.parseInt(leitura[1]));
                                if (l.isValid()) {
                                    this.inter.addLeitura(leitura[0], l);
                                }
                            }
                        }
                    }
                } else if (recebida.getContent().matches("offline:.+")) {
                    
                } else if (recebida.getContent().matches("offlineLista:.+")) {
                    
                }
                
            } else {
                System.out.println("Interface: Não recebi mensagem que me interessa");
            }
        }
        block();
    }
}
