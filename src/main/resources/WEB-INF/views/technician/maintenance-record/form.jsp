<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form> 

     <acme:hidden-data path="maintenanceRecordId"/>
     
 
		<acme:input-moment code="technician.maintenance-record.form.label.moment" path="moment" readonly="true"/>
		<acme:input-select path="status" code="technician.maintenance-record.form.label.status" choices="${statuses}"/>
	
	<acme:input-moment code="technician.maintenance-record.form.label.next-inspection" path="nextInspectionDueTime" placeholder="technician.maintenance-record.form.placeholder.next-inspection"/>
	<acme:input-select code="technician.maintenance-record.form.label.aircraft" path="aircraft" choices="${aircrafts}"/>
	<acme:input-money code="technician.maintenance-record.form.label.estimated-cost" path="estimatedCost" placeholder="technician.maintenance-record.form.placeholder.estimated-cost"/>
	<acme:input-textarea code="technician.maintenance-record.form.label.notes" path="notes"/>

	<jstl:choose>	 
		<jstl:when test="${acme:anyOf(_command, 'show') && draftMode == false}">
		<acme:button code="technician.maintenance-record.form.button.involves" action="/technician/involves/list?masterId=${id}"/>
		</jstl:when>
		<jstl:when test="${acme:anyOf(_command, 'show|update|publish') && draftMode == true}">
			<acme:button code="technician.maintenance-record.form.button.involves" action="/technician/involves/list?masterId=${id}"/>
			<acme:submit code="technician.maintenance-record.form.button.update" action="/technician/maintenance-record/update"/>
			<acme:submit code="technician.maintenance-record.form.button.publish" action="/technician/maintenance-record/publish"/>
			<acme:submit code="technician.maintenance-record.form.button.delete" action="/technician/maintenance-record/delete"/>
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="technician.maintenance-record.form.button.create" action="/technician/maintenance-record/create"/>
		</jstl:when>	
	</jstl:choose>
</acme:form>