/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Behaviours;

import Agents.Interface;
import jade.core.behaviours.SequentialBehaviour;

/**
 *
 * @author PedroJosé
 */
public class IniciaInterface extends SequentialBehaviour{
    private Interface i;
    public IniciaInterface(Interface i){
        super(i);
        this.addSubBehaviour(new PedeOnline(i));
        this.addSubBehaviour(new InterfaceParalelo(i));
    }
}
