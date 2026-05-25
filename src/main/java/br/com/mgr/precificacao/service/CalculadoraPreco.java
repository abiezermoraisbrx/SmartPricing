package br.com.mgr.precificacao.service;

import br.com.mgr.precificacao.exception.BusinessException;
import br.com.mgr.precificacao.model.Precificacao;
import br.com.mgr.precificacao.model.Produto;
import br.com.mgr.precificacao.model.TaxasOperacionais;
import br.com.mgr.precificacao.strategy.EstrategiaArredondamento;

public class CalculadoraPreco {
    
    public Precificacao calcular(Produto produto, TaxasOperacionais taxas, double margemDesejada, EstrategiaArredondamento estrategia) {
        double somatorioPercentuais = taxas.getSomatorio() + margemDesejada;
        
        if (somatorioPercentuais >= 100) {
            throw new BusinessException("A soma das taxas tributárias, comerciais e margem não pode atingir ou ultrapassar 100%.");
        }

        double precoExato = produto.getCustoDiretoInicial() / (1 - (somatorioPercentuais / 100));
        double precoPraticado = estrategia.arredondar(precoExato);

        return new Precificacao(produto, taxas, margemDesejada, precoExato, precoPraticado);
    }
}