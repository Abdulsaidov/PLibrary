package com.artur.library.util;

import com.artur.library.model.Person;
import com.artur.library.service.PeopleService;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class Validator implements org.springframework.validation.Validator {
    private final PeopleService peopleService;

    public Validator(PeopleService peopleService) {
        this.peopleService = peopleService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
    Person person = (Person) target;

    if(peopleService.getPersonByFullName(person.getFullName()).isPresent())
        errors.rejectValue("fullName","","Already exist");
    }
}
