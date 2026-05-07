package sistema.app;

import java.util.Scanner;
import java.util.List;

import sistema.model.Bug;
import sistema.model.Desenvolvedor;
import sistema.model.Gestor;
import sistema.model.Prioridade;
import sistema.model.Qa;
import sistema.model.StatusTicket;
import sistema.model.Ticket;
import sistema.model.Usuario;
import sistema.service.SistemaTickets;

public class Main {
    public static void main(String[] args) {
        SistemaTickets sistemaTickets = new SistemaTickets();
        Scanner scanner = new Scanner(System.in);

        Usuario gestor = criarUsuarioInicial(scanner, "Gestor", 1);
        Usuario desenvolvedor = criarUsuarioInicial(scanner, "Desenvolvedor", 2);
        Usuario qa = criarUsuarioInicial(scanner, "QA", 3);

        boolean executando = true;

        while (executando) {
            System.out.println();
            System.out.println("=== QA TRACK ===");
            System.out.println("1 - Criar bug");
            System.out.println("2 - Listar tickets");
            System.out.println("3 - Atribuir responsavel");
            System.out.println("4 - Alterar status");
            System.out.println("5 - Ver historico de ticket");
            System.out.println("0 - Sair");
            System.out.print("Opcao: ");

            try {
                int opcao = lerInteiro(scanner);
                switch (opcao) {
                    case 1:
                        criarBug(scanner, sistemaTickets, gestor);
                        break;
                    case 2:
                        listarTickets(sistemaTickets);
                        break;
                    case 3:
                        atribuirResponsavel(scanner, sistemaTickets, gestor, desenvolvedor);
                        break;
                    case 4:
                        alterarStatus(scanner, sistemaTickets, desenvolvedor, qa);
                        break;
                    case 5:
                        mostrarHistorico(scanner, sistemaTickets);
                        break;
                    case 0:
                        executando = false;
                        System.out.println("Encerrando sistema.");
                        break;
                    default:
                        System.out.println("Opcao invalida.");
                        break;
                }
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }

        scanner.close();
    }

    private static Usuario criarUsuarioInicial(Scanner scanner, String tipo, int id) {
        System.out.println("=== CADASTRO INICIAL ===");
        System.out.print(tipo + " - Nome: ");
        String nome = lerLinha(scanner);

        if (id == 1) {
            return new Gestor(id, nome);
        }
        if (id == 2) {
            return new Desenvolvedor(id, nome);
        }
        return new Qa(id, nome);
    }

    private static void listarTickets(SistemaTickets sistemaTickets) {
        List<Ticket> tickets = sistemaTickets.listarTickets();
        if (tickets.isEmpty()) {
            System.out.println("Nenhum ticket cadastrado.");
            return;
        }

        System.out.println("=== TICKETS ===");
        for (Ticket ticket : tickets) {
            System.out.println(ticket);
        }
    }

    private static void criarBug(Scanner scanner, SistemaTickets sistemaTickets, Usuario criador) {
        System.out.print("Titulo: ");
        String titulo = lerLinha(scanner);
        System.out.print("Descricao: ");
        String descricao = lerLinha(scanner);
        Prioridade prioridade = escolherPrioridade(scanner);
        System.out.print("Passos para reproduzir: ");
        String passos = lerLinha(scanner);
        System.out.print("Ambiente: ");
        String ambiente = lerLinha(scanner);

        Bug bug = sistemaTickets.criarBug(titulo, descricao, criador, prioridade, passos, ambiente);
        if (bug == null) {
            System.out.println("Nao foi possivel criar o bug. Verifique os dados informados.");
            return;
        }

        System.out.println("Bug criado: " + bug);
    }

    private static void atribuirResponsavel(
        Scanner scanner,
        SistemaTickets sistemaTickets,
        Usuario gestor,
        Usuario desenvolvedor
    ) {
        Ticket ticket = escolherTicket(scanner, sistemaTickets);
        if (ticket == null) {
            return;
        }

        boolean sucesso = sistemaTickets.atribuirResponsavel(ticket.getId(), desenvolvedor, gestor);
        if (sucesso) {
            System.out.println("Responsavel atribuido com sucesso.");
        } else {
            System.out.println(sistemaTickets.explicarFalhaAtribuicao(ticket.getId(), desenvolvedor, gestor));
        }
    }

    private static void alterarStatus(
        Scanner scanner,
        SistemaTickets sistemaTickets,
        Usuario desenvolvedor,
        Usuario qa
    ) {
        Ticket ticket = escolherTicket(scanner, sistemaTickets);
        if (ticket == null) {
            return;
        }

        Usuario autor = escolherAutorPorStatus(ticket.getStatus(), desenvolvedor, qa);
        StatusTicket novoStatus = escolherStatus(scanner);

        System.out.print("Observacao (opcional): ");
        String observacao = lerLinha(scanner);

        boolean sucesso = sistemaTickets.transicionarStatus(ticket.getId(), novoStatus, autor, observacao);
        if (sucesso) {
            System.out.println("Status atualizado com sucesso.");
        } else {
            System.out.println(sistemaTickets.explicarFalhaTransicao(ticket.getId(), novoStatus, autor));
        }
    }

    private static void mostrarHistorico(Scanner scanner, SistemaTickets sistemaTickets) {
        Ticket ticket = escolherTicket(scanner, sistemaTickets);
        if (ticket == null) {
            return;
        }

        System.out.println("=== HISTORICO DO TICKET #" + ticket.getId() + " ===");
        List<String> historico = ticket.getHistorico();
        if (historico.isEmpty()) {
            System.out.println("Nenhum evento registrado.");
            return;
        }

        for (String evento : historico) {
            System.out.println(evento);
        }
    }

    private static Ticket escolherTicket(Scanner scanner, SistemaTickets sistemaTickets) {
        List<Ticket> tickets = sistemaTickets.listarTickets();
        if (tickets.isEmpty()) {
            System.out.println("Nenhum ticket cadastrado.");
            return null;
        }

        listarTickets(sistemaTickets);
        System.out.print("Informe o ID do ticket: ");
        int id = lerInteiro(scanner);
        Ticket ticket = sistemaTickets.buscarTicketPorId(id);

        if (ticket == null) {
            System.out.println("Ticket nao encontrado.");
        }

        return ticket;
    }

    private static Usuario escolherAutorPorStatus(StatusTicket statusAtual, Usuario desenvolvedor, Usuario qa) {
        if (statusAtual == StatusTicket.EM_TESTE) {
            return qa;
        }
        return desenvolvedor;
    }

    private static Prioridade escolherPrioridade(Scanner scanner) {
        System.out.println("=== Prioridade ===");
        Prioridade[] prioridades = Prioridade.values();
        for (int i = 0; i < prioridades.length; i++) {
            System.out.println((i + 1) + " - " + prioridades[i]);
        }

        System.out.print("Escolha: ");
        int indice = lerInteiro(scanner) - 1;
        if (indice < 0 || indice >= prioridades.length) {
            System.out.println("Escolha invalida. Usando MEDIA.");
            return Prioridade.MEDIA;
        }

        return prioridades[indice];
    }

    private static StatusTicket escolherStatus(Scanner scanner) {
        System.out.println("=== Status ===");
        StatusTicket[] status = StatusTicket.values();
        for (int i = 0; i < status.length; i++) {
            System.out.println((i + 1) + " - " + status[i]);
        }

        System.out.print("Escolha: ");
        int indice = lerInteiro(scanner) - 1;
        if (indice < 0 || indice >= status.length) {
            System.out.println("Escolha invalida. Usando ABERTO.");
            return StatusTicket.ABERTO;
        }

        return status[indice];
    }

    private static int lerInteiro(Scanner scanner) {
        while (true) {
            String entrada = scanner.nextLine();
            try {
                return Integer.parseInt(entrada.trim());
            } catch (NumberFormatException e) {
                System.out.print("Digite um numero valido: ");
            }
        }
    }

    private static String lerLinha(Scanner scanner) {
        return scanner.nextLine();
    }
}