
package acme.features.manager.flight;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flight.Flight;
import acme.entities.leg.Leg;
import acme.features.manager.leg.ManagerLegRepository;
import acme.realms.Manager;

@GuiService
public class ManagerFlightPublishService extends AbstractGuiService<Manager, Flight> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private ManagerFlightRepository	repository;

	@Autowired
	private ManagerLegRepository	legRepository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean isManager;
		int flightId;
		Flight flight;
		flightId = super.getRequest().getData("id", int.class);
		flight = this.repository.findById(flightId);
		if (flight != null) {
			int managerId = super.getRequest().getPrincipal().getActiveRealm().getId();
			isManager = flight.getManager().getId() == managerId;
		} else
			isManager = false;
		super.getResponse().setAuthorised(isManager && flight.isDraftMode());
	}

	@Override
	public void load() {
		int id = super.getRequest().getData("id", int.class);
		Flight flight = this.repository.findFlightById(id);
		super.getBuffer().addData(flight);
	}

	@Override
	public void bind(final Flight flight) {
	}

	@Override
	public void validate(final Flight flight) {
		Collection<Leg> legs = this.legRepository.findLegsByflightId(flight.getId());
		boolean hasLeg = !legs.isEmpty();
		super.state(hasLeg, "*", "flight.publish.error.noLegs");
	}

	@Override
	public void perform(final Flight flight) {
		flight.setDraftMode(false);
		this.repository.save(flight);
	}

	@Override
	public void unbind(final Flight flight) {
		Dataset dataset = super.unbindObject(flight, "tag", "indication", "cost", "description");
		dataset.put("departure", flight.getDeparture());
		dataset.put("arrival", flight.getArrival());
		dataset.put("originCity", flight.getOriginCity());
		dataset.put("destinationCity", flight.getDestinationCity());
		dataset.put("numberOfLayovers", flight.getNumberOfLayovers());
		super.getResponse().addData(dataset);
	}
}
