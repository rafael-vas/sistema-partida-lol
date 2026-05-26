package br.com.qatrack.model;

public class Gestor extends Usuario {
    public Gestor(int id, String nome) {
        super(id, nome);
    }

    @Override
    public String getTipo() {
        return "GESTOR";
    }
}
