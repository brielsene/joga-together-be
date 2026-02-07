# my-java-app: Aplicação Java Exemplo com CI/CD GitOps

Este repositório contém uma aplicação Java de exemplo configurada com um pipeline de CI/CD robusto usando GitHub Actions, Docker e um fluxo GitOps para implantação automatizada no Kubernetes via Argo CD.

---

## 🎯 Objetivo do Projeto

O objetivo principal desta aplicação de exemplo é demonstrar um fluxo completo de CI/CD:

1.  **Construir e Testar:** O código Java é testado e empacotado.
2.  **Containerizar:** A aplicação empacotada é transformada em uma imagem Docker.
3.  **Publicar Imagem:** A imagem Docker é enviada para o Docker Hub.
4.  **Atualização GitOps:** Automaticamente, um Pull Request é aberto em um repositório GitOps separado (`platform-gitops`) para atualizar a versão da imagem na configuração de deploy do Kubernetes.
5.  **Implantação Contínua (CD):** O Argo CD, monitorando o repositório GitOps, detecta a mudança e implanta a nova versão no cluster Kubernetes.

---

## 🚀 Estrutura do Projeto

*   `.github/workflows/ci.yml`: Define o pipeline de CI/CD usando GitHub Actions.
*   `pom.xml`: O arquivo de configuração do Maven para o projeto Java.
*   `Dockerfile`: As instruções para construir a imagem Docker da aplicação.
*   `src/`: Contém o código-fonte Java da aplicação de exemplo.

---

## 🛠️ Como Funciona o Pipeline de CI/CD

O pipeline é acionado por pushes e tags Git, e tem uma lógica condicional para controlar o que é construído e implantado.

### 🗺️ Fluxograma da Arquitetura CI/CD (Mermaid.js)

```mermaid
graph TD
    A[Início: Push Git] --> B{Tipo de Push?};

    B -- Push na 'main' --> C{Build & Test Job};
    B -- Push de Tag (vX.Y.Z) --> C;
    B -- Pull Request (main) --> C;

    C --> D{Build & Test Job Concluído com Sucesso?};

    D -- Sim --> E{Push é uma Tag Git (vX.Y.Z)?};
    D -- Não --> F[Fim: Apenas Build & Teste concluído];

    E -- Sim --> G[Build & Push Docker Job];
    E -- Não --> F;

    G --> H[Docker Hub];
    G --> I[Update GitOps Repo Job];

    I --> J[platform-gitops Repo (PR Aberto)];
    J --> K[Argo CD detecta PR e Sincroniza];
    K --> L[Kubernetes Cluster (Deploy)];

    L --> M[Fim: Nova Versão Implantada];
```

### 🧠 Detalhes do Workflow (`.github/workflows/ci.yml`)

O arquivo `ci.yml` define três jobs principais que orquestram o pipeline:

1.  **`build_and_test` (Construção e Teste da Aplicação)**
    *   **Quando executa:** Em qualquer push para a branch `main`, em pushes de tags (`v*.*.*`) e em Pull Requests direcionados à `main`.
    *   **O que faz:**
        *   Faz o `checkout` do código.
        *   Configura o Java (JDK 11).
        *   Executa `mvn test` para rodar os testes unitários.
        *   Executa `mvn package` para compilar o código e empacotá-lo em um arquivo `.jar`.
    *   **Integração:** Usa o `pom.xml` para saber como compilar e testar a aplicação.

2.  **`build_and_push_docker` (Construção e Envio da Imagem Docker)**
    *   **Quando executa:** **APENAS** se o job `build_and_test` for bem-sucedido **E** se o push for de uma **tag Git** (`v*.*.*`).
    *   **O que faz:**
        *   Faz o `checkout` do código.
        *   Configura ferramentas Docker (`QEMU` e `Buildx`).
        *   Faz login no Docker Hub usando secrets do GitHub (`DOCKERHUB_USERNAME`, `DOCKERHUB_TOKEN`).
        *   Constrói a imagem Docker a partir do `Dockerfile`.
        *   Envia a imagem para o Docker Hub com a tag que foi puxada do Git (ex: `ojasonw/my-java-app:v1.0.0`).
    *   **Integração:** Usa o `Dockerfile` para as instruções de build e os `secrets` do GitHub para autenticação com o Docker Hub.

