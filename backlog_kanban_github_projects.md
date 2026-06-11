# Plano de Desenvolvimento e Organização do Kanban — Padaria Trigo Dourado

Este documento detalha o planejamento estruturado em formato de Backlog (Epics e Tasks) para a evolução do MVP do aplicativo **Padaria Trigo Dourado**. Ele foi desenhado para servir de base direta para a criação de Issues e Cards no GitHub Projects, alinhando a implementação técnica em Java puro (MVVM) com a Especificação de Requisitos (SRS) e os Diagramas UML do sistema.

---

## Visão Geral do Fluxo de Trabalho

Como o repositório ainda não possui um projeto configurado na interface do GitHub, as tarefas abaixo foram descritas com **Critérios de Aceitação (AC)** e **Contexto Técnico**, permitindo que sejam copiadas e coladas individualmente assim que o acesso for concedido pelo administrador do repositório.

---

## Backlog Estruturado: Epics & Tasks

### EPIC 1: Refino do Domínio do Carrinho e UX Fluida

**Objetivo:** Solucionar o isolamento de estado do carrinho entre as Activities e enriquecer a experiência visual do usuário ao adicionar produtos, eliminando bugs de sobreposição de interface.

#### Task 1.1: Refatoração do `CartRepository` para DTO `CartItemUI`

- **Descrição:** Separar a entidade de persistência (`ItemPedido`) da camada de apresentação criando um Objeto de Transferência de Dados (DTO) que carregue o nome e a imagem do produto.
- **Contexto Técnico:**
  - Criar a classe `CartItemUI.java` no pacote `data.model`.
  - Atualizar o `CartRepository` (Singleton) para expor uma `List<CartItemUI>` dentro do `CartState` em vez de `ItemPedido`.
  - O repositório deve interceptar a adição do `Produto` e injetar seu `nome` e `imagem` diretamente no DTO reativo.
- **Critérios de Aceitação:**
  - [ ] O carrinho deve manter o nome do produto preservado na memória viva da aplicação.
  - [ ] A alteração não deve quebrar a geração do objeto `Pedido` final no checkout (o mapeamento reverso deve ser feito na ViewModel).

#### Task 1.2: Otimização do `ItensCarrinhoAdapter` e Layout XML

