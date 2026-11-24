package br.com.cafeteria;

import java.sql.*;
import java.util.Scanner;
public class Main {
    private static final String ENDERECO = "jdbc:postgresql://aws-1-us-east-1.pooler.supabase.com:5432/postgres?sslmode=require";
    private static final String USUARIO_BANCO = "postgres.alhwkrvkausblvbfhfgv";
    private static final String SENHA_BANCO = "COLOQUE_A_SENHA_DO_BANCO_AQUI";

    private static final Scanner teclado = new Scanner(System.in);
    private static Connection conexao;
    private static int idUsuarioLogado;

    public static void main(String[] args) {
        try {
            // Abre a conexao com o banco.
            conexao = DriverManager.getConnection(ENDERECO, USUARIO_BANCO, SENHA_BANCO);
            System.out.println("=== CAFE MORENO ===");

            if (fazerLogin()) {
                mostrarMenuPrincipal();
            }
        } catch (SQLException erro) {
            System.out.println("Nao foi possivel conectar ao Supabase.");
            System.out.println("Confira o endereco, usuario e senha do banco no comeco de Main.java.");
            System.out.println("Detalhe: " + erro.getMessage());
        } finally {
            try {
                if (conexao != null) conexao.close();
            } catch (SQLException erro) {
                System.out.println("Erro ao fechar a conexao.");
            }
        }
    }

    // Procura o login na tabela usuarios. O ? recebe o texto digitado.
    private static boolean fazerLogin() throws SQLException {
        System.out.println("Entre com seu usuario e senha.");
        String usuario = ler("Usuario: ");
        String senha = ler("Senha: ");

        String sql = "SELECT id_usuario FROM usuarios WHERE usuario = ? AND senha = ? AND ativo = TRUE";
        PreparedStatement consulta = conexao.prepareStatement(sql);
        consulta.setString(1, usuario);
        consulta.setString(2, senha);
        ResultSet resultado = consulta.executeQuery();

        if (resultado.next()) {
            idUsuarioLogado = resultado.getInt("id_usuario");
            System.out.println("Login realizado. Bem-vindo!\n");
            resultado.close();
            consulta.close();
            return true;
        }

        System.out.println("Usuario ou senha incorretos.");
        resultado.close();
        consulta.close();
        return false;
    }

    private static void mostrarMenuPrincipal() throws SQLException {
        int opcao;
        do {
            System.out.println("\n--- MENU PRINCIPAL ---");
            System.out.println("1 - Produtos do cardapio");
            System.out.println("2 - Comandas");
            System.out.println("3 - Itens dos pedidos");
            System.out.println("4 - Usuarios");
            System.out.println("5 - Consultas e relatorios");
            System.out.println("0 - Sair");
            opcao = lerNumero("Escolha: ");

            switch (opcao) {
                case 1: menuProdutos(); break;
                case 2: menuComandas(); break;
                case 3: menuItens(); break;
                case 4: menuUsuarios(); break;
                case 5: menuConsultas(); break;
                case 0: System.out.println("Ate logo!"); break;
                default: System.out.println("Escolha uma opcao da lista.");
            }
        } while (opcao != 0);
    }

    // PRODUTOS: cadastro, alteracao, exclusao, busca por codigo e listagem.
    private static void menuProdutos() throws SQLException {
        int opcao;
        do {
            System.out.println("\n--- PRODUTOS ---");
            System.out.println("1 - Cadastrar produto\n2 - Alterar produto\n3 - Excluir produto");
            System.out.println("4 - Buscar produto pelo codigo\n5 - Listar cardapio\n0 - Voltar");
            opcao = lerNumero("Escolha: ");

            if (opcao == 1) {
                String nome = ler("Nome: ");
                String categoria = ler("Categoria (Alimento ou Bebida): ");
                double preco = lerPreco("Preco: ");
                executar("INSERT INTO produtos (nome, categoria, preco) VALUES (?, ?, ?)", nome, categoria, preco);
            } else if (opcao == 2) {
                int id = lerNumero("Codigo do produto: ");
                String nome = ler("Novo nome: ");
                String categoria = ler("Nova categoria: ");
                double preco = lerPreco("Novo preco: ");
                executar("UPDATE produtos SET nome = ?, categoria = ?, preco = ? WHERE id_produto = ?", nome, categoria, preco, id);
            } else if (opcao == 3) {
                int id = lerNumero("Codigo do produto para excluir: ");
                executar("DELETE FROM produtos WHERE id_produto = ?", id);
            } else if (opcao == 4) {
                int id = lerNumero("Codigo do produto: ");
                mostrarConsulta("SELECT * FROM produtos WHERE id_produto = ?", id);
            } else if (opcao == 5) {
                mostrarConsulta("SELECT * FROM produtos ORDER BY categoria, nome");
            }
        } while (opcao != 0);
    }

