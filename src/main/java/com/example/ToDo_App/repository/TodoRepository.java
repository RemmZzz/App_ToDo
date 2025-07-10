package com.example.ToDo_App.repository;

import com.example.ToDo_App.entity.Todo;
import com.example.ToDo_App.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TodoRepository extends JpaRepository<Todo, Long> {
    List<Todo> findByUser(User user);
}