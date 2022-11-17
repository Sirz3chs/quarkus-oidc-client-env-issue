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
    public void testPropClient() {
        oidcClientService.getPropUser()
            .subscribe()
            .withSubscriber(UniAssertSubscriber.create())
            .awaitItem()
            .assertCompleted();
    }

    @Test
    public void testEnvClient() {
        assertThrows(NullPointerException.class, () -> oidcClientService.getEnvUser().await().indefinitely());
    }

    @Test
    public void testEnvClientFromProp() {
        oidcClientService.getEnvUserFromProp()
            .subscribe()
            .withSubscriber(UniAssertSubscriber.create())
            .awaitItem()
            .assertCompleted();
    }
}
