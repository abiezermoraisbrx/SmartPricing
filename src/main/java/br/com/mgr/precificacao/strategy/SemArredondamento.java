package br.com.mgr.precificacao.strategy;

public class SemArredondamento implements EstrategiaArredondamento {
    @Override
    public double arredondar(double precoExato) { return precoExato; }

    @Override
    public String getDescricao(double precoExato) {
        return String.format("Manter Preço Exato (R$ %.2f)", precoExato);
    }
}