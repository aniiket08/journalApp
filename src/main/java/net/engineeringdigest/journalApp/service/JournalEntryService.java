package net.engineeringdigest.journalApp.service;

import net.engineeringdigest.journalApp.entity.JournalEntry;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.repository.JournalEntryRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class JournalEntryService {

    @Autowired
    private JournalEntryRepository journalEntryRepository;

    @Autowired
    private UserService userService;

    @Transactional
    public void saveEntry(JournalEntry journalEntry, String username) {
        LocalDate date = LocalDate.now();
        journalEntry.setDate(date);
        JournalEntry saved = journalEntryRepository.save(journalEntry);
        User user = userService.getUserByUsername(username);
        user.getJournalEntries().add(saved);
        userService.createUser(user);
    }

    public void saveEntry(JournalEntry journalEntry) {
        JournalEntry saved = journalEntryRepository.save(journalEntry);
    }

    public List<JournalEntry> getAll() {
        return journalEntryRepository.findAll();
    }

    public Optional<JournalEntry> getById(ObjectId myId) {
        return journalEntryRepository.findById(myId);
    }

    @Transactional
    public void deleteById(String username, ObjectId myId) {
        User user = userService.getUserByUsername(username);

        boolean removed = user.getJournalEntries().removeIf(x -> x.getId().equals(myId));
        if(removed) {
            journalEntryRepository.deleteById(myId);
        }
        userService.createUser(user);
    }

    public JournalEntry updateById(String username, ObjectId myId, JournalEntry newEntry) {

        User user = userService.getUserByUsername(username);

        List<JournalEntry> oldEntry = user.getJournalEntries().stream().filter(x -> x.getId().equals(myId)).collect(Collectors.toList());

        JournalEntry journalEntry = oldEntry.get(0);

        if (journalEntry != null) {
            journalEntry.setTitle(!newEntry.getTitle().equals("") && !newEntry.getTitle().equals(journalEntry.getTitle()) ? newEntry.getTitle() : journalEntry.getTitle());
            journalEntry.setContent(!newEntry.getContent().equals("") && !newEntry.getContent().equals(journalEntry.getContent()) ? newEntry.getContent() : journalEntry.getContent());
        }
        saveEntry(journalEntry);
        return journalEntry;
    }

}
