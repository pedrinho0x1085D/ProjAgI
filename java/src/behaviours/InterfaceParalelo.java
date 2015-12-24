
package behaviours;

import agentes.Interface;
import jade.core.behaviours.ParallelBehaviour;


public class InterfaceParalelo extends ParallelBehaviour{
    private Interface inter;
    public InterfaceParalelo(Interface inter){
        super(inter,ParallelBehaviour.WHEN_ALL);
        this.inter=inter;
        this.addSubBehaviour(new RecebeInfoDoCoordenador(inter));
        this.addSubBehaviour(new PedeTemperaturas(inter, 10000));
    }
}
