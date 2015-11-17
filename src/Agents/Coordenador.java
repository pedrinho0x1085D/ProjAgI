/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Agents;

import Behaviours.PoeOnline;
import Behaviours.SeqInit;
import jade.core.AID;
import jade.core.Agent;
import java.util.ArrayList;

/**
 *
 * @author PedroJosé
 */
public class Coordenador extends Agent {
    ArrayList<AID> historico,atual;
    @Override
    protected void setup() {
        this.atual=new ArrayList<>();
        this.historico=new ArrayList<>();
        super.setup();
        this.addBehaviour(new SeqInit(this));
    }
    public void setAtual(ArrayList<AID> aid){
        this.atual=aid;
        
    }
    public ArrayList<AID> getAtual(){
        return atual;
    }
    protected void takeDown(){
        super.takeDown();
        System.out.println(this.getLocalName()+" a falecer...");
    }
}
