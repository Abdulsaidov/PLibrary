package com.artur.library.controller;

import com.artur.library.model.Book;
import com.artur.library.model.Person;
import com.artur.library.service.BookService;
import com.artur.library.service.PeopleService;
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

    private final BookService bookService;
    private final PeopleService peopleService;

    @Autowired
    public BookController(BookService bookService, PeopleService peopleService) {
        this.bookService = bookService;
        this.peopleService = peopleService;
    }

    @GetMapping()
    public String index(Model model, @RequestParam(value = "page",required = false) Integer page,
                        @RequestParam(value = "books_per_page",required = false) Integer booksPerPage,
                        @RequestParam(value = "sort_by_year",required = false) boolean sortByYear) {
        if (page == null || booksPerPage == null)
        model.addAttribute("books", bookService.books(sortByYear));
        else
            model.addAttribute("books", bookService.findWithPagination(page,booksPerPage,sortByYear));

        return "books/index";
    }

    @GetMapping("/{id}")
    public String show(Model model, @PathVariable int id, @ModelAttribute("person") Person person) {
        model.addAttribute("book", bookService.readBookById(id));

        Optional<Person> ownerBook = bookService.getBookOwner(id);

        if (ownerBook.isPresent())
            model.addAttribute("owner",ownerBook.get());
        else
            model.addAttribute("people", peopleService.people());

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

        bookService.createBook(book);
        return "redirect:/books";
    }

    @GetMapping("/{id}/edit")
    public String bookEditPage(@PathVariable int id, Model model) {
       model.addAttribute("book", bookService.readBookById(id));
       return "books/edit";
    }

    @PatchMapping("/{id}")
    public String updateBook(@PathVariable int id, @ModelAttribute("book") @Valid Book book,
                             BindingResult bindingResult) {
        if(bindingResult.hasErrors())
            return "books/edit";
        bookService.updateBook(id, book);
        return "redirect:/books";
    }

    @DeleteMapping("/{id}")
    public String deleteBook(@PathVariable int id) {
        bookService.deleteBook(id);
        return "redirect:/books";
    }

    @PatchMapping("/{id}/assign")
    public String assignBook(@PathVariable int id, @ModelAttribute("person") Person selectedPerson) {
        bookService.assign(id,selectedPerson);
        return "redirect:/books/" + id;
    }

    @PatchMapping("/{id}/release")
    public String releaseBook(@PathVariable int id) {
        bookService.release(id);
        return "redirect:/books/" + id;
    }

    @GetMapping("/search")
    public String searchPage() {
        return "books/search";
    }

    @PostMapping("/search")
    public String makeSearch (Model model, @RequestParam("query") String query) {
        model.addAttribute("books", bookService.searchByTitle(query));
        return "books/search";
    }
}
