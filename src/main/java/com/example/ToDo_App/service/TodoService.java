package com.example.ToDo_App.service;

import com.example.ToDo_App.entity.Todo;
import com.example.ToDo_App.entity.User;
import com.example.ToDo_App.repository.TodoRepository;
import com.example.ToDo_App.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TodoService {

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private UserRepository userRepository;

    private User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));
    }

    public List<Todo> getAllTodos() {
        return todoRepository.findByUser(getCurrentUser());
    }

    public Todo addTodo(Todo todo) {
        todo.setUser(getCurrentUser());
        return todoRepository.save(todo);
    }

    public Todo updateTodo(Long id, Todo newTodo) {
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy todo"));

        if (!todo.getUser().getId().equals(getCurrentUser().getId())) {
            throw new RuntimeException("Không có quyền sửa todo này");
        }

        todo.setTitle(newTodo.getTitle());
        todo.setDescription(newTodo.getDescription());
        todo.setCompleted(newTodo.isCompleted());

        return todoRepository.save(todo);
    }

    public void deleteTodo(Long id) {
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy todo"));

        if (!todo.getUser().getId().equals(getCurrentUser().getId())) {
            throw new RuntimeException("Không có quyền xóa todo này");
        }

        todoRepository.delete(todo);
    }
}