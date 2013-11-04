//
//package br.com.danielferber.gittocc.external;
//
//import java.io.File;
//import java.util.concurrent.Callable;
//import javax.swing.JEditorPane;
//import junit.framework.TestCase;
//
///**
// *
// * @author a7hs
// */
//public class JConsueloHistoricoMergeTest extends TestCase {
//    
//    public JConsueloHistoricoMergeTest(String testName) {
//        super(testName);
//    }
//    
//    @Override
//    protected void setUp() throws Exception {
//        super.setUp();
//    }
//
//    // TODO add test methods here. The name must begin with 'test'. For example:
//    // public void testHello() {}
//    
//    public void testMerge() throws Exception {
//        File historicoPath = new File("K:\\CcViews\\JCONSUELO_DVL_a7hs_mi00258482\\JCONSUELO\\Fontes\\Modulos-RCP\\Aplicacao\\Aplicacao_Historico\\src\\historico.html");
//        String content = getContent();
//        JEditorPane htmlView = new JEditorPane();
//        htmlView.setContentType("text/html");
//        htmlView.setText(content);
//        
//        Callable<String> mergeCall = new JConsueloHistoricoMerge(historicoPath, htmlView.getText());
//        String novoConteudo = mergeCall.call();
//        System.out.println(novoConteudo);
//    }
//    
//    private String getContent() {
//        return  "<!DOCTYPE html>\n" +
//                "<html>\n" +
//                "    <head>\n" +
//                "        <title>JConsuelo - Histórico de versões</title>\n" +
//                "        <meta charset=\"UTF-8\">\n" +
//                "        <meta name=\"viewport\" content=\"width=device-width\">\n" +
//                "        <style type=\"text/css\">\n" +
//                "            h1 {color:blue;}\n" +
//                "            h2 {color:blue;}\n" +
//                "            h3 {color:blue;}\n" +
//                "            body {\n" +
//                "                background-color: #D8D8D8;\n" +
//                "            }\n" +
//                "        </style>        \n" +
//                "    </head>\n" +
//                "    <body>\n" +
//                "        <h1>JConsuelo - Histórico de Versões</h1>\n" +
//                "	<hr />\n" +
//                "        \n" +
//                "        <div>\n" +
//                "            <h2>Sprint 1: versão 1.2.x [28/10/2013]</h2>\n" +
//                "            <h3>Novas Funcionalidades</h3>\n" +
//                "            <ul>\n" +
//                "                <li>Novas Func Xpto 1</li>\n" +
//                "                <li>Novas Func Xpto 2</li>\n" +
//                "            </ul>\n" +
//                "            <h3>Melhorias</h3>\n" +
//                "            <ul>\n" +
//                "                <li>Melhorias Xpto 1</li>\n" +
//                "                <li>Melhorias Xpto 2</li>\n" +
//                "            </ul>\n" +
//                "            <h3>Correções</h3>\n" +
//                "            <ul>\n" +
//                "                <li>Correções Xpto 1</li>\n" +
//                "                <li>Correções Xpto 2</li>\n" +
//                "            </ul>\n" +
//                "        </div>\n" +
//                "    </body>\n" +
//                "</html>";
//    }
//}
