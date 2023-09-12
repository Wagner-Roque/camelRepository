package org.acme.camel;

import org.apache.camel.BeanInject;
import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.FluentProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.main.junit5.CamelMainTest;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;

@CamelMainTest(
        mainClass = Main.class,
        replaceRouteFromWith = {"integracao-arquivo=direct:inicio"},
        mockEndpointsAndSkip = "(direct:integracaoTransporte[12]|file:transportatora1-erro.*)"
)
public class IntegracaoTransportadoraDoisTest {

    @BeanInject
    FluentProducerTemplate template;
    @EndpointInject("mock:direct:integracaoTransporte1")
    MockEndpoint mockEndpointTransportadora1;
    @EndpointInject("mock:direct:integracaoTransporte2")
    MockEndpoint mockEndpointTransportadora2;

    @EndpointInject("mock:file:transportatora1-erro")
    MockEndpoint mockErroFile;

    @Test
    public void shouldSendToCorrectIntegration() throws URISyntaxException, IOException, InterruptedException {
        var body = Arquivo.lerConteudo("transportadora2.xml");
        mockEndpointTransportadora1.expectedMessageCount(1);
        mockEndpointTransportadora2.expectedBodiesReceived(body);
        mockEndpointTransportadora2.expectedMessageCount(1);
        mockEndpointTransportadora2.expectedBodiesReceived(0);
        mockErroFile.expectedBodiesReceived(0);

        Exchange exchange = template.withDefaultEndpoint("direct:inicio")
                .withBody(body)
                .withHeader("CamelFileName", "teste.xml")
                .send();

        mockEndpointTransportadora1.assertIsSatisfied();
        mockEndpointTransportadora2.assertIsSatisfied();
        mockErroFile.assertIsSatisfied();

    }
}
