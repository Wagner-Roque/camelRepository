package org.acme.camel.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class TransformaTextoProcessor implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {
        var body = exchange.getMessage().getBody(String.class);
        exchange.getMessage().setBody(body.toUpperCase().substring(2));
    }
}
