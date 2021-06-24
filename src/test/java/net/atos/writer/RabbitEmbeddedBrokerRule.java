package net.atos.writer;

import org.junit.rules.ExternalResource;

public class RabbitEmbeddedBrokerRule extends ExternalResource {

    private RabbitEmbeddedBroker embeddedBroker;

    @Override
    protected void before() throws Throwable {

        this.embeddedBroker = new RabbitEmbeddedBroker();
        this.embeddedBroker.start();
    }

    @Override
    protected void after() {

        this.embeddedBroker.shutdown();
    }
}