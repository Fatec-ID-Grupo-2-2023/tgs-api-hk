package br.com.springboot.tgs.models;

import java.util.List;

public interface RepositoriesModel<T> {
    List<T> findAllByStatus(Boolean status);
}
