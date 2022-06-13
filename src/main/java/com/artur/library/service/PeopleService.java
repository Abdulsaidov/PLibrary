package com.artur.library.service;

import com.artur.library.model.Book;
import com.artur.library.model.Person;
import com.artur.library.repository.PeopleRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PeopleService {

    private final PeopleRepository peopleRepository;

    @Autowired
    public PeopleService(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    public List<Person> people() {
       return peopleRepository.findAll();
    }

    public Person readPersonById(int id) {
        return peopleRepository.findById(id).orElse(null);
    }

    @Transactional
    public void createPerson(Person person) {
        peopleRepository.save(person);
    }

    @Transactional
    public void updatePerson( int id, Person updatedPerson) {
        updatedPerson.setId(id);
        peopleRepository.save(updatedPerson);
    }

    @Transactional
    public void deletePerson(int id) {
        peopleRepository.deleteById(id);
    }

    public Optional<Person> getPersonByFullName(String fullName) {
        return peopleRepository.findByFullName(fullName);
    }

    public List<Book> getBooksByPersonId(int id) {
       Person owner = peopleRepository.findById(id).orElse(null);
        if (owner != null){
            Hibernate.initialize(owner.getBooks());
            owner.getBooks().forEach(book ->
            {
                long diffinMillies = Math.abs(book.getTakenAt().getTime() - new Date().getTime());
                if (diffinMillies > 864000000)
                    book.setExpired(true);
            });
            return owner.getBooks();
        }
       else {
           return Collections.emptyList();
        }
    }
}
