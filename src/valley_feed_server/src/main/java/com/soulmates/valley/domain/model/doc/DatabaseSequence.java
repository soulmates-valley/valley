package com.soulmates.valley.domain.model.doc;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@AllArgsConstructor
@Document(collection = "database_sequences")
public class DatabaseSequence {
    @Id
    private String id;

    private long seq;
}
