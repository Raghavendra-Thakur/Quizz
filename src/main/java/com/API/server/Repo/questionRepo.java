package com.API.server.Repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.API.server.entity.Question;
@Repository
public interface questionRepo extends JpaRepository<Question, Integer>{
	
	List<Question> findByStatus(String status);
	List<Question> findByIdAndStatus(int id, String status);
}
