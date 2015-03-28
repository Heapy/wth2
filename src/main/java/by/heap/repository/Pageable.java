package by.heap.repository;


import org.springframework.data.domain.PageRequest;

public final class Pageable {

    public static final org.springframework.data.domain.Pageable SEVEN = new PageRequest(0, 7);

    private Pageable() {
    }
}
