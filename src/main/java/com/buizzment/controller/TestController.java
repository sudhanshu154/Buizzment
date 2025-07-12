package com.buizzment.controller;

import com.buizzment.model.Project;
import com.buizzment.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class TestController {

    private final ProjectRepository projectRepository;

    @GetMapping("/all")
    public String allAccess() {
        return "Public Content.";
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public String userAccess() {
        return "User Content.";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminAccess() {
        return "Admin Board.";
    }

    @GetMapping("/debug/projects/{orgId}")
    public String debugProjects(@PathVariable String orgId) {
        try {
            List<Project> allProjects = projectRepository.findAll();
            List<Project> orgProjects = projectRepository.findByOrganization_Id(orgId);
            
            StringBuilder result = new StringBuilder();
            result.append("All projects count: ").append(allProjects.size()).append("\n");
            result.append("Projects for org ").append(orgId).append(": ").append(orgProjects.size()).append("\n");
            
            if (!allProjects.isEmpty()) {
                result.append("Sample project org_id: ").append(allProjects.get(0).getOrganization() != null ? 
                    allProjects.get(0).getOrganization().getId() : "NULL").append("\n");
            }
            
            return result.toString();
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}