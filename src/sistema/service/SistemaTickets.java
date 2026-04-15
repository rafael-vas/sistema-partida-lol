package sistema.service;

import sistema.model.Prioridade;
import sistema.model.StatusTicket;
import sistema.model.Ticket;
import sistema.model.Usuario;

public class SistemaTickets {
    private int capacidadeTickets;

    private int proximoIdTicket = 1;
    private Ticket[] tickets;
    private int totalTickets;
}
