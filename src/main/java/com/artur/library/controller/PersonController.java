package com.artur.library.controller;

import com.artur.library.dao.PersonDAO;
import com.artur.library.model.Person;
import com.artur.library.util.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/people")
public class PersonController {

    private final PersonDAO personDAO;
    private final Validator validator;

    @Autowired
    public PersonController(PersonDAO personDAO, Validator validator) {
        this.personDAO = personDAO;
        this.validator = validator;
    }

    @GetMapping()
    public String index(Model model) {
        model.addAttribute("people", personDAO.people());
        return "people/index";
    }

    @GetMapping("/{id}")
    public String show(Model model, @PathVariable int id) {
        model.addAttribute("person", personDAO.readPersonById(id));
        model.addAttribute("ownBooks", personDAO.getBooksByPersonId(id));
        return "people/show";
    }

    @GetMapping("/new")
    public String newPerson(@ModelAttribute("person") Person person) {
        return "people/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult) {
        validator.validate(person,bindingResult);

        if(bindingResult.hasErrors())
            return "people/new";
        personDAO.createPerson(person);

        return "redirect:/people";
    }

    @GetMapping("/{id}/edit")
    public String editPage(Model model, @PathVariable int id) {
        model.addAttribute("person", personDAO.readPersonById(id));
        return "people/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult, @PathVariable int id) {
        if(bindingResult.hasErrors())
            return "people/edit";

        personDAO.updatePerson(id, person);
        return "redirect:/people";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable int id) {
        personDAO.deletePerson(id);
        return "redirect:/people";
    }
}
