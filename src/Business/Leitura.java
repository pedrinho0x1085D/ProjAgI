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
    private double tFarenheit,tCelsius;
    private LocalDateTime horaLeitura;
    
    public Leitura(double tFarenheit){
        this.tFarenheit=tFarenheit;
        this.horaLeitura=LocalDateTime.now();
        this.tCelsius=fToCelsius(tFarenheit);
    }

    public LocalDateTime getHoraLeitura() {
        return horaLeitura;
    }

    public double gettCelsius() {
        return tCelsius;
    }

    public double gettFarenheit() {
        return tFarenheit;
    }

    private void setHoraLeitura(LocalDateTime horaLeitura) {
        this.horaLeitura = horaLeitura;
    }

    private void settCelsius(double tCelsius) {
        this.tCelsius = tCelsius;
    }
    private void settFarenheit(double tFarenheit) {
        this.tFarenheit = tFarenheit;
    }
    
    private double fToCelsius(double f){
        return (5/9)*(f-32);
    }
}
