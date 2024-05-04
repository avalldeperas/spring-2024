package edu.uoc.epcsd.user.repositories;

import edu.uoc.epcsd.user.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
