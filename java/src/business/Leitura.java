
package business;

import java.time.LocalDateTime;


public class Leitura {
    private int tFarenheit;
    private LocalDateTime horaLeitura;
    
    public Leitura(int tFarenheit){
        this.tFarenheit=tFarenheit;
        this.horaLeitura=LocalDateTime.now();
        
    }

    public Leitura(int far, LocalDateTime ldt){
        this.tFarenheit = far;
        this.horaLeitura = ldt;
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
