package ru.practicum.explore_with_me.service.statistic_client;

public interface StatisticService {
    Long getStatistic(String endpoint);

    void hitEndpoint(String endpoint, String ipAddress);
}
