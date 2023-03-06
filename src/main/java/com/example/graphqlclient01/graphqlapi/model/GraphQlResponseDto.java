package com.example.graphqlclient01.graphqlapi.model;

import lombok.Data;

@Data
public class GraphQlResponseDto<T> {
    private T data;
}
