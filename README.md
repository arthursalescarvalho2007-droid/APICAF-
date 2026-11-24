# Cafe Comanda - guia simples

Este programa em Java registra produtos, comandas e itens de pedidos usando PostgreSQL no Supabase.

## 1. Prepare o Supabase

1. No Supabase, abra **SQL Editor** e clique em **New query**.
2. Abra o arquivo `banco_supabase.sql` desta pasta, copie todo o conteudo e cole na consulta.
3. Clique em **Run**. Isso cria as quatro tabelas, o usuario inicial e sete produtos.

Usuario inicial do programa: `admin`  
Senha inicial do programa: `cafeteria123`

## 2. Coloque a senha do banco no Java

Abra `src/main/java/br/com/cafeteria/Main.java`. Perto do inicio, encontre:

```java
private static final String SENHA_BANCO = "COLOQUE_A_SENHA_DO_BANCO_AQUI";
```

Troque somente o texto entre aspas pela senha do banco que voce definiu no Supabase. Essa e a senha do banco, nao a senha do usuario `admin` do programa. Nao compartilhe essa senha.

O endereco, a porta, o nome do banco e o usuario ja estao preenchidos com os dados de conexao que voce enviou.

## 3. Abra e execute

1. Extraia o ZIP para uma pasta.
2. No IntelliJ IDEA, escolha **File > Open** e abra a pasta `CafeComanda`.
3. Aguarde o IntelliJ carregar o Maven.
4. Abra `src > main > java > br.com.cafeteria > Main.java`.
5. Clique no triangulo verde ao lado de `main` e escolha **Run**.
6. Entre com `admin` e `cafeteria123`.

## Como fazer uma comanda

1. No menu, escolha **2 - Comandas** e depois **1 - Abrir comanda**. Anote o codigo mostrado.
2. Escolha **3 - Itens dos pedidos** e depois **1 - Adicionar produto a uma comanda**.
3. Digite o codigo da comanda, o codigo do produto e a quantidade.
4. Use **5 - Ver itens de uma comanda** para ver a comanda e o total.

## O que significam alguns nomes

- **Tabela:** uma lista de dados no banco, por exemplo `produtos`.
- **INSERT:** comando SQL que adiciona um registro. O programa faz isso quando voce cadastra um produto ou abre uma comanda.
- **SELECT:** busca registros.
- **UPDATE:** altera registros.
- **DELETE:** remove registros.

## Documento tecnico

O PDF do projeto esta na pasta documentacao/Documento Tecnico Cafe Comanda.pdf.
