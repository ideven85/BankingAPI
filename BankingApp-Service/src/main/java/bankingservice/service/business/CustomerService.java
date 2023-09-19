package bankingservice.service.business;

import bankingservice.service.dao.CustomerDao;
import bankingservice.service.entity.CustomerAuthEntity;
import bankingservice.service.entity.PersonEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.UUID;

@Service
public class CustomerService {

    @Autowired
    CustomerDao customerDao;
    @Autowired
    PasswordCryptographyProvider passwordCryptographyProvider;

    @Transactional(propagation = Propagation.REQUIRED)
    public PersonEntity saveCustomer(PersonEntity customerEntity) {

        String[] encryptedText = passwordCryptographyProvider.encrypt(customerEntity.getPassword());
        customerEntity.setSalt(encryptedText[0]);
        customerEntity.setPassword(encryptedText[1]);
        return customerDao.saveCustomer(customerEntity);
    }


    @Transactional(propagation = Propagation.REQUIRED)
    public CustomerAuthEntity authenticate(String contactNumber, String password) throws Exception {

        PersonEntity customerEntity = customerDao.getCustomerByContactNumber(contactNumber);

        if (customerEntity == null) {
            throw new Exception();
        }

        final String encryptedPassword = PasswordCryptographyProvider.encrypt(password, customerEntity.getSalt());

        if (encryptedPassword.equals(customerEntity.getPassword())) {
            JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(encryptedPassword);
            CustomerAuthEntity customerAuthEntity = new CustomerAuthEntity();
            customerAuthEntity.setUuid(UUID.randomUUID().toString());
            customerAuthEntity.setCustomer(customerEntity);
            ZonedDateTime now = ZonedDateTime.now();
            ZonedDateTime expiresAt = now.plusHours(8);

            customerAuthEntity.setLoginAt(ZonedDateTime.now());
            customerAuthEntity.setExpiresAt(expiresAt);
            customerAuthEntity.setAccessToken(jwtTokenProvider.generateToken(customerEntity.getUuid(), now, expiresAt));

            return customerDao.createCustomerAuth(customerAuthEntity);
        } else {
            throw new Exception();
        }
    }

    public void authorization(String access_token) throws Exception {

        CustomerAuthEntity customerAuthEntity = customerDao.getCustomerAuthByAccesstoken(access_token);

        if (customerAuthEntity == null) {
            throw new Exception("Customer is not Logged in.");
        }

        if (customerAuthEntity.getLogoutAt() != null) {
            throw new Exception("Customer is logged out. Log in again to access this endpoint.");
        }

        if (ZonedDateTime.now().isAfter(customerAuthEntity.getExpiresAt())) {
            throw new Exception("Your session is expired. Log in again to access this endpoint.");
        }
    }

    public PersonEntity getCustomer(String access_token) throws Exception {

        authorization(access_token);
        CustomerAuthEntity customerAuthEntity = customerDao.getCustomerAuthByAccesstoken(access_token);
        return customerAuthEntity.getCustomer();
    }

}
