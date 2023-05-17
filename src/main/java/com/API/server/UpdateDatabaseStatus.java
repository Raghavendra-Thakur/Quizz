package com.API.server;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.API.server.Repo.questionRepo;
import com.API.server.entity.Question;

@Service
public class UpdateDatabaseStatus {
	
	@Autowired
	private questionRepo questionRepo;
	
	@Scheduled(fixedRate = 60000*60) 
    public void updateQuizStatus() {
		System.out.println("i have done my work bye");
        LocalDate now = LocalDate.now();
        List<Question> activeQuizzes = questionRepo.findAll();
        for (Question quiz : activeQuizzes) {
        	String status ="";
    		
    		if(now.isBefore(quiz.getStartdate())) {
    		
    			status = "inactive";
    		}
    		else if(now.isAfter(quiz.getEnddate())) {
    			status = "finished";
    		}
    		else {
				status = "active";
			}
    		
    		
    		quiz.setStatus(status);
    		questionRepo.save(quiz);
    		}
        }
}
