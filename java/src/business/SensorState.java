

package business;


public class SensorState {
    private String state;
    private String name;
    
    public SensorState(String n, String s){
        this.name = n;
        this.state = s;
    }
    
    public String getName(){return this.name;}
    public String getState(){return this.state;}
}
