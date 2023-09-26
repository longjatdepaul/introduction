package com.example.demo.dao;

import com.example.demo.model.Person;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.ArrayList;
import java.util.UUID;

@Repository("fileDao")
public class PersonDataAccessService extends FakePersonDataAccessService {
    private final static String FILE_REPOSITORY = "people.list";

    static {
        try (FileInputStream fileIn = new FileInputStream(FILE_REPOSITORY);
             ObjectInputStream in = new ObjectInputStream(fileIn)) {
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
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            out.writeObject(DB);
            out.flush();
        } catch (FileNotFoundException ignore) {
        } catch (IOException e) {
            System.out.println("Problem serializing: " + e);
        }
    }

    @Override
    public int insertPerson(UUID id, Person person) {
        int success = super.insertPerson(id, person);
        if (success != 0) {
            writeToRepository();
        }
        return success;
    }

    @Override
    public int deletePersonById(UUID id) {
        int success = super.deletePersonById(id);
        if (success != 0) {
            writeToRepository();
        }
        return success;
    }

    @Override
    public int updatePersonById(UUID id, Person update) {
        int success = super.updatePersonById(id, update);
        if (success != 0) {
            writeToRepository();
        }
        return success;
    }
}
