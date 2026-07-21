package net.engineeringdigest.journalApp.service;

import lombok.extern.slf4j.Slf4j;
import net.engineeringdigest.journalApp.entity.JournalEntry;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@Slf4j
// We can set logging leverl in application.yml
public class UserService {

    @Autowired
    public UserRepository userRepository;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public void createNewUser(User user) {
        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRoles(Arrays.asList("User"));
            userRepository.save(user);
        } catch (Exception e) {
            log.error("Error while saving user - {}: {}", user.getUsername(), e.getMessage());
        }
    }

    public void createNewAdmin(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Arrays.asList("User", "ADMIN"));
        userRepository.save(user);
    }

    public void createUser(User user) {
        userRepository.save(user);
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User updateUser(User newUser, String userName) {
        User oldUser = getUserByUsername(userName);
        if (oldUser!=null) {
            oldUser.setUsername(!oldUser.getUsername().equalsIgnoreCase(newUser.getUsername()) && !newUser.getUsername().isEmpty() ? newUser.getUsername() : oldUser.getUsername());
            oldUser.setPassword(passwordEncoder.encode(!oldUser.getPassword().equalsIgnoreCase(newUser.getPassword()) && !newUser.getPassword().isEmpty() ? newUser.getPassword() : oldUser.getPassword()   ));
            createUser(oldUser);
        }
        return oldUser;
    }

    public List<JournalEntry> getAllUserJournalEntries(String userName) {
        User user = userRepository.findByUsername(userName);
        return user.getJournalEntries();
    }

    public void deleteByUsername(String username) {
        userRepository.deleteByUsername(username);
    }

}
