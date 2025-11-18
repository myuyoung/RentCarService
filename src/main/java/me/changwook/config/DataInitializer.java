package me.changwook.config;

import org.springframework.boot.CommandLineRunner;

import java.util.List;

public interface DataInitializer extends CommandLineRunner {

    List<String[]> dummyData();
}
