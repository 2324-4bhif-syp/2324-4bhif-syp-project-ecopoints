package at.htl.ecopoints.util.resteasy;

import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.engines.URLConnectionClientEngineBuilder;
import org.jboss.resteasy.client.jaxrs.internal.LocalResteasyProviderFactory;
import org.jboss.resteasy.spi.ResteasyProviderFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import jakarta.ws.rs.client.ClientBuilder;

@Singleton
public class RestApiClientBuilder {
    final public ScheduledExecutorService scheduledExecutorService;
    final public ExecutorService executorService;

    @Inject
    public RestApiClientBuilder() {
        scheduledExecutorService = Executors.newScheduledThreadPool(1);
        executorService = Executors.newSingleThreadExecutor();
    }
    public <T> T build(Class <? extends T> type, String url) {
        ResteasyProviderFactory.setRegisterBuiltinByDefault(false);
        ResteasyProviderFactory factory = ResteasyProviderFactory.getInstance();
        factory.registerProvider(JsonMessageBodyReader.class, true);
        factory.registerProvider(JsonMessageBodyWriter.class, true);
        ResteasyClientBuilder builder = (ResteasyClientBuilder) ClientBuilder.newBuilder();
        builder.scheduledExecutorService(scheduledExecutorService);
        builder.executorService(executorService, false);
        builder.readTimeout(10, TimeUnit.SECONDS);
        builder.providerFactory(new LocalResteasyProviderFactory(factory));
        org.jboss.resteasy.client.jaxrs.ClientHttpEngine httpEngine = new URLConnectionClientEngineBuilder().resteasyClientBuilder(builder).build();
        builder.httpEngine(httpEngine);
        org.jboss.resteasy.client.jaxrs.ResteasyClient client = builder.build();
        return client.target(url).proxy(type);
    }
}
