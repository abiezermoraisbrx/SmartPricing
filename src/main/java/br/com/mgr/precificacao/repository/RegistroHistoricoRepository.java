package br.com.mgr.precificacao.repository;

import br.com.mgr.precificacao.model.Precificacao;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class RegistroHistoricoRepository {
    private static final String ARQUIVO_PADRAO = "historico_precificacao.csv";
    private static final String CABECALHO = "PRODUTO;CUSTO DIRETO INICIAL;CUSTOS TOTAIS;PRECO EXATO;PRECO PRATICADO;LUCRO LIQUIDO REAL;MARGEM REAL (%)";

    public void salvar(List<Precificacao> historico) {
        if (historico == null || historico.isEmpty()) return;

        Path destino = Paths.get(ARQUIVO_PADRAO);
        
        // Se o arquivo estiver aberto no Excel, desvia o fluxo criando um novo com timestamp
        if (Files.exists(destino) && isArquivoBloqueado(destino)) {
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            destino = Paths.get("historico_precificacao_" + timestamp + ".csv");
            System.out.printf("\n[SISTEMA OS] Planilha padrão bloqueada pelo Excel. Novo arquivo gerado: '%s'\n", destino.getFileName());
        }

        try {
            boolean precisaCabecalho = !Files.exists(destino);
            List<String> linhas = new ArrayList<>();
            
            if (precisaCabecalho) linhas.add(CABECALHO);

            for (Precificacao p : historico) {
                linhas.add(String.format(Locale.US, "%s;%.2f;%.2f;%.2f;%.2f;%.2f;%.2f",
                        p.getProduto().getNome(), 
                        p.getProduto().getCustoDiretoInicial(), 
                        p.getCustosTotais(),
                        p.getPrecoExato(), 
                        p.getPrecoPraticado(), 
                        p.getLucroLiquidoReal(), 
                        p.getMargemRealPercentual()));
            }

            Files.write(destino, linhas, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            System.out.printf("[I/O INFO] Histórico sincronizado com sucesso no arquivo '%s'.\n", destino.getFileName());
        } catch (IOException e) {
            System.err.println("[ERRO CRÍTICO] Falha de persistência de hardware: " + e.getMessage());
        }
    }

    private boolean isArquivoBloqueado(Path path) {
        try (FileChannel channel = FileChannel.open(path, StandardOpenOption.WRITE);
             FileLock lock = channel.tryLock()) {
            return lock == null;
        } catch (IOException e) {
            return true;
        }
    }
}