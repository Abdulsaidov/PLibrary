package com.artur.library.controller;

import com.artur.library.dao.BookDAO;
import com.artur.library.dao.PersonDAO;
import com.artur.library.model.Book;
import com.artur.library.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/books")
public class BookController {

    private final BookDAO bookDAO;
    private final PersonDAO personDAO;

    @Autowired
    public BookController(BookDAO bookDAO, PersonDAO personDAO) {
        this.bookDAO = bookDAO;
        this.personDAO = personDAO;
    }

    @GetMapping()
    public String index(Model model) {
        model.addAttribute("books", bookDAO.books());
        return "books/index";
    }

    @GetMapping("/{id}")
    public String show(Model model, @PathVariable int id, @ModelAttribute("person") Person person) {
        model.addAttribute("book", bookDAO.readBookById(id));

        Optional<Person> ownerBook = bookDAO.getBookOwner(id);

        if (ownerBook.isPresent())
            model.addAttribute("owner",ownerBook.get());
        else
            model.addAttribute("people", personDAO.people());

        return "books/show";
    }

    @GetMapping("/new")
    public String newBookPage(@ModelAttribute("book") Book book) {

        return "books/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("book") @Valid Book book,
                         BindingResult bindingResult) {
        if(bindingResult.hasErrors())
        return "books/new";

        bookDAO.createBook(book);
        return "redirect:/books";
    }

    @GetMapping("/{id}/edit")
    public String bookEditPage(@PathVariable int id, Model model) {
       model.addAttribute("book", bookDAO.readBookById(id));
       return "books/edit";
    }

    @PatchMapping("/{id}")
    public String updateBook(@PathVariable int id, @ModelAttribute("book") @Valid Book book,
                             BindingResult bindingResult) {
        if(bindingResult.hasErrors())
            return "books/edit";
        bookDAO.updateBook(id, book);
        return "redirect:/books";
    }

    @DeleteMapping("/{id}")
    public String deleteBook(@PathVariable int id) {
        bookDAO.deleteBook(id);
        return "redirect:/books";
    }

    @PatchMapping("/{id}/assign")
    public String assignBook(@PathVariable int id, @ModelAttribute("person") Person selectedPerson) {
        bookDAO.assign(id,selectedPerson);
        return "redirect:/books/" + id;
    }

    @PatchMapping("/{id}/release")
    public String releaseBook(@PathVariable int id) {
        bookDAO.release(id);
        return "redirect:/books/" + id;
    }
}
