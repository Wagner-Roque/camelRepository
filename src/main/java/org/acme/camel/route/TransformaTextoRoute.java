package org.acme.camel.route;

import org.acme.camel.processor.TransformaTextoProcessor;
import org.apache.camel.builder.RouteBuilder;

public class TransformaTextoRoute extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        from("timer:transformacao?repeatCount=1")
                .routeId("transform-texto")
                .setBody(constant("Esse texto é uma mensagem de teste"))
                .process(new TransformaTextoProcessor())
                .log("${body}");
    }
}
