package br.com.mgr.precificacao.model;

public class TaxasOperacionais {
    private final double simplesNacional;
    private final double comissaoVenda;
    private final double tarifaCartao;

    public TaxasOperacionais(double simplesNacional, double comissaoVenda, double tarifaCartao) {
        this.simplesNacional = simplesNacional;
        this.comissaoVenda = comissaoVenda;
        this.tarifaCartao = tarifaCartao;
    }

    public double getSomatorio() {
        return simplesNacional + comissaoVenda + tarifaCartao;
    }

    public double getSimplesNacional() { return simplesNacional; }
    public double getComissaoVenda() { return comissaoVenda; }
    public double getTarifaCartao() { return tarifaCartao; }
}