    // COMANDAS: abrir, alterar cliente/mesa, fechar, excluir, buscar e listar.
    private static void menuComandas() throws SQLException {
        int opcao;
        do {
            System.out.println("\n--- COMANDAS ---");
            System.out.println("1 - Abrir comanda\n2 - Alterar cliente e mesa\n3 - Fechar comanda");
            System.out.println("4 - Excluir comanda\n5 - Buscar pelo codigo\n6 - Listar comandas\n0 - Voltar");
            opcao = lerNumero("Escolha: ");

            if (opcao == 1) {
                String cliente = ler("Nome do cliente: ");
                int mesa = lerNumero("Numero da mesa: ");
                executar("INSERT INTO comandas (cliente, mesa, status, id_usuario) VALUES (?, ?, 'ABERTA', ?)", cliente, mesa, idUsuarioLogado);
                System.out.println("Anote o codigo da comanda para adicionar os produtos.");
            } else if (opcao == 2) {
                int id = lerNumero("Codigo da comanda: ");
                String cliente = ler("Novo nome do cliente: ");
                int mesa = lerNumero("Nova mesa: ");
                executar("UPDATE comandas SET cliente = ?, mesa = ? WHERE id_comanda = ?", cliente, mesa, id);
            } else if (opcao == 3) {
                int id = lerNumero("Codigo da comanda para fechar: ");
                executar("UPDATE comandas SET status = 'FECHADA' WHERE id_comanda = ?", id);
            } else if (opcao == 4) {
                int id = lerNumero("Codigo da comanda para excluir: ");
                executar("DELETE FROM comandas WHERE id_comanda = ?", id);
            } else if (opcao == 5) {
                int id = lerNumero("Codigo da comanda: ");
                mostrarConsulta("SELECT * FROM comandas WHERE id_comanda = ?", id);
            } else if (opcao == 6) {
                mostrarConsulta("SELECT * FROM comandas ORDER BY id_comanda DESC");
            }
        } while (opcao != 0);
    }

    // ITENS: cada item liga uma comanda a um produto.
    private static void menuItens() throws SQLException {
        int opcao;
        do {
            System.out.println("\n--- ITENS DA COMANDA ---");
            System.out.println("1 - Adicionar produto a uma comanda\n2 - Alterar quantidade");
            System.out.println("3 - Excluir item\n4 - Buscar item pelo codigo\n5 - Ver itens de uma comanda\n0 - Voltar");
            opcao = lerNumero("Escolha: ");

            if (opcao == 1) {
                int idComanda = lerNumero("Codigo da comanda aberta: ");
                int idProduto = lerNumero("Codigo do produto: ");
                int quantidade = lerNumero("Quantidade: ");
                // Copia o preco atual do produto para guardar o preco deste pedido.
                String sql = "INSERT INTO itens_comanda (id_comanda, id_produto, quantidade, preco_unitario) " +
                        "SELECT ?, p.id_produto, ?, p.preco FROM produtos p " +
                        "JOIN comandas c ON c.id_comanda = ? " +
                        "WHERE p.id_produto = ? AND c.status = 'ABERTA'";
                executar(sql, idComanda, quantidade, idComanda, idProduto);
            } else if (opcao == 2) {
                int id = lerNumero("Codigo do item: ");
                int quantidade = lerNumero("Nova quantidade: ");
                executar("UPDATE itens_comanda SET quantidade = ? WHERE id_item = ?", quantidade, id);
            } else if (opcao == 3) {
                int id = lerNumero("Codigo do item para excluir: ");
                executar("DELETE FROM itens_comanda WHERE id_item = ?", id);
            } else if (opcao == 4) {
                int id = lerNumero("Codigo do item: ");
                mostrarConsulta("SELECT * FROM itens_comanda WHERE id_item = ?", id);
            } else if (opcao == 5) {
                int idComanda = lerNumero("Codigo da comanda: ");
                imprimirComanda(idComanda);
            }
        } while (opcao != 0);
    }

    // USUARIOS: cadastro basico dos acessos ao sistema.
    private static void menuUsuarios() throws SQLException {
        int opcao;
        do {
            System.out.println("\n--- USUARIOS ---");
            System.out.println("1 - Cadastrar usuario\n2 - Alterar usuario\n3 - Excluir usuario");
            System.out.println("4 - Buscar pelo codigo\n5 - Listar usuarios\n0 - Voltar");
            opcao = lerNumero("Escolha: ");

            if (opcao == 1) {
                String nome = ler("Nome: ");
                String usuario = ler("Login: ");
                String senha = ler("Senha: ");
                executar("INSERT INTO usuarios (nome, usuario, senha) VALUES (?, ?, ?)", nome, usuario, senha);
            } else if (opcao == 2) {
                int id = lerNumero("Codigo do usuario: ");
                String nome = ler("Novo nome: ");
                String senha = ler("Nova senha: ");
                executar("UPDATE usuarios SET nome = ?, senha = ? WHERE id_usuario = ?", nome, senha, id);
            } else if (opcao == 3) {
                int id = lerNumero("Codigo do usuario para excluir: ");
                executar("DELETE FROM usuarios WHERE id_usuario = ?", id);
            } else if (opcao == 4) {
                int id = lerNumero("Codigo do usuario: ");
                mostrarConsulta("SELECT id_usuario, nome, usuario, ativo FROM usuarios WHERE id_usuario = ?", id);
            } else if (opcao == 5) {
                mostrarConsulta("SELECT id_usuario, nome, usuario, ativo FROM usuarios ORDER BY nome");
            }
        } while (opcao != 0);
    }

