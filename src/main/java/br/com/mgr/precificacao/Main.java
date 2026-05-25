package br.com.mgr.precificacao;

import br.com.mgr.precificacao.exception.BusinessException;
import br.com.mgr.precificacao.model.*;
import br.com.mgr.precificacao.repository.RegistroHistoricoRepository;
import br.com.mgr.precificacao.service.CalculadoraPreco;
import br.com.mgr.precificacao.strategy.EstrategiaArredondamento;
import br.com.mgr.precificacao.view.ConsoleView;
import java.util.ArrayList;
import java.util.List;

/**
 * Ponto de entrada (Entrypoint) da aplicação.
 * Orquestra a injeção de dependências e controla o loop global do sistema.
 */
public class Main {
    public static void main(String[] args) {
        ConsoleView view = new ConsoleView();
        CalculadoraPreco calculadora = new CalculadoraPreco();
        RegistroHistoricoRepository repository = new RegistroHistoricoRepository();
        List<Precificacao> fluxoCalculosSessao = new ArrayList<>();

        do {
            try {
                view.limparScreen();
                System.out.println("=== CORPORATIVO MGR - GERENCIADOR DE MARGENS INDUSTRIAIS ===");
                
                Produto produto = new Produto(
                        view.inputTexto("\nIdentificação/Nome do Item: "),
                        view.inputDouble("Preço de Compra/Matéria Prima (R$): "),
                        view.inputDouble("Custo Fixo rateado p/ unidade (R$): "),
                        view.inputDouble("Custo direto de Embalagem (R$): ")
                );

                TaxasOperacionais taxas = new TaxasOperacionais(
                        view.inputDouble("Imposto Simples Nacional (%): "),
                        view.inputDouble("Comissão sobre Venda (%): "),
                        view.inputDouble("Taxa de Operação de Cartão (%): ")
                );

                boolean recalcularMesmoProduto;
                do {
                    recalcularMesmoProduto = false;
                    double margemDesejada = view.inputDouble("Margem de Lucro Alvo pretendida (%): ");
                    
                    // Cálculo de Markup Divisor para amostragem prévia de preços na View
                    double somatorioPreliminar = taxas.getSomatorio() + margemDesejada;
                    if (somatorioPreliminar >= 100) {
                        System.out.println("\n[ERRO DE ALGO] Margem e impostos extrapolam o limite do Markup. Reajuste a margem.");
                        recalcularMesmoProduto = true;
                        continue;
                    }
                    
                    double precoExatoPreliminar = produto.getCustoDiretoInicial() / (1 - (somatorioPreliminar / 100));
                    EstrategiaArredondamento est = view.selecionarEstrategia(precoExatoPreliminar);

                    // Execução centralizada via Regra de Negócio (Service)
                    Precificacao precificacao = calculadora.calcular(produto, taxas, margemDesejada, est);
                    fluxoCalculosSessao.add(precificacao);

                    view.renderizarResultado(precificacao);

                    if (view.confirmar("\nExecutar simulador de faturamento por volume de metas? (S/N): ")) {
                        view.renderizarSimuladorMeta(precificacao);
                    }

                    view.renderizarGridSessao(fluxoCalculosSessao);

                    if (view.confirmar("\nDeseja simular outra margem de lucro para este mesmo item? (S/N): ")) {
                        recalcularMesmoProduto = true;
                    }
                } while (recalcularMesmoProduto);

            } catch (BusinessException e) {
                System.out.printf("\n[VIOLAÇÃO DE REGRA] Operação abortada: %s\n", e.getMessage());
                if (!view.confirmar("Deseja reiniciar a operação? (S/N): ")) break;
            }

        } while (view.confirmar("\nIniciar a precificação de um produto totalmente novo? (S/N): "));

        // LINHA CORRIGIDA: Garanta que o nome bate com a lista declarada no início
        repository.salvar(fluxoCalculosSessao);
        
        System.out.println("\n[SISTEMA] Processo finalizado com integridade de dados assegurada.");
    }
}