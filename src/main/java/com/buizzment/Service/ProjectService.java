package com.buizzment.Service;

import com.buizzment.ExceptionHandler.ResourceNotFoundException;
import com.buizzment.dto.ProjectRequest;
import com.buizzment.dto.ProjectResponse;
import com.buizzment.model.Org;
import com.buizzment.model.Project;
import com.buizzment.repository.OrgRepository;
import com.buizzment.repository.ProjectRepository;
import com.buizzment.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final OrgRepository orgRepository;
    private final UserRepository userRepository;
    private final SequenceGeneratorService sequenceGenerator;

    @Transactional
    public ProjectResponse createProject(ProjectRequest request, String orgId, String creatorId) {
        // Validate organization exists
        Org org = orgRepository.findById(orgId)
                .orElseThrow(() -> new ResourceNotFoundException("Organization not found"));

        // Generate project code if not provided
        String projectCode = request.getProjectCode();
        if (projectCode == null || projectCode.isBlank()) {
            projectCode = "PRJ-" + sequenceGenerator.generateSequence(Project.SEQUENCE_NAME);
        }

        // Create project
        Project project = Project.builder()
                .name(request.getName())
                .orderNo(request.getOrderNo())
                .el1No(request.getEl1No())
                .projectCode(projectCode)
                .startingDate(request.getStartingDate())
                .tentativeEndingDate(request.getTentativeEndingDate())
                .organization(org)
                .createdBy(creatorId)
                .teamMemberIds(request.getInitialTeamMembers() != null ?
                        request.getInitialTeamMembers() : new HashSet<>())
                .build();

        Project savedProject = projectRepository.save(project);

        // Update organization's project references
        org.getProjectIds().add(savedProject.getId());
        org.setActiveProjectsCount(org.getActiveProjectsCount() + 1);
        orgRepository.save(org);

        return ProjectToProjectResponse(List.of(savedProject)).get(0);
    }

    private ProjectResponse mapToProjectResponse(Project project, String orgName) {
        return ProjectResponse.builder()
                .id(project.getId())
                .name(project.getName())
                // ... map all other fields
                .orgName(orgName)
                .build();
    }

    public ProjectResponse getProjectDetails(String projectId) {


        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

        return ProjectToProjectResponse(List.of(project)).get(0);
    }

    private static List<ProjectResponse> ProjectToProjectResponse(List<Project> projects) {

        return projects.stream()
                .map(project -> ProjectResponse.builder()
                        .name(project.getName())
                        .createdAt(project.getCreatedAt())
                        .el1No(project.getEl1No())
                        .orgName(project.getOrganization() != null ? project.getOrganization().getName() : "Unknown")
                        .teamMembers(project.getTeamMemberIds())
                        .taskCount(0)
                        .projectCode(project.getProjectCode())
                        .orderNo(project.getOrderNo())
                        .status(project.getStatus())
                        .orgId(project.getOrganization() != null ? project.getOrganization().getId() : null)
                        .tentativeEndingDate(project.getTentativeEndingDate())
                        .createdBy(project.getCreatedBy())
                        .startingDate(project.getStartingDate())
                        .id(project.getId())
                        .build())
                .collect(Collectors.toList());
    }

    public List<ProjectResponse> getProjectsByOrg(String orgId, Project.ProjectStatus status) {
        List<Project> projects;
        if (status != null) {
            projects = projectRepository.findByOrganizationIdAndStatus(orgId, status);
        } else {
            projects = projectRepository.findByOrganization_Id(orgId);
        }
        
        // Debug logging
        System.out.println("Organization ID: " + orgId);
        System.out.println("Status: " + status);
        System.out.println("Found projects count: " + (projects != null ? projects.size() : 0));
        if (projects != null && !projects.isEmpty()) {
            System.out.println("First project: " + projects.get(0));
        }
        
        return ProjectToProjectResponse(projects);
    }
}