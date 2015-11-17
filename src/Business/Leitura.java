/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Business;

import java.time.LocalDateTime;


/**
import java.time.LocalDateTime;
import java.util.GregorianCalendar;
 *
 * @author PedroJosé
 */
public class Leitura {
    private int tFarenheit;
    private LocalDateTime horaLeitura;
    
    public Leitura(int tFarenheit){
        this.tFarenheit=tFarenheit;
        this.horaLeitura=LocalDateTime.now();
        
    }

    public LocalDateTime getHoraLeitura() {
        return horaLeitura;
    }


    public int gettFarenheit() {
        return tFarenheit;
    }

    private void setHoraLeitura(LocalDateTime horaLeitura) {
        this.horaLeitura = horaLeitura;
    }

  
    private void settFarenheit(int tFarenheit) {
        this.tFarenheit = tFarenheit;
    }
    
    public double fToCelsius(){
        return (this.tFarenheit-32)*(5/9);
    }
    
    public boolean isValid(){
        return !(this.tFarenheit<41||this.tFarenheit>86);
    }
}
