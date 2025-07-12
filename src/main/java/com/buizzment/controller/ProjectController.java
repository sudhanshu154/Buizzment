package com.buizzment.controller;

import com.buizzment.Service.ProjectService;
import com.buizzment.annotation.CurrentUser;
import com.buizzment.dto.ProjectRequest;
import com.buizzment.dto.ProjectResponse;
import com.buizzment.model.Project;
import com.buizzment.security.UserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/orgs/{orgId}/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;
//    private final TaskService taskService;

//    @PostMapping
//    @PreAuthorize("@orgSecurity.hasOrgPermission(#orgId, 'project.create')")
//    public ResponseEntity<ProjectResponse> createProject(
//            @PathVariable String orgId,
//            @Valid @RequestBody ProjectRequest request,
//            @CurrentUser UserPrincipal userPrincipal) {
//
//        ProjectResponse response = projectService.createProject(request, orgId, userPrincipal.getId());
//        return ResponseEntity.status(HttpStatus.CREATED).body(response);
//    }

    @PostMapping
    @PreAuthorize("@orgSecurity.hasOrgPermission(#orgId, 'project.create')")
    public ResponseEntity<?> createProject(
            @PathVariable String orgId,
            @Valid @RequestBody ProjectRequest request,
            BindingResult result,
            @CurrentUser UserPrincipal userPrincipal) {

        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            result.getFieldErrors().forEach(err ->
                    errors.put(err.getField(), err.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errors);
        }

        ProjectResponse response = projectService.createProject(request, orgId, userPrincipal.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }





    @GetMapping("/{projectId}")
    @PreAuthorize("@orgSecurity.hasOrgPermission(#orgId, 'project.read')")
    public ResponseEntity<ProjectResponse> getProject(
            @PathVariable String orgId,
            @PathVariable String projectId) {

        ProjectResponse response = projectService.getProjectDetails(projectId);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @PreAuthorize("@orgSecurity.isMember(#orgId)")
    public ResponseEntity<List<ProjectResponse>> getOrgProjects(
            @PathVariable String orgId,
            @RequestParam(required = false) Project.ProjectStatus status) {

        List<ProjectResponse> projects = projectService.getProjectsByOrg(orgId, status);
        log.info("Organisation: {} \n List of Projects: {}",orgId,projects);
        return ResponseEntity.ok(projects);
    }
}