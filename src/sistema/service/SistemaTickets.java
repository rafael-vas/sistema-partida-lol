package sistema.service;

import sistema.model.Bug;
import sistema.model.Desenvolvedor;
import sistema.model.Feature;
import sistema.model.Gestor;
import sistema.model.Melhoria;
import sistema.model.Prioridade;
import sistema.model.Qa;
import sistema.model.StatusTicket;
import sistema.model.Ticket;
import sistema.model.Usuario;

public class SistemaTickets {
    private int capacidadeTickets;

    private int proximoIdTicket = 1;
    private Ticket[] tickets;
    private int totalTickets;

    public SistemaTickets() {
        this.capacidadeTickets = 100;
        this.tickets = new Ticket[capacidadeTickets];
        this.totalTickets = 0;
    }

    public Bug criarBug(
            String titulo,
            String descricao,
            Usuario criador,
            Prioridade prioridade,
            String passosParaReproduzir
    ) {
        return criarBug(titulo, descricao, criador, prioridade, passosParaReproduzir, "Nao informado");
    }

    public Bug criarBug(
            String titulo,
            String descricao,
            Usuario criador,
            Prioridade prioridade,
            String passosParaReproduzir,
            String ambiente
    ) {
        if (totalTickets >= tickets.length) {
            return null;
        }

        Bug bug = new Bug(proximoIdTicket++, titulo, descricao, criador, prioridade, passosParaReproduzir, ambiente);
        adicionarTicket(bug);
        return bug;
    }

    public Feature criarFeature(
            String titulo,
            String descricao,
            Usuario criador,
            Prioridade prioridade
    ) {
        return criarFeature(titulo, descricao, criador, prioridade, "Nao informado");
    }

    public Feature criarFeature(
            String titulo,
            String descricao,
            Usuario criador,
            Prioridade prioridade,
            String valorDeNegocio
    ) {
        if (totalTickets >= tickets.length) {
            return null;
        }

        Feature feature = new Feature(proximoIdTicket++, titulo, descricao, criador, prioridade, valorDeNegocio);
        adicionarTicket(feature);
        return feature;
    }

    public Melhoria criarMelhoria(
            String titulo,
            String descricao,
            Usuario criador,
            Prioridade prioridade
    ) {
        return criarMelhoria(titulo, descricao, criador, prioridade, "Nao informado");
    }

    public Melhoria criarMelhoria(
            String titulo,
            String descricao,
            Usuario criador,
            Prioridade prioridade,
            String areaImpactada
    ) {
        if (totalTickets >= tickets.length) {
            return null;
        }

        Melhoria melhoria = new Melhoria(proximoIdTicket++, titulo, descricao, criador, prioridade, areaImpactada);
        adicionarTicket(melhoria);
        return melhoria;
    }

    public Ticket buscarTicketPorId(int idTicket) {
        for (int i = 0; i < totalTickets; i++) {
            if (tickets[i].getId() == idTicket) {
                return tickets[i];
            }
        }
        return null;
    }

    public boolean atribuirResponsavel(int idTicket, Usuario responsavel, Usuario autor) {
        Ticket ticket = buscarTicketPorId(idTicket);
        if (ticket == null || autor == null || responsavel == null) {
            return false;
        }
        if (!ehGestor(autor)) {
            return false;
        }
        if (!ehDesenvolvedor(responsavel)) {
            return false;
        }

        ticket.definirResponsavel(responsavel, autor);
        return true;
    }

    public String explicarFalhaAtribuicao(int idTicket, Usuario responsavel, Usuario autor) {
        Ticket ticket = buscarTicketPorId(idTicket);
        if (ticket == null) {
            return "Ticket nao encontrado.";
        }
        if (autor == null) {
            return "Autor nao informado.";
        }
        if (responsavel == null) {
            return "Responsavel nao informado.";
        }
        if (!ehGestor(autor)) {
            return "Apenas um gestor pode atribuir responsaveis.";
        }
        if (!ehDesenvolvedor(responsavel)) {
            return "O responsavel precisa ser um desenvolvedor.";
        }
        return "Nao foi possivel atribuir responsavel.";
    }

