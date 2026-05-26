package br.com.qatrack.model;

public abstract class Usuario {
    private int id;
    private String nome;

    public Usuario(int id, String nome) {
        this.id = id;

        if (nome == null || nome.isBlank()) {
            this.nome = "Usuario";
        } else {
            this.nome = nome;
        }
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        if (nome == null || nome.isBlank()) {
            return;
        }
        this.nome = nome;
    }

    public abstract String getTipo();

    @Override
    public String toString() {
        return nome + " (" + getTipo() + ")";
    }
}