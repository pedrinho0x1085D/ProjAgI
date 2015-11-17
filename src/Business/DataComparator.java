/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Business;

import java.io.Serializable;
import java.util.Comparator;

/**
 *
 * @author PedroJosé
 */
public class DataComparator implements Comparator<Leitura>,Serializable{
    public int compare(Leitura l1,Leitura l2){
        if(l1.getHoraLeitura().isBefore(l2.getHoraLeitura())) return 1;
        else if (l1.getHoraLeitura().isEqual(l2.getHoraLeitura())) return 0;
        else return -1;
    }
    
}
