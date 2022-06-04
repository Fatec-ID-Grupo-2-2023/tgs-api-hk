package br.com.springboot.tgs.interfaces;

import java.util.List;

import org.springframework.http.ResponseEntity;

public interface RestControllerModel<T, ID> {
    ResponseEntity<Object> findById(ID id);
    List<T> findByStatus(Boolean status);    
    ResponseEntity<Object> createAndUpdate(T t);
    ResponseEntity<Object> remove(T t);
}
