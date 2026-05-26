package br.com.qatrack.model;

public class Bug extends Ticket {
    private String passosParaReproduzir;
    private String ambiente;

    public Bug(
        int id,
        String titulo,
        String descricao,
        Usuario criador,
        Prioridade prioridade,
        String passosParaReproduzir
    ) {
        this(id, titulo, descricao, criador, prioridade, passosParaReproduzir, "Nao informado");
    }

    public Bug(
        int id,
        String titulo,
        String descricao,
        Usuario criador,
        Prioridade prioridade,
        String passosParaReproduzir,
        String ambiente
    ) {
        super(id, titulo, descricao, criador, prioridade);

        if (passosParaReproduzir == null || passosParaReproduzir.isBlank()) {
            this.passosParaReproduzir = "Nao informado";
        } else {
            this.passosParaReproduzir = passosParaReproduzir;
        }

        if (ambiente == null || ambiente.isBlank()) {
            this.ambiente = "Nao informado";
        } else {
            this.ambiente = ambiente;
        }
    }

    public String getPassosParaReproduzir() {
        return passosParaReproduzir;
    }

    public String getAmbiente() {
        return ambiente;
    }

    @Override
    public String getTipo() {
        return "Bug";
    }

    @Override
    public String toString() {
        return super.toString() + " | Ambiente: " + ambiente;
    }
}