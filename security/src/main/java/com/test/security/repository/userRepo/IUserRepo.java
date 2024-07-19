package com.test.security.repository.userRepo;

import com.test.security.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserRepo extends MongoRepository<User, String> {
    User findByUsername(String username);

}
