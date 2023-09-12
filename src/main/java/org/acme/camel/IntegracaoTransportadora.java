package org.acme.camel;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.file.GenericFileOperationFailedException;

public class IntegracaoTransportadora extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        onException(GenericFileOperationFailedException.class)
                .useOriginalMessage()
                .handled(true)
                        .maximumRedeliveries(5).redeliveryDelay(5000)
                .to("file:{{diretorioTransportadoraErro}}");

        from("direct:integracaoTransporte1")
                .routeId("integracao-arquivo-transportes1")
                .to("file:{{diretorioSaida}}?fileName=${date:now:HHmmss}_${file:name}");

    }
}
