package com.parfait.study.simplecache.person.service;

import com.parfait.study.simplecache.person.domain.Person;
import com.parfait.study.simplecache.person.domain.PersonRepository;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@CacheConfig(cacheNames = "person")
public class PersonService {

    private final PersonRepository personRepository;

    public PersonService(@NonNull PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @CachePut(key = "#person.id", unless = "#result.id != null")
    public Person save(Person person) {
        log.info("save() called");
        return personRepository.save(person);
    }

    @Cacheable
    public Person get(@NonNull Long id) {
        log.info("get(Long) called");
        return personRepository.findOne(id);
    }
}
