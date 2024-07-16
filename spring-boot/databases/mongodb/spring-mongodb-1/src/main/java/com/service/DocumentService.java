package com.service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import org.bson.Document;

import com.entity.Vocabulary;

public interface DocumentService {

    // BSON & Document: doc.toJSON() & Document.parse(json)

    Document mongoDate();

    Document insertAny(String json);

    List<Vocabulary> basicQuery(); // <- MongoDB `raw` query

    List<Document> bsonFilter();

    List<Document> bsonSort();

}
