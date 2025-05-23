
package acme.features.assistanceAgents.trackingLog;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claim.Claim;
import acme.entities.trackingLogs.ClaimStatus;
import acme.entities.trackingLogs.TrackingLog;
import acme.realms.AssistanceAgents;

@GuiService
public class AssistanceAgentTrackingLogCreateService extends AbstractGuiService<AssistanceAgents, TrackingLog> {

	@Autowired
	private TrackingLogRepository repository;


	@Override
	public void authorise() {
		boolean status = super.getRequest().getPrincipal().hasRealmOfType(AssistanceAgents.class);
		super.getResponse().setAuthorised(status);

	}
	@Override
	public void load() {
		TrackingLog trackingLog;

		trackingLog = new TrackingLog();
		trackingLog.setDraftMode(true);
		trackingLog.setLastUpdateMoment(MomentHelper.getCurrentMoment());

		super.getBuffer().addData(trackingLog);
	}

	@Override
	public void bind(final TrackingLog trackingLog) {
		super.bindObject(trackingLog, "lastUpdateMoment", "step", "resolutionPercentage", "status", "resolution", "claim");

	}

	@Override
	public void validate(final TrackingLog trackingLog) {
		boolean valid;
		valid = trackingLog.getResolutionPercentage() != null;
		super.state(valid, "ResolutionPercentage", "assistanceAgent.trackingLog.form.error.cantBeNull");

		if (trackingLog.getResolutionPercentage() != null && trackingLog.getResolutionPercentage() < 100.0) {
			valid = trackingLog.getStatus().equals(ClaimStatus.PENDING);
			super.state(valid, "status", "assistanceAgent.trackingLog.form.error.badStatus");
		} else if (trackingLog.getStatus() != null) {
			valid = !trackingLog.getStatus().equals(ClaimStatus.PENDING);
			super.state(valid, "status", "assistanceAgent.trackingLog.form.error.badStatus2");
		}
		if (trackingLog.getStatus() != null && trackingLog.getStatus().equals(ClaimStatus.PENDING)) {
			valid = trackingLog.getResolution() == null || trackingLog.getResolution().isBlank();
			super.state(valid, "resolution", "assistanceAgent.trackingLog.form.error.badResolution");
		} else {
			valid = trackingLog.getResolution() != null && !trackingLog.getResolution().isBlank();
			super.state(valid, "resolution", "assistanceAgent.trackingLog.form.error.badResolution2");
		}
		if (trackingLog.getClaim() != null) {
			TrackingLog highestTrackingLog;
			Optional<List<TrackingLog>> trackingLogs = this.repository.findOrderTrackingLog(trackingLog.getClaim().getId());
			if (trackingLog.getResolutionPercentage() != null && trackingLogs.isPresent() && trackingLogs.get().size() > 0) {
				highestTrackingLog = trackingLogs.get().get(0);
				long completedTrackingLogs = trackingLogs.get().stream().filter(t -> t.getResolutionPercentage() == 100).count();
				if (highestTrackingLog.getId() != trackingLog.getId())
					if (highestTrackingLog.getResolutionPercentage() == 100 && trackingLog.getResolutionPercentage() == 100) {
						valid = !highestTrackingLog.isDraftMode() && completedTrackingLogs < 2;
						super.state(valid, "resolutionPercentage", "assistanceAgent.trackingLog.form.error.maxcompleted");
					} else {
						valid = highestTrackingLog.getResolutionPercentage() < trackingLog.getResolutionPercentage();
						super.state(valid, "resolutionPercentage", "assistanceAgent.trackingLog.form.error.badPercentage");
					}
			}
		}

	}

	@Override
	public void perform(final TrackingLog trackingLog) {

		this.repository.save(trackingLog);
	}

	@Override
	public void unbind(final TrackingLog trackingLog) {

		Collection<Claim> claims;
		SelectChoices statusChoices;
		SelectChoices claimChoices;
		Dataset dataset;
		int assistanceAgentId;
		assistanceAgentId = super.getRequest().getPrincipal().getActiveRealm().getId();
		statusChoices = SelectChoices.from(ClaimStatus.class, trackingLog.getStatus());

		claims = this.repository.findClaimsByAssistanceAgent(assistanceAgentId);
		claimChoices = SelectChoices.from(claims, "id", trackingLog.getClaim());

		dataset = super.unbindObject(trackingLog, "claim", "lastUpdateMoment", "step", "resolutionPercentage", "status", "resolution", "draftMode", "id");
		dataset.put("statusChoices", statusChoices);
		dataset.put("claimChoices", claimChoices);

		super.getResponse().addData(dataset);

	}

}
