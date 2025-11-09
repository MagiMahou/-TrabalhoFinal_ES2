# Trabalho Final - Engenharia de Software II (MVP Microsserviços com IAM/SSO)

Este repositório contém o MVP para o trabalho final da disciplina, demonstrando autenticação e autorização (OAuth2/OIDC) com Keycloak em um ecossistema de microsserviços Spring Boot.

## 1. Pré-requisitos

* Docker
* Docker Compose

## 2. Como Executar

1.  Clone o repositório:
   Pode ser baixado direto pelo link do repositorio do GitHub

2.  Suba todos os containers (incluindo o Keycloak):
    Instale o Docke na sua maquina(Foi usado para os tesets o Docker Desktop)
    O keyclok foi rodado na propria maquina(não foi usado serviços em nuvem)

    ```
    docker run -p 8080:8080 -e KEYCLOAK_ADMIN=admin -e KEYCLOAK_ADMIN_PASSWORD=admin quay.io/keycloak/keycloak:latest start-dev
    ```
    O comando acima irá criar o Keycloak com essas credencias de acesso. (A interface web pode ser acessar pelo link http://localhost:8080 após a criação do keycloak)

    ```bash
    docker-compose up --build
    ```

    O comando acimar irá subir a subir todos os serviçõs do container. É preciso que rode o comando no caminho que está todos os arquivos(....\-TrabalhoFinal_ES2)

    (Aguarde alguns minutos para que todos os serviços iniciem e se registrem no Eureka, é normal demorar para acessar a interface da KeyCloak após subir os serviços).

## 3. Endpoints Principais

* **API Gateway (Ponto de Entrada):** `http://localhost:8765`
    *(Todas as requisições de teste devem ser feitas para esta URL)*

* **Keycloak (Admin Console):** `http://localhost:8080`
* **Eureka (Naming Server):** `http://localhost:8761`

## 4. Como Testar (Jornadas Obrigatórias)

Foi usado o Postman para obter os tokens e testar as rotas no Gateway (`:8765`).

### Credenciais (Keycloak)

* **Realm:** `trabalho-es2-realm`
* **Usuário Comum:** `u_comum_teste`
* **Usuário Admin:** `u_admin`

### Testes

1.  **Jornada 2 (Não Logado):**
    * `GET /convert/from/USD/to/BRL`
    * **(Sem Token)**
    * **Resultado:** `401 Unauthorized`

2.  **Jornada 1 (Usuário Comum):**
    * `GET /convert/from/USD/to/BRL`
    * **(Token: `usuariofinal`)**
    * **Resultado:** `200 OK` (JSON com a conversão)

3.  **Jornada 3 (Admin - Criar Taxa):**
    * `POST /currency-exchange`
    * **(Token: `u_admin`)**
    * **Body (JSON):** `{ "id": 10005, "from": "EUR", "to": "GBP", "conversionMultiple": 0.85 }`
    * **Resultado:** `200 OK` (JSON do objeto criado)

4.  **Jornada 4 (Admin - Alterar Taxa):**
    * `PUT /currency-exchange/10005`
    * **(Token: `u_admin`)**
    * **Body (JSON):** `{ "id": 10005, "from": "EUR", "to": "GBP", "conversionMultiple": 0.90 }`
    * **Resultado:** `200 OK` (JSON com o valor atualizado)