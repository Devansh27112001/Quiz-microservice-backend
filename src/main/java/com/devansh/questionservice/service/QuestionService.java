package com.devansh.questionservice.service;


import com.devansh.questionservice.model.ClientResponse;
import com.devansh.questionservice.model.Question;
import com.devansh.questionservice.model.QuestionWrapper;
import com.devansh.questionservice.repo.QuestionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
public class QuestionService {


    @Autowired
    private QuestionRepo repo;

    public ResponseEntity<List<Question>> getAllQuestions() {
        try {
            return new ResponseEntity<>(repo.findAll(), HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<List<Question>> getByCategory(String category){
        try {
            return new ResponseEntity<>(repo.findByCategory(category), HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<String> addQuestion(Question question){
        try {
            repo.save(question);
            return new ResponseEntity<>("Added Question successfully", HttpStatus.CREATED);
        }catch(Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>("Added Question failed", HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<List<Integer>> generateRandomQuestions(String category,Integer numQues){
        List<Integer> questionIds = repo.findRandomQuestionsByCategory(
                category,
                numQues
        );

        // If the above code gives you a list of questions you can use the below code.
        // List<Integer> questionIds = questions.stream().map(Question::getId).toList();
        return new ResponseEntity<>(questionIds,  HttpStatus.OK);
    }

    public ResponseEntity<List<QuestionWrapper>> getAllQuestionsFromId(List<Integer> questionIds){
        List<Question> questions = repo.findAllById(questionIds);

        List<QuestionWrapper> questionWrappers = new ArrayList<>();
        for(Question question : questions){
            QuestionWrapper wrapper = new QuestionWrapper(
                    question.getId(),
                    question.getQuestionTitle(),
                    question.getOption1(),
                    question.getOption2(),
                    question.getOption3(),
                    question.getOption4()
            );
            questionWrappers.add(wrapper);
        }

        return new ResponseEntity<>(questionWrappers, HttpStatus.OK);
    }

    public ResponseEntity<Integer> calculateScore(List<ClientResponse> responses) {
        try{
            int score = 0;
            // Get all the ids from the coming responses
            List<Integer> questionIds = responses
                    .stream()
                    .map(ClientResponse::getId)
                    .toList();

            // Get all the Questions from the retrieved ids above
            List<Question> questions = repo.findAllById(questionIds);

            // Loop over the responses and calculate the score.
            for(ClientResponse response:responses){
                Question question = questions.stream()
                        .filter(q -> q.getId().equals(response.getId()))
                        .findFirst()
                        .orElse(null);

                if(question != null && response.getResponse().equals(question.getRightAnswer())){
                    score++;
                }
            }

            return new ResponseEntity<>(score, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
