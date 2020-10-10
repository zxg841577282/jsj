package com.zxg.neo4j.entity;

import lombok.Getter;
import lombok.Setter;
import org.neo4j.ogm.annotation.*;

@Getter
@Setter
@RelationshipEntity(type = "PersonRelation")
public class PersonRelation {

    @Id
    @GeneratedValue
    private Long id;

    //开始节点
    @StartNode
    private Person startNode;

    //结束节点
    @EndNode
    private EndNode endNode;
}
