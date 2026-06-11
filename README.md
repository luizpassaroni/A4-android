# Sistema Padaria Trigo Dourado

Este projeto consiste em um aplicativo nativo para a plataforma Android desenvolvido como requisito parcial para a disciplina de Desenvolvimento Mobile. O software estabelece uma plataforma de panificação 100% digital focada estritamente no modelo de delivery, otimizando o fluxo desde a captação do pedido até a entrega final ao cliente.

## 1. Escopo do Sistema

O aplicativo foi projetado para atender duas frentes de utilização: a interface do cliente (focada em usabilidade, rapidez e conveniência) e a interface administrativa do gerente (focada no controle operacional e de estoque).

### Requisitos Funcionais (RF)

- **RF01 - Visualização de Cardápio:** Apresentação da lista de produtos de panificação disponíveis para compra.
- **RF02 - Carrinho de Compras:** Fluxo dinâmico para adição, remoção e alteração de quantidade de itens com cálculo de totais em tempo real.
- **RF03 - Fechamento de Pedido:** Checkout unificado para seleção de endereço, método de pagamento e consolidação da compra.
- **RF04 - Cadastro e Login de Usuários:** Autenticação e controle de contas com armazenamento persistente de sessão.
- **RF05 - Histórico de Compras:** Consulta e acompanhamento dos status das ordens anteriores realizadas pelo cliente.
- **RF06 - Gestão de Estoque:** Painel exclusivo do administrador para habilitar ou desabilitar a exibição de produtos.

### Regras de Negócio (RN)

- **RN01 - Limitação Geográfica de Entrega:** O atendimento é restrito a bairros selecionados do Rio de Janeiro. Pedidos com CEPs fora da zona de cobertura são bloqueados automaticamente durante o cadastro de endereços.
- **RN02 - Sincronização com Fornadas:** A disponibilidade dos produtos no cardápio do cliente reflete em tempo real a linha de produção física interna gerenciada pelo administrador.

## 2. Arquitetura e Tecnologias

A engenharia do software baseia-se nos padrões recomendados pelo Google para o desenvolvimento nativo, garantindo robustez, alta performance e separação de responsabilidades.

- **Linguagem:** Java Puro (Compatibilidade mínima definida para a API 26 - Android 8.0)
- **Padrão de Arquitetura:** MVVM (Model-View-ViewModel) de forma estrita.
- **Vinculação de Telas:** View Binding ativo em todo o projeto, eliminando o uso de métodos legados como `findViewById`.
- **Persistência de Dados Local:** Biblioteca Room (ORM sob o banco de dados SQLite) para o armazenamento de credenciais e endereços salvos do usuário.
- **Comunicação com APIs Externas:** Retrofit 2 integrado ao conversor Gson para consumo síncrono e assíncrono da API do ViaCEP.
- **Interface de Usuário:** XML estruturado sob as diretrizes do Material Design 3 (M3). Uso de componentes como `BottomSheetDialogFragment` para o checkout fluido, `NestedScrollView` para responsividade e tratamento de `WindowInsets` para adaptação a telas com tecnologia Edge-to-Edge.
- **Gerenciamento de Estado:** Uso de `LiveData` e `MutableLiveData` para manter a interface reativa às mutações dos repositórios centralizados (_Single Source of Truth_).

## 3. Estrutura de Diretórios e Pacotes

O código-fonte está estruturado por responsabilidade arquitetural dentro do pacote principal `com.trigodourado.app`:

```text
com.trigodourado.app/
│
├── data/
│   ├── api/          # Configuração do Retrofit, endpoints e DTOs (ex: ViaCepResponse)
│   ├── local/        # Configuração do Room (AppDatabase e interfaces DAO)
│   ├── model/        # Entidades do domínio (Usuario, Produto, Pedido) e DTOs de UI (CartItemUI)
│   └── repository/   # Repositórios centralizadores de dados (AuthRepository, CartRepository)
│
├── viewmodel/        # ViewModels globais da aplicação
│
└── ui/               # Camada de visualização (Views) estruturada por funcionalidade
    ├── adapter/      # Adaptadores especializados para o RecyclerView (ProdutosAdapter, ItensCarrinhoAdapter)
    ├── admin/        # Fragmentos e atividades exclusivas da visão do gerente
    ├── auth/         # Fluxo de login e cadastro de usuários
    ├── cart/         # Gerenciamento da tela de listagem de carrinho
    ├── checkout/     # Componentes de fechamento de pedido (CheckoutBottomSheet)
    ├── history/      # Listagem do histórico de ordens do cliente
    └── menu/         # Catálogo principal e navegação por abas de categorias
```

## 4. Configuração do Ambiente e Execução

Para compilar e executar o projeto localmente, certifique-se de cumprir os requisitos de ambiente descritos:

## Pré-requisitos

- Android Studio Ladybug (ou versão superior) instalado

- Java JDK 17 configurado no sistema (gerenciado opcionalmente via ferramenta mise)

## Instruções

1. Clone o repositório para o seu diretório local.

2. Abra o Android Studio e selecione a opção "Open" apontando para a pasta raiz do projeto.

3. Aguarde a indexação inicial do Gradle e o download das dependências declaradas no arquivo build.gradle.kts.

4. Configure um dispositivo virtual (AVD) no gerenciador do Android Studio (Recomendado: Perfil baseado no Pixel 10 Pro rodando API API 34/35 com aceleração de hardware ativa via Windows Hypervisor Platform).

5. Clique no botão "Run" (ícone de execução verde) para compilar e instalar o arquivo .apk no emulador.

## 6. Histórico de Versões

> Versão 1.0 (20 de Maio de 2026): Concepção inicial da arquitetura, mapeamento do dicionário de dados em entidades do Room, integração assíncrona do ViaCEP e estabelecimento do fluxo reativo MVVM.
