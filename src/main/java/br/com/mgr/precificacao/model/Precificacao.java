package br.com.mgr.precificacao.model;

public class Precificacao {
    private final Produto produto;
    private final TaxasOperacionais taxas;
    private final double margemDesejada;
    private final double precoExato;
    private final double precoPraticado;

    public Precificacao(Produto produto, TaxasOperacionais taxas, double margemDesejada, double precoExato, double precoPraticado) {
        this.produto = produto;
        this.taxas = taxas;
        this.margemDesejada = margemDesejada;
        this.precoExato = precoExato;
        this.precoPraticado = precoPraticado;
    }

    public double getSimplesCalculado() { return precoPraticado * (taxas.getSimplesNacional() / 100); }
    public double getComissaoCalculada() { return precoPraticado * (taxas.getComissaoVenda() / 100); }
    public double getCartaoCalculado() { return precoPraticado * (taxas.getTarifaCartao() / 100); }
    
    public double getCustosTotais() {
        return produto.getCustoDiretoInicial() + getSimplesCalculado() + getComissaoCalculada() + getCartaoCalculado();
    }

    public double getLucroLiquidoReal() {
        return precoPraticado - getCustosTotais();
    }

    public double getMargemRealPercentual() {
        return precoPraticado > 0 ? (getLucroLiquidoReal() / precoPraticado) * 100 : 0;
    }

    public Produto getProduto() { return produto; }
    public TaxasOperacionais getTaxas() { return taxas; }
    public double getMargemDesejada() { return margemDesejada; }
    public double getPrecoExato() { return precoExato; }
    public double getPrecoPraticado() { return precoPraticado; }
}