package banking.api.controller;

import banking.api.model.StatementList;
import banking.api.model.StatementResponse;
import bankingservice.service.business.AccountService;
import bankingservice.service.business.CustomerService;
import bankingservice.service.business.StatementService;
import bankingservice.service.entity.AccountsEntity;
import bankingservice.service.entity.PersonEntity;
import bankingservice.service.entity.StatementsEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@CrossOrigin
public class StatementController {

    @Autowired
    StatementService statementService;
    @Autowired
    CustomerService customerService;
    @Autowired
    AccountService accountService;

    @RequestMapping(method = RequestMethod.GET, path = "/statement", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<StatementResponse> getStatement(@RequestParam(required = true) String accountNumber, @RequestHeader("authorization") final String authorization) throws Exception {
        String access_token = authorization.split("Bearer ")[1];
        PersonEntity customerEntity = customerService.getCustomer(access_token);
        List<AccountsEntity> accountsList = new ArrayList<>();
        accountsList = accountService.getAccounts(customerEntity);
        boolean flag = false;
        for (AccountsEntity a : accountsList) {
            if (a.getAccountNumber().equals(accountNumber)) {
                flag = true;
                break;
            }
        }
        if (!flag) {
            throw new Exception();
        }

        List<StatementsEntity> list = new ArrayList<>();
        list = statementService.getStatement(accountNumber);
        StatementResponse statementResponse = new StatementResponse();
        ArrayList<StatementList> list1 = new ArrayList<>();
        for (StatementsEntity s : list) {
            StatementList statementList = new StatementList();
            statementList.setBalance(s.getBalance());
            statementList.setStatus("Success");
            statementList.setCredit(s.getCredit());
            statementList.setDate(s.getDate().toString());
            statementList.setDebit(s.getDebit());
            statementList.setReferenceNumber(s.getReferenceNumber());
            list1.add(statementList);
        }
        statementResponse.setStatement(list1);
        return new ResponseEntity<>(statementResponse, HttpStatus.OK);

    }
}
