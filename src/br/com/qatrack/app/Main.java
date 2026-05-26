package br.com.qatrack.app;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.List;

import br.com.qatrack.model.Bug;
import br.com.qatrack.model.Desenvolvedor;
import br.com.qatrack.model.Feature;
import br.com.qatrack.model.Gestor;
import br.com.qatrack.model.Melhoria;
import br.com.qatrack.model.Prioridade;
import br.com.qatrack.model.Qa;
import br.com.qatrack.model.StatusTicket;
import br.com.qatrack.model.Ticket;
import br.com.qatrack.model.Usuario;
import br.com.qatrack.service.SistemaTickets;

public class Main {
    public static void main(String[] args) {
        SistemaTickets sistemaTickets = new SistemaTickets();
        Scanner scanner = new Scanner(System.in);

        try {
            int proximoIdUsuario = 1;
            List<Usuario> usuarios = new ArrayList<>();

            usuarios.add(criarUsuarioInicial(scanner, "Gestor", proximoIdUsuario++));
            usuarios.add(criarUsuarioInicial(scanner, "Desenvolvedor", proximoIdUsuario++));
            usuarios.add(criarUsuarioInicial(scanner, "QA", proximoIdUsuario++));

            boolean executando = true;

            while (executando) {
                mostrarMenuPrincipal();

                int opcao = lerInteiro(scanner);
                switch (opcao) {
                    case 1:
                        criarTicket(scanner, sistemaTickets, usuarios);
                        break;
                    case 2:
                        listarTickets(sistemaTickets);
                        break;
                    case 3:
                        atribuirResponsavel(scanner, sistemaTickets, usuarios);
                        break;
                    case 4:
                        alterarStatus(scanner, sistemaTickets, usuarios);
                        break;
                    case 5:
                        mostrarHistorico(scanner, sistemaTickets);
                        break;
                    case 6:
                        proximoIdUsuario = cadastrarUsuario(scanner, usuarios, proximoIdUsuario);
                        break;
                    case 7:
                        listarUsuarios(usuarios);
                        break;
                    case 0:
                        executando = false;
                        System.out.println("Encerrando sistema.");
                        break;
                    default:
                        System.out.println("Opcao invalida.");
                        break;
                }
            }
        } finally {
            scanner.close();
        }
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

    private static void mostrarMenuPrincipal() {
        System.out.println();
        System.out.println("=== QA TRACK ===");
        System.out.println("1 - Criar ticket");
        System.out.println("2 - Listar tickets");
        System.out.println("3 - Atribuir responsavel");
        System.out.println("4 - Alterar status");
        System.out.println("5 - Ver historico de ticket");
        System.out.println("6 - Cadastrar usuario");
        System.out.println("7 - Listar usuarios");
        System.out.println("0 - Sair");
        System.out.print("Opcao: ");
    }

    private static void listarUsuarios(List<Usuario> usuarios) {
        if (usuarios.isEmpty()) {
            System.out.println("Nenhum usuario cadastrado.");
            return;
        }

        System.out.println("=== USUARIOS ===");
        for (Usuario usuario : usuarios) {
            System.out.println(usuario.getId() + " - " + usuario);
        }
    }

    private static int cadastrarUsuario(
        Scanner scanner,
        List<Usuario> usuarios,
        int proximoIdUsuario
    ) {
        System.out.println("=== Cadastro de usuario ===");
        System.out.println("1 - Gestor");
        System.out.println("2 - Desenvolvedor");
        System.out.println("3 - QA");
        System.out.print("Escolha o tipo: ");

        int tipo = lerInteiro(scanner);
        if (tipo < 1 || tipo > 3) {
            System.out.println("Tipo de usuario invalido.");
            return proximoIdUsuario;
        }

        System.out.print("Nome: ");
        String nome = lerLinha(scanner);

        Usuario novoUsuario = criarUsuarioPorTipo(tipo, proximoIdUsuario, nome);
        usuarios.add(novoUsuario);

        System.out.println("Usuario cadastrado: " + novoUsuario);
        return proximoIdUsuario + 1;
    }

    private static Usuario criarUsuarioPorTipo(int tipo, int id, String nome) {
        if (tipo == 1) {
            return new Gestor(id, nome);
        }
        if (tipo == 2) {
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

    private static void criarTicket(
        Scanner scanner,
        SistemaTickets sistemaTickets,
        List<Usuario> usuarios
    ) {
        System.out.println("=== Tipo de ticket ===");
        System.out.println("1 - Bug");
        System.out.println("2 - Feature");
        System.out.println("3 - Melhoria");
        System.out.print("Escolha: ");

        int tipo = lerInteiro(scanner);
        if (tipo < 1 || tipo > 3) {
            System.out.println("Tipo de ticket invalido.");
            return;
        }

        Usuario criador = escolherCriador(scanner, usuarios);

        System.out.print("Titulo: ");
        String titulo = lerLinha(scanner);
        System.out.print("Descricao: ");
        String descricao = lerLinha(scanner);
        Prioridade prioridade = escolherPrioridade(scanner);

        switch (tipo) {
            case 1: {
                System.out.print("Passos para reproduzir: ");
                String passos = lerLinha(scanner);
                System.out.print("Ambiente: ");
                String ambiente = lerLinha(scanner);

                Bug bug = sistemaTickets.criarBug(
                    titulo,
                    descricao,
                    criador,
                    prioridade,
                    passos,
                    ambiente
                );
                if (bug == null) {
                    System.out.println("Nao foi possivel criar o bug. Verifique os dados informados.");
                    return;
                }

                System.out.println("Bug criado: " + bug);
                break;
            }
            case 2: {
                System.out.print("Valor de negocio: ");
                String valorDeNegocio = lerLinha(scanner);

                Feature feature = sistemaTickets.criarFeature(
                    titulo,
                    descricao,
                    criador,
                    prioridade,
                    valorDeNegocio
                );
                if (feature == null) {
                    System.out.println("Nao foi possivel criar a feature. Verifique os dados informados.");
                    return;
                }

                System.out.println("Feature criada: " + feature);
                break;
            }
            case 3: {
                System.out.print("Area impactada: ");
                String areaImpactada = lerLinha(scanner);

                Melhoria melhoria = sistemaTickets.criarMelhoria(
                    titulo,
                    descricao,
                    criador,
                    prioridade,
                    areaImpactada
                );
                if (melhoria == null) {
                    System.out.println("Nao foi possivel criar a melhoria. Verifique os dados informados.");
                    return;
                }

                System.out.println("Melhoria criada: " + melhoria);
                break;
            }
        }
    }

    private static Usuario escolherCriador(
        Scanner scanner,
        List<Usuario> usuarios
    ) {
        return escolherUsuario(scanner, "=== Criador do ticket ===", usuarios, usuarios.get(0));
    }

    private static void atribuirResponsavel(
        Scanner scanner,
        SistemaTickets sistemaTickets,
        List<Usuario> usuarios
    ) {
        Ticket ticket = escolherTicket(scanner, sistemaTickets);
        if (ticket == null) {
            return;
        }

        List<Usuario> gestores = filtrarUsuariosPorTipo(usuarios, 1);
        List<Usuario> desenvolvedores = filtrarUsuariosPorTipo(usuarios, 2);

        if (gestores.isEmpty()) {
            System.out.println("Nao ha gestor cadastrado para autorizar a atribuicao.");
            return;
        }

        if (desenvolvedores.isEmpty()) {
            System.out.println("Nao ha desenvolvedor cadastrado para receber o ticket.");
            return;
        }

        Usuario gestorAutor = escolherUsuario(
            scanner,
            "=== Gestor autor da atribuicao ===",
            gestores,
            gestores.get(0)
        );
        Usuario responsavel = escolherUsuario(
            scanner,
            "=== Desenvolvedor responsavel ===",
            desenvolvedores,
            desenvolvedores.get(0)
        );

        boolean sucesso = sistemaTickets.atribuirResponsavel(ticket.getId(), responsavel, gestorAutor);
        if (sucesso) {
            System.out.println("Responsavel atribuido com sucesso.");
        } else {
            System.out.println(sistemaTickets.explicarFalhaAtribuicao(ticket.getId(), responsavel, gestorAutor));
        }
    }

    private static void alterarStatus(
        Scanner scanner,
        SistemaTickets sistemaTickets,
        List<Usuario> usuarios
    ) {
        Ticket ticket = escolherTicket(scanner, sistemaTickets);
        if (ticket == null) {
            return;
        }

        Usuario autor = escolherAutorPorStatus(scanner, ticket.getStatus(), usuarios);
        if (autor == null) {
            return;
        }

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

    private static Usuario escolherAutorPorStatus(
        Scanner scanner,
        StatusTicket statusAtual,
        List<Usuario> usuarios
    ) {
        List<Usuario> desenvolvedores = filtrarUsuariosPorTipo(usuarios, 2);
        List<Usuario> qas = filtrarUsuariosPorTipo(usuarios, 3);

        if (statusAtual == StatusTicket.EM_TESTE) {
            if (qas.isEmpty()) {
                System.out.println("Nao ha QA cadastrado para esta transicao.");
                return null;
            }
            return escolherUsuario(scanner, "=== QA autor da transicao ===", qas, qas.get(0));
        }

        if (desenvolvedores.isEmpty()) {
            System.out.println("Nao ha desenvolvedor cadastrado para esta transicao.");
            return null;
        }

        return escolherUsuario(
            scanner,
            "=== Desenvolvedor autor da transicao ===",
            desenvolvedores,
            desenvolvedores.get(0)
        );
    }

    private static List<Usuario> filtrarUsuariosPorTipo(List<Usuario> usuarios, int tipo) {
        List<Usuario> filtrados = new ArrayList<>();
        for (Usuario usuario : usuarios) {
            if (tipo == 1 && usuario instanceof Gestor) {
                filtrados.add(usuario);
            } else if (tipo == 2 && usuario instanceof Desenvolvedor) {
                filtrados.add(usuario);
            } else if (tipo == 3 && usuario instanceof Qa) {
                filtrados.add(usuario);
            }
        }
        return filtrados;
    }

    private static Usuario escolherUsuario(Scanner scanner, String titulo, List<Usuario> usuarios, Usuario padrao) {
        System.out.println(titulo);
        for (int i = 0; i < usuarios.size(); i++) {
            System.out.println((i + 1) + " - " + usuarios.get(i));
        }
        System.out.print("Escolha: ");

        int opcao = lerInteiro(scanner);
        int indice = opcao - 1;
        if (indice < 0 || indice >= usuarios.size()) {
            System.out.println("Escolha invalida. Usando " + padrao.getNome() + ".");
            return padrao;
        }

        return usuarios.get(indice);
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