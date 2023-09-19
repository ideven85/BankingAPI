package bankingservice.service.dao;

import bankingservice.service.entity.AccountsEntity;
import bankingservice.service.entity.StatementsEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

@Repository
public class StatementDao {

    @PersistenceContext
    EntityManager entityManager;


    public void addStatement(StatementsEntity statementEntity) {
        entityManager.persist(statementEntity);
    }


    public List<StatementsEntity> getStatement(AccountsEntity aid) {

        List<StatementsEntity> list = new ArrayList<>();
        list = entityManager.createNamedQuery("getStatements", StatementsEntity.class).setParameter("aid", aid).getResultList();
        return list;
    }
}
