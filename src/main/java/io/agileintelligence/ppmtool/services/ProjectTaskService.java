package io.agileintelligence.ppmtool.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;

import io.agileintelligence.ppmtool.domain.Backlog;
import io.agileintelligence.ppmtool.domain.Project;
import io.agileintelligence.ppmtool.domain.ProjectTask;
import io.agileintelligence.ppmtool.exceptions.ProjectIdException;
import io.agileintelligence.ppmtool.exceptions.ProjectNotFoundException;
import io.agileintelligence.ppmtool.repositories.BacklogRepository;
import io.agileintelligence.ppmtool.repositories.ProjectRepository;
import io.agileintelligence.ppmtool.repositories.ProjectTaskRepository;

@Service
public class ProjectTaskService {

	
	@Autowired
	private BacklogRepository backlogRepository;
	
	@Autowired
	private ProjectTaskRepository projectTaskRepository;
	
	@Autowired
	private ProjectRepository projectRepository;
	
	@Autowired
	private ProjectServices projectService;
	
	public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask, String username) {
		
		//try {
			//PTs to be added to a specific project, project != null, BL exists
			//Backlog backlog = backlogRepository.findByProjectIdentifier(projectIdentifier);
		    Backlog backlog = projectService.findProjectByIdentifier(projectIdentifier, username).get(0).getBacklog();
			
			
			//set the backlog to pt
			projectTask.setBacklog(backlog);
			
			//we want our project sequence to be like this : IDPRO-1 IDPRO-2 ...100 101
			Integer BacklogSequence = backlog.getPTSequence();
		
			//Update the BL SEQUENCE
			BacklogSequence++;
			
			backlog.setPTSequence(BacklogSequence);
			
			//Add Sequence to Project Task
			projectTask.setProjectSequence(projectIdentifier+"-"+BacklogSequence);
			projectTask.setProjectIdentifier(projectIdentifier);
			
			//INITIAL priority when priority null
			//fix bug with priority in spring, needs to check null first
			if(projectTask.getPriority()==null || projectTask.getPriority() == 0) {
				// in the future we need projectTask.getPriority() == 0 to handle the form
				projectTask.setPriority(3);
			}
			
			//INITIAL status when status is null
			if(projectTask.getStatus()=="" || projectTask.getStatus()==null) {
				projectTask.setStatus("TO_DO");
			}
			
			return projectTaskRepository.save(projectTask);
			
			
		//}catch(Exception e) {
		//	throw new ProjectNotFoundException("Project Not Found");
		//}
	
	}
	
	public Iterable<ProjectTask> findBacklogById(String id, String username){
		
		//List<Project> project = projectRepository.findByProjectIdentifer(id);
		
		 //if(project.isEmpty()) {
		 //	 throw new ProjectNotFoundException("Project with ID : '" + id + "' does not exist");
		 //}
		projectService.findProjectByIdentifier(id, username);
		
		return projectTaskRepository.findByProjectIdentifierOrderByPriority(id);
	}
	
	public ProjectTask findByProjectSequence(String backlog_id, String pt_id, String username) {
		
		// make sure we are searching on an existing backlog
		//Backlog backlog = backlogRepository.findByProjectIdentifier(backlog_id);
		//if(backlog==null) {
		//	throw new ProjectNotFoundException("Project with ID : '" + backlog_id + "' does not exist");
		//}
		projectService.findProjectByIdentifier(backlog_id, username);
		
		// make sure that our task exists		
		ProjectTask projectTask = projectTaskRepository.findByProjectSequence(pt_id);
		if(projectTask == null) {
			throw new ProjectNotFoundException("Project Task : '" + pt_id + "' not found");
		}
		
		// make sure we are searching on the right backlog
		if(!projectTask.getProjectIdentifier().equals(backlog_id)) {
			throw new ProjectNotFoundException("Project Task : '" + pt_id + "' does not exist in project(backlog): '"+ backlog_id);
		}
		
		return projectTask;
		
	}
	
	public ProjectTask updateByProjectSequence(ProjectTask updatedTask, String backlog_id, String pt_id, String username) {
		
		ProjectTask projectTask = findByProjectSequence(backlog_id, pt_id, username);
		
		projectTask = updatedTask;
		
		return projectTaskRepository.save(projectTask);
		
	}
	
	public void deletePTByProjectSequence(String backlog_id, String pt_id, String username) {
		ProjectTask projectTask = findByProjectSequence(backlog_id, pt_id, username);

// if dont't use orphan and refresh, you should use this.
//		Backlog backlog = projectTask.getBacklog();
//		List<ProjectTask> pts = backlog.getProjectTasks();
//		pts.remove(projectTask);
//		backlogRepository.save(backlog);
		
		projectTaskRepository.delete(projectTask);
	}
	
	
	
	// Update project task
	
	// Find existing project task
	
	// Replace it with updated task
	
	// Save update
	
	
	
	
}
