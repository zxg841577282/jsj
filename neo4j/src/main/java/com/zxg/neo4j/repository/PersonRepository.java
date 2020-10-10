package com.zxg.neo4j.repository;

import com.zxg.neo4j.entity.Person;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface  PersonRepository extends Neo4jRepository<Person,Long> {

    @Query("MATCH (n:Person) RETURN n ")
    List<Person> getPersonNodeList();

    @Query("create (n:Person{name:{name},born:{born}}) RETURN n ")
    List<Person> addPersonNodeList(@Param("name") String name, @Param("born")int born);

}
