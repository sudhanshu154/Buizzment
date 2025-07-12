package com.buizzment.repository;

import com.buizzment.model.Project;
import com.buizzment.model.Project.ProjectStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ProjectRepository extends MongoRepository<Project, String> {

    // Basic CRUD operations are provided by MongoRepository

    // Find project by ID within an organization
    @Query("{ '_id': ?0, 'org_id': ?1 }")
    Optional<Project> findByIdAndOrganizationId(String projectId, String orgId);

    // Check if project exists in organization (used in security)
    @Query(value = "{ '_id': ?0, 'org_id': ?1 }", exists = true)
    boolean existsByIdAndOrganizationId(String projectId, String orgId);

    // Get all projects for an organization with optional status filter
    @Query(value = "{ 'org_id': ?0, $or: [ { 'status': ?1 }, { ?1: null } ] }")
    List<Project> findByOrganizationIdAndStatus(String orgId, ProjectStatus status);

    // Find projects by multiple statuses
    @Query("{ 'org_id': ?0, 'status': { $in: ?1 } }")
    List<Project> findByOrganizationIdAndStatusIn(String orgId, Set<ProjectStatus> statuses);

    // Find projects within date range
    @Query("{ 'org_id': ?0, 'startingDate': { $gte: ?1 }, 'tentativeEndingDate': { $lte: ?2 } }")
    List<Project> findActiveProjectsBetweenDates(String orgId, LocalDate start, LocalDate end);

    // Find projects by team member
    @Query("{ 'org_id': ?0, 'teamMemberIds': ?1 }")
    List<Project> findByOrganizationAndTeamMember(String orgId, String userId);

    // Count projects by status in an organization
    @Query(value = "{ 'org_id': ?0, 'status': ?1 }", count = true)
    long countByOrganizationIdAndStatus(String orgId, ProjectStatus status);

    // Find projects by order number (case insensitive)
    @Query("{ 'org_id': ?0, 'orderNo': { $regex: ?1, $options: 'i' } }")
    List<Project> findByOrganizationIdAndOrderNoContaining(String orgId, String orderNo);

    // Find projects due before a specific date
    @Query("{ 'org_id': ?0, 'tentativeEndingDate': { $lt: ?1 }, 'status': { $ne: 'COMPLETED' } }")
    List<Project> findOverdueProjects(String orgId, LocalDate date);

    // Bulk update project status
    @Query("{ '_id': { $in: ?0 } }")
    @Update("{ $set: { 'status': ?1 } }")
    void updateProjectsStatus(Set<String> projectIds, ProjectStatus status);

    // Find projects created by a specific user
    @Query("{ 'org_id': ?0, 'createdBy': ?1 }")
    List<Project> findByOrganizationAndCreator(String orgId, String userId);

    // Custom query for dashboard statistics
    @Query(value = "{ 'org_id': ?0 }", fields = "{ 'status': 1 }")
    List<Project> findProjectStatusCounts(String orgId);

//    @Query("{ 'org_id.$id': ?0 }")
//    List<Project> findByOrganizationId(String orgId);

    List<Project> findByOrganization_Id(String orgId);
}