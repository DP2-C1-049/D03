
package acme.entities.airport;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidEmail;
import acme.client.components.validation.ValidString;
import acme.client.components.validation.ValidUrl;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Airport extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@Automapped
	@ValidString(min = 1, max = 50)
	private String				name;

	@Mandatory
	@Automapped
	@ValidString(pattern = "^[A-Z]{3}$")
	@Column(unique = true)
	private String				iATACode;

	@Mandatory
	@Automapped
	@Valid
	private Scope				scope;

	@Mandatory
	@Automapped
	@ValidString(min = 1, max = 50)
	private String				city;

	@Mandatory
	@Automapped
	@ValidString(min = 1, max = 50)
	private String				country;

	@Optional
	@Automapped
	@ValidUrl
	private String				website;

	@Optional
	@Automapped
	@ValidEmail
	private String				email;

	@Optional
	@Automapped
	@ValidString(pattern = "^\\+?\\d{6,15}$")
	private String				phoneNumber;
}
