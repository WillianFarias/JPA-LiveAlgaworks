package com.algaworks.sistemafuncionario.model;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
public class Funcionario {

    @Id
    private Integer id;

    //toda vez que funcionário for atualizado versao será incrementado em 1
    //toda vez que for feita qlquer mudança em funcionário será verificado este campo, para só depois ser atualizado
    //caso tudo estiver o.k
    @Version
    private Integer versao;

    private String nome;

    private Integer bancoDeHoras;

    private BigDecimal salario;

    private BigDecimal valorHoraExtra;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getVersao() {
        return versao;
    }

    public void setVersao(Integer versao) {
        this.versao = versao;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getBancoDeHoras() {
        return bancoDeHoras;
    }

    public void setBancoDeHoras(Integer bancoDeHoras) {
        this.bancoDeHoras = bancoDeHoras;
    }

    public BigDecimal getSalario() {
        return salario;
    }

    public void setSalario(BigDecimal salario) {
        this.salario = salario;
    }

    public BigDecimal getValorHoraExtra() {
        return valorHoraExtra;
    }

    public void setValorHoraExtra(BigDecimal valorHoraExtra) {
        this.valorHoraExtra = valorHoraExtra;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Funcionario usuario = (Funcionario) o;

        return id.equals(usuario.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}