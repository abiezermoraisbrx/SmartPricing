package br.com.mgr.precificacao.view;

import br.com.mgr.precificacao.model.Precificacao;
import br.com.mgr.precificacao.strategy.*;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class ConsoleView {
    private final Scanner scanner = new Scanner(System.in).useLocale(Locale.US);
    private final List<EstrategiaArredondamento> estrategias = List.of(
            new SemArredondamento(), new InteiroSuperior(), new ComercialNoventa(), new ComercialNoventaENove()
    );

    public void limparScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public String inputTexto(String msg) {
        System.out.print(msg);
        return scanner.nextLine().trim();
    }

    public double inputDouble(String msg) {
        while (true) {
            System.out.print(msg);
            try {
                double valor = Double.parseDouble(scanner.nextLine().trim().replace(",", "."));
                if (valor >= 0) return valor;
                System.out.println("[Erro] Informe um valor numérico positivo.");
            } catch (NumberFormatException e) {
                System.out.println("[Erro] Entrada inválida. Digite apenas algarismos.");
            }
        }
    }

    public EstrategiaArredondamento selecionarEstrategia(double precoExato) {
        System.out.println("\n--- Configuração de Arredondamento de Gôndola ---");
        for (int i = 0; i < estrategias.size(); i++) {
            System.out.printf("%d - %s\n", i + 1, estrategias.get(i).getDescricao(precoExato));
        }
        
        while (true) {
    System.out.printf("Escolha o método (1-%d): ", estrategias.size());
    try {
        int escolha = Integer.parseInt(scanner.nextLine().trim()) - 1;
        if (escolha >= 0 && escolha < estrategias.size()) return estrategias.get(escolha); // Corrigido aqui
        System.out.println("[Erro] Código de arredondamento inexistente.");
    } catch (NumberFormatException e) {
        System.out.println("[Erro] Digite um número inteiro correspondente.");
        }
        }
    }

    public boolean confirmar(String msg) {
        System.out.print(msg);
        String res = scanner.nextLine().trim().toUpperCase();
        return !res.isEmpty() && res.startsWith("S");
    }

    public void renderizarResultado(Precificacao p) {
        System.out.println("\n=========================================================");
        System.out.printf("  PANIFICADORA / INDÚSTRIA - RELATÓRIO DO ITEM: %s\n", p.getProduto().getNome());
        System.out.println("=========================================================");
        System.out.printf("  (+) Custo Bruto Matéria-Prima: R$ %7.2f\n", p.getProduto().getValorCompra());
        System.out.printf("  (+) Rateio de Custos Fixos:    R$ %7.2f\n", p.getProduto().getCustoFixoUnitario());
        System.out.printf("  (+) Insumo de Embalagem:       R$ %7.2f\n", p.getProduto().getCustoEmbalagem());
        System.out.printf("  (+) Provisão Simples Nacional: R$ %7.2f\n", p.getSimplesCalculado());
        System.out.printf("  (+) Despesa de Comissões:      R$ %7.2f\n", p.getComissaoCalculada());
        System.out.printf("  (+) Taxa de Cartão / Adquirente: R$ %7.2f\n", p.getCartaoCalculado());
        System.out.println("  ---------------------------------------------------------");
        System.out.printf("  (=) LUCRO LÍQUIDO NO CAIXA (R$): R$ %7.2f\n", p.getLucroLiquidoReal());
        System.out.printf("  (=) MARGEM LÍQUIDA REAL EM GÔNDOLA:   %7.2f%%\n", p.getMargemRealPercentual());
        System.out.println("  ---------------------------------------------------------");
        System.out.printf("  PREÇO DE VENDA PRATICADO (PDV):R$ %7.2f\n", p.getPrecoPraticado());
        System.out.println("=========================================================");
    }

    public void renderizarSimuladorMeta(Precificacao p) {
        System.out.println("\n>>> PROJEÇÃO DE CUSTO-VOLUME-LUCRO <<<");
        double alvoLucro = inputDouble("Defina a meta de Lucro Líquido total para este item (R$): ");
        if (alvoLucro > 0) {
            int qteNecessaria = (int) Math.ceil(alvoLucro / p.getLucroLiquidoReal());
            System.out.println("---------------------------------------------------------");
            System.out.printf("  Para reter R$ %.2f de margem líquida consolidada:\n", alvoLucro);
            System.out.printf("  Giro mínimo de estoque: %d unidades de %s\n", qteNecessaria, p.getProduto().getNome());
            System.out.printf("  Faturamento Alvo no PDV: R$ %.2f\n", qteNecessaria * p.getPrecoPraticado());
            System.out.println("---------------------------------------------------------");
        }
    }

    public void renderizarGridSessao(List<Precificacao> historico) {
        if (historico.isEmpty()) return;
        System.out.println("\nÚLTIMOS PRODUTOS PRECIFICADOS NA SESSÃO ATUAL:");
        System.out.printf("%-18s | %-12s | %-12s | %-12s\n", "PRODUTO", "PREÇO EXATO", "PREÇO PRAT.", "MARGEM REAL");
        System.out.println("-----------------------------------------------------------------------");
        int corte = Math.max(0, historico.size() - 3);
        for (Precificacao pr : historico.subList(corte, historico.size())) {
            String nomeCortado = pr.getProduto().getNome();
            if (nomeCortado.length() > 18) nomeCortado = nomeCortado.substring(0, 15) + "...";
            System.out.printf("%-18s | R$ %-9.2f | R$ %-9.2f | %-10.1f%%\n",
                    nomeCortado, pr.getPrecoExato(), pr.getPrecoPraticado(), pr.getMargemRealPercentual());
        }
    }
}