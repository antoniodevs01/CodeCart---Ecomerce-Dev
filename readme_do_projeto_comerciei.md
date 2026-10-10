# 🛒 Comerciei API

O **Comerciei** é uma API RESTful desenvolvida em Java com Spring Boot, projetada para gerir o catálogo de produtos de um sistema de e-commerce. Este projeto demonstra a implementação completa de operações CRUD (Create, Read, Update, Delete) com persistência de dados em PostgreSQL.

## 🚀 Tecnologias Utilizadas

Este projeto foi construído utilizando as seguintes tecnologias e ferramentas:

*   **Java 17**
*   **Spring Boot (v4.1.1)**
*   **Spring Data JPA / Hibernate** (Mapeamento Objeto-Relacional)
*   **PostgreSQL 17** (Base de Dados Relacional)
*   **Maven** (Gestão de Dependências)
*   **Bruno** (Testes de API)

## ⚙️ Pré-requisitos

Para executar este projeto localmente, vai precisar de ter instalado na sua máquina:
*   [Java Development Kit (JDK) 17](https://adoptium.net/)
*   [PostgreSQL](https://www.postgresql.org/) (em execução na porta 5432)
*   Uma base de dados criada com o nome `ecommerce_db`.

## 🛠️ Como Executar o Projeto

1. Clone este repositório para a sua máquina local.
2. Certifique-se de que o serviço do PostgreSQL está em execução:
   * No Windows (como Administrador): `net start postgresql-x64-17`
3. Abra o terminal, navegue até à pasta raiz do projeto (onde está o ficheiro `mvnw`).
4. Execute o seguinte comando para iniciar o servidor Spring Boot:

```bash
.\mvnw spring-boot:run
```

A API ficará disponível em `http://localhost:8080`.

## 📌 Endpoints da API (Rotas)

A API expõe o recurso `/api/products` para gestão do catálogo. Os IDs gerados são do tipo UUID.

| Método | Rota | Descrição | Status de Sucesso |
| :--- | :--- | :--- | :--- |
| `GET` | `/api/products` | Lista todos os produtos cadastrados. | 200 OK |
| `GET` | `/api/products/{id}` | Busca um produto específico pelo seu ID. | 200 OK |
| `POST` | `/api/products` | Cria um novo produto. | 201 Created |
| `PUT` | `/api/products/{id}` | Atualiza os dados de um produto existente. | 200 OK |
| `DELETE` | `/api/products/{id}` | Remove um produto do sistema. | 204 No Content |

### Exemplo de Corpo de Requisição (JSON)

Para as requisições **POST** e **PUT**, envie os dados no formato JSON conforme o exemplo abaixo:

```json
{
  "name": "CLP Siemens S7-1200",
  "description": "Controlador Lógico Programável com 14 entradas digitais.",
  "price": 2500.00,
  "stockQuantity": 20
}
```

## 🏗️ Estrutura do Projeto

O projeto segue a arquitetura padrão em camadas (MVC) do Spring Boot:
*   **Domain/Entity:** Representação das tabelas do banco de dados (ex: `Product`).
*   **Repository:** Interfaces de comunicação direta com o banco de dados via Spring Data JPA.
*   **Service:** Camada que concentra as regras de negócio.
*   **Controller:** Camada que expõe os endpoints REST e gere as requisições HTTP.

---
*Desenvolvido como parte do estudo de Engenharia de Software e Desenvolvimento Backend.*