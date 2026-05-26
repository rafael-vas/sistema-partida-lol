package br.com.qatrack.model;

public class Desenvolvedor extends Usuario {
    public Desenvolvedor(int id, String nome) {
        super(id, nome);
    }

    @Override
    public String getTipo() {
        return "DESENVOLVEDOR";
    }
}

