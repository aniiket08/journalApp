package net.engineeringdigest.journalApp.Controller;

// Controller is a special type of component which handle the HTTP request. Also knows as RestController

import net.engineeringdigest.journalApp.entity.JournalEntry;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.service.JournalEntryService;
import net.engineeringdigest.journalApp.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/*
  Methods inside a controller class must be public so that they can be accessed and
  invoked by the Spring Framework or external HTTP requests.
 */

/*
  ORM - Object Relational Mapping
  JPA - Java Persistence API
  Not Required in MongoDB
 */

/*
  Controller -> Service -> Repository
 */

@RestController
@RequestMapping("/journal")
public class JournalEntryController {

    @Autowired
    private JournalEntryService journalEntryService;

    @Autowired
    public UserService userService;

    @GetMapping()
    public ResponseEntity<?> getAll() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();

        User user = userService.getUserByUsername(userName);

        if (user != null) {
            List<JournalEntry> JList = user.getJournalEntries();
            return new ResponseEntity<>(JList, HttpStatus.FOUND);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @PostMapping()
    public ResponseEntity<?> createEntry(@RequestBody JournalEntry myEntry) {
        try {

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userName = authentication.getName();

            journalEntryService.saveEntry(myEntry, userName);
            return new ResponseEntity<>(myEntry, HttpStatus.CREATED);

        } catch (Exception e) {

            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        }

    }

    @GetMapping("/id/{myId}")
    public ResponseEntity<?> /*We can also use wildcard(?) instead of Journal Entry inn the <>*/ getById(@PathVariable ObjectId myId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();

        User user = userService.getUserByUsername(userName);
        List<JournalEntry> journalEntries = user.getJournalEntries().stream().filter(x -> x.getId().equals(myId)).collect(Collectors.toList());

        if (!journalEntries.isEmpty()) {
            return new ResponseEntity<>(journalEntries.get(0), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{myId}")
    public ResponseEntity<?> deleteById(@PathVariable String username, @PathVariable ObjectId myId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();

        User user = userService.getUserByUsername(userName);

        journalEntryService.deleteById(username, myId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }

    @PutMapping("/update/{myId}")
    public ResponseEntity<?> updateById(@PathVariable String username, @PathVariable ObjectId myId, @RequestBody JournalEntry newEntry) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();

        try {
            journalEntryService.updateById(username, myId, newEntry);
            return new ResponseEntity<>(newEntry, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(newEntry, HttpStatus.BAD_REQUEST);
        }
    }

}