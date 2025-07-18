# API Banco Digital

![Java](https://img.shields.io/badge/Java-17%2B-blue?style=for-the-badge&logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x.x-green?style=for-the-badge&logo=spring)
![JWT](https://img.shields.io/badge/JWT-Authentication-black?style=for-the-badge&logo=jsonwebtokens)
![Swagger](https://img.shields.io/badge/Swagger-API_Docs-orange?style=for-the-badge&logo=swagger)
![License](https://img.shields.io/badge/License-MIT-lightgrey?style=for-the-badge)

## 📌 Visão Geral

Esta é uma API RESTful completa desenvolvida em Spring Boot que simula o back-end de um banco digital. O projeto implementa funcionalidades essenciais como gerenciamento de clientes, contas, cartões e um sistema de segurança robusto baseado em tokens JWT.

## 📄 Contexto do Projeto

* Este projeto foi desenvolvido como a avaliação final do bootcamp **Codigo de Base**. As especificações, regras de negócio e endpoints foram definidos nos documentos fornecidos pelo bootcamp.

---

## ✨ Como Funciona

O sistema permite que clientes sejam cadastrados e classificados em diferentes categorias (`Comum`, `Super`, `Premium`), o que impacta diretamente as regras de negócio, como taxas e limites. Cada cliente pode ter contas `corrente` e `poupança`, que servem de base para todas as operações financeiras.

#### Contas e Transações
* **Conta Corrente**: Sofre um débito mensal de uma taxa de manutenção, cujo valor depende da categoria do cliente.
* **Conta Poupança**: Recebe um crédito mensal de rendimentos com base em uma taxa de juros composta.
* **Operações**: A API suporta depósitos, saques, consulta de saldo e transferências entre contas.

#### Cartões de Crédito e Débito
* **Emissão**: Cartões são emitidos vinculados a uma conta.
* **Cartão de Crédito**: Possui um limite de crédito que varia com a categoria do cliente e permite o gerenciamento da fatura.
* **Cartão de Débito**: Opera com um limite de transação diário que pode ser configurado pelo usuário.
* **Segurança**: Um seguro contra fraudes é gerado automaticamente para cada cartão de crédito.

---

## 🛠️ Stack Tecnológico

* **Linguagem**: Java 17+
* **Framework**: Spring Boot 3
* **Módulos Spring**: Web, Data JPA, Security
* **Banco de Dados**: H2 (In-Memory)
* **Segurança**: JSON Web Tokens (JWT)
* **Documentação**: Swagger/OpenAPI 3
* **Build Tool**: Maven

---

## 🚀 Primeiros Passos

#### Requisitos
* JDK 17 ou superior
* Maven 3.8+

#### Execução
1.  Clone este repositório.
2.  Navegue até a pasta raiz do projeto.
3.  Execute o comando para iniciar a aplicação:
    ```bash
    ./mvnw spring-boot:run
    ```
4.  A API estará disponível em `http://localhost:8080`.

#### Acesso e Documentação
* **Documentação Interativa (Swagger)**: `http://localhost:8080/swagger-ui.html`
* **Console do Banco H2**: `http://localhost:8080/h2-console`
    * **JDBC URL**: `jdbc:h2:mem:bancodigitaldb`
    * **Usuário**: `sa`
    * **Senha**: `password`

---

## Endpoints da API

Abaixo uma lista resumida dos endpoints disponíveis na API. Para detalhes completos sobre os parâmetros e respostas, consulte a [Documentação do Swagger](http://localhost:8080/swagger-ui.html).

<details>
<summary><strong>Clique para expandir a lista de endpoints</strong></summary>

| Verbo | Rota | Descrição |
| :--- | :--- | :--- |
| **authentication-controller** |
| `POST` | `/login` | Realiza o login do usuário e retorna um token JWT. |
| **cliente-controller** |
| `POST` | `/cliente/add` | Adiciona um novo cliente. |
| `GET` | `/clientes/listAll` | Lista todos os clientes. |
| `GET` | `/clientes/{id}` | Obtém detalhes de um cliente. |
| `PUT` | `/clientes/{id}` | Atualiza informações de um cliente. |
| `DELETE` | `/cliente/{id}` | Remove um cliente. |
| **conta-controller** |
| `POST` | `/conta` | Cria uma nova conta. |
| `GET` | `/conta/{id}/saldo` | Consulta o saldo da conta. |
| `POST` | `/conta/{id}/deposito` | Realiza um depósito na conta. |
| `POST` | `/conta/{id}/saque` | Realiza um saque da conta. |
| `POST` | `/conta/{id}/transferencia` | Realiza transferência entre contas. |
| `PUT` | `/conta/{id}/manutencao` | Aplica a taxa de manutenção (conta corrente). |
| `PUT` | `/conta/{id}/rendimentos` | Aplica os rendimentos (conta poupança). |
| **cartao-controller** |
| `POST` | `/cartoes` | Emite um novo cartão. |
| `GET` | `/cartoes/{id}/fatura` | Consulta a fatura do cartão. |
| `POST` | `/cartoes/{id}/pagamento` | Realiza um pagamento com o cartão. |
| `POST` | `/cartoes/{id}/fatura/pagamento` | Realiza o pagamento da fatura do cartão. |
| `PUT` | `/cartoes/{id}/status` | Ativa ou desativa um cartão. |
| `PUT` | `/cartoes/{id}/limite` | Altera o limite do cartão. |
| `PUT` | `/cartoes/{id}/limite-diario` | Altera o limite diário do cartão. |
| **seguro-controller** |
| `GET` | `/seguros` | Lista os seguros disponíveis. |

</details>

---

## ✒️ Autor

**Wirizada**

* GitHub: [Wirizada](https://github.com/Wirizada)

## 📄 Licença

Este projeto está sob a licença MIT.
