package io.agileintelligence.ppmtool.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import io.agileintelligence.ppmtool.domain.Backlog;
import io.agileintelligence.ppmtool.domain.Project;
import io.agileintelligence.ppmtool.domain.User;
import io.agileintelligence.ppmtool.exceptions.ProjectIdException;
import io.agileintelligence.ppmtool.exceptions.ProjectNotFoundException;
import io.agileintelligence.ppmtool.repositories.BacklogRepository;
import io.agileintelligence.ppmtool.repositories.ProjectRepository;
import io.agileintelligence.ppmtool.repositories.UserRepository;

@Service
public class ProjectServices {

	
	@Autowired
	private ProjectRepository projectRepository;
	
	@Autowired
	private BacklogRepository backlogRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	public Project saveOrUpdateProject(Project project, String username) {
		
		// project.getId != null
		// find by db id -> null
		
		// if update, id already exists
		if(project.getId() != null) {
			Project existingProject = projectRepository.findByProjectIdentifer(project.getProjectIdentifer()).get(0);
		   
			if(existingProject != null && (!existingProject.getProjectLeader().equals(username))) {
				throw new ProjectNotFoundException("Project not found in you account");
			}else if(existingProject == null) {
				throw new ProjectNotFoundException("Project with ID: '"+project.getProjectIdentifer()+"' cannot be updated because it doesn't exist");
			}
			
		}
		
		try {
			
			User user = userRepository.findByUsername(username);
			
			project.setUser(user);
			project.setProjectLeader(user.getUsername());
			
			
			project.setProjectIdentifer(project.getProjectIdentifer().toUpperCase());
			
			// update x, create 할때..
			if(project.getId() == null) {
				Backlog backlog = new Backlog();
				project.setBacklog(backlog);
				backlog.setProject(project);
				backlog.setProjectIdentifier(project.getProjectIdentifer().toUpperCase());
			}
			
			if(project.getId() != null) {
				project.setBacklog(backlogRepository.findByProjectIdentifier(project.getProjectIdentifer().toUpperCase()));
			}
			
			return projectRepository.save(project);
		}catch(Exception e){
			throw new ProjectIdException("Project ID '" + project.getProjectIdentifer().toUpperCase() + "' already exists");
		}
		
	
	}
	
	
	
	  public List<Project> findProjectByIdentifier(String projectId, String username) {
	  
	 	  
		 List<Project> project =  projectRepository.findByProjectIdentifer(projectId.toUpperCase());

		 
		 if(project.isEmpty()) {
			 throw new ProjectIdException("Project'" + projectId + "'does not exist");
		 }
		 
		 if(!project.get(0).getProjectLeader().equals(username)) {
			 throw new ProjectNotFoundException("Project not found in your account");
		 }
		
		 
		 return project;
	  
	  }
	 
	  
	  public Iterable<Project> findAllProjects(String username){
		return projectRepository.findAllByProjectLeader(username);  
	  }
	  
	  public void deleteProjectByIdentifier(String projectId, String username) {
		/*
		 * List<Project> project =
		 * projectRepository.findByProjectIdentifer(projectId.toUpperCase());
		 * 
		 * if(project.isEmpty()) { throw new
		 * ProjectIdException("Cannot Project with ID '"
		 * +projectId+"'. This project does not exist."); }
		 * 
		 * for(Project pro : project) { projectRepository.delete(pro); }
		 */
		  
		  projectRepository.delete(findProjectByIdentifier(projectId, username).get(0));
		  
	  }
	
	
}
