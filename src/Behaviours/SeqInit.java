/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Behaviours;

import Agents.Coordenador;
import jade.core.behaviours.SequentialBehaviour;

/**
 *
 * @author PedroJosé
 */
public class SeqInit extends SequentialBehaviour{
    private Coordenador c;
    public SeqInit(Coordenador c){
        super(c);
        this.addSubBehaviour(new PoeOnline(c));
        this.addSubBehaviour(new PedeLeiturasBehaviour(c, 10000));
    }
}
