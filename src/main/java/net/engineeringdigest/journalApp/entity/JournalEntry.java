package net.engineeringdigest.journalApp.entity;


import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

/*
Project Lombok - Aims to reduce boilerplate code - such as getters, setters, constructors, etc.
Lombok generates code automatically during compilation based on annotations you add to your Java Classes
This generated code is added to the compiles class files (.class files)
 */

@Document(collection = "journal_entries") // Maps to a MongoDB collection and tells that this is a Document (row)
@Data // Adds Getters,Setters and more
@NoArgsConstructor // Required when converting from JSON to POJO
public class JournalEntry {

    @Id // Marks as the Primary Key
    private ObjectId id;

    @NonNull
    private String title;

    @NonNull
    private String content;

    private LocalDate date;

}
