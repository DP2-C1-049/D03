
package acme.features.technician.task;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.task.Involves;
import acme.entities.task.Task;

@Repository
public interface TechnicianTaskRepository extends AbstractRepository {

	@Query("select t from Task t where t.technician.id =:technicianId")
	Collection<Task> findTasksByTechnicianId(int technicianId);

	@Query("select t from Task t where t.id =:taskId ")
	Task findTaskById(int taskId);
	@Query("select i from Involves i where i.task.id = :id")
	Collection<Involves> findTaskInvolvedIn(int id);

	@Query("select t from Task t where t.ticker = :ticker")
	Task findTaskByTicker(String ticker);
	@Query("select t from Task t where t.draftMode = False")
	Collection<Task> findTasksPublish();

}
