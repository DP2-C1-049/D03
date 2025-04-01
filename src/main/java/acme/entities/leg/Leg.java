
package acme.entities.leg;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidString;
import acme.entities.aircraft.Aircraft;
import acme.entities.airport.Airport;
import acme.entities.flight.Flight;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Leg extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@Automapped
	@ValidString
	@Column(unique = true)
	private String				flightNumber;

	@Mandatory
	@Automapped
	@ValidMoment
	@Temporal(TemporalType.TIMESTAMP)
	private Date				departure;

	@Mandatory
	@Automapped
	@ValidMoment
	@Temporal(TemporalType.TIMESTAMP)
	private Date				arrival;


	@Transient()
	public Double getDuration() {

		long departureMilieconds = this.getDeparture().getTime();
		long arrivalMilieconds = this.getArrival().getTime();
		return (arrivalMilieconds - departureMilieconds) / 3600000.0;

	}


	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Flight		flight;

	@Mandatory
	@Automapped
	@Valid
	private Status		status;

	@Mandatory
	@Automapped
	@Valid
	@ManyToOne
	private Airport		departureAirport;

	@Mandatory
	@Automapped
	@Valid
	@ManyToOne
	private Airport		arrivalAirport;

	@Mandatory
	@Automapped
	@Valid
	@ManyToOne
	private Aircraft	aircraft;
}
