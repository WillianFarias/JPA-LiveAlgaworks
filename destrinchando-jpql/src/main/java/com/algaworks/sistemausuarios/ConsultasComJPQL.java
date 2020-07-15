package com.algaworks.sistemausuarios;

import com.algaworks.sistemausuarios.dto.UsuarioDTO;
import com.algaworks.sistemausuarios.model.Configuracao;
import com.algaworks.sistemausuarios.model.Dominio;
import com.algaworks.sistemausuarios.model.Usuario;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

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
        //fazendoLeftJoins(entityManager);
        //carregamentoComJoinFetch(entityManager);
        //filtrandoRegistros(entityManager);
        //utilizandoOperadoresLogicos(entityManager);
        //utilizandoOperadorIn(entityManager);
        ordenandoResultados(entityManager);
        //paginandoResultados(entityManager);


        entityManager.close();
        entityManagerFactory.close();
    }

    //Paginacao
    public static void paginandoResultados(EntityManager entityManager){
        String jpql = "SELECT u FROM Usuario u";
        TypedQuery<Usuario> typedQuery = entityManager.createQuery(jpql, Usuario.class)
                //De onde voc vai começar a buscar seus resultados
                .setFirstResult(0)
                //Máximo de resultado por página que sera exibido
                .setMaxResults(2);
        List<Usuario> list = typedQuery.getResultList();
        list.forEach(u -> System.out.println(u.getId() + ", " + u.getNome() + ", " + u.getDominio().getNome()));
    }


    //Ordenacao ORDER BY
    public static void ordenandoResultados(EntityManager entityManager){

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        CriteriaQuery<Usuario> criteriaQuery = criteriaBuilder.createQuery(Usuario.class);
        Root<Usuario> root = criteriaQuery.from(Usuario.class);

        criteriaQuery.select(root);
        criteriaQuery.orderBy(criteriaBuilder.asc(root.get("nome")));

        TypedQuery<Usuario> typedQuery = entityManager.createQuery(criteriaQuery);
        List<Usuario> lista = typedQuery.getResultList();
        lista.forEach(u -> System.out.println(u.getId() + ", " + u.getNome()));

        /*String jpql = "SELECT u FROM Usuario u ORDER BY u.nome";
        TypedQuery<Usuario> typedQuery = entityManager.createQuery(jpql, Usuario.class);
        List<Usuario> list = typedQuery.getResultList();
        list.forEach(u -> System.out.println(u.getId() + ", " + u.getNome() + ", " + u.getDominio().getNome()));*/
    }

    public static void utilizandoOperadorIn(EntityManager entityManager){
        //esta contido
        String jpql = "SELECT u FROM Usuario u WHERE u.id in (:ids)";
        TypedQuery<Usuario> typedQuery = entityManager.createQuery(jpql, Usuario.class)
                .setParameter("ids", Arrays.asList(1, 2));
        List<Usuario> list = typedQuery.getResultList();
        list.forEach(u -> System.out.println(u.getId() + ", " + u.getNome()));
    }

    public static void utilizandoOperadoresLogicos(EntityManager entityManager){
        //&&=and ||=or
        String jpql = "SELECT u FROM Usuario u WHERE " +
                "u.ultimoAcesso > :ontem and u.ultimoAcesso < :hoje" +
                "or u.ultimoAcesso";
        TypedQuery<Usuario> typedQuery = entityManager.createQuery(jpql, Usuario.class)
                .setParameter("ontem", LocalDateTime.now().minusDays(1))
                .setParameter("hoje", LocalDateTime.now());
        List<Usuario> list = typedQuery.getResultList();
        list.forEach(u -> System.out.println(u.getId() + " ," + u.getNome()));
    }

    public static void filtrandoRegistros(EntityManager entityManager){
        //LIKE, IS NULL, IS EMPTY, BETWEEN, >, <, >=, <=, <>

        // LIKE = select u from Usuario u where u.nome like concat(:nomeUsuario, '%')
        // IS NULL = select u from Usuario u where u.senha is null
        // IS EMPTY = select d from Dominio d where d.usuarios is empty
        //String jpql = "SELECT u FROM Usuario u WHERE u.nome like :nomeUsuario)";
        //.setParameter("nomeUsuario", "Cal%");

        String jpql = "SELECT u FROM Usuario u WHERE u.ultimoAcesso between :ontem and :hoje";
        TypedQuery<Usuario> typedQuery = entityManager.createQuery(jpql, Usuario.class)
                .setParameter("ontem", LocalDateTime.now().minusDays(1))
                .setParameter("hoje", LocalDateTime.now());
        List<Usuario> list = typedQuery.getResultList();
        list.forEach(u -> System.out.println(u.getId() + ", " + u.getNome() + ", " + u.getDominio().getNome()));
    }


    //Join Fetch
    public static void carregamentoComJoinFetch(EntityManager entityManager){
        //retorna todos os usuarios, nao levando em consideracao se eles possuem configuracao ou dominio por exemplo
        //String jpql = "SELECT u FROM Usuario u";
        //utilizando o JOIN FETCH é feita apenas a consulta que realmente é passada no jpql
        //é feita quando eu realmente preciso do dominio e da configuracao, ou seja só me retorna os usuarios
        //que possuem ambas
        String jpql = "SELECT u FROM Usuario u JOIN FETCH u.configuracao c JOIN FETCH u.dominio d";
        TypedQuery<Usuario> typedQuery = entityManager.createQuery(jpql, Usuario.class);
        List<Usuario> list = typedQuery.getResultList();
        //Maps id é o mesmo do usuario, por isso ele faz a mesma requisicao de configuracao varias vezes por ser null
        list.forEach(u -> System.out.println(u.getId() + ", " + u.getNome() + ", " + u.getDominio().getNome()));
    }

    //Joins
    public static void fazendoLeftJoins(EntityManager entityManager){
        //sera retornado uma lista, indice 0 usuario e indice 1 configuracao
        //Tras todos os usuarios que possuem correspondencia na tabela configuracao e os que tbm nao possuem
        //Projecao
        String jpql = "SELECT u, c FROM Usuario u left join u.configuracao c";
        TypedQuery<Object[]> typedQuery = entityManager.createQuery(jpql, Object[].class);
        List<Object[]> list = typedQuery.getResultList();

        list.forEach(arr -> {
            //arr[0] usuario
            //arr[1] configuracao

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

    //Projecoes
    public static void passandoParametros(EntityManager entityManager){

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        CriteriaQuery<Usuario> criteriaQuery = criteriaBuilder.createQuery(Usuario.class);
        Root<Usuario> root = criteriaQuery.from(Usuario.class);

        criteriaQuery.select(root);
        criteriaQuery.where(criteriaBuilder.equal(root.get("login"), "ria"));

        TypedQuery<Usuario> typedQuery = entityManager.createQuery(criteriaQuery);
        Usuario usuario = typedQuery.getSingleResult();
        System.out.println(usuario.getId() + ", " + usuario.getNome());


        /*
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
        */
    }

    public static void fazendoProjecoes(EntityManager entityManager){

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        CriteriaQuery<UsuarioDTO> criteriaQuery = criteriaBuilder.createQuery(UsuarioDTO.class);
        Root<Usuario> root = criteriaQuery.from(Usuario.class);

        criteriaQuery.select(criteriaBuilder.construct(UsuarioDTO.class,
                root.get("id"), root.get("login"), root.get("nome")));

        TypedQuery<UsuarioDTO> typedQuery = entityManager.createQuery(criteriaQuery);
        List<UsuarioDTO> listDTO = typedQuery.getResultList();
        listDTO.forEach(u -> System.out.println("DTO " + u.getId() + ", " + u.getNome()));

        /*Utilizando multiselect
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        CriteriaQuery<Object[]> criteriaQuery = criteriaBuilder.createQuery(Object[].class);
        Root<Usuario> root = criteriaQuery.from(Usuario.class);

        criteriaQuery.multiselect(root.get("id"), root.get("login"), root.get("nome"));

        TypedQuery<Object[]> typedQuery = entityManager.createQuery(criteriaQuery);
        List<Object[]> lista = typedQuery.getResultList();
        lista.forEach(arr -> System.out.println(String.format("%s, %s, %s", arr)));*/

        /*JPQL
        String jpqlArr = "SELECT id, login, nome FROM Usuario";
        TypedQuery<Object[]> typedQueryArr = entityManager.createQuery(jpqlArr, Object[].class);
        List<Object[]> listArr = typedQueryArr.getResultList();
        listArr.forEach(arr -> System.out.println(String.format("%s, %s, %s", arr)));


        String jpqlDTO = "SELECT new com.algaworks.sistemausuarios.dto.UsuarioDTO(id, login, nome) FROM Usuario";
        TypedQuery<UsuarioDTO> typedQueryDTO = entityManager.createQuery(jpqlDTO, UsuarioDTO.class);
        List<UsuarioDTO> listDTO = typedQueryDTO.getResultList();
        listDTO.forEach(u -> System.out.println("DTO " + u.getId() + ", " + u.getNome()));
        */
    }

    public static void escolhendoRetorno(EntityManager entityManager){

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Dominio> criteriaQuery = criteriaBuilder.createQuery(Dominio.class);
        Root<Usuario> root = criteriaQuery.from(Usuario.class);
        criteriaQuery.select(root.get("dominio"));

        TypedQuery<Dominio> typedQuery = entityManager.createQuery(criteriaQuery);
        List<Dominio> lista = typedQuery.getResultList();
        lista.forEach(u -> System.out.println(u.getId() + ", " + u.getNome()));


        /*
        //Consultando atributo de referencia
        String jpql = "SELECT u.dominio FROM Usuario u WHERE u.id = 1";
        TypedQuery<Dominio> typedQuery = entityManager.createQuery(jpql, Dominio.class);
        Dominio dominio = typedQuery.getSingleResult();
        System.out.println(dominio.getId() + ", " + dominio.getNome());


        //Retornando um tipo primitivo ao inves de um Objeto
        String jpqlNome = "SELECT u.nome FROM Usuario u";
        TypedQuery<String> typedQueryNome = entityManager.createQuery(jpqlNome, String.class);
        List<String> listaNome = typedQueryNome.getResultList();
        listaNome.forEach(nome -> System.out.println(nome));*/

    }

    //Consultas simples
    public static void primeirasConsultas(EntityManager entityManager){
        //entidades raizes para poder montar a consulta
        //utilizado maioria das vezes no FROM, WHERE, filtros, ordenacoes
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        //possui metodos equivalentes a clausulas SQL, select etc
        CriteriaQuery<Usuario> criteriaQuery = criteriaBuilder.createQuery(Usuario.class);
        //Equivalente ao apelido em SQL
        Root<Usuario> root = criteriaQuery.from(Usuario.class);
        criteriaQuery.select(root);

        TypedQuery<Usuario> typedQuery = entityManager.createQuery(criteriaQuery);
        List<Usuario> lista = typedQuery.getResultList();
        lista.forEach(u -> System.out.println(u.getId() + ", " + u.getNome()));


        /*
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
        System.out.println(usuario2.getId() + ", " + usuario2.getNome());*/
    }
}
