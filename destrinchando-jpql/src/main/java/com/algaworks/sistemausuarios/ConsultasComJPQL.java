package com.algaworks.sistemausuarios;

import com.algaworks.sistemausuarios.dto.UsuarioDTO;
import com.algaworks.sistemausuarios.model.Configuracao;
import com.algaworks.sistemausuarios.model.Dominio;
import com.algaworks.sistemausuarios.model.Usuario;

import javax.persistence.*;
import java.security.DomainCombiner;
import java.util.List;
import java.util.SplittableRandom;

public class ConsultasComJPQL {

    public static void main(String[] args) {

        //Criando gerenciador
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("Usuarios-PU");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        //primeirasConsultas(entityManager);
        //escolhendoRetorno(entityManager);
        //fazendoProjecoes(entityManager);
        //passandoParametros(entityManager);
        //fazendoJoins(entityManager);
        fazendoLeftJoins(entityManager);


        entityManager.close();
        entityManagerFactory.close();
    }

    //Joins
    public static void fazendoLeftJoins(EntityManager entityManager){
        //sera retornado uma lista, indice 0 usuario e indice 1 configuracao
        //Tras todos os usuarios que possuem correspondencia na tabela configuracao e os que tbm nao possuem
        String jpql = "SELECT u, c FROM Usuario u left join u.configuracao c";
        TypedQuery<Object[]> typedQuery = entityManager.createQuery(jpql, Object[].class);
        List<Object[]> list = typedQuery.getResultList();

        list.forEach(arr -> {
            String out = ((Usuario) arr[0]).getNome();
            if (arr[1] == null){
                out += ", NULL";
            }else{
                out += ", " + ((Configuracao) arr[1]).getId();
            }
            System.out.println(out);
        });
    }

    public static void fazendoJoins(EntityManager entityManager){
        String jpql = "SELECT u FROM Usuario u join u.dominio d WHERE d.id = 1";
        TypedQuery<Usuario> typedQuery = entityManager.createQuery(jpql, Usuario.class);
        List<Usuario> list = typedQuery.getResultList();
        list.forEach(u -> System.out.println(u.getId() + ", " + u.getNome()));
    }


    public static void passandoParametros(EntityManager entityManager){
        String jpql = "SELECT u FROM Usuario u WHERE u.id = :idUsuario";
        TypedQuery<Usuario> typedQuery = entityManager.createQuery(jpql, Usuario.class);
        //id do usuario que eu quero buscar "idUsuario"
        typedQuery.setParameter("idUsuario", 1);
        Usuario usuario = typedQuery.getSingleResult();
        System.out.println(usuario.getId() + ", " + usuario.getNome());


        String jpqlLog = "SELECT u FROM Usuario u WHERE u.login = :loginUsuario";
        TypedQuery<Usuario> typedQueryLogin = entityManager.
                createQuery(jpqlLog, Usuario.class)
                .setParameter("loginUsuario", "ria");
        Usuario usuarioLog = typedQueryLogin.getSingleResult();
        System.out.println(usuarioLog.getId() + ", " + usuarioLog.getNome());
    }

    public static void fazendoProjecoes(EntityManager entityManager){
        String jpqlArr = "SELECT id, login, nome FROM Usuario";
        TypedQuery<Object[]> typedQueryArr = entityManager.createQuery(jpqlArr, Object[].class);
        List<Object[]> listArr = typedQueryArr.getResultList();
        listArr.forEach(arr -> System.out.println(String.format("%s, %s, %s", arr)));


        String jpqlDTO = "SELECT new com.algaworks.sistemausuarios.dto.UsuarioDTO(id, login, nome) FROM Usuario";
        TypedQuery<UsuarioDTO> typedQueryDTO = entityManager.createQuery(jpqlDTO, UsuarioDTO.class);
        List<UsuarioDTO> listDTO = typedQueryDTO.getResultList();
        listDTO.forEach(u -> System.out.println("DTO " + u.getId() + ", " + u.getNome()));
    }

    public static void escolhendoRetorno(EntityManager entityManager){
        //Consultando atributo de referencia
        String jpql = "SELECT u.dominio FROM Usuario u WHERE u.id = 1";
        TypedQuery<Dominio> typedQuery = entityManager.createQuery(jpql, Dominio.class);
        Dominio dominio = typedQuery.getSingleResult();
        System.out.println(dominio.getId() + ", " + dominio.getNome());


        //Retornando um tipo primitivo ao inves de um Objeto
        String jpqlNome = "SELECT u.nome FROM Usuario u";
        TypedQuery<String> typedQueryNome = entityManager.createQuery(jpqlNome, String.class);
        List<String> listaNome = typedQueryNome.getResultList();
        listaNome.forEach(nome -> System.out.println(nome));

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
