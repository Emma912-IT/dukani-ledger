package com.dukaniledger.service;

import com.dukaniledger.dto.WorkerRequest;
import com.dukaniledger.entity.*;
import com.dukaniledger.repository.BusinessRepository;
import com.dukaniledger.repository.UserRepository;
import com.dukaniledger.repository.WorkerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkerService {
    private final WorkerRepository workerRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final BusinessRepository businessRepository;
    private final BusinessContextService businessContextService;

    @PreAuthorize("hasRole('OWNER')")
    public Worker addWorker(
            WorkerRequest request,
            String ownerEmail
    ){
        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword())
                )
                .role(Role.WORKER)
                .build();

        User savedUser = userRepository.save(user);

        var owner = userRepository.findByEmail(ownerEmail)
                .orElseThrow(
                        () -> new RuntimeException(
                                "Owner not Found"
                        )
                );

        Business business = businessRepository.findByOwnerId(owner.getId())
                .orElseThrow(
                        () -> new RuntimeException("Business not found")
                );

        Worker worker = Worker.builder()
                .fullname(request.getFullname())
                .email(request.getEmail())
                .phone(request.getPhone())
                .address(request.getAddress())
                .gender(request.getGender())
                .status(WorkerStatus.ACTIVE)
                .business(business)
                .user(savedUser)
                .build();

        return workerRepository.save(worker);
    }

    public List<Worker> getWorkers() {
        User owner = businessContextService.getOwnerForCurrentUser();
        Business business = businessRepository.findByOwnerId(owner.getId())
                .orElseThrow(() -> new RuntimeException("Business not found"));
        return workerRepository.findByBusinessId(business.getId());
    }
}