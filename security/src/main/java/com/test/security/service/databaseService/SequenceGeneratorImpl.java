package com.test.security.service.databaseService;

import com.test.security.model.SequenceGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static org.springframework.data.mongodb.core.FindAndModifyOptions.options;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Service
public class SequenceGeneratorImpl implements ISequenceGenerator{

    @Autowired
    private MongoOperations mongoOperations;
    @Override
    public String generateSequence(String sequence) {
        SequenceGenerator counter =  mongoOperations.findAndModify(query(where("_id").is(sequence)),
                new Update().inc("seq", 1), options().returnNew(true).upsert(true), SequenceGenerator.class);
        return !Objects.isNull(counter) ? String.valueOf(counter.getSeq()) : "1";
    }
}
