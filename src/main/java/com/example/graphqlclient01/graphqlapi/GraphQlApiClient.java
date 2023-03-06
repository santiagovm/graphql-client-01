package com.example.graphqlclient01.graphqlapi;

import com.example.graphqlclient01.ApiClient;
import com.example.graphqlclient01.graphqlapi.model.GraphQlQuery;
import com.example.graphqlclient01.graphqlapi.model.GraphQlResponseDto;
import com.example.graphqlclient01.graphqlapi.model.TeamDto;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class GraphQlApiClient implements ApiClient {

    private final WebClient webClient;

    public String getTeamName(long teamId) {

        String query = GraphqlSchemaReaderUtil.getSchemaFromFileName("getTeam");
        String variables = GraphqlSchemaReaderUtil.getSchemaFromFileName("getTeamVariables");

        var getTeamQuery = new GraphQlQuery();
        getTeamQuery.setQuery(query);
        getTeamQuery.setVariables(variables.replace("${id}", Long.toString(teamId)));

        GraphQlResponseDto<TeamDto> responseDto = webClient.post()
                .uri("/graphql")
                .bodyValue(getTeamQuery)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<GraphQlResponseDto<TeamDto>>() {})
                .block();

        if (responseDto == null) {
            return "";
        }

        return responseDto.getData().getName();
    }
}
