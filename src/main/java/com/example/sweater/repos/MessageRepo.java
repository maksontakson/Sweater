package com.example.sweater.repos;

import com.example.sweater.domain.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepo extends JpaRepository<Message, Integer> {
    List<Message> findByText(String field);
}
