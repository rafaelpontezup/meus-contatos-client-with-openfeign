package br.com.zup.edu.meuscontatosclient.contatos;

import br.com.zup.edu.meuscontatosclient.client.ContatoResponse;

public class ContatoDaEmpresaResponse {

    private final Long id;
    private final String nome;

    public ContatoDaEmpresaResponse(ContatoResponse contato) {
        this.id = contato.getId();
        this.nome = contato.getNome();
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }
}
