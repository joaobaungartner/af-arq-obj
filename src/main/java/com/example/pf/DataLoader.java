package com.example.pf;

import com.example.pf.models.Papel;
import com.example.pf.models.User;
import com.example.pf.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;

    public DataLoader(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) {
        String cpfAdmin = "00000000000";

        Optional<User> adminExistente = userRepository.findByCpf(cpfAdmin);

        if (adminExistente.isPresent()) {
            System.out.println("ADMIN já existe.");
            System.out.println("Use este header no Postman:");
            System.out.println("X-USER-ID: " + adminExistente.get().getId());
            return;
        }

        User admin = new User();
        admin.setNome("Admin");
        admin.setCpf(cpfAdmin);
        admin.setPapel(Papel.ADMIN);

        User adminSalvo = userRepository.save(admin);

        System.out.println("ADMIN criado com sucesso!");
        System.out.println("Use este header no Postman:");
        System.out.println("X-USER-ID: " + adminSalvo.getId());
    }
}