    public boolean transicionarStatus(int idTicket, StatusTicket novoStatus, Usuario autor, String observacao) {
        Ticket ticket = buscarTicketPorId(idTicket);
        if (!validarTransicaoStatus(ticket, novoStatus, autor)) {
            return false;
        }

        ticket.atualizarStatus(novoStatus, autor, observacao);
        return true;
    }

    public boolean transicionarStatus(int idTicket, StatusTicket novoStatus, Usuario autor) {
        return transicionarStatus(idTicket, novoStatus, autor, "");
    }

    public String explicarFalhaTransicao(int idTicket, StatusTicket novoStatus, Usuario autor) {
        Ticket ticket = buscarTicketPorId(idTicket);
        if (ticket == null) {
            return "Ticket nao encontrado.";
        }
        if (novoStatus == null) {
            return "Novo status nao informado.";
        }
        if (autor == null) {
            return "Autor nao informado.";
        }

        StatusTicket atual = ticket.getStatus();
        if (atual == novoStatus) {
            return "O ticket ja esta neste status.";
        }

        switch (atual) {
            case ABERTO:
            case REABERTO:
                if (!ehDesenvolvedor(autor)) {
                    return "Somente desenvolvedor pode mover de ABERTO ou REABERTO para EM_DESENVOLVIMENTO.";
                }
                if (novoStatus != StatusTicket.EM_DESENVOLVIMENTO) {
                    return "De ABERTO ou REABERTO, o proximo status precisa ser EM_DESENVOLVIMENTO.";
                }
                break;
            case EM_DESENVOLVIMENTO:
                if (!ehDesenvolvedor(autor)) {
                    return "Somente desenvolvedor pode mover de EM_DESENVOLVIMENTO para EM_TESTE.";
                }
                if (novoStatus != StatusTicket.EM_TESTE) {
                    return "De EM_DESENVOLVIMENTO, o proximo status precisa ser EM_TESTE.";
                }
                break;
            case EM_TESTE:
                if (!ehQa(autor)) {
                    return "Somente QA pode mover de EM_TESTE para RESOLVIDO ou REABERTO.";
                }
                if (novoStatus != StatusTicket.RESOLVIDO && novoStatus != StatusTicket.REABERTO) {
                    return "De EM_TESTE, o status precisa ser RESOLVIDO ou REABERTO.";
                }
                break;
            case RESOLVIDO:
                return "Ticket resolvido nao pode ser alterado.";
            default:
                return "Transicao nao permitida.";
        }

        return "Nao foi possivel alterar o status.";
    }

    public Ticket[] listarTickets() {
        Ticket[] copia = new Ticket[totalTickets];
        for (int i = 0; i < totalTickets; i++) {
            copia[i] = tickets[i];
        }
        return copia;
    }

    public int getTotalTickets() {
        return totalTickets;
    }

    private boolean adicionarTicket(Ticket ticket) {
        if (totalTickets >= tickets.length) {
            return false;
        }

        tickets[totalTickets] = ticket;
        totalTickets++;
        return true;
    }

    private boolean validarTransicaoStatus(Ticket ticket, StatusTicket novoStatus, Usuario autor) {
        if (ticket == null || novoStatus == null || autor == null) {
            return false;
        }

        StatusTicket atual = ticket.getStatus();
        if (atual == novoStatus) {
            return false;
        }

        switch (atual) {
            case ABERTO:
            case REABERTO:
                if (!ehDesenvolvedor(autor)) {
                    return false;
                }
                return novoStatus == StatusTicket.EM_DESENVOLVIMENTO;
            case EM_DESENVOLVIMENTO:
                if (!ehDesenvolvedor(autor)) {
                    return false;
                }
                return novoStatus == StatusTicket.EM_TESTE;
            case EM_TESTE:
                if (!ehQa(autor)) {
                    return false;
                }
                return novoStatus == StatusTicket.RESOLVIDO || novoStatus == StatusTicket.REABERTO;
            case RESOLVIDO:
                return false;
            default:
                return false;
        }
    }

    private boolean ehGestor(Usuario usuario) {
        return usuario instanceof Gestor;
    }

    private boolean ehDesenvolvedor(Usuario usuario) {
        return usuario instanceof Desenvolvedor;
    }

    private boolean ehQa(Usuario usuario) {
        return usuario instanceof Qa;
    }
}
