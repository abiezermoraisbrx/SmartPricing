package br.com.mgr.precificacao.repository;

import br.com.mgr.precificacao.model.Precificacao;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class HistoricoTxtRepository {

    private static final String NOME_ARQUIVO = "ultimos_calculos.txt";

    public void salvarUltimosCinco(List<Precificacao> historicoCompleto) {
        if (historicoCompleto == null || historicoCompleto.isEmpty()) {
            return;
        }

        // Descobre o ponto de corte para pegar no máximo os últimos 5 elementos
        int tamanho = historicoCompleto.size();
        int indiceInicial = Math.max(0, tamanho - 5);
        List<Precificacao> ultimosCinco = historicoCompleto.subList(indiceInicial, tamanho);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(NOME_ARQUIVO, false))) { // false substitui o arquivo antigo
            
            writer.write("=========================================================\n");
            writer.write("        MGR SMARTPRICING - ÚLTIMOS 5 CÁLCULOS DA SESSÃO\n");
            writer.write("        Gerado em: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")) + "\n");
            writer.write("=========================================================\n\n");

            for (int i = 0; i < ultimosCinco.size(); i++) {
                Precificacao p = ultimosCinco.get(i);
                writer.write(String.format("%d. PRODUTO: %s\n", (i + 1), p.getProduto().getNome()));
                writer.write(String.format("   [-] Custo Direto (MP + Fixo + Emb): R$ %.2f\n", p.getProduto().getCustoDiretoInicial()));
                writer.write(String.format("   [+] Preço de Venda Sugerido (PDV): R$ %.2f\n", p.getPrecoPraticado()));
                writer.write(String.format("   [=] Lucro Líquido Real por Unidade: R$ %.2f\n", p.getLucroLiquidoReal()));
                writer.write(String.format("   [=] Margem Real Obtida no Caixa:   %.2f%%\n", p.getMargemRealPercentual()));
                writer.write("---------------------------------------------------------\n");
            }
            
            System.out.println("[SISTEMA] Arquivo 'ultimos_calculos.txt' atualizado com sucesso!");

        } catch (IOException e) {
            System.err.println("[ERRO CRÍTICO] Não foi possível salvar o histórico em TXT: " + e.getMessage());
        }
    }
}