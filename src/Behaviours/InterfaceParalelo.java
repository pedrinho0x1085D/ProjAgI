/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Behaviours;

import Agents.Interface;
import jade.core.behaviours.ParallelBehaviour;

/**
 *
 * @author PedroJosé
 */
public class InterfaceParalelo extends ParallelBehaviour{
    private Interface inter;
    public InterfaceParalelo(Interface inter){
        super(inter,ParallelBehaviour.WHEN_ALL);
        this.inter=inter;
        this.addSubBehaviour(new RecebeInfoDoCoordenador(inter));
        this.addSubBehaviour(new PedeTemperaturas(inter, 30000));
    }
}
