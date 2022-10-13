package ru.practicum.explore_with_me.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.explore_with_me.model.event.CreateEventDto;
import ru.practicum.explore_with_me.service.event.EventService;
import ru.practicum.explore_with_me.service.request.ParticipationRequestService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @MockBean
    private EventService eventService;

    @MockBean
    private ParticipationRequestService requestService;

    @Autowired
    private MockMvc mvc;

    @Autowired
    ObjectMapper mapper;

    CreateEventDto createEventDto;

    @BeforeEach
    void setUp() {
        createEventDto = CreateEventDto.builder()
                .annotation("annotation")
                .category(1L)
                .description("description")
                .eventDate(LocalDateTime.now().plusDays(1))
                .location(CreateEventDto.Location.builder().lat(54.92f).lon(71.27f).build())
                .participantLimit(1500)
                .paid(true)
                .title("title")
                .build();
    }

    @Test
    void createEventTest() throws Exception {
        String event = "{\"annotation\":\"annotation\"," +
                "\"category\":1," +
                "\"description\":\"description\"," +
                "\"eventDate\":\"2022-12-12 01:10:32\"," +
                "\"location\":{\"lat\":54.92,\"lon\":71.27}," +
                "\"paid\":true," +
                "\"participantLimit\":1500," +
                "\"requestModeration\":true," +
                "\"title\":\"title\"}";

        createEventDto = mapper.readValue(event, CreateEventDto.class);

        mvc.perform(post("/users/1/events")
                        .content(event)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(eventService, Mockito.times(1)).createEvent(1L, createEventDto);
    }
}