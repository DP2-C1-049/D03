
package acme.features.assistanceAgents.trackingLog;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.trackingLogs.TrackingLog;
import acme.realms.AssistanceAgents;

@GuiController
public class AssistanceAgentTrackingLogController extends AbstractGuiController<AssistanceAgents, TrackingLog> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AssistanceAgentTrackingLogListService		listService;

	@Autowired
	private AssistanceAgentTrackingLogShowService		showService;
	@Autowired
	private AssistanceAgentTrackingLogCreateService		createService;

	@Autowired
	private AssistanceAgentTrackingLogUpdateService		updateService;

	@Autowired
	private AssistanceAgentTrackingLogPublishService	publishService;

	@Autowired
	private AssistanceAgentTrackingLogDeleteService		deleteService;


	// Constructors -----------------------------------------------------------
	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("update", this.updateService);
		super.addBasicCommand("delete", this.deleteService);
		super.addCustomCommand("publish", "update", this.publishService);
	}

}
