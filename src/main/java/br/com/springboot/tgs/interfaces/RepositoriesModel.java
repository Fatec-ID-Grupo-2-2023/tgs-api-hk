package br.com.springboot.tgs.interfaces;

import java.util.List;

public interface RepositoriesModel<T> {
    List<T> findAllByStatus(Boolean status);
}
