package br.com.zup.edu.meuscontatosclient;

import br.com.zup.edu.meuscontatosclient.client.ContatoResponse;
import br.com.zup.edu.meuscontatosclient.client.MeusContatosClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

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
