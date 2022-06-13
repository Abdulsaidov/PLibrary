package com.artur.library.repository;

import com.artur.library.model.Book;
import com.artur.library.service.PeopleService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BooksRepository extends JpaRepository<Book,Integer> {
    List<Book> findByTitleStartingWith(String title);
}
