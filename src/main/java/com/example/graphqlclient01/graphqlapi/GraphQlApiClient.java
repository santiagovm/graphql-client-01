package com.example.graphqlclient01.graphqlapi;

import com.example.graphqlclient01.ApiClient;
import com.example.graphqlclient01.graphqlapi.model.TeamDto;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.client.HttpGraphQlClient;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class GraphQlApiClient implements ApiClient {

    private final WebClient webClient;

    public String getTeamName(long teamId) {

        String query = GraphqlSchemaReaderUtil.getSchemaFromFileName("getTeam");

        HttpGraphQlClient graphQlClient = HttpGraphQlClient.create(webClient);

        TeamDto teamDto = graphQlClient
                .document(query)
                .variable("id", teamId)
                .retrieve("team")
                .toEntity(TeamDto.class)
                .block();

        if (teamDto == null) {
            return "";
        }

        return teamDto.getName();
    }
}
