/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Behaviours;

import Agents.Coordenador;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

/**
 *
 * @author PedroJosé
 */
public class RecebePedidosInterface extends CyclicBehaviour {

    private Coordenador coord;
    
    public RecebePedidosInterface(Coordenador c){this.coord = c;}
    
    @Override
    public void action() {
        ACLMessage mensagem = this.coord.receive();
        if(mensagem != null){
            
            if(mensagem.getPerformative() == ACLMessage.REQUEST){
                //como é request é da interface
                System.out.println("REQUEST: "+mensagem.getSender().getLocalName());  
                if(mensagem.getContent().matches("temperaturas:.+")){
                    //iface quer temperaturas
                    String[] ls = mensagem.getContent().split(":");
                    new PedeLeiturasBehaviour(coord, ls[1].split(";")).action();
                    
                    
                    
                }
                else if(mensagem.getContent().equals("online")) {
                    //iface quer todos online
                    new PoeOnline(this.coord).action();
                }
                else if(mensagem.getContent().matches("online:[a-zA-Z]+")){
                    String[] pedido = mensagem.getContent().split(":");
                    new PoeOnline(this.coord,pedido[1]).action();
                }
                else if(mensagem.getContent().equals("offline")) {
                    new PoeOffline(this.coord).action();
                }
                else if(mensagem.getContent().matches("offline:[a-zA-Z]+")){
                    String[] pedido = mensagem.getContent().split(":");
                    new PoeOffline(this.coord,pedido[1]).action();
                }
            }
            else if(mensagem.getPerformative() == ACLMessage.INFORM){
                System.out.println("INFORM: "+mensagem.getSender().getLocalName()); 
                if(mensagem.getContent().matches("[0-9]+")){
                    
                }
            }
            else {
                //qualquer cena
            }
        }
        block();
    }
    
}
