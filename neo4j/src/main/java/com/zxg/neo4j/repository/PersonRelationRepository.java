package com.zxg.neo4j.repository;

import com.zxg.neo4j.entity.PersonRelation;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PersonRelationRepository extends Neo4jRepository<PersonRelation, Long> {
    @Query("match p=(n:Person)<-[r:PersonRelation]->(n1:Person) where p.name={firstName} and n1.name={secondName} return p")
    List<PersonRelation> findPersonRelationByName(@Param("firstName")String firstName, @Param("secondName")String secondName);

    @Query ("match (fu:Person),(su:Person) where fu.name={firstName} and su.name={secondName}, create p=(fu)-[r:PersonRelation]->(su) return p")
    List<PersonRelation> addPersonRelation(@Param("firstName")String firstName, @Param("secondName") String secondName);

}
