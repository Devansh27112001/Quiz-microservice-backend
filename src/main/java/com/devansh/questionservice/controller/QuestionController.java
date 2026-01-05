package com.devansh.questionservice.controller;


import com.devansh.questionservice.model.ClientResponse;
import com.devansh.questionservice.model.Question;
import com.devansh.questionservice.model.QuestionWrapper;
import com.devansh.questionservice.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/question")
public class QuestionController {


    @Autowired
    private QuestionService service;

    @GetMapping("/all")
    public ResponseEntity<List<Question>> allQuestions() {
        return service.getAllQuestions();
    }

    @GetMapping("category/{category}")
    public ResponseEntity<List<Question>> getQuestionsByCategory(@PathVariable String category){
        return service.getByCategory(category);
    }

    @PostMapping("/add")
    public ResponseEntity<String> addQuestion(@RequestBody Question question){
        return service.addQuestion(question);
    }

    // Generate
    @GetMapping("/generate")
    public ResponseEntity<List<Integer>> generateQuestions(@RequestParam String category, @PathVariable Integer numQues){
        return service.generateRandomQuestions(category,numQues);
    }

    // getQuestions (questionId)
    @GetMapping("getQuestions")
    public ResponseEntity<List<QuestionWrapper>> getAllQuestionsFromId(@RequestBody List<Integer> questionIds){
        return service.getAllQuestionsFromId(questionIds);
    }

    // Calculate score
    @PostMapping("/score")
    public ResponseEntity<Integer> calculateScore(@RequestBody List<ClientResponse> responses){
        return service.calculateScore(responses);
    }
}
