package it.uniroma3.copywritergpt.controller;

import it.uniroma3.copywritergpt.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class JobsController {

    @Autowired
    private JobService jobService;

    @GetMapping("/jobs")
    public void getJobsList() {

    }

    @PostMapping("/jobs")
    public void createNewJob() {

    }

    @GetMapping("/jobs/{jobId}")
    public void getJobInfo(@PathVariable String jobId) {

    }

    @PostMapping("/jobs/{jobId}/tables")
    public void addTablesToJob(@PathVariable String jobId) {

    }

    @PostMapping("/jobs/{jobId}/start")
    public void startJob(@PathVariable String jobId) {

    }

}
