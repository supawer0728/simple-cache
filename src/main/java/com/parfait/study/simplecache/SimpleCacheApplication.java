package com.parfait.study.simplecache;

import com.parfait.study.simplecache.person.domain.Person;
import com.parfait.study.simplecache.person.domain.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SimpleCacheApplication implements CommandLineRunner {

    @Autowired
    private PersonRepository personRepository;

    public static void main(String[] args) {
        SpringApplication.run(SimpleCacheApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        personRepository.save(new Person("John", 12));
        personRepository.save(new Person("Michel", 16));
        personRepository.save(new Person("Chris", 52));
        personRepository.save(new Person("Michael", 25));
        personRepository.save(new Person("Susan", 34));
        personRepository.save(new Person("Kim", 44));
    }
}
