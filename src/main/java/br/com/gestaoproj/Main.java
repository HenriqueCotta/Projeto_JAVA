package br.com.gestaoproj;

import br.com.gestaoproj.controller.SistemaController;
import br.com.gestaoproj.util.ConexaoMongo;
import br.com.gestaoproj.view.SistemaView;

public class Main {
    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread(ConexaoMongo::close));
        SistemaController controller = new SistemaController();
        SistemaView view = new SistemaView(controller);
        view.iniciar();
        ConexaoMongo.close();
    }
}
