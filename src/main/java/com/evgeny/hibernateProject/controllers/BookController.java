package com.evgeny.hibernateProject.controllers;


import com.evgeny.hibernateProject.models.Book;
import com.evgeny.hibernateProject.models.Person;
import com.evgeny.hibernateProject.services.BookService;
import com.evgeny.hibernateProject.services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/book")
public class BookController {

    private final BookService bookService;
    private final PersonService personService;

    @Autowired
    public BookController(BookService bookService, PersonService personService) {
        this.bookService = bookService;
        this.personService = personService;
    }

    @GetMapping()
    public String index(Model model) {
        model.addAttribute("books", bookService.findAll());
        return "bookPages/index";
    }

    //Сортировка по году
    @GetMapping(params = "sort_by_year")
    public String index(Model model, @RequestParam(name = "sort_by_year") boolean sortByYear) {
        model.addAttribute("books", bookService.findAll(sortByYear));
        return "bookPages/index";
    }

    //Пагинация
    @GetMapping(params = {"page", "books_per_page"})
    public String index(Model model, @RequestParam(name = "page") int page, @RequestParam(name = "books_per_page") int booksPerPage) {
        System.out.println("index: Пагинация с двумя параметрами");
        model.addAttribute("books", bookService.findAll(page, booksPerPage));
        return "bookPages/index";
    }

    //Пагинация с сортировкой
    @GetMapping(params = {"page", "books_per_page", "sort_by_year"})
    public String indexPaging(Model model, @RequestParam("page") int page, @RequestParam("books_per_page") int booksPerPage,
                              @RequestParam("sort_by_year") boolean sortByYear) {
        model.addAttribute("books", bookService.findAllAndSort(page, booksPerPage, sortByYear));
        return "bookPages/index";
    }

    @GetMapping("/new")
    public String creatingBookForm(@ModelAttribute("book") Book book) {
        return "bookPages/creatingBookPage";
    }

    @PostMapping()
    public String createBook(@ModelAttribute("book") @Valid Book book,
                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "bookPages/creatingBookPage";
        }
        bookService.save(book);
        return "redirect:/book";
    }

    @GetMapping("/{id}/edit")
    public String editBook(Model model, @PathVariable("id") int id) {
        model.addAttribute("bookToEdit", bookService.findById(id));
        return "bookPages/editBook";
    }

    @PatchMapping("/{id}")
    public String updateBook(@ModelAttribute("book") @Valid Book book,
                             BindingResult bindingResult, @PathVariable("id") int id) {
        if (bindingResult.hasErrors()) {
            return "bookPages/editBook";
        }
        bookService.updateBook(book);
        return "redirect:/book";
    }

    @GetMapping("/{id}")
    public String bookPage(@PathVariable("id") int id, Model model, @ModelAttribute("person") Person person) {
        model.addAttribute("bookToShow", bookService.findById(id));
        model.addAttribute("peopleList", personService.findAll());
        return "bookPages/bookPage";
    }

    @PatchMapping("/{id}/setPersonToBook")
    public String setPersonToBook(@PathVariable("id") int id,
                                  @ModelAttribute("person") Person person) {
        bookService.setPersonToBook(id, person);
        return "redirect:/book";
    }

    @GetMapping("/{id}/resetPerson")
    public String resetPerson(@PathVariable("id") int id) {
        bookService.resetPerson(id);
        return "redirect:/book";
    }

    @DeleteMapping("/{id}")
    public String deleteBook(@PathVariable("id") int id) {
        bookService.deleteById(id);
        return "redirect:/book";
    }

    //Поиск
    @GetMapping("/search")
    public String searchPage() {
        return "bookPages/searchPage";
    }

    @PostMapping("/search/result")
    public String searchResultPage(Model model, @RequestParam(name = "nameOfBook") String nameOfBook) {
        model.addAttribute("book", bookService.findBook(nameOfBook));
        return "bookPages/searchPage";
    }
}
