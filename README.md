# 📚 Biblioteca Virtual — API REST

Sistema de gerenciamento de biblioteca pública com disponibilização de livros em PDF, desenvolvido com Spring Boot.

---
## 👥 Equipe

| Membro | Responsabilidade |
|---|---|
| Luis Matheus | Autor, Categoria, integração com MinIO e PostgreSQL, configurações gerais e correções|
| Caio Eduardo | Usuário, Livro e Empréstimo |
| Wesley | Migrations, exceções e Swagger |

---

## 📋 Descrição do Projeto

A Biblioteca Virtual é uma API REST que permite gerenciar o acervo de uma biblioteca pública online. Com ela é possível cadastrar livros em PDF, organizar por autores e categorias, registrar usuários e controlar empréstimos. Os arquivos PDF são armazenados no MinIO, um serviço de armazenamento de objetos compatível com Amazon S3, que roda localmente via Docker.

---

## 🛠️ Tecnologias utilizadas

- **Java 21**
- **Spring Boot 4.x**
- **Spring Data JPA** — persistência de dados
- **Spring Web** — criação das rotas REST
- **Spring Validation** — validação dos DTOs
- **Flyway** — versionamento e criação das tabelas no banco
- **PostgreSQL 16** — banco de dados relacional
- **MinIO** — armazenamento de arquivos PDF
- **Lombok** — redução de código boilerplate
- **SpringDoc OpenAPI (Swagger)** — documentação das rotas
- **Docker / Docker Compose** — orquestração dos serviços
- **pgAdmin 4** — interface gráfica para o banco de dados

---

## ✅ Pré-requisitos

Antes de rodar o projeto, garanta que você tem instalado:

