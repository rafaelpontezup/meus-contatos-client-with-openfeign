package br.com.zup.edu.meuscontatosclient.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(
    name = "meusContatosClient",
    url = "http://localhost:8080/meus-contatos/"
)
public interface MeusContatosClient {

    @GetMapping("/api/contatos")
    public List<ContatoResponse> lista();

}
