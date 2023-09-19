package banking.api.controller;

import banking.api.model.LoginResponse;
import banking.api.model.SignupCustomerRequest;
import banking.api.model.SignupCustomerResponse;
import bankingservice.service.business.AccountService;
import bankingservice.service.business.CustomerService;
import bankingservice.service.entity.AccountsEntity;
import bankingservice.service.entity.CustomerAuthEntity;
import bankingservice.service.entity.PersonEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@Controller
@CrossOrigin
public class PersonController {
    @Autowired
    CustomerService customerService;

    @Autowired
    AccountService accountService;

    @RequestMapping(method = RequestMethod.POST, path = "/signup", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SignupCustomerResponse> signup(@RequestBody(required = false) final SignupCustomerRequest signupCustomerRequest) {
        final PersonEntity customerEntity = new PersonEntity();

        customerEntity.setUuid(UUID.randomUUID().toString());
        customerEntity.setFirstName(signupCustomerRequest.getFirstName());
        customerEntity.setLastName(signupCustomerRequest.getLastName());
        customerEntity.setContactNumber(signupCustomerRequest.getContactNumber());
        customerEntity.setEmail(signupCustomerRequest.getEmailAddress());
        customerEntity.setPassword(signupCustomerRequest.getPassword());

        AccountsEntity accountEntity = new AccountsEntity();
        accountEntity.setBalance(0);
        accountEntity.setAccountNumber(UUID.randomUUID().toString());

        final PersonEntity createdCustomerEntity = customerService.saveCustomer(customerEntity);
        accountEntity.setPersonEntity(createdCustomerEntity);
        accountService.createAccount(accountEntity);
        SignupCustomerResponse customerResponse = new SignupCustomerResponse().id(accountEntity.getAccountNumber()).status("CUSTOMER SUCCESSFULLY REGISTERED");
        return new ResponseEntity(customerResponse, HttpStatus.OK);
    }


    @RequestMapping(method = RequestMethod.POST, path = "/login", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<LoginResponse> login(@RequestHeader("authorization") final String authorization) throws Exception {

        byte[] decode = Base64.getDecoder().decode(authorization.split("Basic ")[1]);
        String decodedText = new String(decode);
        String[] decodedArray = decodedText.split(":");
        if (decodedText.matches(":") == Boolean.TRUE) throw new Exception();
        CustomerAuthEntity createdCustomerAuthEntity = customerService.authenticate(decodedArray[0], decodedArray[1]);
        PersonEntity customerEntity = createdCustomerAuthEntity.getCustomer();
        LoginResponse loginResponse = new LoginResponse().id(customerEntity.getUuid()).message("LOGGED IN SUCCESSFULLY").firstName(customerEntity.getFirstName()).lastName(customerEntity.getLastName()).contactNumber(customerEntity.getContactNumber()).emailAddress(customerEntity.getEmail());

        HttpHeaders headers = new HttpHeaders();
        headers.add("access-token", createdCustomerAuthEntity.getAccessToken());
        List<String> header = new ArrayList<>();
        header.add("access-token");
        headers.setAccessControlExposeHeaders(header);
        return new ResponseEntity<LoginResponse>(loginResponse, headers, HttpStatus.OK);
    }
}
