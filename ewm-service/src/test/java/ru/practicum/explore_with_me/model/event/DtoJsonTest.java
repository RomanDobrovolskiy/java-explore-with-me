package ru.practicum.explore_with_me.model.event;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class DtoJsonTest {

    @Autowired
    private JacksonTester<CreateEventDto> jsonTester;

    @Test
    public void testDeserializeDto() throws IOException {

        String input = "{\"annotation\":\"annotation\"," +
                "\"category\":6," +
                "\"description\":\"description\"," +
                "\"eventDate\":\"2095-09-06 13:30:38\"," +
                "\"location\":{\"lat\":10,\"lon\":580}," +
                "\"paid\":true," +
                "\"participantLimit\":0," +
                "\"requestModeration\":true," +
                "\"title\":\"title\"}";

        final LocalDateTime eventDate = LocalDateTime.parse("2095-09-06 13:30:38",
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        CreateEventDto dto = jsonTester.parseObject(input);

        assertThat(dto).isNotNull().matches(e -> e.getCategory().equals(6L)
                && e.getAnnotation().equals("annotation") && e.getEventDate().equals(eventDate)
                && e.getDescription().equals("description") && e.getLocation().getLon().equals(580f)
                && e.getLocation().getLat().equals(10f) && e.getPaid().equals(true)
                && e.getParticipantLimit().equals(0) && e.getRequestModeration().equals(true)
                && e.getTitle().equals("title"));
    }
}
