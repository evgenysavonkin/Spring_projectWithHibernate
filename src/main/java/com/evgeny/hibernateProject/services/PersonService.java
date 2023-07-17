package com.evgeny.hibernateProject.services;

import com.evgeny.hibernateProject.models.Book;
import com.evgeny.hibernateProject.models.Person;
import com.evgeny.hibernateProject.repositories.PersonRepository;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class PersonService {

    private final PersonRepository personRepository;

    private final EntityManagerFactory entityManagerFactory;

    @Autowired
    public PersonService(PersonRepository personRepository, EntityManagerFactory entityManagerFactory) {
        this.personRepository = personRepository;
        this.entityManagerFactory = entityManagerFactory;
    }

    public List<Person> findAll() {
        return personRepository.findAll();
    }

    @Transactional
    public void save(Person person) {
        personRepository.save(person);
    }

    public Person findById(int id) {
        return personRepository.findById(id).orElse(null);
    }

    public List<Book> findByIdAndGetBooks(int id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Session session = entityManager.unwrap(Session.class);
        session.beginTransaction();
        List<Book> books = session.createQuery("SELECT b FROM Book b LEFT JOIN FETCH b.owner " +
                "WHERE b.owner.id = :id").setParameter("id", id).getResultList();
        session.getTransaction().commit();
        return books;
    }

    @Transactional
    public void updatePerson(Person person) {
        personRepository.save(person);
    }

    @Transactional
    public void deleteById(int id) {
        personRepository.deleteById(id);
    }


}
