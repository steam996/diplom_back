package org.myproject.diplom_backend.repositories;

import org.myproject.diplom_backend.entity.UserFile;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FileRepository extends CrudRepository<UserFile, Long> {
    Optional<UserFile> findByNameAndUserId(String filename, Long UserId);
}
