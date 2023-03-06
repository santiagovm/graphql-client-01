package com.example.graphqlclient01.graphqlapi.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GraphQlQuery {
    private String query;
    private Object variables;
}
