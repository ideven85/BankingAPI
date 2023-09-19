package banking.api.controller;

import banking.api.model.*;
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
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Controller
@CrossOrigin
public class AccountController {
    @Autowired
    AccountService accountService;
    @Autowired
    StatementService statementService;
    @Autowired
    CustomerService customerService;


    @RequestMapping(method = RequestMethod.PUT, path = "/credit", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AccountResponse> deposit(@RequestBody(required = true) AccountRequest accountRequest, @RequestHeader("authorization") final String authorization) throws Exception {
        String access_token = authorization.split("Bearer ")[1];
        PersonEntity customerEntity = customerService.getCustomer(access_token);
        String accountNumber = accountRequest.getAccountNumber();
        AccountsEntity entity = accountService.getAmount(accountNumber);
        if (accountRequest.getAmount() <= 0) {
            throw new Exception();
        }
        int totalAmount = accountRequest.getAmount() + entity.getBalance();
        entity.setBalance(totalAmount);
        accountService.creditAmount(entity);


        StatementsEntity statementEntity = new StatementsEntity();
        statementEntity.setAccountsEntity(entity);
        statementEntity.setBalance(totalAmount);
        statementEntity.setReferenceNumber(UUID.randomUUID().toString());
        statementEntity.setCredit(accountRequest.getAmount());
        statementEntity.setDebit(0);
        statementEntity.setDate(new Date());
        statementService.addStatement(statementEntity);


        AccountResponse accountResponse = new AccountResponse();
        accountResponse.setId(entity.getId().toString());
        accountResponse.setStatus("Amount credited");
        return new ResponseEntity<>(accountResponse, HttpStatus.OK);
    }


    @RequestMapping(method = RequestMethod.PUT, path = "/debit", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AccountResponse> withdraw(@RequestBody(required = true) AccountRequest accountRequest, @RequestHeader("authorization") final String authorization) throws Exception {
        String access_token = authorization.split("Bearer ")[1];
        PersonEntity customerEntity = customerService.getCustomer(access_token);
        String accountNumber = accountRequest.getAccountNumber();
        AccountsEntity entity = accountService.getAmount(accountNumber);
        if (accountRequest.getAmount() <= 0) {
            throw new Exception();
        }
        int totalAmount = entity.getBalance() - accountRequest.getAmount();
        if (totalAmount < 0) {
            throw new Exception();
        }
        entity.setBalance(totalAmount);
        accountService.creditAmount(entity);


        StatementsEntity statementEntity = new StatementsEntity();
        statementEntity.setAccountsEntity(entity);
        statementEntity.setBalance(totalAmount);
        statementEntity.setReferenceNumber(UUID.randomUUID().toString());
        statementEntity.setDebit(accountRequest.getAmount());
        statementEntity.setCredit(0);
        statementEntity.setDate(new Date());
        statementService.addStatement(statementEntity);


        AccountResponse accountResponse = new AccountResponse();
        accountResponse.setId(entity.getId().toString());
        accountResponse.setStatus("Amount debited");
        return new ResponseEntity<>(accountResponse, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.PUT, path = "/transfer", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AmountResponse> transfer(@RequestBody(required = true) AmountRequest amountRequest, @RequestHeader("authorization") final String authorization) throws Exception {
        String access_token = authorization.split("Bearer ")[1];
        PersonEntity customerEntity = customerService.getCustomer(access_token);
        AccountRequest accountRequest = new AccountRequest();
        accountRequest.setAccountNumber(amountRequest.getFromAccountNumber());
        accountRequest.setAmount(amountRequest.getAmount());
        withdraw(accountRequest, authorization);

        String accountNumber = amountRequest.getToAccountNumber();
        accountRequest.setAccountNumber(accountNumber);
        accountRequest.setAmount(amountRequest.getAmount());
        deposit(accountRequest, authorization);

        AmountResponse amountResponse = new AmountResponse();
        amountResponse.setId(amountRequest.getFromAccountNumber());
        amountResponse.setStatus("Amount Successfully transferred");
        return new ResponseEntity<>(amountResponse, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/addAccount", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AccountResponse> addAccount(@RequestHeader("authorization") final String authorization) throws Exception {
        String access_token = authorization.split("Bearer ")[1];
        PersonEntity customerEntity = customerService.getCustomer(access_token);
        AccountsEntity accountsEntity = new AccountsEntity();
        accountsEntity.setPersonEntity(customerEntity);
        accountsEntity.setAccountNumber(UUID.randomUUID().toString());
        accountsEntity.setBalance(0);
        accountService.createAccount(accountsEntity);
        AccountResponse accountResponse = new AccountResponse();
        accountResponse.setId(accountsEntity.getAccountNumber());
        accountResponse.setStatus("Account Created");
        return new ResponseEntity<>(accountResponse, HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/getAccount", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<GetAccountResponse> getAccounts(@RequestHeader("authorization") final String authorization) throws Exception {
        String access_token = authorization.split("Bearer ")[1];
        PersonEntity customerEntity = customerService.getCustomer(access_token);
        List<AccountsEntity> list;
        list = accountService.getAccounts(customerEntity);
        List<AccountList> l2 = new ArrayList<>();
        for (AccountsEntity entity : list) {
            AccountList l = new AccountList();
            l.setAccountNumber(entity.getAccountNumber());
            l.setBalance(entity.getBalance());
            l.setStatus("Active");
            l2.add(l);
        }
        GetAccountResponse getAccountResponse = new GetAccountResponse();
        getAccountResponse.setAccountList(l2);

        return new ResponseEntity<>(getAccountResponse, HttpStatus.OK);
    }


}
