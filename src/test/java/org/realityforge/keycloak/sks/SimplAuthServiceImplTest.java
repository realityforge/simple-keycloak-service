package org.realityforge.keycloak.sks;

import java.lang.reflect.Field;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.keycloak.adapters.OidcKeycloakAccount;
import org.keycloak.adapters.spi.KeycloakAccount;
import org.realityforge.guiceyloops.server.AssertUtil;
import org.testng.annotations.Test;
import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

public class SimplAuthServiceImplTest
{
  @Test
  public void isCdiType()
  {
    AssertUtil.assertNoFinalMethodsForCDI( SimplAuthServiceImpl.class );
  }

  @Test
  public void basicWorkflow()
    throws Exception
  {
    final OidcKeycloakAccount account = mock( OidcKeycloakAccount.class );
    final HttpServletRequest request = mock( HttpServletRequest.class );
    final HttpSession session = mock( HttpSession.class );

    final SimplAuthServiceImpl s = new SimplAuthServiceImpl();
    setField( s, request );

    // Test if it present on request
    {
      reset( request, session );
      when( request.getAttribute( KeycloakAccount.class.getName() ) ).
        thenReturn( account );

      assertEquals( s.findAccount(), account );

      verify( request ).getAttribute( KeycloakAccount.class.getName() );
      verify( request, never() ).getSession( false );
      verify( session, never() ).getAttribute( KeycloakAccount.class.getName() );
    }

    // Test if it present on session
    {
      reset( request, session );
      when( request.getAttribute( KeycloakAccount.class.getName() ) ).
        thenReturn( null );
      when( request.getSession( false ) ).
        thenReturn( session );
      when( session.getAttribute( KeycloakAccount.class.getName() ) ).
        thenReturn( account );

      assertEquals( s.findAccount(), account );

      verify( request ).getAttribute( KeycloakAccount.class.getName() );
      verify( request ).getSession( false );
      verify( session ).getAttribute( KeycloakAccount.class.getName() );
    }

    // Test if it is not present
    {
      reset( request, session );
      when( request.getAttribute( KeycloakAccount.class.getName() ) ).
        thenReturn( null );
      when( request.getSession( false ) ).
        thenReturn( session );
      when( session.getAttribute( KeycloakAccount.class.getName() ) ).
        thenReturn( null );

      assertEquals( s.findAccount(), null );

      verify( request ).getAttribute( KeycloakAccount.class.getName() );
      verify( request ).getSession( false );
      verify( session ).getAttribute( KeycloakAccount.class.getName() );
    }

    // Test if it is not present and no session
    {
      reset( request, session );
      when( request.getAttribute( KeycloakAccount.class.getName() ) ).
        thenReturn( null );
      when( request.getSession( false ) ).
        thenReturn( null );

      assertEquals( s.findAccount(), null );

      verify( request ).getAttribute( KeycloakAccount.class.getName() );
      verify( request ).getSession( false );
      verify( session, never() ).getAttribute( KeycloakAccount.class.getName() );
    }

    // Test getAccount no raise an exception if present
    {
      reset( request, session );
      when( request.getAttribute( KeycloakAccount.class.getName() ) ).
        thenReturn( account );

      assertEquals( s.getAccount(), account );

      verify( request ).getAttribute( KeycloakAccount.class.getName() );
      verify( request, never() ).getSession( false );
      verify( session, never() ).getAttribute( KeycloakAccount.class.getName() );
    }

    // Test getAccount raises exception if not present
    {
      reset( request, session );
      when( request.getAttribute( KeycloakAccount.class.getName() ) ).
        thenReturn( null );
      when( request.getSession( false ) ).
        thenReturn( null );

      assertThrows( MissingKeycloakAccountException.class, s::getAccount );

      verify( request ).getAttribute( KeycloakAccount.class.getName() );
      verify( request ).getSession( false );
      verify( session, never() ).getAttribute( KeycloakAccount.class.getName() );
    }
  }

  private void setField( final SimplAuthServiceImpl object, final Object value )
    throws NoSuchFieldException, IllegalAccessException
  {
    final Field field = SimplAuthServiceImpl.class.getDeclaredField( "_httpRequest" );
    field.setAccessible( true );
    field.set( object, value );
  }
}
