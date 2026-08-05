package com.dukaniledger.controller;

import com.dukaniledger.dto.WorkerRequest;
import com.dukaniledger.entity.Worker;
import com.dukaniledger.service.WorkerService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/workers")
@RequiredArgsConstructor
public class WorkerController {
    private final WorkerService workerService;

    @PostMapping
    public Worker addWorker(
            @RequestBody WorkerRequest request,
            Authentication authentication
            ){
        return workerService.addWorker(
                request,
                authentication.getName()
        );
    }

    @GetMapping
    public List<Worker> getWorkers() {
        return workerService.getWorkers();
    }
}
