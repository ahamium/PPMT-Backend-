package io.agileintelligence.ppmtool.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import io.agileintelligence.ppmtool.domain.Project;

@Repository
public interface ProjectRepository extends CrudRepository<Project, Long> {

	
	List<Project> findByProjectIdentifer(String projectId);
	
    @Override
    Iterable<Project> findAll();
    
    Iterable<Project> findAllByProjectLeader(String username);
    
    
    
}
