package br.com.qatrack.model;

public class Melhoria extends Ticket {
    private String areaImpactada;

    public Melhoria(
        int id,
        String titulo,
        String descricao,
        Usuario criador,
        Prioridade prioridade
    ) {
        this(id, titulo, descricao, criador, prioridade, "Nao informado");
    }

    public Melhoria(
        int id,
        String titulo,
        String descricao,
        Usuario criador,
        Prioridade prioridade,
        String areaImpactada
    ) {
        super(id, titulo, descricao, criador, prioridade);

        if (areaImpactada == null || areaImpactada.isBlank()) {
            this.areaImpactada = "Nao informado";
        } else {
            this.areaImpactada = areaImpactada;
        }
    }

    public String getAreaImpactada() {
        return areaImpactada;
    }

    @Override
    public String getTipo() {
        return "Melhoria";
    }

    @Override
    public String toString() {
        return super.toString() + " | Area impactada: " + areaImpactada;
    }
}



