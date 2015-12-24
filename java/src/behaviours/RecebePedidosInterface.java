/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package behaviours;

import agentes.Coordenador;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

import java.util.ArrayList;


public class RecebePedidosInterface extends CyclicBehaviour {

    private Coordenador coord;

    public RecebePedidosInterface(Coordenador c){this.coord = c;}

    @Override
    public void action() {
        ACLMessage mensagem = this.coord.receive();
        if(mensagem != null){
            if(mensagem.getPerformative() == ACLMessage.REQUEST){
                if(mensagem.getContent().matches("temperaturas:.+")){
                    if(mensagem.getConversationId().matches("temps.+")){
                        System.out.println("Interface pediu: "+mensagem.getContent());
                        String[] ls = mensagem.getContent().split(":")[1].split(";");
                        ArrayList<String> temp = new ArrayList<>();
                        for(String s : ls){
                            if(!s.isEmpty()){
                                temp.add(s);
                            }
                        }
                        String envia[] = new String[temp.size()];
                        for(int i = 0; i<temp.size(); i++)
                            envia[i] = temp.get(i);
                        new PedeLeiturasBehaviour(this.coord, envia).action();
                    }
                    block();
                }
                else if(mensagem.getContent().equals("listaAgentes")){
                    new ListaAgentesBehaviour(this.coord).action();
                    block();
                }
                else if(mensagem.getContent().equals("online")) {
                    //iface quer todos online
                    new PoeOnline(this.coord).action();
                    block();
                }
                else if(mensagem.getContent().matches("online:.+")){
                    String[] pedido = mensagem.getContent().split(":");
                    new PoeOnline(this.coord,pedido[1].split(";")).action();
                    block();
                }
                else if(mensagem.getContent().equals("offline")) {
                    new PoeOffline(this.coord).action();
                    block();
                }
                else if(mensagem.getContent().matches("offline:.+")){
                    String[] pedido = mensagem.getContent().split(":");
                    new PoeOffline(this.coord,pedido[1].split(";")).action();
                    block();
                }
            }
            else if(mensagem.getPerformative() == ACLMessage.INFORM){
                System.out.println("Chegou um inform de"+mensagem.getSender().getLocalName());
                if(!mensagem.getSender().getLocalName().equals("interface")){
                    String envia[] = {mensagem.getSender().getLocalName()};
                    new PedeLeiturasBehaviour(this.coord, envia).action();
                }
            }
            else {
                //qualquer cena
            }
        }

    }

}
