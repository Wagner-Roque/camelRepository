package org.acme.camel;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.http.HttpConstants;

public class IntegracaoTransportadora2 extends RouteBuilder {
    @Override
    public void configure() throws Exception {

        errorHandler(deadLetterChannel("file:{{diretorioTransportadoraErro}}").maximumRedeliveries(2));

        from("direct:integracaoTransporte2")
                .routeId("integracao-arquivo-transportes1")
                .throttle(1).timePeriodMillis(5000).asyncDelayed()
                    .setHeader(HttpConstants.HTTP_METHOD, constant("POST"))
                    .setHeader(HttpConstants.HTTP_URI, constant("{{urlApiTransportadora2}}"))
                    .setHeader(HttpConstants.HTTP_PATH, constant("nfes"))
                    .setHeader(HttpConstants.CONTENT_TYPE, constant("application/xml"))
                .to("http:servidorTransportadora2");
    }
}
