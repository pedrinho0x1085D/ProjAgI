/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Agents;

import Behaviours.PoeOnline;
import Behaviours.RecebePedidosInterface;
import Business.DataComparator;
import Business.Leitura;
import jade.core.AID;
import jade.core.Agent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

/**
 *
 * @author PedroJosé
 */
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
