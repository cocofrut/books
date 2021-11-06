package com.example.books.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor
@Entity
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String title;

    @NotNull
    private String author;

    private String genre;

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(!(o instanceof  Book)) return false;
        Book book = (Book) o;
        return Objects.equals(this.id, book.id) &&
                Objects.equals(this.author, book.author) &&
                Objects.equals(this.title, book.title) &&
                Objects.equals(this.genre, book.genre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.author, this.title, this.genre);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Book{id=\"").append(this.id)
                .append("\", author=\"").append(this.author)
                .append("\", title=\"").append(this.title)
                .append("\", genre=\"").append(this.genre)
                .append("\"}");

        return builder.toString();
    }
}
