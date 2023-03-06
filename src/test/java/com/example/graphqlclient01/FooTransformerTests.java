package com.example.graphqlclient01;

import com.example.graphqlclient01.model.source.FooSource;
import com.example.graphqlclient01.model.target.FooTarget;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FooTransformerTests {

    FooSource source = new FooSource("some name", 222);

    @Test
    void transform_name() {
        // given
        FooTransformer fooTransformer = createTransformer();

        // when
        FooTarget target = fooTransformer.transform(source);

        // then
        assertThat(target.name()).isEqualTo("--SOME NAME--");
    }

    @Test
    void transform_add_team_name() {
        // given
        FooTransformer fooTransformer = createTransformer("--SOME TEAM NAME 222--");

        // when
        FooTarget target = fooTransformer.transform(source);

        // then
        assertThat(target.teamName()).isEqualTo("--SOME TEAM NAME 222--");
    }

    FooTransformer createTransformer() {
        return createTransformer(null);
    }

    FooTransformer createTransformer(String teamName) {
        ApiClient apiClient = mock(ApiClient.class);
        when(apiClient.getTeamName(222)).thenReturn(teamName);
        return new FooTransformer(apiClient);
    }
}