- [Java 21+](https://www.oracle.com/java/technologies/downloads/)
- [Maven 3.8+](https://maven.apache.org/download.cgi) (ou use o `mvnw` incluído no projeto)
- [Docker Desktop](https://www.docker.com/products/docker-desktop/)
- [Postman](https://www.postman.com/downloads/) (opcional, para testes)

---

## 🚀 Como rodar o projeto

### 1. Clone o repositório

```bash
git clone https://github.com/seu-usuario/biblioteca.git
cd biblioteca
```

### 2. Suba os serviços com Docker

Na raiz do projeto, onde está o `docker-compose.yaml`:

No windows:
```bash
docker-compose up -d
```
No linux:
```bash
docker compose up -d
```


Confirma que os três containers estão rodando:

```bash
docker ps
```

Você deve ver:

| Container | Serviço | Porta |
|---|---|---|
| `postgres-spring` | PostgreSQL | 5432 |
| `pgadmin` | pgAdmin | 8081 |
| `biblioteca-minio` | MinIO API | 9000 |
| `biblioteca-minio` | MinIO Console | 9001 |

### 3. Rode a aplicação

```bash
./mvnw spring-boot:run
```

> No linux use `mvn spring-boot:run`

A aplicação estará disponível em `http://localhost:8080`.

Na inicialização, o **Flyway** executa automaticamente os scripts SQL e cria todas as tabelas no banco. Você verá no console:

```
Migrating schema "public" to version "1 - create autor"
Migrating schema "public" to version "2 - create categoria"
...
Successfully applied 5 migrations
```

---

## 🔗 URLs disponíveis

| Serviço | URL | Credenciais |
|---|---|---|
| API REST | `http://localhost:8080` | — |
| Swagger UI | `http://localhost:8080/swagger-ui.html` | — |
| pgAdmin | `http://localhost:8081` | `admin@admin.com` / `admin123` |
| MinIO Console | `http://localhost:9001` | `minioadmin` / `minioadmin` |

---

## 🗄️ Configuração do pgAdmin

Para visualizar os dados no banco pelo pgAdmin:

1. Acesse `http://localhost:8081`
2. Faça login com `admin@admin.com` / `admin123`
3. Clique com botão direito em **Servers → Register → Server**
4. Preencha:

**Aba General:**
```
Name: Biblioteca
```

**Aba Connection:**
```
Host:     postgres-spring
Port:     5432
Database: biblioteca
Username: admin
Password: 123456
```

5. Clique em **Save**

---

## 📦 Variáveis de configuração

Todas as configurações ficam em `src/main/resources/application.properties`:

```properties
# Banco
spring.datasource.url=jdbc:postgresql://localhost:5432/biblioteca
spring.datasource.username=admin
spring.datasource.password=123456
spring.datasource.driver-class-name=org.postgresql.Driver

# JPA
spring.jpa.hibernate.ddl-auto=update
spring.jpa.open-in-view=false

# Flyway
spring.flyway.enabled=true
spring.flyway.locations=classpath:db/migration
spring.flyway.baseline-on-migrate=true
spring.flyway.validate-on-migrate=false

# Log geral
logging.level.org.flywaydb=DEBUG
logging.level.org.springframework=INFO
logging.level.com.biblioteca=DEBUG

# Swagger
springdoc.api-docs.path=/api-docs
springdoc.swagger-ui.path=/swagger-ui.html
springdoc.swagger-ui.enabled=true

# MinIO
minio.url=http://localhost:9000
minio.access-key=minioadmin
minio.secret-key=minioadmin
minio.bucket-name=biblioteca-pdfs
```

---

## 📁 Estrutura do projeto

```
biblioteca/
├── docker-compose.yaml
├── pom.xml
└── src/
    └── main/
        ├── java/com/biblioteca/
        │   ├── config/
        │   │   ├── MinioConfig.java
        │   │   ├── MinioProperties.java
        │   │   └── SwaggerConfig.java
        │   ├── controller/
        │   │   ├── AutorController.java
        │   │   ├── CategoriaController.java
        │   │   ├── EmprestimoController.java
        │   │   ├── LivroController.java
        │   │   └── UsuarioController.java
        │   ├── dto/
        │   │   ├── autor/
        │   │   ├── categoria/
        │   │   ├── emprestimo/
        │   │   ├── livro/
        │   │   └── usuario/
        │   ├── exception/
        │   │   ├── ErrorResponse.java
        │   │   ├── GlobalExceptionHandler.java
        │   │   └── ResourceNotFoundException.java
        │   ├── model/
        │   │   ├── Autor.java
        │   │   ├── Categoria.java
        │   │   ├── Emprestimo.java
        │   │   ├── Livro.java
        │   │   └── Usuario.java
        │   ├── repository/
        │   │   ├── AutorRepository.java
        │   │   ├── CategoriaRepository.java
        │   │   ├── EmprestimoRepository.java
        │   │   ├── LivroRepository.java
        │   │   └── UsuarioRepository.java
        │   └── service/
        │       ├── AutorService.java
        │       ├── CategoriaService.java
        │       ├── EmprestimoService.java
        │       ├── LivroService.java
        │       ├── MinioService.java
        │       └── UsuarioService.java
        └── resources/
            ├── application.properties
            └── db/migration/
                ├── V1__create_autor.sql
                ├── V2__create_categoria.sql
                ├── V3__create_usuario.sql
                ├── V4__create_livro.sql
                └── V5__create_emprestimo.sql
```

---

## 🔄 Rotas da API

### Autores
| Método | Rota | Descrição |
|---|---|---|
| POST | `/api/autores` | Cadastrar autor |
| GET | `/api/autores` | Listar todos |
| GET | `/api/autores/{id}` | Buscar por ID |
| PUT | `/api/autores/{id}` | Atualizar |
| DELETE | `/api/autores/{id}` | Excluir |

### Categorias
| Método | Rota | Descrição |
|---|---|---|
| POST | `/api/categorias` | Cadastrar categoria |
| GET | `/api/categorias` | Listar todas |
| GET | `/api/categorias/{id}` | Buscar por ID |
| PUT | `/api/categorias/{id}` | Atualizar |
| DELETE | `/api/categorias/{id}` | Excluir |

### Usuários
| Método | Rota | Descrição |
|---|---|---|
| POST | `/api/usuarios` | Cadastrar usuário |
| GET | `/api/usuarios` | Listar todos |
| GET | `/api/usuarios/{id}` | Buscar por ID |
| PUT | `/api/usuarios/{id}` | Atualizar |
| DELETE | `/api/usuarios/{id}` | Excluir |

### Livros
| Método | Rota | Descrição |
|---|---|---|
| POST | `/api/livros` | Cadastrar livro com PDF |
| GET | `/api/livros` | Listar todos |
| GET | `/api/livros/{id}` | Buscar por ID |
| GET | `/api/livros/{id}/download` | Gerar link de download do PDF |
| PUT | `/api/livros/{id}` | Atualizar |
| DELETE | `/api/livros/{id}` | Excluir |

### Empréstimos
| Método | Rota | Descrição |
|---|---|---|
| POST | `/api/emprestimos` | Registrar empréstimo |
| GET | `/api/emprestimos` | Listar todos |
| GET | `/api/emprestimos/{id}` | Buscar por ID |
| PUT | `/api/emprestimos/{id}` | Atualizar |
| PATCH | `/api/emprestimos/{id}/status` | Atualizar status |
| DELETE | `/api/emprestimos/{id}` | Excluir |

---

## 📤 Como enviar um livro com PDF

A rota de criação de livros usa `multipart/form-data`. No Postman:

1. Selecione **Body → form-data**
2. Adicione o campo `dados` com tipo `Text` e Content-Type `application/json`:
```json
{
  "titulo": "Clean Code",
  "isbn": "978-0132350884",
  "anoPublicacao": 2008,
  "autorId": 1,
  "categoriaId": 1
}
```
3. Adicione o campo `arquivo` com tipo `File` e selecione um `.pdf`

O PDF será armazenado automaticamente no MinIO. Para acessá-lo, use a rota `GET /api/livros/{id}/download`, que retorna um link temporário válido por 1 hora.

---

## ⚠️ Respostas de erro

Todos os erros seguem o mesmo formato:

```json
{
  "status": 404,
  "error": "Not Found",
  "message": "Autor não encontrado: id=99",
  "timestamp": "2024-10-15T14:00:00"
}
```

| Status | Situação |
|---|---|
| 400 | Dados inválidos ou campo obrigatório ausente |
| 404 | Recurso não encontrado |
| 409 | Dado duplicado (CPF, e-mail, ISBN, código) |
| 500 | Erro inesperado no servidor |

---

## 🛑 Encerrando os serviços

Para parar os containers sem apagar os dados:

```bash
docker-compose down
```

Para parar e apagar todos os dados do banco e do MinIO:

```bash
docker-compose down -v
```

---

