package org.realityforge.keycloak.sks;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Typed;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.keycloak.adapters.OidcKeycloakAccount;
import org.keycloak.adapters.spi.KeycloakAccount;

/**
 * A very simple authentication service for accessing keycloak credentials.
 */
@ApplicationScoped
@Typed( SimpleAuthService.class )
public class SimplAuthServiceImpl
  implements SimpleAuthService
{
  @Inject
  private HttpServletRequest _httpRequest;

  @Nullable
  @Override
  public OidcKeycloakAccount findAccount()
  {
    final OidcKeycloakAccount account =
      (OidcKeycloakAccount) _httpRequest.getAttribute( KeycloakAccount.class.getName() );
    if ( null == account )
    {
      final HttpSession session = _httpRequest.getSession( false );
      if ( null != session )
      {
        return (OidcKeycloakAccount) session.getAttribute( KeycloakAccount.class.getName() );
      }
      else
      {
        return null;
      }
    }
    else
    {
      return account;
    }
  }

  @Nonnull
  @Override
  public OidcKeycloakAccount getAccount()
  {
    final OidcKeycloakAccount account = findAccount();
    if ( null == account )
    {
      throw new MissingKeycloakAccountException();
    }
    return account;
  }
}
