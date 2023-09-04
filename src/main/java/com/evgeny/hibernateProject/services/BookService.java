package com.evgeny.hibernateProject.services;

import com.evgeny.hibernateProject.models.Book;
import com.evgeny.hibernateProject.models.Person;
import com.evgeny.hibernateProject.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class BookService {

    private final BookRepository bookRepository;
    private final PersonService personService;

    @Autowired
    public BookService(BookRepository bookRepository, PersonService personService) {
        this.bookRepository = bookRepository;
        this.personService = personService;
    }

    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    public List<Book> findAll(int page, int booksPerPage) {
        return bookRepository.findAll(PageRequest.of(page, booksPerPage)).getContent();
    }

    public List<Book> findAll(boolean sortByYear) {
        if (sortByYear) {
            return bookRepository.findAll(Sort.by("yearOfPublication"));
        } else {
            return bookRepository.findAll();
        }
    }

    public List<Book> findAllAndSort(int page, int booksPerPage, boolean sortByYear) {
        if (sortByYear) {
            return bookRepository.findAll(PageRequest.of(page, booksPerPage, Sort.by("yearOfPublication"))).getContent();
        } else {
            return bookRepository.findAll(PageRequest.of(page, booksPerPage)).getContent();
        }
    }

    @Transactional
    public void save(Book book) {
        bookRepository.save(book);
    }

    public Book findById(int id) {
        return bookRepository.findById(id).orElse(null);
    }

    @Transactional
    public void updateBook(Book book) {
        bookRepository.save(book);
    }

    @Transactional
    public void setPersonToBook(int id, Person person) {
        Optional<Book> optionalBook = bookRepository.findById(id);
        if (optionalBook.isPresent()) {
            Book book = optionalBook.get();
            Person owner = personService.findById(person.getId());
            book.setTakenTime(new Date());
            book.setOwner(owner);
        }
    }

    @Transactional
    public void resetPerson(int id) {
        Book book = this.findById(id);
        book.setTakenTime(null);
        book.setOwner(null);
    }

    @Transactional
    public void deleteById(int id) {
        bookRepository.deleteById(id);
    }

    public Book findBook(String nameOfBook) {
        return bookRepository.findBookByNameOfBookStartingWith(nameOfBook);
    }
}
