package osu.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import osu.project.enums.ProjectStatus;
import osu.project.model.Project;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    List<Project> findByProjectCodeIgnoreCase(String projectCode);

    List<Project> findByProjectNameIgnoreCaseContaining(String projectName);

    List<Project> findByProjectStatus(ProjectStatus projectStatus);

    List<Project> findAllByOrderByProjectStartAsc();

    List<Project> findAllByOrderByProjectStartDesc();

    List<Project> findAllByOrderByProjectEndAsc();

    List<Project> findAllByOrderByProjectEndDesc();

}