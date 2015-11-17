/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Agents;

import Behaviours.IniciaInterface;
import Behaviours.InterfaceParalelo;
import Business.DataComparator;
import Business.Leitura;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.ParallelBehaviour;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

/**
 *
 * @author PedroJosé
 */
public class Interface extends Agent {
    
    ArrayList<AID> historico;
    HashMap<String, TreeSet<Leitura>> leituras;
    ArrayList<String> timeouts, atual;
    
    @Override
    protected void setup() {
        this.atual = new ArrayList<>();
        this.historico = new ArrayList<>();
        this.leituras = new HashMap<>();
        this.timeouts = new ArrayList<>();
        super.setup();
        this.addBehaviour(new IniciaInterface(this));
        System.out.println("Interface a iniciar..");
    }
    
    @Override
    protected void takeDown() {
        super.takeDown(); //To change body of generated methods, choose Tools | Templates.
        System.out.println("Interface a falecer..");
    }
    
    public void setAtual(String[] aid) {
        this.atual = new ArrayList<>();
        for (String s : aid) {
            this.atual.add(s);
        }
    }

    public ArrayList<String> getAtual() {
        return atual;
    }
    
    public void addAtual(String aid) {
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

    public synchronized TreeSet<Leitura> getListaLeituras(String nome) {
        if (this.leituras.containsKey(nome)) {
            return this.leituras.get(nome);
        } else {
            this.leituras.put(nome, new TreeSet<>(new DataComparator()));
            return this.leituras.get(nome);
        }
    }
    
    public synchronized HashMap<String, TreeSet<Leitura>> getMapaLeituras() {
        return this.leituras;
    }
    
    public synchronized void addLeitura(String nome, Leitura l) {
        TreeSet<Leitura> leituras = getListaLeituras(nome);
        leituras.add(l);
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
}
