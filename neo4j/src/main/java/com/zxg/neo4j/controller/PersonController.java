package com.zxg.neo4j.controller;

import com.zxg.neo4j.entity.Person;
import com.zxg.neo4j.service.Neo4jService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PersonController {

    @Autowired
    private Neo4jService neo4jService;

    @GetMapping("/getPersonList")
    public Object getPersonList(){
        return neo4jService.getUserNodeList();
    }

    @PostMapping("/addPerson")
    public boolean addPerson(Person person){
        return neo4jService.addUser(person);
    }

}
