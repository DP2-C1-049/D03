
package acme.features.customer.booking;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.booking.Booking;
import acme.entities.flight.Flight;

@Repository
public interface CustomerBookingRepository extends AbstractRepository {

	@Query("select b from Booking b where b.customers.id = :customerId")
	Collection<Booking> getAllBookingOf(int customerId);

	@Query("select b from Booking b where b.id = :id")
	Booking getBookingById(int id);

	@Query("select f from Flight f")
	Collection<Flight> findAllFlights();

	@Query("select b from Booking b where b.locatorCode= :locatorCode")
	Booking findBookingByLocator(String locatorCode);

}
