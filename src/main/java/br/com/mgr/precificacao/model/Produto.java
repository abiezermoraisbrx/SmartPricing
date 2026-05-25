package br.com.mgr.precificacao.model;

import br.com.mgr.precificacao.exception.BusinessException;

public class Produto {
    private final String nome;
    private final double valorCompra;
    private final double custoFixoUnitario;
    private final double custoEmbalagem;

    public Produto(String nome, double valorCompra, double custoFixoUnitario, double custoEmbalagem) {
        if (nome == null || nome.isBlank()) {
            throw new BusinessException("O nome do produto é de preenchimento obrigatório.");
        }
        this.nome = nome.trim().toUpperCase();
        this.valorCompra = valorCompra;
        this.custoFixoUnitario = custoFixoUnitario;
        this.custoEmbalagem = custoEmbalagem;
    }

    public double getCustoDiretoInicial() {
        return valorCompra + custoFixoUnitario + custoEmbalagem;
    }

    public String getNome() { return nome; }
    public double getValorCompra() { return valorCompra; }
    public double getCustoFixoUnitario() { return custoFixoUnitario; }
    public double getCustoEmbalagem() { return custoEmbalagem; }
}