# Trabalho Final - Engenharia de Software II (MVP Microsserviços com IAM/SSO)

Este repositório contém o MVP (Produto Mínimo Viável) para o trabalho final da disciplina de Engenharia de Software II.

O objetivo é demonstrar autenticação e autorização (OAuth2/OIDC) de ponta-a-ponta para um ecossistema de microsserviços Spring Boot, usando um provedor de identidade (IAM).

## Arquitetura

Este projeto utiliza uma arquitetura híbrida:

* **Microsserviços (Locais):** Os 5 microsserviços (API Gateway, Naming Server, Conversion, Exchange, History) correm localmente via Docker.
* **Provedor de Identidade (Nuvem):** O Keycloak (IAM) é hospedado na nuvem (Cloud-IAM)

## Pré-requisitos

Para executar este projeto, precisas ter instalados:
* Docker
* Docker Compose

## Como Executar (Passos Obrigatórios)

**A partir da pasta raiz do projeto:**

### 1. Compilar os Microsserviços

Para exceutar os tudo de uma vez, rode o comando abaixo no terminal

```bash
docker-compose up --build
```
### 2.Como Testar (As 4 Jornadas)
Todos os testes devem ser feitos através do API Gateway (http://localhost:8765).

# 1. Obter Tokens de Acesso:

Primeiro, obtém os tokens de acesso (JWT) para os utilizadores usuariofinal e u_admin no Postman.

Access Token URL: https://lemur-17.cloud-iam.com/auth/realms/trabalho-es2-realm/protocol/openid-connect/token

# 2. Jornada 2: Usuário Não Logado (Falha):
Ação: GET
URL: http://localhost:8765/convert/from/USD/to/BRL/quantity/10
Token: (Nenhum)
Resultado Esperado: 401 UNAUTHORIZED.

# 3. Jornada 1: Usuário Comum (Sucesso):
Ação: GET
URL: http://localhost:8765/convert/from/USD/to/BRL/quantity/10
Token: Bearer Token do usuario_comum

Resultado Esperado: 200 OK com o JSON da conversão.

# 4. Teste de Segurança (Falha de Permissão)
Ação: POST
URL: http://localhost:8765/currency-exchange
Token: Bearer Token do usuariofinal
Body (JSON): { "id": 10009, "from": "TEST", "to": "FAIL", "conversionMultiple": 1.0 }
Resultado Esperado: 403 FORBIDDEN (Acesso negado para usuario_comum).

# 5. Jornada 3: Admin (Criar Taxa)
Ação: POST
URL: http://localhost:8765/currency-exchange
Token: Bearer Token do u_admin
Body (JSON): { "id": 10005, "from": "EUR", "to": "GBP", "conversionMultiple": 0.85 }

Resultado Esperado: 200 OK (ou 201 Created) com o JSON da nova taxa.

# 6. Jornada 4: Admin (Alterar Taxa)
Ação: PUT
URL: http://localhost:8765/currency-exchange/10005 (usando o ID da taxa criada acima)
Token: Bearer Token do u_admin
Body (JSON): { "id": 10005, "from": "EUR", "to": "GBP", "conversionMultiple": 0.90 }

Resultado Esperado: 200 OK com o JSON da taxa atualizada.