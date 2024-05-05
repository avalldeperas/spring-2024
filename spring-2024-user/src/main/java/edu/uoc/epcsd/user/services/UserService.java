package edu.uoc.epcsd.user.services;

import edu.uoc.epcsd.user.entities.Alert;
import edu.uoc.epcsd.user.entities.User;
import edu.uoc.epcsd.user.repositories.AlertRepository;
import edu.uoc.epcsd.user.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AlertRepository alertRepository;

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public User createUser(String email, String password, String fullName, String phoneNumber) {
        // As requested in PRAC1 solution, email must not exist already in database
        validateEmail(email);

        User user = User.builder().email(email).password(password).fullName(fullName).phoneNumber(phoneNumber).build();

        return userRepository.save(user);
    }

    private void validateEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent())
            throw new IllegalArgumentException(String.format("A user with email '%s' already exists.",email));
    }

    public Set<User> getUsersToAlert(Long productId, LocalDate availableOnDate) {
        return alertRepository.findAlertsByProductIdAndInterval(productId, availableOnDate).stream()
                .map(Alert::getUser)
                .collect(Collectors.toSet());
    }
}
