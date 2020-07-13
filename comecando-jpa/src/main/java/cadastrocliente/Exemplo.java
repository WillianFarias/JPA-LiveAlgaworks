package cadastrocliente;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class Exemplo {
    public static void main(String[] args) {

        //Fabrica de persistencia, recebe as configuracoes de Clientes-PU
        EntityManagerFactory entityManagerFactory = Persistence.
                createEntityManagerFactory("Clientes-PU");

        //Gerente de entidades, abrindo conexao com o bd
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        /*
        * Busca de cliente
        Cliente cliente = entityManager.find(Cliente.class, 1);
        System.out.println(cliente.getNome());
        */



        //* Cadastro de cliente
        /*Cliente cliente = new Cliente();
        //cliente.setId(2);
        cliente.setNome("Eletrônica Sousa");

        //É necessário iniciar uma transacao para persistir algo no banco
        //Transaction garante que a insercao sera feita, pois muita das vezes existem dependencias
        //caso de alguma falha é possível utilizar o rollback
        entityManager.getTransaction().begin();

        //nao existe um momento exato para este método ser executado
        entityManager.persist(cliente);

        //entityManager.flush();//forca o envio para o bd
        entityManager.getTransaction().commit();*/



        //* Remocao de cliente
        //Só é possível remover algo do banco que já está na memória do entityManager
        /*Cliente clienteRemovido = entityManager.find(Cliente.class, 4);

        entityManager.getTransaction().begin();
        entityManager.remove(clienteRemovido);
        entityManager.getTransaction().commit();

        //fechando instancias
        //Web o entityManager inicia e finaliza quando se inica e se finaliza uma requisicao
        */

        //Atualizar
        /* primeira forma
        Cliente cliente = entityManager.find(Cliente.class, 2);

        entityManager.getTransaction().begin();
        cliente.setNome(cliente.getNome() + "Atualizado");
        entityManager.getTransaction().commit();*/

        Cliente cliente = new Cliente();
        cliente.setId(2);
        cliente.setNome("Livraria JAVA");

        entityManager.getTransaction().begin();
        //passou id vai atualizar existente, nao possou vai inserir novo elemento no bd
        entityManager.merge(cliente);
        entityManager.getTransaction().commit();

        entityManager.close();
        entityManagerFactory.close();
    }
}
