package com.evgeny.hibernateProject.models;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "Book")
public class Book {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name_of_book")
    @NotEmpty(message = "Поле \"Название книги\" не должно быть пустым")
    @Size(min = 2, max = 100, message = "Поле \"Название книги\" должно быть между 2 и 100 символами")
    @Pattern(regexp = "^[a-zA-Zа-яА-я\\s]+$", message = "Поле \"Название книги\" должно содержать только буквы и пробелы")
    private String nameOfBook;
    @Column(name = "author")
    @NotEmpty(message = "Поле \"Автор\" не должно быть пустым")
    @Size(min = 2, max = 100, message = "Поле \"Автор\" должно быть между 2 и 100 символами")
    @Pattern(regexp = "^[a-zA-Zа-яА-я\\s]+$", message = "Поле \"Автор\" должно содержать только буквы и пробелы")
    private String author;

    @Column(name = "year_of_publication")
    @Min(value = 1000, message = "Минимальный год публикации равен 1000")
    @Max(value = 2023, message = "Максимальный год публикации равен 2023")
    private int yearOfPublication; //Год издания
    @ManyToOne
    @JoinColumn(name = "human_id", referencedColumnName = "id")
    private Person owner;

    @Column(name = "taken_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date takenTime;

    @Transient
    private boolean overdue;

    public Book() {

    }

    public Book(String nameOfBook, String author, int yearOfPublication) {
        this.nameOfBook = nameOfBook;
        this.author = author;
        this.yearOfPublication = yearOfPublication;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNameOfBook() {
        return nameOfBook;
    }

    public void setNameOfBook(String nameOfBook) {
        this.nameOfBook = nameOfBook;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getYearOfPublication() {
        return yearOfPublication;
    }

    public void setYearOfPublication(int yearOfPublication) {
        this.yearOfPublication = yearOfPublication;
    }

    public Person getOwner() {
        return owner;
    }

    public void setOwner(Person owner) {
        this.owner = owner;
    }

    public Date getTakenTime() {
        return takenTime;
    }

    public void setTakenTime(Date takenTime) {
        this.takenTime = takenTime;
    }

    public boolean getOverdue() {
        Date nowDate = new Date();
        if (this.getTakenTime() == null) {
            return false;
        } else if ((nowDate.getTime() - this.getTakenTime().getTime()) / (1000 * 60 * 24) >= 10) {
            this.overdue = true;
        }
        return overdue;
    }

    public void setOverdue(boolean overdue) {
        this.overdue = overdue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return id == book.id && yearOfPublication == book.yearOfPublication && Objects.equals(nameOfBook, book.nameOfBook) && Objects.equals(author, book.author) && Objects.equals(owner, book.owner);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nameOfBook, author, yearOfPublication, takenTime, overdue);
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", nameOfBook='" + nameOfBook + '\'' +
                ", author='" + author + '\'' +
                ", yearOfPublication=" + yearOfPublication +
                ", takenTime=" + takenTime +
                ", isOverdue=" + overdue +
                '}';
    }
}
