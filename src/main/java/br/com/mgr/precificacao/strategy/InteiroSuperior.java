package br.com.mgr.precificacao.strategy;

public class InteiroSuperior implements EstrategiaArredondamento {
    @Override
    public double arredondar(double precoExato) { return Math.ceil(precoExato); }

    @Override
    public String getDescricao(double precoExato) {
        return String.format("Próximo Inteiro Superior (R$ %.2f)", arredondar(precoExato));
    }
}