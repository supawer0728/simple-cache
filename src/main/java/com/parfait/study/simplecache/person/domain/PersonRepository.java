package com.parfait.study.simplecache.person.domain;

import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class PersonRepository {

    private Map<Long, Person> database = new ConcurrentHashMap<>();
    private AtomicLong seq = new AtomicLong(1);

    public Person save(Person person) {

        if (person.getId() == null) {
            person.setId(seq.getAndIncrement());
        }

        return database.put(person.getId(), person);
    }

    public Person findOne(Long id) {
        return database.get(id);
    }
}
