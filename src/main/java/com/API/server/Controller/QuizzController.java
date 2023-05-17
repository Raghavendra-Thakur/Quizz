package com.API.server.Controller;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.API.server.Repo.questionRepo;
import com.API.server.entity.Question;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;

@RestController
public class QuizzController {
	@Autowired
	questionRepo questionRepo ; 
	
	Logger logger = LoggerFactory.getLogger(QuizzController.class);
	
	@PostMapping("/quizzes")
    @RateLimiter(name = "default" ,fallbackMethod = "rateLimiterFallbackresultquestion")
	public ResponseEntity<Question> createQuestion(@RequestBody Question question){
		
		if(question == null) {
			return ResponseEntity.badRequest().build();
		}
		
		String status ="";
		
		LocalDate date = LocalDate.now();
		if(date.isBefore(question.getStartdate())) {
		
			status = "inactive";
		}
		else if(date.isAfter(question.getEnddate())) {
			status = "finished";
		}
		else {
			status = "active";
		}
		
		
		question.setStatus(status);
		
		Question question2 = questionRepo.save(question);
		
		
		return new ResponseEntity<>(question2 , HttpStatus.CREATED);
	}
	
	@GetMapping("/quizzes/all")
	@Cacheable(cacheNames = "quizzes")
	@RateLimiter(name = "default" ,fallbackMethod = "rateLimiterFallback")
	public ResponseEntity<List<Question>> getQuestions(){
		
		
		List<Question> questions = questionRepo.findAll();
		
		List<Question> que = questions.stream().map(s -> {
			if(s.getStatus().equals("active") || s.getStatus().equals("inactive")) {
				s.setRightanswer("The Answer Will Be Available once Quizz Ends");
			}
			return s;
		}).collect(Collectors.toList());
		logger.info("getting from db");
		return ResponseEntity.ok(que) ;
	}
	
	@GetMapping("/quizzes/active")
    @RateLimiter(name = "default" ,fallbackMethod = "rateLimiterFallback")
	@Cacheable(cacheNames = "activequizzes")
	public ResponseEntity<List<Question>> getActiveQuizz(){
		List<Question> questions = questionRepo.findByStatus("active");
		
		List<Question> que = questions.stream().map(s -> {
			if(s.getStatus().equals("active") || s.getStatus().equals("inactive")) {
				s.setRightanswer("The Answer Will Be Available once Quizz Ends");
			}
			return s;
		}).collect(Collectors.toList());

		
		return ResponseEntity.ok(que) ;
		}
	

	@GetMapping( value = "quizzes/{id}/result" , produces = "application/json" )
	public ResponseEntity<String> getQuestionResult(@PathVariable int id) {
		
		Question question = questionRepo.findById(id).orElse(null);
		
		if(question == null) {
			return ResponseEntity.notFound().build();
		}
		
		if(question.getStatus().equals("finished") ) {
			return ResponseEntity.ok( "{\"Answer\":\"" + question.getRightanswer() +"\"}" );
		}
		
		return new ResponseEntity<>("{\"Answer\": \"The Question is Active\"}" , HttpStatus.BAD_REQUEST);

		
	}
	
	
	
	//fallbackmethods
	 public ResponseEntity<List<Question>> rateLimiterFallback(Exception e) {
		 	List<Question> questions = null;
	        return new ResponseEntity<>(questions ,HttpStatus.TOO_MANY_REQUESTS);
	 }
	  
	 public ResponseEntity<Question> rateLimiterFallbackresultquestion(@RequestBody Question question){
		 	Question question1 = null;
			return new ResponseEntity<>(question1, HttpStatus.TOO_MANY_REQUESTS);
	 }
	
	
}	

