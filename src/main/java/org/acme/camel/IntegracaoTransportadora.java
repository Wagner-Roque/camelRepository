package org.acme.camel;

import org.apache.camel.builder.RouteBuilder;

public class IntegracaoTransportadora extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        from("direct:integracaoTransporte1")
                .routeId("integracao-arquivo-transportes1")
                .to("file:{{diretorioSaida}}?fileName=${date:now:HHmmss}_${file:name}");

    }
}
