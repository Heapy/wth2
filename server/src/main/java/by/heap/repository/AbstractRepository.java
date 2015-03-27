package by.heap.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;


public interface AbstractRepository<T,ID extends Serializable> extends JpaRepository<T, ID> {
}
