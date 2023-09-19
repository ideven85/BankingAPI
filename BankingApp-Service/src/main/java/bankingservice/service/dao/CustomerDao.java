package bankingservice.service.dao;

import bankingservice.service.entity.CustomerAuthEntity;
import bankingservice.service.entity.PersonEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class CustomerDao {
    @PersistenceContext
    private EntityManager entityManager;

    public PersonEntity saveCustomer(PersonEntity customer) {
        entityManager.persist(customer);
        return customer;
    }


    public CustomerAuthEntity createCustomerAuth(CustomerAuthEntity customerAuthEntity) {
        entityManager.persist(customerAuthEntity);
        return customerAuthEntity;
    }


    public PersonEntity getCustomerByContactNumber(String contactNumber) {
        try {
            return entityManager.createNamedQuery("customerByContactNumber", PersonEntity.class).setParameter("contactNumber", contactNumber).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    public CustomerAuthEntity getCustomerAuthByAccesstoken(String accesstoken) {
        try {
            return entityManager.createNamedQuery("customerAuthByAccesstoken", CustomerAuthEntity.class).setParameter("accesstoken", accesstoken).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

}
