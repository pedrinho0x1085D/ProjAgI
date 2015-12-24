
package agentes;

import behaviours.IniciaInterface;
import business.DataComparator;
import business.Leitura;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import jade.core.Agent;
import jade.wrapper.AgentContainer;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.TreeSet;


public class Interface extends Agent {
    
    private HashMap<String, TreeSet<Leitura>> leituras;
    private HashMap<String,String> estadoSensores;
    private boolean started = false;
    private AgentContainer sensoresContainer;
    
    //estes não estão a ser usados para nada neste momento mas não vou apagar
    private ArrayList<String> timeouts, atual, historico;
    
    
    public Interface(){
        this.atual = new ArrayList<>();
        this.historico = new ArrayList<>();
        this.leituras = new HashMap<>();
        this.timeouts = new ArrayList<>();
        this.estadoSensores = new HashMap<>();
    }
    
    @Override
    protected void setup() {
        super.setup();
        this.addBehaviour(new IniciaInterface(this));
        System.out.println("Interface a iniciar..");
    }
    
    @Override
    protected void takeDown() {
        super.takeDown(); //To change body of generated methods, choose Tools | Templates.
        System.out.println("Interface a falecer..");
    }
    
    public void setSensoresContainer(AgentContainer ac){
        this.sensoresContainer = ac;
    }
    public AgentContainer getSensoresContainer(){
        return this.sensoresContainer;
    }

    public boolean hasStarted(){
        return this.started;
    }
    public void justStarted(){
        this.started = true;
    }
    
    public void setAtual(String[] aid) {
        this.atual = new ArrayList<>();
        for (String s : aid) {
            this.atual.add(s);
        }
    }

    public synchronized HashMap<String, String> getEstadoSensores(){
        return this.estadoSensores;
    }
    public synchronized void atualizaEstadoSensor(String nome_sensor, String estado){
        this.estadoSensores.put(nome_sensor,estado);
    }
    
    public synchronized Leitura getLastLeitura(String nome){
        Leitura ultima = null;
        if(this.leituras.containsKey(nome))
            if(this.leituras.get(nome).size() > 0)
                ultima = this.leituras.get(nome).first();
        
        return ultima;
    }
    
    public ArrayList<String> getAtual() {
        return atual;
    }
    
    public synchronized void addAtual(String aid) {
        if (!this.atual.contains(aid)) {
            this.atual.add(aid);
        }
    }
    
    public void removeAtual(String aid) {
        this.atual.remove(aid);
    }

    public void clearAtual() {
        this.atual.clear();
    }

    public HashMap<String, TreeSet<Leitura>> getMapaLeituras() {
        return this.leituras;
    }
    


    public void readFile(String path) throws IOException {

        String content = new String(Files.readAllBytes(Paths.get(path)));
        ObjectMapper mapper = new ObjectMapper();

        HashMap<String, ArrayList<LinkedHashMap>> cena = mapper.readValue(content,HashMap.class);

        for(String s : cena.keySet()) {
            for (LinkedHashMap lhm : cena.get(s)) {
                Gson gson = new Gson();
                String json = gson.toJson(lhm ,LinkedHashMap.class);

                int far = 0;
                LocalDateTime ldt = null;
                int contador = 0;
                Leitura l;
                for(Object o : lhm.keySet()){
                    if(o.toString().equals("tFarenheit")){
                        far = Integer.parseInt(lhm.get(o).toString());
                    }
                    else if(o.toString().equals("horaLeitura")){
                        int year = 0;
                        int month = 0;
                        int dayOfMonth = 0;
                        int hour = 0;
                        int minute = 0;
                        int second = 0;
                        int nanosec = 0;
                        LinkedHashMap ll = (LinkedHashMap)lhm.get(o);
                        for(Object oo : ll.keySet()){
                            String atual = oo.toString();
                            switch (atual) {
                                case "dayOfMonth":
                                    dayOfMonth = Integer.parseInt(ll.get(oo).toString());
                                    break;
                                case "monthValue":
                                    month = Integer.parseInt(ll.get(oo).toString());
                                    break;
                                case "year":
                                    year = Integer.parseInt(ll.get(oo).toString());
                                    break;
                                case "hour":
                                    hour = Integer.parseInt(ll.get(oo).toString());
                                    break;
                                case "minute":
                                    minute = Integer.parseInt(ll.get(oo).toString());
                                    break;
                                case "second":
                                    second = Integer.parseInt(ll.get(oo).toString());
                                    break;
                                case "nano":
                                    nanosec = Integer.parseInt(ll.get(oo).toString());
                                    break;
                            }
                        }
                        ldt = LocalDateTime.of(year,month,dayOfMonth,hour,minute,second,nanosec);
                    }
                    contador++;
                    if(contador == 3){
                        l = new Leitura(far,ldt);
                        addLeitura(s,l);
                        contador = 0;
                    }

                }
            }
        }
        System.out.println("Li o ficheiro "+path);
    }

    public synchronized void addLeitura(String nome, Leitura l) {
        if(this.leituras.containsKey(nome)){
            this.leituras.get(nome).add(l);
        }
        else {
            TreeSet<Leitura> novo = new TreeSet<>(new DataComparator());
            novo.add(l);
            this.leituras.put(nome,novo);
        }
    }

    public synchronized ArrayList<String> getTimeouts() {
        return this.timeouts;
    }
    
    public synchronized void addTimeout(String timeout) {
        this.timeouts.add(timeout);
    }

    public synchronized void limpaTimeouts() {
        this.timeouts.clear();
    }

    public synchronized void addHistorico(String ss) {
        if(!this.historico.contains(ss)){
            this.historico.add(ss);
            System.out.println("Adicionei "+ss+" ao historico");
        }
            
    }
    public ArrayList<String> getHistorico(){return this.historico;}
}
