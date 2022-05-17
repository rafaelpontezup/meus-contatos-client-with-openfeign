# Roteiro

## Implementando HTTP Client com OpenFeign

1. Objetivo:
   1. Criar um microservice para consumir API de Meus Contatos;
   2. Deve filtrar os contatos **por empresa**;
   3. Microservice deve expor uma API REST;
   4. Demonstrar listagem de contatos no Insomnia;
2. Crie projeto no Spring Initialzer
   1. Web (`spring-boot-starter-web`)
   2. OpenFeign (`spring-cloud-starter-openfeign`)
   3. Bean Validation (opcional)
   4. Spring Security OAuth2 Client (`spring-boot-starter-oauth2-client`);
3. Import no IntelliJ;
5. Desenha API no Insominia;
6. Implemente classe `ListaContatosDaEmpresaController`:
```java
@RestController
public class ListaContatosDaEmpresaController {

   @GetMapping("/api/contatos-por-empresa")
   public ResponseEntity<?> lista(@RequestParam String empresa) {

      List<ContatoDaEmpresaResponse> contatosDaEmpresa = // busca contatos e filtra por empresa 

      return ResponseEntity
              .ok(contatosDaEmpresa);
   }

}
```
4. Starta aplicação e faz testes na API. **Ve ela falhar por causa do Spring Security**;
5. Explica que não precisamos proteger essa API REST no nosso contexto, mas que em outros contextos pode fazer sentido;
6. Desabilita regras de acesso do Spring Security:
```java
@Configuration
public class ClientSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // faz nada
    }
}
```
7. Agora, vamos implementar o client...
8. Habilita OpenFeign no projeto via `@EnableFeignClients`
9. Implementa HTTP client do OpenFeign para **Cadastrar novo contato**:
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

6. Injeta e utiliza `MeusContatosClient` no controller:
```java
@RestController
public class ListaContatosDaEmpresaController {

   @Autowired
   private MeusContatosClient client;

   @GetMapping("/api/contatos-por-empresa")
   public ResponseEntity<?> lista(@RequestParam String empresa) {

      List<ContatoDaEmpresaResponse> contatosDaEmpresa = client.lista().stream()
                                .filter(contato -> empresa.equalsIgnoreCase(contato.getEmpresa()))
                                .map(ContatoDaEmpresaResponse::new)
                                .collect(toList());

      return ResponseEntity
              .ok(contatosDaEmpresa);
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

11. Configura `application.yml` (copia e ajusta da [doc do Spring Security](https://docs.spring.io/spring-security/reference/servlet/oauth2/client/authorization-grants.html#_using_the_access_token)):
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
```
16. Restarta a aplicação novamente;
17. Invoca endpoint e vê funcionar. 
18. Faz testes com empresas diferentes;
19. Finaliza;