package com.fiap.fomezero.domain.model;

public class Restaurante {

    // Atributos básicos do restaurante
    private Long id;
    private String nome;

    // Composições: Um restaurante TEM um endereço e TEM um dono (usuário)
    private Endereco endereco;
    private String tipoCozinha;
    private String horarioFuncionamento;
    private Usuario dono;

    // Construtor vazio (necessário para manipulação de objetos e frameworks de mapeamento futuros)
    public Restaurante() {
    }

    // Construtor completo para facilitar a criação do objeto de uma vez só
    public Restaurante(Long id, String nome, Endereco endereco, String tipoCozinha, String horarioFuncionamento, Usuario dono) {
        this.id = id;
        this.nome = nome;
        this.endereco = endereco;
        this.tipoCozinha = tipoCozinha;
        this.horarioFuncionamento = horarioFuncionamento;
        this.dono = dono;
    }

    // ========================================================================
    // Getters e Setters - Métodos de acesso para ler e modificar os atributos
    // ========================================================================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public String getTipoCozinha() {
        return tipoCozinha;
    }

    public void setTipoCozinha(String tipoCozinha) {
        this.tipoCozinha = tipoCozinha;
    }

    public String getHorarioFuncionamento() {
        return horarioFuncionamento;
    }

    public void setHorarioFuncionamento(String horarioFuncionamento) {
        this.horarioFuncionamento = horarioFuncionamento;
    }

    public Usuario getDono() {
        return dono;
    }

    public void setDono(Usuario dono) {
        this.dono = dono;
    }
}