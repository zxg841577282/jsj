package com.zxg.neo4j.service;


import com.zxg.neo4j.entity.Person;
import com.zxg.neo4j.repository.PersonRelationRepository;
import com.zxg.neo4j.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Neo4jService {
    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private PersonRelationRepository personRelationRepository;

    public boolean addUser(Person person) {
        personRepository.addPersonNodeList(person.getName(), person.getBorn());
        return true;
    }

    public List<Person> getUserNodeList() {
        return personRepository.getPersonNodeList();
    }
}
