# Roteiro

## Implementando HTTP Client com OpenFeign

1. Crie projeto no Spring Initialzer
   1. Web (`spring-boot-starter-web`)
   2. OpenFeign (`spring-cloud-starter-openfeign`)
2. Import no IntelliJ;
3. Implemente classe `StartupRunner` e explica como ela funciona
```java
@Component
public class StartupRunner implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // lista todos os contatos
    }
}
```
4. Habilita OpenFeign no projeto via `@EnableFeignClients`
5. Implementa HTTP client do OpenFeign para **Cadastrar novo contato**:
```java
@FeignClient(
    name = "meusContatosClient",
    url = "http://localhost:8080/meus-contatos/"
)
public interface MeusContatosClient {

   @GetMapping("/api/contatos")
   public List<ContatoResponse> lista();

}

// DTOs
public class ContatoResponse {

    private final Long id;
    private final String nome;
    private final String empresa;

    // ...
}
```

6. Implementa cadastro de contato no `StartupRunner`:
```java
@Component
public class StartupRunner implements ApplicationRunner {

    @Autowired
    private MeusContatosClient client;

    @Override
    public void run(ApplicationArguments args) throws Exception {

       // lista todos os contatos
       List<ContatoResponse> contatos = client.lista();
       contatos.forEach(System.out::println);

    }
}
```
9. Executa aplicação e lê erro de **HTTP Status Code 401**;
10. Habilita log do OpenFeign:
```yml
feign:
    client:
        config:
            meusContatosClient:
                loggerLevel: full

logging.level.br.com.zup.edu.meuscontatosclient.client.MeusContatosClient: DEBUG
```
12. Adiciona dependência do `spring-security-oauth2-client`:
```xml
<dependency>
   <groupId>org.springframework.security</groupId>
   <artifactId>spring-boot-starter-oauth2-client</artifactId>
</dependency>
```
11. Configura `application.yml` (copia e ajusta da [doc do Spring Security](https://docs.spring.io/spring-security/reference/servlet/oauth2/client/authorization-grants.html#_initiating_the_authorization_request)):
```yml
spring:
    security:
        oauth2:
            client:
                provider:
                    meus-contatos:
                        token-uri: http://localhost:18080/auth/realms/meus-contatos/protocol/openid-connect/token
                registration:
                    meus-contatos:
                        authorization-grant-type: client_credentials
                        client-id: meus-contatos-microservice
                        client-secret: kMt0xOywlXUA8BgJdHqn3ilmCWqv9CN1
                        scope: contatos:read,contatos:write
```
13. Configura novo **Client** no Keycloak: `meus-contatos-microservice`;
14. Cria interceptor do OpenFeign: `OAuth2FeignRequestInterceptor`;
15. Cria factory do interceptor: `OAuth2FeignConfiguration`;
16. Configura um `OAuth2AuthorizedClientManager` (exemplo de codigo na [doc do Spring Security](https://docs.spring.io/spring-security/reference/servlet/oauth2/client/core.html#oauth2Client-authorized-manager-provider)):
```java
@Configuration
public class OAuth2ClientConfig {

   /**
    * https://docs.spring.io/spring-security/reference/servlet/oauth2/client/core.html#oauth2Client-authorized-manager-provider
    */
   @Bean
   public OAuth2AuthorizedClientManager authorizedClientManager(ClientRegistrationRepository clientRegistrationRepository,
                                                                OAuth2AuthorizedClientService authorizedClientService) {

      OAuth2AuthorizedClientProvider authorizedClientProvider = OAuth2AuthorizedClientProviderBuilder
              .builder()
              .clientCredentials()
              .build();

      AuthorizedClientServiceOAuth2AuthorizedClientManager authorizedClientManager =
              new AuthorizedClientServiceOAuth2AuthorizedClientManager(clientRegistrationRepository, authorizedClientService);
      authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider);

      return authorizedClientManager;
   }
}
```
16. Restarta a aplicação novamente;