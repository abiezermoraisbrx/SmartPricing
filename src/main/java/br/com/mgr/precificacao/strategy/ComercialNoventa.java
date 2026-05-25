package br.com.mgr.precificacao.strategy;

public class ComercialNoventa implements EstrategiaArredondamento {
    @Override
    public double arredondar(double precoExato) {
        double base = Math.floor(precoExato);
        return (base + 0.90 < precoExato) ? Math.ceil(precoExato) + 0.90 : base + 0.90;
    }

    @Override
    public String getDescricao(double precoExato) { return "Padrão Comercial Final ,90"; }
}