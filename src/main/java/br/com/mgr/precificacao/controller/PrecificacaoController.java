package br.com.mgr.precificacao.controller;

import br.com.mgr.precificacao.exception.BusinessException;
import br.com.mgr.precificacao.model.*;
import br.com.mgr.precificacao.service.CalculadoraPreco;
import br.com.mgr.precificacao.strategy.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class PrecificacaoController {

    @FXML private TextField txtNome;
    @FXML private TextField txtCustoCompra;
    @FXML private TextField txtCustoFixo;
    @FXML private TextField txtEmbalagem;
    @FXML private TextField txtSimples;
    @FXML private TextField txtComissao;
    @FXML private TextField txtCartao;
    @FXML private TextField txtMargem;
    @FXML private ComboBox<String> comboEstrategia;
    @FXML private Label lblPrecoPraticado;
    @FXML private Label lblLucroReal;
    @FXML private Label lblMargemReal;

    private final CalculadoraPreco calculadora = new CalculadoraPreco();

    // Esta linha avisa ao compilador do NetBeans para ignorar o alerta de que a variável "não é lida" aqui dentro
    @SuppressWarnings("FieldCanBeLocal")
    private final java.util.List<Precificacao> historicoSessao = new java.util.ArrayList<>();
    
    @FXML private javafx.scene.chart.PieChart graficoPizza;
        
    @FXML
    public void initialize() {
        // Popula as opções do ComboBox com as estratégias comerciais
        comboEstrategia.setItems(FXCollections.observableArrayList(
                "Sem Arredondamento",
                "Próximo Inteiro Superior",
                "Padrão Comercial Final ,90",
                "Padrão Comercial Final ,99"
        ));
        comboEstrategia.getSelectionModel().selectFirst();
    }

    @FXML
    @SuppressWarnings("unused")
    private void handleCalcular() {
        try {
            // 1. Cria o objeto Produto capturando a entrada gráfica
            Produto produto = new Produto(
                    txtNome.getText(),
                    parseInput(txtCustoCompra.getText()),
                    parseInput(txtCustoFixo.getText()),
                    parseInput(txtEmbalagem.getText())
            );

            // 2. Cria as Taxas Operacionais
            TaxasOperacionais taxas = new TaxasOperacionais(
                    parseInput(txtSimples.getText()),
                    parseInput(txtComissao.getText()),
                    parseInput(txtCartao.getText())
            );

            double margemAlvo = parseInput(txtMargem.getText());
            EstrategiaArredondamento estrategia = resolverEstrategia(comboEstrategia.getValue());

            // 3. Executa a regra de cálculo corporativa intocada
            Precificacao resultado = calculadora.calcular(produto, taxas, margemAlvo, estrategia);
            
            historicoSessao.add(resultado);
            
            // 4. Atualiza os componentes Visuais com os retornos da Classe Precificacao
            lblPrecoPraticado.setText(String.format("R$ %.2f", resultado.getPrecoPraticado()));
            lblLucroReal.setText(String.format("R$ %.2f", resultado.getLucroLiquidoReal()));
            lblMargemReal.setText(String.format("%.2f%%", resultado.getMargemRealPercentual()));
            atualizarGrafico(produto, taxas, resultado);
            
        } catch (BusinessException e) {
            exibirAlerta("Erro de Negócio", e.getMessage(), Alert.AlertType.WARNING);
        } catch (NumberFormatException e) {
            exibirAlerta("Erro de Digitação", "Verifique os campos numéricos. Use apenas números e pontos.", Alert.AlertType.ERROR);
        }
    }

    private double parseInput(String texto) {
        if (texto == null || texto.trim().isEmpty()) return 0.0;
        return Double.parseDouble(texto.trim().replace(",", "."));
    }

    private EstrategiaArredondamento resolverEstrategia(String nome) {
        return switch (nome) {
            case "Próximo Inteiro Superior" -> new InteiroSuperior();
            case "Padrão Comercial Final ,90" -> new ComercialNoventa();
            case "Padrão Comercial Final ,99" -> new ComercialNoventaENove();
            default -> new SemArredondamento();
        };
    }

    private void exibirAlerta(String titulo, String mensagem, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensagem);
        alerta.showAndWait();
    }
    
    @FXML
    private void handleMudarFoco(javafx.event.ActionEvent event) {
    Object origem = event.getSource();

    if (origem == txtNome) {
        txtCustoCompra.requestFocus();
    } else if (origem == txtCustoCompra) {
        txtCustoFixo.requestFocus();
    } else if (origem == txtCustoFixo) {
        txtEmbalagem.requestFocus();
    } else if (origem == txtEmbalagem) {
        txtSimples.requestFocus();
    } else if (origem == txtSimples) {
        txtComissao.requestFocus();
    } else if (origem == txtComissao) {
        txtCartao.requestFocus();
    } else if (origem == txtCartao) {
        txtMargem.requestFocus();
    } else if (origem == txtMargem) {
        comboEstrategia.requestFocus(); // Vai para a seleção de arredondamento
    }
}
    
    public java.util.List<Precificacao> getHistoricoSessao() {
        return this.historicoSessao;
    }
    
    private void atualizarGrafico(Produto produto, TaxasOperacionais taxas, Precificacao resultado) {
    // 1. Calcula o valor em R$ correspondente às taxas sobre o preço praticado
    double precoVenda = resultado.getPrecoPraticado();
    double totalTaxasPercentual = taxas.getSimplesNacional() + taxas.getComissaoVenda() + taxas.getTarifaCartao();
    double totalTaxasReais = precoVenda * (totalTaxasPercentual / 100.0);
    
    // 2. Separa os custos de matéria prima e embalagem
    double custosDiretos = produto.getValorCompra() + produto.getCustoEmbalagem();
    double custosFixos = produto.getCustoFixoUnitario();
    double lucroLiquido = resultado.getLucroLiquidoReal();

    // 3. Limpa dados anteriores do gráfico
    graficoPizza.getData().clear();

    // 4. Alimenta a "pizza" com fatias proporcionais em R$
    javafx.scene.chart.PieChart.Data fatiaCustosDiretos = new javafx.scene.chart.PieChart.Data("Custos Diretos", custosDiretos);
    javafx.scene.chart.PieChart.Data fatiaCustosFixos = new javafx.scene.chart.PieChart.Data("Custo Fixo", custosFixos);
    javafx.scene.chart.PieChart.Data fatiaTaxas = new javafx.scene.chart.PieChart.Data("Impostos/Taxas", totalTaxasReais);
    javafx.scene.chart.PieChart.Data fatiaLucro = new javafx.scene.chart.PieChart.Data("Lucro Líquido", lucroLiquido);

    graficoPizza.getData().addAll(fatiaCustosDiretos, fatiaCustosFixos, fatiaTaxas, fatiaLucro);
    
    // Estilização opcional: destaca a fatia de Lucro Líquido deixando-a verde
    fatiaLucro.getNode().setStyle("-fx-pie-color: #22c55e;"); 
    fatiaCustosDiretos.getNode().setStyle("-fx-pie-color: #ef4444;");
}
}