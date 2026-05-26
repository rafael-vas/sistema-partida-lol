package br.com.qatrack.model;

public class Feature extends Ticket {
    private String valorDeNegocio;

    public Feature(
        int id,
        String titulo,
        String descricao,
        Usuario criador,
        Prioridade prioridade
    ) {
        this(id, titulo, descricao, criador, prioridade, "Nao informado");
    }

    public Feature(
        int id,
        String titulo,
        String descricao,
        Usuario criador,
        Prioridade prioridade,
        String valorDeNegocio
    ) {
        super(id, titulo, descricao, criador, prioridade);

        if (valorDeNegocio == null || valorDeNegocio.isBlank()) {
            this.valorDeNegocio = "Nao informado";
        } else {
            this.valorDeNegocio = valorDeNegocio;
        }
    }

    public String getValorDeNegocio() {
        return valorDeNegocio;
    }

    @Override
    public String getTipo() {
        return "Feature";
    }

    @Override
    public String toString() {
        return super.toString() + " | Valor de negocio: " + valorDeNegocio;
    }
}