    private static void menuConsultas() throws SQLException {
        int opcao;
        do {
            System.out.println("\n--- CONSULTAS E RELATORIOS ---");
            System.out.println("1 - Procurar produto pelo nome\n2 - Procurar comanda pelo cliente");
            System.out.println("3 - Ver comanda detalhada (3 tabelas)\n4 - Ver comanda com funcionario (4 tabelas)");
            System.out.println("0 - Voltar");
            opcao = lerNumero("Escolha: ");

            if (opcao == 1) {
                String nome = ler("Digite parte do nome: ");
                mostrarConsulta("SELECT * FROM produtos WHERE nome LIKE ?", "%" + nome + "%");
            } else if (opcao == 2) {
                String cliente = ler("Digite parte do nome do cliente: ");
                mostrarConsulta("SELECT * FROM comandas WHERE cliente LIKE ?", "%" + cliente + "%");
            } else if (opcao == 3) {
                int id = lerNumero("Codigo da comanda: ");
                imprimirComanda(id);
            } else if (opcao == 4) {
                int id = lerNumero("Codigo da comanda: ");
                String sql = "SELECT c.id_comanda, c.cliente, c.mesa, c.status, u.nome AS funcionario, " +
                        "p.nome AS produto, i.quantidade, i.preco_unitario, " +
                        "(i.quantidade * i.preco_unitario) AS subtotal " +
                        "FROM comandas c JOIN usuarios u ON c.id_usuario = u.id_usuario " +
                        "LEFT JOIN itens_comanda i ON c.id_comanda = i.id_comanda " +
                        "LEFT JOIN produtos p ON i.id_produto = p.id_produto WHERE c.id_comanda = ?";
                mostrarConsulta(sql, id);
            }
        } while (opcao != 0);
    }

    // Mostra cada produto da conta e, no final, o total a pagar.
    private static void imprimirComanda(int idComanda) throws SQLException {
        String sql = "SELECT p.nome, i.quantidade, i.preco_unitario, " +
                "(i.quantidade * i.preco_unitario) AS subtotal " +
                "FROM itens_comanda i JOIN produtos p ON i.id_produto = p.id_produto " +
                "WHERE i.id_comanda = ?";
        mostrarConsulta(sql, idComanda);

        String sqlTotal = "SELECT COALESCE(SUM(quantidade * preco_unitario), 0) AS total " +
                "FROM itens_comanda WHERE id_comanda = ?";
        mostrarConsulta(sqlTotal, idComanda);
    }

    // Executa INSERT, UPDATE ou DELETE. Cada ? recebe um valor da lista.
    private static void executar(String sql, Object... valores) throws SQLException {
        PreparedStatement comando = conexao.prepareStatement(sql);
        for (int i = 0; i < valores.length; i++) {
            comando.setObject(i + 1, valores[i]);
        }
        int linhas = comando.executeUpdate();
        if (linhas > 0) {
            System.out.println("Pronto! Operacao realizada.");
        } else {
            System.out.println("Nenhum registro foi alterado. Confira os codigos e o status da comanda.");
        }
        comando.close();
    }

    // Executa um SELECT e mostra os nomes das colunas com os dados encontrados.
    private static void mostrarConsulta(String sql, Object... valores) throws SQLException {
        PreparedStatement consulta = conexao.prepareStatement(sql);
        for (int i = 0; i < valores.length; i++) {
            consulta.setObject(i + 1, valores[i]);
        }
        ResultSet resultado = consulta.executeQuery();
        ResultSetMetaData colunas = resultado.getMetaData();
        int quantidadeColunas = colunas.getColumnCount();
        int quantidadeLinhas = 0;

        while (resultado.next()) {
            quantidadeLinhas++;
            for (int i = 1; i <= quantidadeColunas; i++) {
                System.out.print(colunas.getColumnLabel(i) + ": " + resultado.getString(i) + " | ");
            }
            System.out.println();
        }
        if (quantidadeLinhas == 0) {
            System.out.println("Nenhum resultado encontrado.");
        }
        resultado.close();
        consulta.close();
    }

    private static String ler(String pergunta) {
        System.out.print(pergunta);
        return teclado.nextLine();
    }

    private static int lerNumero(String pergunta) {
        try {
            return Integer.parseInt(ler(pergunta));
        } catch (NumberFormatException erro) {
            System.out.println("Digite um numero inteiro.");
            return lerNumero(pergunta);
        }
    }

    private static double lerPreco(String pergunta) {
        try {
            return Double.parseDouble(ler(pergunta).replace(',', '.'));
        } catch (NumberFormatException erro) {
            System.out.println("Digite um preco, por exemplo 8,50.");
            return lerPreco(pergunta);
        }
    }
}
