/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agentes;


import behaviours.RecebePedidosInterface;
import jade.core.Agent;


public class Coordenador extends Agent {
    
    @Override
    protected void setup() {
        
        super.setup();
        System.out.println("Coordenador a iniciar..");
        this.addBehaviour(new RecebePedidosInterface(this));
    }
    
    @Override
    protected void takeDown(){
        super.takeDown();
        System.out.println(this.getLocalName()+" a falecer...");
    }
}
