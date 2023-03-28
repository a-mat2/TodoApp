package com.company.todoapp.controller;

import com.company.todoapp.model.Task;
import com.company.todoapp.model.TaskRepository;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

class TaskControllerWithoutRest {
    private static final Logger logger = LoggerFactory.getLogger(TaskControllerWithoutRest.class);
    private final TaskRepository repository;

    public TaskControllerWithoutRest(TaskRepository sqlTaskRepository) {
        this.repository = sqlTaskRepository;
    }

    @RequestMapping(method = RequestMethod.POST)
    ResponseEntity<?> createTask(@RequestBody @Valid Task toCreate) {
        Task result = repository.save(toCreate);
        logger.warn("Exposing one task");
        return ResponseEntity.created(URI.create("/" + result.getId())).body(result);
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, params = {"!sort", "!page", "!size"})
    ResponseEntity<List<Task>> readAllTasks() {
        logger.warn("Exposing all the tasks!");
        return ResponseEntity.ok(repository.findAll());
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET)
    ResponseEntity<List<Task>> readAllTasks(Pageable page) {
        logger.info("Custom pageable");
        return ResponseEntity.ok(repository.findAll(page).getContent());
    }

    @ResponseBody
    @GetMapping(value = "/tasks/{id}")
    ResponseEntity<Task> readTaskById(@PathVariable int id) {
        return repository.findById(id).map(task -> ResponseEntity.ok(task))
                .orElse(ResponseEntity.notFound().build());
    }

    @RequestMapping(method = RequestMethod.PUT, path = "/{id}")
    ResponseEntity<?> updateTask(@PathVariable int id, @RequestBody @Valid Task toUpdate) {
        if (!repository.existsById(id)) {
            logger.warn("Task doesn't exist");
            return ResponseEntity.notFound().build();
        }
        repository.findById(id)
                .ifPresent(task -> {
                    task.updateFrom(toUpdate);
                    repository.save(task);
                });
        return ResponseEntity.noContent().build();
    }

}
