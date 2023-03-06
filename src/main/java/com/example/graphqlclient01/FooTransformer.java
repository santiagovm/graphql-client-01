package com.example.graphqlclient01;

import com.example.graphqlclient01.model.source.FooSource;
import com.example.graphqlclient01.model.target.FooTarget;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FooTransformer {

    private final ApiClient apiClient;

    public FooTarget transform(FooSource source) {

        String targetName = String.format("--%s--", source.name().toUpperCase());
        String teamName = apiClient.getTeamName(source.teamId());

        return new FooTarget(targetName, teamName);
    }
}
