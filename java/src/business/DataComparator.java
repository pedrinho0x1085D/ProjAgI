
package business;

import java.io.Serializable;
import java.util.Comparator;


public class DataComparator implements Comparator<Leitura>,Serializable{
    public int compare(Leitura l1,Leitura l2){
        if(l1.getHoraLeitura().isBefore(l2.getHoraLeitura())) return 1;
        else if (l1.getHoraLeitura().isEqual(l2.getHoraLeitura())) return 0;
        else return -1;
    }
    
}
