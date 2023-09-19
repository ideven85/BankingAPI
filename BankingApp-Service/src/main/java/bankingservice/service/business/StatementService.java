package bankingservice.service.business;

import bankingservice.service.dao.StatementDao;
import bankingservice.service.entity.AccountsEntity;
import bankingservice.service.entity.StatementsEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StatementService {

    @Autowired
    StatementDao statementDao;

    @Autowired
    AccountService accountService;

    @Transactional(propagation = Propagation.REQUIRED)
    public void addStatement(StatementsEntity entity) {
        statementDao.addStatement(entity);
    }

    public List<StatementsEntity> getStatement(String number) {
        AccountsEntity accountsEntity = accountService.getAmount(number);
        return statementDao.getStatement(accountsEntity);
    }
}
