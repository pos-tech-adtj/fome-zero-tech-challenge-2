# Fome Zero - Tech Challenge FIAP

API REST para gerenciamento de um sistema de delivery/restaurantes, cobrindo usuários, restaurantes e itens de cardápio, desenvolvida como tech challenge da FIAP.

## Tecnologias

| Tecnologia | Versão |
|---|---|
| Java | 21 |
| Spring Boot | 4.0.5 |
| PostgreSQL | 16 |
| Flyway | — |
| Lombok | — |
| Spring Security | — |
| springdoc-openapi (Swagger UI) | 3.0.3 |
| Docker / Docker Compose | — |

## Arquitetura

O projeto segue uma estrutura inspirada em Clean Architecture com separação clara entre camadas:

- `controller` e `infrastructure/web/controller` — entrada HTTP via REST controllers
- `application/dto/request` e `application/dto/response` — objetos de entrada e saída
- `application/usecase` — regras de negócio
- `domain/model` — entidades e enums de domínio
- `infrastructure/persistence` — entidades JPA e implementações de repositório
- `infrastructure/security` — autenticação, autorização e filtro JWT
- `infrastructure/config` — configurações gerais, Swagger e integrações

## Endpoints

### Autenticação

| Método | Rota | Descrição | Acesso |
|---|---|---|---|
| `POST` | `/v1/auth/login` | Autentica o usuário e retorna um token JWT | Público |

### Usuários

| Método | Rota | Descrição | Acesso |
|---|---|---|---|
| `POST` | `/v1/usuarios` | Criar usuário | Público |
| `GET` | `/v1/usuarios` | Listar todos os usuários | Autenticado |
| `GET` | `/v1/usuarios/{id}` | Buscar usuário por ID | Autenticado |
| `GET` | `/v1/usuarios/nome/{nome}` | Buscar usuários por nome | Autenticado |
| `PUT` | `/v1/usuarios/{id}` | Atualizar dados do usuário | Autenticado |
| `PATCH` | `/v1/usuarios/{id}/senha` | Alterar senha | Autenticado |
| `DELETE` | `/v1/usuarios/{id}` | Deletar usuário | Autenticado |

### Restaurantes

| Método | Rota | Descrição | Acesso |
|---|---|---|---|
| `POST` | `/v1/restaurantes` | Criar restaurante | Autenticado |
| `GET` | `/v1/restaurantes` | Listar todos os restaurantes | Autenticado |
| `GET` | `/v1/restaurantes/{id}` | Buscar restaurante por ID | Autenticado |
| `PUT` | `/v1/restaurantes/{id}` | Atualizar restaurante | Autenticado |
| `DELETE` | `/v1/restaurantes/{id}` | Deletar restaurante | Autenticado |

### Itens de Cardápio

| Método | Rota | Descrição | Acesso |
|---|---|---|---|
| `POST` | `/v1/itens-cardapio` | Criar item de cardápio | Autenticado |
| `GET` | `/v1/itens-cardapio/restaurante/{restauranteId}` | Listar itens de um restaurante | Autenticado |
| `GET` | `/v1/itens-cardapio/{id}` | Buscar item por ID | Autenticado |
| `PUT` | `/v1/itens-cardapio/{id}` | Atualizar item de cardápio | Autenticado |
| `DELETE` | `/v1/itens-cardapio/{id}` | Deletar item de cardápio | Autenticado |

Rotas marcadas como **Autenticado** exigem o header `Authorization: Bearer <token>`, obtido em `/v1/auth/login`. As rotas públicas são o login, a criação de usuário (`POST /v1/usuarios`) e a documentação do Swagger.

A documentação completa dos endpoints, com exemplos de request e response, está disponível via Swagger UI em `http://localhost:8082/swagger-ui.html` após subir a aplicação.

## Pré-requisitos

- [Docker](https://www.docker.com/) e Docker Compose instalados

## Como executar

### 1. Clone o repositório

```bash
git clone https://github.com/pos-tech-adtj/fome-zero-tech-challenge-2
cd fome-zero-tech-challenge-2
```

### 2. Configure as variáveis de ambiente

Copie o arquivo de exemplo e ajuste conforme necessário:

```bash
cp .env.example .env
```

Conteúdo padrão do `.env`:

```env
# PostgreSQL
POSTGRES_USER=postgres
POSTGRES_PASSWORD=postgres
POSTGRES_DB=fomezerodb
```

### 3. Suba a aplicação com Docker Compose

```bash
docker compose up --build
```

Isso irá:
- Subir o PostgreSQL 16 na porta `5432`
- Buildar a imagem da aplicação com Maven e executar a API em container
- Iniciar a aplicação somente após o banco ficar saudável
- Executar as migrações do Flyway automaticamente na subida da aplicação
- Expor a API na porta `8082`

### 4. Acesse a aplicação

| Recurso | URL |
|---|---|
| API | `http://localhost:8082` |
| Swagger UI | `http://localhost:8082/swagger-ui.html` |

## Collection Postman

A collection com todos os endpoints e exemplos de request está disponível no arquivo [`Fome Zero API - Tech Challenge.postman_collection.json`](./Fome%20Zero%20API%20-%20Tech%20Challenge.postman_collection.json) na raiz do projeto.

Para importar: abra o Postman > **Import** > selecione o arquivo.

## Variáveis de ambiente da aplicação

Caso queira rodar a aplicação fora do Docker Compose, configure as seguintes variáveis:

| Variável | Padrão |
|---|---|
| `SPRING_DATASOURCE_URL` | `jdbc:postgresql://localhost:5432/fomezerodb` |
| `SPRING_DATASOURCE_USERNAME` | `postgres` |
| `SPRING_DATASOURCE_PASSWORD` | `postgres` |
| `SERVER_PORT` | `8082` |
