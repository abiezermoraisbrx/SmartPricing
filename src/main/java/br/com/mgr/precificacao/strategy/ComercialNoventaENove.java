package br.com.mgr.precificacao.strategy;

public class ComercialNoventaENove implements EstrategiaArredondamento {
    @Override
    public double arredondar(double precoExato) {
        double base = Math.floor(precoExato);
        return (base + 0.99 < precoExato) ? Math.ceil(precoExato) + 0.99 : base + 0.99;
    }

    @Override
    public String getDescricao(double precoExato) { return "Padrão Comercial Final ,99"; }
}