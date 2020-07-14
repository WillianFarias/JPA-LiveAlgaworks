package com.algaworks.sistemausuarios;

import com.algaworks.sistemausuarios.model.Dominio;
import com.algaworks.sistemausuarios.model.Usuario;

import javax.persistence.*;
import java.util.List;

public class ConsultasComJPQL {

    public static void main(String[] args) {

        //Criando gerenciador
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("Usuarios-PU");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        //primeirasConsultas(entityManager);
        escolhendoRetorno(entityManager);


        entityManager.close();
        entityManagerFactory.close();
    }

    public static void escolhendoRetorno(EntityManager entityManager){
        String jpql = "SELECT u.dominio FROM Usuario u WHERE u.id = 1";
        TypedQuery<Dominio> typedQuery = entityManager.createQuery(jpql, Dominio.class);
        Dominio dominio = typedQuery.getSingleResult();
        System.out.println(dominio.getId() + ", " + dominio.getNome());
    }

    //Consultas simples
    public static void primeirasConsultas(EntityManager entityManager){

        //PESQUISA POR TODOS USUÁRIOS
        String jpql = "SELECT u FROM Usuario u";
        //Query tipada
        //primeiro parametro é passada uma string jpql e no segundo o que se espera receber com a consulta
        //neste caso um usuario
        TypedQuery<Usuario> typedQuery = entityManager.createQuery(jpql, Usuario.class);
        List<Usuario> lista = typedQuery.getResultList();
        lista.forEach(u -> System.out.println(u.getId() + ", " + u.getNome()));

        //CONSUTAR POR USUÁRIO ESPECIFICO
        String jpqlSing = "SELECT u FROM Usuario u WHERE u.id = 1";
        TypedQuery<Usuario> typedQuerySingle = entityManager.createQuery(jpqlSing, Usuario.class);
        Usuario usuario = typedQuerySingle.getSingleResult();
        System.out.println(usuario.getId() + ", " + usuario.getNome());

        //CONSUTAR POR USUÁRIO ESPECIFICO UTILIZANDO APENAS QUERY
        String jpqlSingCast = "SELECT u FROM Usuario u WHERE u.id = 2";
        Query querySingle = entityManager.createQuery(jpqlSingCast);
        Usuario usuario2 = (Usuario) querySingle.getSingleResult();
        System.out.println(usuario2.getId() + ", " + usuario2.getNome());
    }
}