3.  **`update_gitops_repo` (Atualiza o Repositório GitOps)**
    *   **Quando executa:** **APENAS** se o job `build_and_push_docker` for bem-sucedido **E** se o push for de uma **tag Git** (`v*.*.*`).
    *   **O que faz:**
        *   Faz o `checkout` do repositório `platform-gitops` (não o repositório atual!) usando um Personal Access Token (PAT) do GitHub (`GITOPS_PAT`).
        *   Usa `sed` para atualizar o arquivo `apps/my-service/overlays/dev/kustomization.yaml` no repositório `platform-gitops`, alterando a `newTag` para a tag da imagem que acabou de ser enviada (ex: `v1.0.0`).
        *   Cria uma nova branch, faz um commit com a alteração e abre um Pull Request no repositório `platform-gitops`.
    *   **Integração:** Ação `peter-evans/create-pull-request` para criar o PR. Requer o `GITOPS_PAT` com permissão de `repo`.

---

## 🔑 Configuração de Secrets (IMPORTE!)

Para que o pipeline funcione, você precisa configurar os seguintes secrets no seu repositório `my-java-app` no GitHub (`Settings > Secrets and variables > Actions`):

*   **`DOCKERHUB_USERNAME`**: Seu nome de usuário do Docker Hub.
*   **`DOCKERHUB_TOKEN`**: Um token de acesso gerado no Docker Hub (não sua senha!).
*   **`GITOPS_PAT`**: Um Personal Access Token (PAT) do GitHub, gerado na sua conta `ojasonw`. Este PAT precisa ter o escopo `repo` para permitir que a Action leia e escreva em outros repositórios seus (no caso, o `platform-gitops`).

---

## 🏷️ Como Disparar um Release (com Tags Manuais)

Para disparar o fluxo completo de CI/CD que constrói e implanta uma nova versão, siga estes passos:

1.  **Faça suas alterações** no código, commit e push para a branch `main`. (Isso só rodará o `build_and_test`).
2.  Quando uma versão estiver pronta para ser lançada, **crie uma tag Git** localmente:
    ```bash
    git tag v1.0.0  # Use um nome de tag seguindo o Semantic Versioning (vMAJOR.MINOR.PATCH)
    ```
3.  **Envie a tag para o GitHub:**
    ```bash
    git push origin v1.0.0
    ```
    (Ou `git push origin --tags` para enviar todas as tags locais para o remoto).

Ao enviar a tag, a GitHub Action será acionada, construirá a imagem, a enviará para o Docker Hub e criará um Pull Request no seu repositório `platform-gitops` com a nova versão.

---

## 📦 Implantação com Argo CD

Uma vez que o Pull Request no `platform-gitops` for aprovado e mesclado na branch `main`:

1.  O **Argo CD** (que está monitorando o `platform-gitops`) detectará a nova versão da imagem.
2.  Ele iniciará automaticamente o processo de sincronização e implantação da nova versão no seu cluster Kubernetes.

---

## 🔮 Próximos Passos e Melhorias

*   **oldTag para Rollback:** Para ter um mecanismo de rollback mais rápido, podemos implementar a lógica de manter uma `oldTag` no `kustomization.yaml` (isso exigiria manipulação de YAML mais avançada na Action).
*   **Automação de Tags:** Explorar ferramentas como Semantic Release para gerar tags Git automaticamente com base em convenções de commit.
*   **Multi-ambiente:** Estender o fluxo para promover imagens para ambientes de `prod` após testes em `dev`.
*   **Testes de Integração/E2E:** Adicionar mais estágios de teste ao pipeline.

---