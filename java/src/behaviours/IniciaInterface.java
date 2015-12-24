/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package behaviours;

import agentes.Interface;
import jade.core.behaviours.SequentialBehaviour;

/**
 *
 * @author PedroJosé
 */
public class IniciaInterface extends SequentialBehaviour{
    private Interface i;
    public IniciaInterface(Interface i){
        super(i);
        this.addSubBehaviour(new PedeListaAgentes(i));
        this.addSubBehaviour(new InterfaceParalelo(i));
    }
}
