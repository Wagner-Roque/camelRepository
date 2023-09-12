package org.acme.camel;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.file.FileConstants;

public class IntegracaoArquivo extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        /**
         *Captures any type of error
         */
        errorHandler(deadLetterChannel("file:{{diretorioTransportadoraErro}}")
                .useOriginalBody()
                .maximumRedeliveries(2));

        from("file:{{diretorioEntrada}}?delay=5000")
                .routeId("integracao-arquivo")
                .process(exchange -> {
                    System.out.println(exchange.getMessage().getBody(String.class));
                })
                .log("Processando o arquivo: ${file:name}")
                .setHeader(FileConstants.FILE_NAME, simple("${date:now:HHmmss}_${file:name}"))
                .to("file:{{diretorioSaida}}");
    }
}
