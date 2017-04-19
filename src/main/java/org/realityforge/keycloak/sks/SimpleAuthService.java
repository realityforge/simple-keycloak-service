package org.realityforge.keycloak.sks;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.keycloak.adapters.OidcKeycloakAccount;

public interface SimpleAuthService
{
  /**
   * Retrieve the Keycloak account associated with the current web request if any.
   */
  @Nullable
  OidcKeycloakAccount findAccount();


  /**
   * Return the Keycloak account associated with the current request.
   * If current request is not authenticated then throw a MissingKeycloakAccountException exception.
   */
  @Nonnull
  OidcKeycloakAccount getAccount()
    throws MissingKeycloakAccountException;
}
