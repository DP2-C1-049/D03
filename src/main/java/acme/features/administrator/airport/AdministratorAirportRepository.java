
package acme.features.administrator.airport;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.airport.Airport;

@Repository
public interface AdministratorAirportRepository extends AbstractRepository {

	@Query("select a from Airport a")
	Collection<Airport> getAll();

	@Query("select a from Airport a where a.id = :id")
	Airport findAirportById(int id);

	@Query("SELECT a FROM Airport a where a.iataCode = :iataCode")
	Airport findAirportByIataCode(String iataCode);
}
