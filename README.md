# Cafe Comanda - guia simples

Este programa em Java registra produtos, comandas e itens de pedidos usando PostgreSQL no Supabase.

## 1. Prepare o Supabase

1. No Supabase, abra **SQL Editor** e clique em **New query**.
2. Abra o arquivo `banco_supabase.sql`, copie o conteudo e cole na consulta.
3. Clique em **Run**. Isso cria as tabelas, o usuario inicial e os produtos de exemplo.

Usuario inicial do programa: `admin`  
Senha inicial do programa: `cafeteria123`

## 2. Configure a conexao do banco no IntelliJ

Os dados de conexao nao ficam gravados no codigo. No IntelliJ IDEA, abra **Run > Edit Configurations**, selecione a configuracao da classe `Main` e adicione em **Environment variables**:

```text
SUPABASE_DB_URL=jdbc:postgresql://host:porta/banco?sslmode=require
SUPABASE_DB_USER=seu_usuario_do_banco
SUPABASE_DB_PASSWORD=sua_senha_do_banco
```

Copie os valores correspondentes do Supabase. Mantenha esses dados somente na configuracao local do IntelliJ; nao os coloque no codigo nem os publique.

## 3. Abra e execute

1. Extraia o ZIP para uma pasta.
2. No IntelliJ IDEA, escolha **File > Open** e abra a pasta do projeto.
3. Aguarde o IntelliJ carregar o Maven.
4. Abra `src > main > java > br.com.cafeteria > Main.java`.
5. Clique no triangulo verde ao lado de `main` e escolha **Run**.
6. Entre com `admin` e `cafeteria123`.

## Como cadastrar produtos e fazer uma comanda

1. Para cadastrar cafe, bebida ou alimento, escolha **1 - Produtos do cardapio > 1 - Cadastrar produto**.
2. Abra a comanda em **2 - Comandas > 1 - Abrir comanda** e anote o codigo.
3. Escolha **3 - Itens dos pedidos > 1 - Adicionar produto a uma comanda**. Informe o codigo da comanda, o codigo do produto e a quantidade.
4. Antes de fechar, escolha **2 - Comandas > 3 - Mostrar total da comanda**. A tela mostra os produtos pedidos, quantidades, precos unitarios, subtotais e o total.
5. Se estiver tudo certo, escolha **4 - Fechar comanda**.

## O que significam alguns nomes

- **Tabela:** uma lista de dados no banco, por exemplo `produtos`.
- **INSERT:** comando SQL que adiciona um registro.
- **SELECT:** busca registros.
- **UPDATE:** altera registros.
- **DELETE:** remove registros.

## Documento tecnico

O PDF do projeto esta em `documentacao/Documento Tecnico Cafe Comanda.pdf`.
