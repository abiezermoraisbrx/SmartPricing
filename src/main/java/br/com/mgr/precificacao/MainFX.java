package br.com.mgr.precificacao;

import br.com.mgr.precificacao.controller.PrecificacaoController;
import br.com.mgr.precificacao.repository.HistoricoTxtRepository;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainFX extends Application {

    @Override
public void start(Stage stage) throws Exception {
    // IMPORTANTE: Removemos a "/" do início para que o FXMLLoader procure 
    // a partir da raiz de pacotes correta dentro de 'src/main/resources'
    FXMLLoader loader = new FXMLLoader(getClass().getResource("view/MainView.fxml"));
    Parent root = loader.load();
    
    PrecificacaoController controller = loader.getController();
    
    Scene scene = new Scene(root, 850, 700); // 850 de largura por 600 de altura dá um ótimo espaço;
    stage.setScene(scene);
    stage.setTitle("MGR SmartPricing - Módulo Industrial");
    stage.setResizable(false);
    
    stage.setOnCloseRequest(event -> {
        System.out.println("\n[SISTEMA] Fechamento detectado. Iniciando rotina de persistência...");
        HistoricoTxtRepository txtRepository = new HistoricoTxtRepository();
        txtRepository.salvarUltimosCinco(controller.getHistoricoSessao());
        System.out.println("[SISTEMA] Aplicação encerrada com segurança.");
    });
    
    stage.show();
}

    public static void main(String[] args) {
        launch(args);
    }
}