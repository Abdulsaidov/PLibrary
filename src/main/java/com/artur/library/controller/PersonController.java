package com.artur.library.controller;

import com.artur.library.model.Person;
import com.artur.library.service.PeopleService;
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

    private final PeopleService peopleService;
    private final Validator validator;

    @Autowired
    public PersonController( PeopleService peopleService, Validator validator) {
        this.peopleService = peopleService;
        this.validator = validator;
    }

    @GetMapping()
    public String index(Model model) {
        model.addAttribute("people", peopleService.people());
        return "people/index";
    }

    @GetMapping("/{id}")
    public String show(Model model, @PathVariable int id) {
        model.addAttribute("person", peopleService.readPersonById(id));
        model.addAttribute("ownBooks", peopleService.getBooksByPersonId(id));
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
        peopleService.createPerson(person);

        return "redirect:/people";
    }

    @GetMapping("/{id}/edit")
    public String editPage(Model model, @PathVariable int id) {
        model.addAttribute("person", peopleService.readPersonById(id));
        return "people/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult, @PathVariable int id) {
        if(bindingResult.hasErrors())
            return "people/edit";

        peopleService.updatePerson(id,person);
        return "redirect:/people";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable int id) {
        peopleService.deletePerson(id);
        return "redirect:/people";
    }
}