- **Descrição:** Ajustar o adaptador do RecyclerView do carrinho para consumir o novo DTO e corrigir o bug visual onde os itens aparecem numerados de forma genérica (ex: _Produto #1_).
- **Contexto Técnico:**
  - Alterar a tipagem do `ListAdapter` em `ItensCarrinhoAdapter.java` de `ItemPedido` para `CartItemUI`.
  - No método `bind()`, substituir a concatenação de ID pelo método `item.getNomeProduto()`.
  - No `item_carrinho.xml`, adicionar as propriedades `android:ellipsize="end"` e `android:maxLines="2"` no TextView do nome.
- **Critérios de Aceitação:**
  - [ ] O carrinho deve exibir os nomes reais dos produtos (ex: "Pão Francês", "Croissant").
  - [ ] Nomes muito longos devem ser truncados com reticências após a segunda linha, sem quebrar o alinhamento dos botões de quantidade.

#### Task 1.3: Substituição de Toast por Snackbar Ancorado na `CardapioActivity`

- **Descrição:** Corrigir o bug de concorrência visual onde o aviso flutuante do sistema bloqueia a visualização e o clique no preview da sacola na base do cardápio.
- **Contexto Técnico:**
  - Na `CardapioActivity.java`, remover a chamada ao `Toast.makeText()`.
  - Implementar o `com.google.android.material.snackbar.Snackbar`.
  - Utilizar o método `.setAnchorView(binding.previewSacola)` antes de disparar o `.show()`.
- **Critérios de Aceitação:**
  - [ ] O alerta de confirmação de item adicionado deve flutuar imediatamente _acima_ do card da sacola.
  - [ ] O card da sacola deve permanecer visível e clicável durante a exibição do alerta.

---

### EPIC 2: Dashboard Administrativo (Visão do Gerente — RF06)

**Objetivo:** Implementar a fatia vertical exclusiva do ator Administrador mapeada no Diagrama de Casos de Uso, permitindo o controle de estoque atrelado às fornadas e a gestão de pedidos.

#### Task 2.1: Estrutura Base de Navegação da Dashboard (Tabs + ViewPager2)

- **Descrição:** Construir a casca estrutural da área do gerente utilizando navegação por abas para otimizar o espaço em tela.
- **Contexto Técnico:**
  - Criar a `DashboardActivity.java` no pacote `ui.admin` e seu respectivo `activity_dashboard.xml`.
  - Utilizar os componentes `TabLayout` e `ViewPager2` integrados com um `FragmentStateAdapter`.
  - Configurar duas abas nativas: "Pedidos Ativos" e "Controle de Estoque".
- **Critérios de Aceitação:**
  - [ ] O usuário gerente deve conseguir alternar entre as duas telas administrativas deslizando horizontalmente ou clicando nas abas.
  - [ ] A barra de ações padrão (ActionBar) deve ser ocultada através do tema.

#### Task 2.2: Implementação do Fragment de Pedidos Ativos

- **Descrição:** Construir a interface de monitoramento onde o gerente visualiza os pedidos que caem no sistema em tempo real e altera seus estados de produção.
- **Contexto Técnico:**
  - Criar o `PedidosAdminFragment.java` contendo um RecyclerView.
  - Consumir a lista de pedidos do `PedidoRepository` filtrando por status (`RECEBIDO` ou `EM_PREPARO`).
  - O layout do item deve conter um botão "Avançar Status" que realiza o update da propriedade `StatusPedido` no banco.
- **Critérios de Aceitação:**
  - [ ] Ao clicar em avançar, o status do pedido deve mudar (ex: de `RECEBIDO` para `EM_PREPARO`) e sumir da lista se finalizado.
  - [ ] A alteração deve refletir imediatamente na tela de histórico do cliente correspondente.

#### Task 2.3: Implementação do Fragment de Controle de Estoque (Fornadas — RN02)

- **Descrição:** Atender à regra de negócio **RN02**, fornecendo uma tela para o gerente ativar ou desativar a visibilidade de produtos com base nas fornadas físicas da padaria.
- **Contexto Técnico:**
  - Criar o `EstoqueAdminFragment.java`.
  - Reutilizar o adaptador de listagem de produtos, substituindo o botão de compra por um componente `com.google.android.material.materialswitch.MaterialSwitch`.
  - O estado do Switch deve vir amarrado ao campo `ativo: BOOLEAN` da entidade `Produto`.
- **Critérios de Aceitação:**
  - [ ] Ao desativar o Switch de um produto, o banco de dados deve ser atualizado em thread secundária.
  - [ ] O produto desativado deve sumir imediatamente do cardápio visualizado na conta do cliente.

---

### EPIC 3: Persistência de Sessão e Controle de Acesso (RF04)

**Objetivo:** Garantir a segurança dos dados cadastrais (requisito não funcional de segurança do SRS) e automatizar o login do usuário para evitar digitação contínua.

#### Task 3.1: Camada de Criptografia local com `SessionManager`

- **Descrição:** Desenvolver um motor centralizador de persistência para guardar as credenciais e o nível de permissão do usuário no armazenamento do aparelho de forma segura.
- **Contexto Técnico:**
  - Criar a classe `SessionManager.java` utilizando o padrão Singleton.
  - Instanciar o `EncryptedSharedPreferences` da biblioteca Jetpack Security (`androidx.security:security-crypto`).
  - Criar métodos expostos: `salvarSessao(int idUsuario, String role)`, `getIdUsuario()`, `getRole()` e `encerrarSessao()`.
- **Critérios de Aceitação:**
  - [ ] Os dados armazenados no XML interno do Android devem ser ilegíveis caso o aparelho sofra root.
  - [ ] O sistema deve conseguir recuperar a string de privilégios (`CLIENTE` ou `GERENTE`) a qualquer momento.

#### Task 3.2: Splash Screen Guard (Roteamento Dinâmico)

- **Descrição:** Criar uma tela de abertura que decide o destino do usuário antes de inflar a interface, impedindo que gerentes acessem o fluxo do cliente e vice-versa.
- **Contexto Técnico:**
  - Configurar a `SplashActivity.java` como a rota `MAIN` e `LAUNCHER` principal no `AndroidManifest.xml`.
  - No `onCreate()`, ler os dados do `SessionManager`.
  - Aplicar a árvore de decisão:
    - Sessão nula/vazia ➡️ Redirecionar para `LoginActivity`.
    - Role igual a `"CLIENTE"` ➡️ Redirecionar para `CardapioActivity`.
    - Role igual a `"GERENTE"` ➡️ Redirecionar para `DashboardActivity`.
  - Chamar obrigatoriamente o método `finish()` após disparar o `Intent`.
- **Critérios de Aceitação:**
  - [ ] Se o usuário já estiver logado, o app deve pular a tela de login direto para o seu painel específico.
  - [ ] O usuário não pode conseguir voltar para a animação de Splash ao pressionar o botão físico de voltar do Android.

---

### EPIC 4: Identidade Visual e Enriquecimento de Catálogo (Branding)

**Objetivo:** Fortalecer a presença da marca "Trigo Dourado" estabelecendo uma identidade visual própria e oferecendo uma experiência de vitrine atrativa com imagens reais de produtos.

#### Task 4.1: Criação de Logo e Ícones do Aplicativo

- **Descrição:** Desenvolver e integrar o logotipo oficial da padaria para uso no ícone de lançamento do aplicativo e nas interfaces principais.
- **Contexto Técnico:**
  - Desenhar/obter o logo em formato vetorial (`.svg` convertido para `VectorDrawable` no Android Studio) ou `.webp` de alta resolução.
  - Utilizar a ferramenta _Image Asset Studio_ para gerar o `ic_launcher` adaptativo (foreground e background) para suportar diferentes formatos de ícones do Android.
  - Substituir os títulos em texto puro na barra superior (TopBar) e na Splash Screen pelo logotipo da marca.
- **Critérios de Aceitação:**
  - [ ] O aplicativo deve exibir o logotipo customizado na gaveta de aplicativos do celular, não o ícone padrão do Android.
  - [ ] A tela inicial e as áreas de destaque devem adotar as cores e a identidade do logo.

#### Task 4.2: Expansão do Catálogo e Integração de Imagens de Produtos

- **Descrição:** Aumentar a variedade de produtos disponíveis na base de dados e implementar o carregamento dinâmico de imagens/ícones ilustrativos para cada item.
- **Contexto Técnico:**
  - Atualizar a base de dados (`AppDatabase`) ou o mock no `CardapioViewModel` adicionando novos produtos (ex: bebidas, bolos, doces finos).
  - Adicionar a dependência da biblioteca **Glide** ou **Picasso** no `build.gradle` (caso as imagens venham de URLs externas) ou mapear os arquivos para a pasta `/res/drawable/`.
  - Refatorar o `item_produto.xml` e o `item_carrinho.xml` para incluir um componente `ImageView` e atualizar os Adapters correspondentes para renderizar a imagem de acordo com a string/URL salva no banco.
- **Critérios de Aceitação:**
  - [ ] O cardápio deve possuir uma lista expandida e categorizada de produtos.
  - [ ] Cada produto no cardápio e no carrinho deve exibir sua imagem correspondente perfeitamente enquadrada, sem distorção visual.

---

## Padrões de Engenharia de Versionamento (Git)

Para garantir que o histórico do repositório conte a evolução lógica da arquitetura e facilite revisões de código futuras, as Tasks devem ser integradas utilizando commits granulares baseados na especificação do **Conventional Commits**:

| Prefixo do Commit | Finalidade Aplicada ao Projeto                                       | Exemplo Prático                                                         |
| :---------------- | :------------------------------------------------------------------- | :---------------------------------------------------------------------- |
| `feat:`           | Adição de uma nova funcionalidade ou componente                      | `feat: implementa DTO CartItemUI para carregar nomes reais no carrinho` |
| `fix:`            | Correção de um bug visual, de layout ou lógica                       | `fix: ancora snackbar acima da sacola para evitar sobreposição`         |
| `chore:`          | Atualização de dependências, build gradle ou tarefas administrativas | `chore: adiciona biblioteca jetpack security crypto no build.gradle`    |
| `docs:`           | Alterações exclusivas na documentação ou comentários                 | `docs: atualiza readme com instrucoes de execucao do banco local Room`  |
| `style:`          | Ajustes visuais, de formatação ou assets gráficos                    | `style: adiciona logo oficial e icones vetorizados de produtos`         |
