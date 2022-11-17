package org.acme.config;

import io.quarkus.oidc.client.NamedOidcClient;
import io.quarkus.oidc.client.OidcClient;
import io.quarkus.oidc.client.Tokens;
import io.quarkus.runtime.StartupEvent;
import io.smallrye.mutiny.Uni;
import java.util.regex.Pattern;
import java.util.stream.StreamSupport;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import org.eclipse.microprofile.config.ConfigProvider;
import org.jboss.logging.Logger;

@ApplicationScoped
public class OidcClientService {

    /**
     * Default client, not named
     */
    @Inject
    protected OidcClient defaultClient;

    /**
     * Client declared in application.properties file
     */
    @Inject
    @NamedOidcClient("prop-client")
    protected OidcClient propClient;

    /**
     * Client declared in .env file
     */
    @Inject
    @NamedOidcClient("env-client")
    protected OidcClient envClient;

    /**
     * Client declared in application.properties file but use .env configs
     */
    @Inject
    @NamedOidcClient("env-client-from-prop")
    protected OidcClient envClientFromProp;

    @Inject
    protected Logger log;

    private Pattern pattern = Pattern.compile("quarkus.oidc+");

    protected void onStart(@Observes final StartupEvent event) {
        var config = ConfigProvider.getConfig();
        StreamSupport
            .stream(config.getPropertyNames().spliterator(), false)
            .filter(prop -> pattern.matcher(prop.toLowerCase()).find())
            .sorted()
            .forEach(prop -> log.debug(prop + "=" + config.getOptionalValue(prop, String.class).orElse("")));
        log.info("default client: " + defaultClient);
        log.info("prop-client: " + propClient);
        log.info("env-client: " + envClient);
        log.info("env-client-from-prop: " + envClientFromProp);
    }

    public Uni<Tokens> getDefaultToken() {
        return defaultClient.getTokens();
    }

    public Uni<Tokens> getPropToken() {
        return propClient.getTokens();
    }

    public Uni<Tokens> getEnvToken() {
        return envClient.getTokens();
    }

    public Uni<Tokens> getEnvTokenFromProp() {
        return envClientFromProp.getTokens();
    }
}
