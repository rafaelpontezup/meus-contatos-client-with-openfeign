package br.com.zup.edu.meuscontatosclient.contatos;

import br.com.zup.edu.meuscontatosclient.client.MeusContatosClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static java.util.stream.Collectors.toList;

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
