/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package sample.aad.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.microsoft.azure.spring.autoconfigure.aad.UserGroup;
import com.microsoft.azure.spring.autoconfigure.aad.UserPrincipal;

import sample.aad.model.TodoItem;


@RestController
public class TodolistController {
    private final List<TodoItem> todoList = new ArrayList<>();

    public TodolistController() {
        todoList.add(0, new TodoItem(2398, "anything", "whoever"));
    }

    @RequestMapping("/home")
    public Map<String, Object> home() {
        final Map<String, Object> model = new HashMap<>();
        model.put("id", UUID.randomUUID().toString());
        model.put("content", "home");
        return model;
    }

    /**
     * HTTP GET
     */
    @RequestMapping(value = "/api/todolist/{index}",
            method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> getTodoItem(@PathVariable("index") int index) {
        if (index > todoList.size() - 1) {
            return new ResponseEntity<>(new TodoItem(-1, "index out of range", null), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(todoList.get(index), HttpStatus.OK);
    }

    /**
     * HTTP GET ALL
     */
    @PreAuthorize("hasRole('ROLE_chief_geo')")
    @RequestMapping(value = "/api/todolist", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<List<TodoItem>> getAllTodoItems() {
        return new ResponseEntity<>(todoList, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_chief_geo')")
    @RequestMapping(value = "/api/todolist", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addNewTodoItem(@RequestBody TodoItem item) {
    	
    	Object c = SecurityContextHolder.getContext();
    	if(c!= null ) {
    		
    		Authentication a = SecurityContextHolder.getContext().getAuthentication();
    		if(c!= null ) {
    			System.out.println("Authentication  from Context  " + a);
    			System.out.println("a.getAuthorities().toString()  " + a.getAuthorities().toString());
    			Object principal = a.getPrincipal();
    			System.out.println("princi  " + principal);
        	if (principal instanceof UserDetails) {
          	  String username = ((UserDetails)principal).getUsername();
          	} else {
          	  String username = principal.toString();
          	}
    		
    	}
    	}
    		
    	item.setID(todoList.size() + 1);
        todoList.add(todoList.size(), item);
        return new ResponseEntity<>("Entity created", HttpStatus.CREATED);
    }
    

    /**
     * HTTP PUT
     */
    @PreAuthorize("hasRole('ROLE_chief_geo')")
    @RequestMapping(value = "/api/todolist", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateTodoItem(@RequestBody TodoItem item) {
        final List<TodoItem> find =
                todoList.stream().filter(i -> i.getID() == item.getID()).collect(Collectors.toList());
        if (!find.isEmpty()) {
            todoList.set(todoList.indexOf(find.get(0)), item);
            return new ResponseEntity<>("Entity is updated", HttpStatus.OK);
        }
        return new ResponseEntity<>("Entity not found", HttpStatus.OK);
    }

    /**
     * HTTP DELETE
     */
    @RequestMapping(value = "/api/todolist/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteTodoItem(@PathVariable("id") int id,
                                                 PreAuthenticatedAuthenticationToken authToken) {
        final UserPrincipal current = (UserPrincipal) authToken.getPrincipal();

        if (current.isMemberOf(
                new UserGroup("xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx", "group1"))) {
            final List<TodoItem> find = todoList.stream().filter(i -> i.getID() == id).collect(Collectors.toList());
            if (!find.isEmpty()) {
                todoList.remove(todoList.indexOf(find.get(0)));
                return new ResponseEntity<>("OK", HttpStatus.OK);
            }
            return new ResponseEntity<>("Entity not found", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Access is denied", HttpStatus.OK);
        }

    }
}
