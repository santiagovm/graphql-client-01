package com.example.graphqlclient01.graphqlapi;

import lombok.SneakyThrows;

import java.io.InputStream;

public class GraphqlSchemaReaderUtil {

    @SneakyThrows
    public static String getSchemaFromFileName(String fileName) {

        String resourceName = String.format("graphql/%s.graphql", fileName);

        try (InputStream stream = GraphqlSchemaReaderUtil.class.getClassLoader().getResourceAsStream(resourceName)) {
            if (stream == null) {
                return "";
            }

            return new String(stream.readAllBytes());
        }
    }
}
