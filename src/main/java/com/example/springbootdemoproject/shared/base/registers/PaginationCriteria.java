package com.example.springbootdemoproject.shared.base.registers;

public record PaginationCriteria(Integer page, Integer size, String[] sort) {
}
