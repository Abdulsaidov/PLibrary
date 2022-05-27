package com.artur.library.util;

import com.artur.library.dao.PersonDAO;
import com.artur.library.model.Person;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class Validator implements org.springframework.validation.Validator {
    private final PersonDAO personDAO;

    public Validator(PersonDAO personDAO) {
        this.personDAO = personDAO;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
    Person person = (Person) target;

    if(personDAO.getPersonByFullName(person.getFullName()).isPresent())
        errors.rejectValue("fullName","","Already exist");
    }
}
