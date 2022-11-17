package org.acme.config;

import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.helpers.test.UniAssertSubscriber;
import javax.inject.Inject;
import static org.junit.Assert.assertThrows;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class OidcClientServiceTest {

    @Inject
    protected OidcClientService oidcClientService;

    @Test
    public void testDefaultClient() {
        oidcClientService.getDefaultToken()
            .subscribe()
            .withSubscriber(UniAssertSubscriber.create())
            .awaitItem()
            .assertCompleted();
    }

    @Test
    public void testPropClient() {
        oidcClientService.getPropToken()
            .subscribe()
            .withSubscriber(UniAssertSubscriber.create())
            .awaitItem()
            .assertCompleted();
    }

    @Test
    public void testEnvClient() {
        assertThrows(NullPointerException.class, () -> oidcClientService.getEnvToken().await().indefinitely());
    }

    @Test
    public void testEnvClientFromProp() {
        // Grant options not present ?
        oidcClientService.getEnvTokenFromProp()
            .subscribe()
            .withSubscriber(UniAssertSubscriber.create())
            .awaitItem()
            .assertCompleted();
    }
}
