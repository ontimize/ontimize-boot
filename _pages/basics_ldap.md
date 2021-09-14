---
title: "LDAP Security"
layout: single
permalink: /basics/ldap/
toc: true
toc_label: "Table of Contents"
toc_sticky: true
breadcrumbs: true
---

# Introduction

The **L**ightweight **D**irectory **A**ccess **P**rotocol (**LDAP**) is an open protocol for accessing a company's information services. Typically, this protocol is used to provide all company users with an easy way to use all services that require access credentials with a single username and password.

# Prerequisites

You can follow this tutorial using your own application, although for this example we will use an application created using the archetype that can be found [on this page](https://ontimize.github.io/ontimize-boot/v3/getting_started/) and with a REST service.

There are 2 options to follow this tutorial, clone the repository with the initial state and follow the tutorial step by step, or download the final example and see which files are new and which have been updated.

<div class="multiColumnRow multiColumnRowJustify">
<div class="multiColumn multiColumnGrow" >
  {{ "**Initial project**

    /$ git clone https://github.com/ontimize/ontimize-examples 
    /ontimize-examples$ cd ontimize-examples
    /ontimize-examples$ git checkout boot-ldap-login-3.x.x-initial" 
    | markdownify }}
</div>
<div class="verticalDivider"></div>
<div class="multiColumn multiColumnGrow" >

  {{ "**Final example**

    /$ git clone https://github.com/ontimize/ontimize-examples 
    /ontimize-examples$ cd ontimize-examples
    /ontimize-examples$ git checkout boot-ldap-login-3.x.x" 
    | markdownify }}

</div>
</div>

# Steps

## Database

### Add a new user

With the database started, we create a new user with the same username that we have registered in the domain. When we try to login we need to use the password of the domain, not the password that inserts into the database. Then, we need to bind this new user with an existing role.

{% highlight sql linenos %}
INSERT INTO TUSER (USER_, PASSWORD, NAME, SURNAME, EMAIL, NIF, USERBLOCKED, LASTPASSWORDUPDATE, FIRSTLOGIN) VALUES('domain.username', 'somepassword', 'Name', 'Surname', 'Email', 'Nif', NULL, NULL, NULL);
{% endhighlight %}

{% highlight sql linenos %}
INSERT INTO TUSER_ROLE (ID_ROLENAME,USER_) VALUES (0,'domain.username');
{% endhighlight %}


## Autoconfigurators

### Add LDAP autoconfigurators

<div class="multiColumnRow">
<div class="multiColumn jstreeloader">
<ul>
  <li data-jstree='{"opened":true, "icon":"fas fa-folder-open"}'>
  ontimize-examples
  <ul>
    <li data-jstree='{"icon":"fas fa-folder-open"}'>
    ldap-api
    <ul>
      <li data-jstree='{"icon":"fas fa-folder-open"}'>
      src
      <ul>
        <li data-jstree='{"icon":"fas fa-folder-open"}'>
        main
        <ul>
          <li data-jstree='{"icon":"fas fa-folder-open"}'>
          java
          <ul>
            <li data-jstree='{"icon":"fas fa-folder-open"}'>
            com
            <ul>
              <li data-jstree='{"icon":"fas fa-folder-open"}'>
              ontimize
              <ul>
                <li data-jstree='{"icon":"fas fa-folder-open"}'>
                ldap
                <ul>
                  <li data-jstree='{"icon":"fas fa-folder-open"}'>
                  api
                  <ul>
                    <li data-jstree='{"icon":"fas fa-folder-open"}'>
                    core
                    <ul>
                      <li data-jstree='{"icon":"fas fa-folder-open"}'>
                      service
                      <ul>
                        <li data-jstree='{"icon":"fas fa-file"}'>IUserService.java</li>
                      </ul>
                      </li>
                    </ul>
                    </li>
                  </ul>
                  </li>
                </ul>
                </li>
              </ul>
              </li>
            </ul>
            </li>
          </ul>
          </li>
        </ul>
        </li>
      </ul>
      </li>
      <li data-jstree='{"icon":"fas fa-file"}'>pom.xml</li>
    </ul>
    </li>
    <li data-jstree='{"icon":"fas fa-folder-open"}'>
    ldap-boot
    <ul>
      <li data-jstree='{"icon":"fas fa-folder-open"}'>
      src
      <ul>
        <li data-jstree='{"icon":"fas fa-folder-open"}'>
        main
        <ul>
          <li data-jstree='{"icon":"fas fa-folder-open"}'>
          java
          <ul>
            <li data-jstree='{"icon":"fas fa-folder-open"}'>
            com
            <ul>
              <li data-jstree='{"icon":"fas fa-folder-open"}'>
              ontimize
              <ul>
                <li data-jstree='{"icon":"fas fa-folder-open"}'>
                ldap
                <ul>
                  <li data-jstree='{"icon":"fas fa-folder-open"}'>
                  security
                  <ul>
                    <li data-jstree='{"selected":true,"icon":"fas fa-file"}'>CustomSecurityAutoConfiguration.java</li>
                    <li data-jstree='{"selected":true,"icon":"fas fa-file"}'>LdapAuthenticationMechanism.java</li>
                    <li data-jstree='{"selected":true,"icon":"fas fa-file"}'>LdapError.java</li>
                  </ul>
                  </li>
                  <li data-jstree='{"icon":"fas fa-file"}'>ServerApplication.java</li>
                </ul>
                </li>
              </ul>
              </li>
            </ul>
            </li>
          </ul>
          </li>
          <li data-jstree='{"icon":"fas fa-folder-open"}'>
          resources
          <ul>
            <li data-jstree='{"icon":"fas fa-file"}'>application.yml</li>
          </ul>
          </li>
        </ul>
        </li>
      </ul>
      </li>
      <li data-jstree='{"icon":"fas fa-file"}'>pom.xml</li>
    </ul>
    </li>
    <li data-jstree='{"icon":"fas fa-folder-open"}'>
    ldap-model
    <ul>
      <li data-jstree='{"icon":"fas fa-folder-open"}'>
      src
      <ul>
        <li data-jstree='{"icon":"fas fa-folder-open"}'>
        main
        <ul>
          <li data-jstree='{"icon":"fas fa-folder-open"}'>
          db
          <ul>
            <li data-jstree='{"icon":"fas fa-file"}'>templateDB.properties</li>
            <li data-jstree='{"icon":"fas fa-file"}'>templateDB.txt</li>
          </ul>
          </li>
          <li data-jstree='{"icon":"fas fa-folder-open"}'>
          java
          <ul>
            <li data-jstree='{"icon":"fas fa-folder-open"}'>
            com
            <ul>
              <li data-jstree='{"icon":"fas fa-folder-open"}'>
              ontimize
              <ul>
                <li data-jstree='{"icon":"fas fa-folder-open"}'>
                ldap
                <ul>
                  <li data-jstree='{"icon":"fas fa-folder-open"}'>
                  model
                  <ul>
                    <li data-jstree='{"icon":"fas fa-folder-open"}'>
                    core
                    <ul>
                      <li data-jstree='{"icon":"fas fa-folder-open"}'>
                      dao
                      <ul>
                        <li data-jstree='{"icon":"fas fa-file"}'>UserDao.java</li>
                        <li data-jstree='{"icon":"fas fa-file"}'>UserRoleDao.java</li>
                      </ul>
                      </li>
                      <li data-jstree='{"icon":"fas fa-folder-open"}'>
                      service
                      <ul>
                        <li data-jstree='{"icon":"fas fa-file"}'>UserService.java</li>
                      </ul>
                      </li>
                    </ul>
                    </li>
                  </ul>
                  </li>
                </ul>
                </li>
              </ul>
              </li>
            </ul>
            </li>
          </ul>
          </li>
          <li data-jstree='{"icon":"fas fa-folder-open"}'>
          resources
          <ul>
            <li data-jstree='{"icon":"fas fa-folder-open"}'>
            dao
            <ul>
              <li data-jstree='{"icon":"fas fa-file"}'>placeholders.properties</li>
              <li data-jstree='{"icon":"fas fa-file"}'>RoleDao.xml</li>
              <li data-jstree='{"icon":"fas fa-file"}'>RoleServerPermissionDao.xml</li>
              <li data-jstree='{"icon":"fas fa-file"}'>ServerPermissionDao.xml</li>
              <li data-jstree='{"icon":"fas fa-file"}'>UserDao.xml</li>
              <li data-jstree='{"icon":"fas fa-file"}'>UserRoleDao.xml</li>
            </ul>
            </li>
          </ul>
          </li>
        </ul>
        </li>
      </ul>
      </li>
      <li data-jstree='{"icon":"fas fa-file"}'>pom.xml</li>
    </ul>
    </li>
    <li data-jstree='{"icon":"fas fa-folder-open"}'>
    ldap-ws
    <ul>
      <li data-jstree='{"icon":"fas fa-folder-open"}'>
      src
      <ul>
        <li data-jstree='{"icon":"fas fa-folder-open"}'>
        main
        <ul>
          <li data-jstree='{"icon":"fas fa-folder-open"}'>
          java
          <ul>
            <li data-jstree='{"icon":"fas fa-folder-open"}'>
            com
            <ul>
              <li data-jstree='{"icon":"fas fa-folder-open"}'>
              ontimize
              <ul>
                <li data-jstree='{"icon":"fas fa-folder-open"}'>
                ldap
                <ul>
                  <li data-jstree='{"icon":"fas fa-folder-open"}'>
                  ws
                  <ul>
                    <li data-jstree='{"icon":"fas fa-folder-open"}'>
                    core
                    <ul>
                      <li data-jstree='{"icon":"fas fa-folder-open"}'>
                      rest
                      <ul>
                        <li data-jstree='{"icon":"fas fa-file"}'>MainRestController.java</li>
                        <li data-jstree='{"icon":"fas fa-file"}'>TestRestController.java</li>
                        <li data-jstree='{"icon":"fas fa-file"}'>UserRestController.java</li>
                      </ul>
                      </li>
                    </ul>
                    </li>
                  </ul>
                  </li>
                </ul>
                </li>
              </ul>
              </li>
            </ul>
            </li>
          </ul>
          </li>
        </ul>
        </li>
      </ul>
      </li>
      <li data-jstree='{"icon":"fas fa-file"}'>pom.xml</li>
    </ul>
    </li>
    <li data-jstree='{"icon":"fas fa-file"}'>.gitignore</li>
    <li data-jstree='{"icon":"fas fa-file"}'>pom.xml</li>
    <li data-jstree='{"icon":"fas fa-file"}'>README.md</li>
  </ul>
  </li>
</ul>
</div>
<div class="multiColumn multiColumnGrow">

  {{ "**LdapError.java**"| markdownify }}

{% highlight java %}
package com.ontimize.projectwiki.security;

public interface LdapError {

	public static final String NO_LDAP_CONNECTION = "NO_CONNECT_TO_LDAP";
	public static final String ERROR_SEARCHING_IN_LDAP = "ERROR_SEARCHING_IN_LDAP";
	public static final String ERROR_LOGIN_LDAP = "LOGINEXCEPTION_WITH_LDAP";
	public static final String ERROR_IO_LDAP = "IOEXCEPTION_WITH_LDAP";
	public static final String EMPTY_LDAP_HOST = "HOST_CANNOT_BE_EMPTY";
	public static final String EMPTY_LDAP_USER = "USER_CANNOT_BE_EMPTY";
	public static final String EMPTY_LDAP_PASSWORD = "PASSWORD_CANNOT_BE_EMPTY";;
	public static final String LDAP_AUTH_USER_PASS_NOT_VALID = "LDAP_CREDENTIALS_NOT_VALID";

}

{% endhighlight %}
  {{ "**LdapAuthenticationMechanism.java**"| markdownify }}

{% highlight java %}
package com.ontimize.projectwiki.security;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Hashtable;
import java.util.StringTokenizer;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.aspectj.weaver.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.stereotype.Component;

import com.ontimize.jee.server.security.authentication.AuthenticationResult;
import com.ontimize.jee.server.security.authentication.IAuthenticationMechanism;
import com.ontimize.jee.server.security.authentication.OntimizeAuthenticationProvider;

@Component

public class LdapAuthenticationMechanism implements IAuthenticationMechanism {

	private static final Logger LOGGER = LoggerFactory.getLogger(LdapAuthenticationMechanism.class);
	private String credentialsCharset = "UTF-8";

	@Value("${ldap.host}")
	private String ldapHost;

	@Value("${ldap.port}")
	private int ldapPort;

	@Value("${ldap.login-type}")
	private String loginType;

	@Value("${ldap.bind.dn}")
	private String ldapBindDn;

	@Value("${ldap.domain}")
	private String ldapDomain;

	@Value("${ldap.base.dn}")
	private String ldapBaseDn;

	@Override
	public AuthenticationResult authenticate(final HttpServletRequest request, final HttpServletResponse response,

			final AuthenticationManager authenticationManager, final UserDetailsService userDetailsService) {

		try {

			final String header = request.getHeader("Authorization");
			if ((header == null) || !header.startsWith("Basic ")) {
				return null;
			}

			final String[] tokens = this.extractAndDecodeHeader(header, request);
			assert tokens.length == 2;
			final String username = tokens[0];
			final String password = tokens[1];

			LdapAuthenticationMechanism.LOGGER.trace("Validating access for user : '{}'", username);

			DirContext dirContext = null;

			if (loginType.equals("DN")) {
				String userDn = "uid=" + username + "," + ldapBindDn;
				dirContext = connect(userDn, password, ldapHost, ldapPort, null, false);
			} else if (loginType.equals("simple")) {
				dirContext = connect(username, password, ldapHost, ldapPort, ldapDomain, false);
			}

			if (dirContext != null) {
				return new AuthenticationResult(true, new UsernamePasswordAuthenticationToken(username,
						OntimizeAuthenticationProvider.NO_AUTHENTICATION_TOKEN));
			} else {
				LOGGER.error("System authentication failed: no connect to LDAP");
				throw new BadCredentialsException(LdapError.NO_LDAP_CONNECTION.toString());
			}
		} catch (NamingException e) {
			LOGGER.error("System authentication failed: NamingException searching into server LDAP", e);
			throw new BadCredentialsException(LdapError.ERROR_SEARCHING_IN_LDAP.toString());
		} catch (LoginException e) {
			LOGGER.error("System authentication failed: LoginException with server LDAP", e);
			throw new BadCredentialsException(LdapError.ERROR_LOGIN_LDAP.toString());
		} catch (IOException e) {
			LOGGER.error("System authentication failed: IOException with server LDAP", e);
			throw new BadCredentialsException(LdapError.ERROR_IO_LDAP.toString());
		}

	}

	public static synchronized DirContext connect(final String user, final String password, final String hosts,
			final int port, final String adddomain, boolean ssl)
			throws NamingException, java.io.IOException, LoginException {

		if ((hosts == null) || (hosts.length() == 0)) {
			LOGGER.error("LDAP host cannot be neither null nor empty");
			throw new IllegalArgumentException(LdapError.EMPTY_LDAP_HOST.toString());
		}

		StringTokenizer st = new StringTokenizer(hosts, ";");

		while (st.hasMoreTokens()) {
			String host = st.nextToken();
			return _connect(user, password, host, port, adddomain, ssl);
		}

		return null;

	}

	private static synchronized DirContext _connect(final String user, final String password, final String host,
			final int port, final String adddomain, boolean ssl)
			throws NamingException, java.io.IOException, LoginException {

		Hashtable<String, String> props = new Hashtable<>();
		if ((user == null) || (user.length() == 0)) {
			LOGGER.error("user cannot be neither null nor empty");
			throw new IllegalArgumentException(LdapError.EMPTY_LDAP_USER.toString());

		}

		if ((password == null) || (password.length() == 0)) {
			LOGGER.error("password cannot be neither null nor empty");
			throw new IllegalArgumentException(LdapError.EMPTY_LDAP_PASSWORD.toString());

		}

		props.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		props.put(Context.PROVIDER_URL, "ldap://" + host + ":" + port);
		props.put(Context.SECURITY_AUTHENTICATION, "simple");

		if (adddomain != null) {
			props.put(Context.SECURITY_PRINCIPAL, user + "@" + adddomain);
		} else {
			props.put(Context.SECURITY_PRINCIPAL, user);
		}

		props.put(Context.SECURITY_CREDENTIALS, password);

		if (ssl) {
			props.put(Context.SECURITY_PROTOCOL, "ssl");
		}

		props.put(Context.REFERRAL, "follow");
		DirContext ctx = null;

		try {
			ctx = new InitialDirContext(props);
			LOGGER.info("Authentication sucessfully in LDAP");

		} catch (Exception e) {
			LOGGER.error("System authentication failed: wrong user and/or pass in LDAP");
			throw new BadCredentialsException(LdapError.LDAP_AUTH_USER_PASS_NOT_VALID.toString());

		}

		return ctx;

	}

	private String[] extractAndDecodeHeader(final String header, final HttpServletRequest request) {

		try {
			final byte[] base64Token = header.substring(6).getBytes("UTF-8");
			byte[] decoded;
			decoded = Base64.decode(base64Token);

			final String token = new String(decoded, this.getCredentialsCharset(request));
			final int delim = token.indexOf(':');

			if (delim == -1) {
				throw new BadCredentialsException("Invalid basic authentication token");
			}
			return new String[] { token.substring(0, delim), token.substring(delim + 1) };
		} catch (IllegalArgumentException | UnsupportedEncodingException error) {
			throw new BadCredentialsException("Failed to decode basic authentication token", error);
		}
	}

	protected String getCredentialsCharset(final HttpServletRequest httpRequest) {
		return this.credentialsCharset;
	}

}
{% endhighlight %}
  {{ "**CustomSecurityAutoConfiguration.java**"| markdownify }}

{% highlight java %}
package com.ontimize.projectwiki.security;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import com.ontimize.boot.autoconfigure.security.DefaultSecurityAutoConfiguration;
import com.ontimize.jee.server.security.authentication.IAuthenticationMechanism;
import com.ontimize.jee.server.security.authentication.OntimizeAuthenticationFilter;
import com.ontimize.jee.server.security.authentication.OntimizeAuthenticationSuccessHandler;

@Configuration 
@EnableWebSecurity
@EnableAutoConfiguration
@ConditionalOnProperty(name = "ontimize.security.mode", havingValue = "ldap", matchIfMissing = false) 
public class CustomSecurityAutoConfiguration extends DefaultSecurityAutoConfiguration { 

    @Value("${ontimize.security.service-path:/**}") 
    private String servicePath; 

    @Value("${ontimize.security.ignore-paths:}") 
    private String[] ignorePaths; 
 
    @Override 
    public OntimizeAuthenticationFilter preAuthFilterOntimize() throws Exception { 
        OntimizeAuthenticationFilter filter = new OntimizeAuthenticationFilter(this.servicePath); 
        filter.setUserDetailsService(this.userDetailsService()); 
        filter.setUserCache(this.userCache()); 
        filter.setTokenGenerator(this.tokenGenerator()); 
        filter.setGenerateJwtHeader(true); 
        filter.setAuthenticationManager(this.authenticationManager()); 
        filter.setAuthenticationEntryPoint(this.authenticationEntryPoint()); 
        filter.setAuthenticationMechanismList(new ArrayList<>()); 
		filter.getAuthenticationMechanismList().add(this.jwtAuthenticator());
		filter.getAuthenticationMechanismList().add(this.ldapAuthenticator()); 
        filter.setAuthenticationSuccessHandler(new OntimizeAuthenticationSuccessHandler()); 
        filter.afterPropertiesSet(); 
        return filter; 

    } 
  
    @Bean 
    public IAuthenticationMechanism ldapAuthenticator() { 
        return new LdapAuthenticationMechanism(); 
    } 
} 

{% endhighlight %}

</div>
</div>

## Modify application.yml

### Add LDAP properties

In the *application.yml* we need to change the ontimize security mode to **ldap** and add the following properties (More information in [this link](/ontimize-boot/v3/basics/autoconfigurators/#ldap)):

<div class="multiColumnRow">
<div class="multiColumn jstreeloader">
<ul>
  <li data-jstree='{"opened":true, "icon":"fas fa-folder-open"}'>
  ontimize-examples
  <ul>
    <li data-jstree='{"icon":"fas fa-folder-open"}'>
    ldap-api
    <ul>
      <li data-jstree='{"icon":"fas fa-folder-open"}'>
      src
      <ul>
        <li data-jstree='{"icon":"fas fa-folder-open"}'>
        main
        <ul>
          <li data-jstree='{"icon":"fas fa-folder-open"}'>
          java
          <ul>
            <li data-jstree='{"icon":"fas fa-folder-open"}'>
            com
            <ul>
              <li data-jstree='{"icon":"fas fa-folder-open"}'>
              ontimize
              <ul>
                <li data-jstree='{"icon":"fas fa-folder-open"}'>
                ldap
                <ul>
                  <li data-jstree='{"icon":"fas fa-folder-open"}'>
                  api
                  <ul>
                    <li data-jstree='{"icon":"fas fa-folder-open"}'>
                    core
                    <ul>
                      <li data-jstree='{"icon":"fas fa-folder-open"}'>
                      service
                      <ul>
                        <li data-jstree='{"icon":"fas fa-file"}'>IUserService.java</li>
                      </ul>
                      </li>
                    </ul>
                    </li>
                  </ul>
                  </li>
                </ul>
                </li>
              </ul>
              </li>
            </ul>
            </li>
          </ul>
          </li>
        </ul>
        </li>
      </ul>
      </li>
      <li data-jstree='{"icon":"fas fa-file"}'>pom.xml</li>
    </ul>
    </li>
    <li data-jstree='{"icon":"fas fa-folder-open"}'>
    ldap-boot
    <ul>
      <li data-jstree='{"icon":"fas fa-folder-open"}'>
      src
      <ul>
        <li data-jstree='{"icon":"fas fa-folder-open"}'>
        main
        <ul>
          <li data-jstree='{"icon":"fas fa-folder-open"}'>
          java
          <ul>
            <li data-jstree='{"icon":"fas fa-folder-open"}'>
            com
            <ul>
              <li data-jstree='{"icon":"fas fa-folder-open"}'>
              ontimize
              <ul>
                <li data-jstree='{"icon":"fas fa-folder-open"}'>
                ldap
                <ul>
                  <li data-jstree='{"icon":"fas fa-folder-open"}'>
                  security
                  <ul>
                    <li data-jstree='{"icon":"fas fa-file"}'>CustomSecurityAutoConfiguration.java</li>
                    <li data-jstree='{"icon":"fas fa-file"}'>LdapAuthenticationMechanism.java</li>
                    <li data-jstree='{"icon":"fas fa-file"}'>LdapError.java</li>
                  </ul>
                  </li>
                  <li data-jstree='{"icon":"fas fa-file"}'>ServerApplication.java</li>
                </ul>
                </li>
              </ul>
              </li>
            </ul>
            </li>
          </ul>
          </li>
          <li data-jstree='{"icon":"fas fa-folder-open"}'>
          resources
          <ul>
            <li data-jstree='{"selected":true,"icon":"fas fa-file"}'>application.yml</li>
          </ul>
          </li>
        </ul>
        </li>
      </ul>
      </li>
      <li data-jstree='{"icon":"fas fa-file"}'>pom.xml</li>
    </ul>
    </li>
    <li data-jstree='{"icon":"fas fa-folder-open"}'>
    ldap-model
    <ul>
      <li data-jstree='{"icon":"fas fa-folder-open"}'>
      src
      <ul>
        <li data-jstree='{"icon":"fas fa-folder-open"}'>
        main
        <ul>
          <li data-jstree='{"icon":"fas fa-folder-open"}'>
          db
          <ul>
            <li data-jstree='{"icon":"fas fa-file"}'>templateDB.properties</li>
            <li data-jstree='{"icon":"fas fa-file"}'>templateDB.txt</li>
          </ul>
          </li>
          <li data-jstree='{"icon":"fas fa-folder-open"}'>
          java
          <ul>
            <li data-jstree='{"icon":"fas fa-folder-open"}'>
            com
            <ul>
              <li data-jstree='{"icon":"fas fa-folder-open"}'>
              ontimize
              <ul>
                <li data-jstree='{"icon":"fas fa-folder-open"}'>
                ldap
                <ul>
                  <li data-jstree='{"icon":"fas fa-folder-open"}'>
                  model
                  <ul>
                    <li data-jstree='{"icon":"fas fa-folder-open"}'>
                    core
                    <ul>
                      <li data-jstree='{"icon":"fas fa-folder-open"}'>
                      dao
                      <ul>
                        <li data-jstree='{"icon":"fas fa-file"}'>UserDao.java</li>
                        <li data-jstree='{"icon":"fas fa-file"}'>UserRoleDao.java</li>
                      </ul>
                      </li>
                      <li data-jstree='{"icon":"fas fa-folder-open"}'>
                      service
                      <ul>
                        <li data-jstree='{"icon":"fas fa-file"}'>UserService.java</li>
                      </ul>
                      </li>
                    </ul>
                    </li>
                  </ul>
                  </li>
                </ul>
                </li>
              </ul>
              </li>
            </ul>
            </li>
          </ul>
          </li>
          <li data-jstree='{"icon":"fas fa-folder-open"}'>
          resources
          <ul>
            <li data-jstree='{"icon":"fas fa-folder-open"}'>
            dao
            <ul>
              <li data-jstree='{"icon":"fas fa-file"}'>placeholders.properties</li>
              <li data-jstree='{"icon":"fas fa-file"}'>RoleDao.xml</li>
              <li data-jstree='{"icon":"fas fa-file"}'>RoleServerPermissionDao.xml</li>
              <li data-jstree='{"icon":"fas fa-file"}'>ServerPermissionDao.xml</li>
              <li data-jstree='{"icon":"fas fa-file"}'>UserDao.xml</li>
              <li data-jstree='{"icon":"fas fa-file"}'>UserRoleDao.xml</li>
            </ul>
            </li>
          </ul>
          </li>
        </ul>
        </li>
      </ul>
      </li>
      <li data-jstree='{"icon":"fas fa-file"}'>pom.xml</li>
    </ul>
    </li>
    <li data-jstree='{"icon":"fas fa-folder-open"}'>
    ldap-ws
    <ul>
      <li data-jstree='{"icon":"fas fa-folder-open"}'>
      src
      <ul>
        <li data-jstree='{"icon":"fas fa-folder-open"}'>
        main
        <ul>
          <li data-jstree='{"icon":"fas fa-folder-open"}'>
          java
          <ul>
            <li data-jstree='{"icon":"fas fa-folder-open"}'>
            com
            <ul>
              <li data-jstree='{"icon":"fas fa-folder-open"}'>
              ontimize
              <ul>
                <li data-jstree='{"icon":"fas fa-folder-open"}'>
                ldap
                <ul>
                  <li data-jstree='{"icon":"fas fa-folder-open"}'>
                  ws
                  <ul>
                    <li data-jstree='{"icon":"fas fa-folder-open"}'>
                    core
                    <ul>
                      <li data-jstree='{"icon":"fas fa-folder-open"}'>
                      rest
                      <ul>
                        <li data-jstree='{"icon":"fas fa-file"}'>MainRestController.java</li>
                        <li data-jstree='{"icon":"fas fa-file"}'>TestRestController.java</li>
                        <li data-jstree='{"icon":"fas fa-file"}'>UserRestController.java</li>
                      </ul>
                      </li>
                    </ul>
                    </li>
                  </ul>
                  </li>
                </ul>
                </li>
              </ul>
              </li>
            </ul>
            </li>
          </ul>
          </li>
        </ul>
        </li>
      </ul>
      </li>
      <li data-jstree='{"icon":"fas fa-file"}'>pom.xml</li>
    </ul>
    </li>
    <li data-jstree='{"icon":"fas fa-file"}'>.gitignore</li>
    <li data-jstree='{"icon":"fas fa-file"}'>pom.xml</li>
    <li data-jstree='{"icon":"fas fa-file"}'>README.md</li>
  </ul>
  </li>
</ul>
</div>
<div class="multiColumn multiColumnGrow">

  {{ "**application.yml**"| markdownify }}

{% highlight yml %}

ontimize:
   security:
      mode: ldap
ldap: 
   active: true 
   host: 10.0.0.1
   port: 389
   login-type: simple
   bind.dn: 
   base.dn: 
   domain: yourdomain.com

{% endhighlight %}

</div>
</div>