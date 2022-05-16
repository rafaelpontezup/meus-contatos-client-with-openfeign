package br.com.zup.edu.meuscontatosclient.client;

public class ContatoResponse {

    private final Long id;
    private final String nome;
    private final String empresa;

    public ContatoResponse(Long id, String nome, String empresa) {
        this.id = id;
        this.nome = nome;
        this.empresa = empresa;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmpresa() {
        return empresa;
    }

    @Override
    public String toString() {
        return "ContatoResponse{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", empresa='" + empresa + '\'' +
                '}';
    }
}
