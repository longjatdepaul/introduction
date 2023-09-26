package com.example.demo.dao;

import com.example.demo.model.Person;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository("fileDao")
public class PersonDataAccessService implements PersonDao {
    private final static String FILE_REPOSITORY = "data/people.list";
    private final static List<Person> DB = new ArrayList<>();

    static {
        try (FileInputStream fileIn = new FileInputStream(FILE_REPOSITORY);
             ObjectInputStream in = new ObjectInputStream(fileIn);) {
            DB.addAll((ArrayList) in.readObject());
        } catch (FileNotFoundException ignore) {
        } catch (IOException e) {
            System.out.println("Problem deserializing: " + e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void writeToRepository() {
        try (FileOutputStream fileOut = new FileOutputStream(FILE_REPOSITORY);
             ObjectOutputStream out = new ObjectOutputStream(fileOut);) {
            out.writeObject(DB);
            out.flush();
        } catch (FileNotFoundException ignore) {
        } catch (IOException e) {
            System.out.println("Problem serializing: " + e);
        }
    }

    @Override
    public int insertPerson(UUID id, Person person) {
        DB.add(new Person(id, person.getName()));
        writeToRepository();
        return 1;
    }

    @Override
    public List<Person> selectAllPeople() {
        return DB;
    }

    @Override
    public Optional<Person> selectPersonById(UUID id) {
        return DB.stream()
                .filter(person -> person.getId().equals(id))
                .findFirst();
    }

    @Override
    public int deletePersonById(UUID id) {
        Optional<Person> personMaybe = selectPersonById(id);
        if (personMaybe.isEmpty()) {
            return 0;
        }
        DB.remove(personMaybe.get());
        writeToRepository();
        return 1;
    }

    @Override
    public int updatePersonById(UUID id, Person update) {
        return selectPersonById(id)
                .map(person -> {
                    int indexOfPersonToUpdate = DB.indexOf(person);
                    if (indexOfPersonToUpdate >= 0) {
                        DB.set(indexOfPersonToUpdate, new Person(id, update.getName()));
                        writeToRepository();
                        return 1;
                    }
                    return 0;
                })
                .orElse(0);
    }
}
