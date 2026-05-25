package br.com.mgr.precificacao.strategy;

public interface EstrategiaArredondamento {
    double arredondar(double precoExato);
    String getDescricao(double precoExato);
}