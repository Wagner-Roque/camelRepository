package org.acme.camel;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.support.builder.Namespaces;

public class IntegracaoArquivoXmlTransportadora extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        var ns = new Namespaces("ns", "http://www.portalfiscal.inf.br/nfe");
//        onException(IOException.class);
//        onException(ClassNotFoundException.class);
        from("file:{{diretorioEntrada}}?delay=5000")
                .routeId("integracao-arquivo")
                .log("Processando o arquivo: ${file:name}")
                .setProperty("CNPJ", xpath("{{xpathCnpjTransportadora}}", ns))
                .choice()
                .when(exchangeProperty("CNPJ").isEqualTo("1"))
                .to("direct:integracaoTransporte1")
                .when(exchangeProperty("CNPJ").isEqualTo("2"))
                .to("direct:integracaoTransporte2")
                .otherwise()
                .log("Transportadora não integrada")
                .end();

    }
}



