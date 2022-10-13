package ru.practicum.statistic.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.statistic.service.StatService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class StatsControllerTest {

    @MockBean
    private StatService statService;

    @Autowired
    private MockMvc mvc;

    @Autowired
    ObjectMapper mapper;

    @Test
    void testGetStatParams() throws Exception {
        final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

        LocalDateTime startRange = LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC);
        LocalDateTime endRange = LocalDateTime.now().plusYears(20).withNano(0);
        Set<String> uris = new HashSet<>();
        uris.add("/event/1");

        mvc.perform(get("/stats")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .param("start", startRange.format(formatter))
                        .param("end", endRange.format(formatter))
                        .param("uris", uris.stream().findFirst().get())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(statService, Mockito.times(1)).findStat(startRange, endRange, uris, false);
    }
}