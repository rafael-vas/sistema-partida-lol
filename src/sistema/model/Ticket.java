package sistema.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public abstract class Ticket {
    private static final int LIMITE_HISTORICO = 100;

    private Usuario usuarioSistema;

    private int id;
    private String titulo;
    private String descricao;
    private Usuario criador;
    private Usuario responsavel;
    private Prioridade prioridade;
    private StatusTicket status;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;
    private List<String> historico;

    protected Ticket(int id, String titulo, String descricao, Usuario criador, Prioridade prioridade) {
        this.usuarioSistema = new Gestor(0, "Sistema");
        this.id = id;

        if (titulo == null || titulo.isBlank()) {
            this.titulo = "Sem titulo";
        } else {
            this.titulo = titulo;
        }

        if (descricao == null || descricao.isBlank()) {
            this.descricao = "Sem descricao";
        } else {
            this.descricao = descricao;
        }

        if (criador == null) {
            this.criador = usuarioSistema;
        } else {
            this.criador = criador;
        }

        if (prioridade == null) {
            this.prioridade = Prioridade.MEDIA;
        } else {
            this.prioridade = prioridade;
        }

        this.status = StatusTicket.ABERTO;
        this.criadoEm = LocalDateTime.now();
        this.atualizadoEm = this.criadoEm;
        this.historico = new ArrayList<>();
        registrarHistorico("Ticket criado por " + this.criador.getNome() + " com prioridade " + this.prioridade + ".");
    }

    public int getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public Usuario getCriador() {
        return criador;
    }

    public Usuario getResponsavel() {
        return responsavel;
    }

    public Prioridade getPrioridade() {
        return prioridade;
    }

    public StatusTicket getStatus() {
        return status;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public LocalDateTime getAtualizadoEm() {
        return atualizadoEm;
    }

    public List<String> getHistorico() {
        return new ArrayList<>(historico);
    }

    public void definirResponsavel(Usuario responsavel, Usuario autor) {
        if (responsavel == null || autor == null) {
            return;
        }
        this.responsavel = responsavel;
        this.atualizadoEm = LocalDateTime.now();
        registrarHistorico("Responsavel definido para " + responsavel.getNome() + " por " + autor.getNome() + ".");
    }

    public void atualizarStatus(StatusTicket novoStatus, Usuario autor, String observacao) {
        if (novoStatus == null || autor == null) {
            return;
        }
        StatusTicket anterior = this.status;
        this.status = novoStatus;
        this.atualizadoEm = LocalDateTime.now();

        String mensagem = "Status alterado de " + anterior + " para " + novoStatus + " por " + autor.getNome() + ".";
        if (observacao != null && !observacao.isBlank()) {
            mensagem += " Obs: " + observacao;
        }
        registrarHistorico(mensagem);
    }

    public void atualizarStatus(StatusTicket novoStatus, Usuario autor) {
        atualizarStatus(novoStatus, autor, "");
    }

    protected void registrarHistorico(String evento) {
        if (historico.size() >= LIMITE_HISTORICO) {
            return;
        }
        historico.add(LocalDateTime.now() + " - " + evento);
    }

    public abstract String getTipo();

    @Override
    public String toString() {
        return "#" + id + " [" + getTipo() + "] " + titulo + " | Status: " + status + " | Prioridade: " + prioridade;
    }
}