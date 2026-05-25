# MGR SmartPricing - Módulo Industrial

O **MGR SmartPricing** é uma aplicação desktop corporativa desenvolvida em Java para automação e análise de precificação de produtos industriais e de panificação. Utilizando o método **Markup**, o sistema calcula o preço de venda ideal com base em custos diretos, indiretos e taxas operacionais, oferecendo uma interface visual moderna e relatórios gráficos em tempo real para tomada de decisão estratégica.

---

Funcionalidades Principais

* **Motor de Cálculo Markup:** Blindado contra margens ilusórias, calculando o preço correto sobre o faturamento real (Preço de Venda = Custo / [1 - Taxas - Margem]).
* **Interface Gráfica de Alta Performance (JavaFX):** Layout responsivo, limpo e estruturado em Grid para preenchimento rápido.
* **Ergonomia Operacional:** Navegação inteligente entre campos de texto utilizando a tecla `Enter`, otimizando a digitação em massa (padrão PDV).
* **Gráficos de Pizza Interativos (BI):** Renderização dinâmica da composição do preço em tempo real, dividindo os valores entre Custos Diretos, Custos Fixos, Taxas/Impostos e o Lucro Líquido Real.
* **Estratégias de Gôndola (Design Pattern Strategy):** Módulo de arredondamento comercial dinâmico (Sem arredondamento, Próximo Inteiro, Final ,90 e Final ,99).
* **Persistência Automatizada de Sessão:** Ao fechar o aplicativo pelo botão de saída, o sistema filtra e salva automaticamente os últimos 5 cálculos realizados em um arquivo `ultimos_calculos.txt`.
* **Validação de Regras de Negócio:** Tratamento de exceções customizadas (`BusinessException`) para evitar quebras por dados corrompidos ou margens comerciais impossíveis (acima de 100%).

---

Arquitetura de Software

O projeto foi construído seguindo os padrões da **Programação Orientada a Objetos (POO)** e do modelo **MVC (Model-View-Controller)**, garantindo desacoplamento total entre o motor de cálculo e a interface gráfica.

```text
src/br/com/mgr/precificacao/
├── exception/    # Exceções customizadas e regras de validação (BusinessException)
├── model/        # Entidades puras do negócio (Produto, TaxasOperacionais, Precificacao)
├── service/      # Core business e motor de cálculo (CalculadoraPreco)
├── strategy/     # Algoritmos independentes de arredondamento (EstrategiaArredondamento)
├── repository/   # Camada de persistência em arquivos físicos (HistoricoTxtRepository)
├── controller/   # Intermediador de eventos da tela e do negócio (PrecificacaoController)
└── view/         # Arquivos de layout visual FXML (MainView.fxml)


Tecnologias Utilizadas
Java 17 (ou superior)

JavaFX 17.0.2 (Componentes Visuais e Gráficos Nativos)

Apache Maven (Gerenciador de Dependências e Build)

Pré-requisitos e Execução
No Ambiente de Desenvolvimento (NetBeans / IntelliJ / Eclipse):
Certifique-se de ter o JDK 17 instalado e configurado no seu sistema.

Certifique-se de possuir o plugin do Maven ativo na IDE.

Abra o projeto e execute um Clean and Build para sincronizar as dependências do JavaFX especificadas no pom.xml.

Para rodar a aplicação de forma segura desviando dos bloqueios de runtime do Java modular, execute o arquivo:
br.com.mgr.precificacao.Launcher.java

Exemplo de Relatório Gerado (ultimos_calculos.txt)
Toda vez que a aplicação é encerrada, um arquivo estruturado é salvo na raiz do projeto com o seguinte formato:

=========================================================
        MGR SMARTPRICING - ÚLTIMOS 5 CÁLCULOS DA SESSÃO
        Gerado em: 25/05/2026 09:56:00
=========================================================

1. PRODUTO: Pão Francês KG
   [-] Custo Direto (MP + Fixo + Emb): R$ 4,50
   [+] Preço de Venda Sugerido (PDV): R$ 12,90
   [=] Lucro Líquido Real por Unidade: R$ 3,87
   [=] Margem Real Obtida no Caixa:   30,00%
---------------------------------------------------------

Desenvolvedor
MGR Corporativo - Design de Arquitetura e Engenharia de Negócios
ABIEZER ZIONE DE ALMEIDA MORAIS
