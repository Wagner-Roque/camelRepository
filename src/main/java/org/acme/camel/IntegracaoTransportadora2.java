package org.acme.camel;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.http.HttpConstants;
import org.apache.camel.http.base.HttpOperationFailedException;

public class IntegracaoTransportadora2 extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        onException(HttpOperationFailedException.class)
//                .continued(true);
                .handled(true)
                .maximumRedeliveries(5)
                .to("file:{{diretorioTransportadoraErro}}")
                .process(exchange -> {
                    var exception = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, HttpOperationFailedException.class);
                    exchange.getMessage().setBody(exception.getResponseBody());
                })
                .to("file:{{diretorioTransportadoraErro}}?fileName=${file:name}.erro");

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
