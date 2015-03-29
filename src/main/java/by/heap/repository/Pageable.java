package by.heap.repository;


import org.springframework.data.domain.PageRequest;

public final class Pageable {

    public static final org.springframework.data.domain.Pageable SEVEN = new PageRequest(0, 7);
    public static final org.springframework.data.domain.Pageable SIX = new PageRequest(0, 6);

    private Pageable() {
    }
}
