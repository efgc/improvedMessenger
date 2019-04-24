package com.asche.mensajeria.messengerImproved.resources;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Response.Status;

import com.asche.mensajeria.messengerImproved.beans.FilterBean;
import com.asche.mensajeria.messengerImproved.model.Link;
import com.asche.mensajeria.messengerImproved.model.Profile;
import com.asche.mensajeria.messengerImproved.service.ProfileService;
import com.sun.jndi.toolkit.url.Uri;

//Esta clase atiende las peticiones segun sea el metodo enviado
//Anotacion path indica la ruta que atendera
@Path("/profiles")
//Este endpoint es capaz de producir y consumir JSON y XML
@Produces(value = { MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
@Consumes(value = { MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
public class ProfileResource {

	ProfileService profileService = new ProfileService();

	@GET
	public List<Profile> getProfilesJson(@BeanParam FilterBean filterBean, @Context UriInfo uriInfo) {
		// List starts counting from 0 so we deduct 1
		List<Profile> profileList = new ArrayList<>();;
		
		filterBean.setStart(filterBean.getStart() - 1);
		if (filterBean.getYear() > 0) {
			profileList = profileService.getAllProfilesForYear(filterBean.getYear());
			
			return profileList;
		}
		if (filterBean.getStart() >= 0 && filterBean.getSize() > 0) {
			profileList= profileService.getAllProfilesPaginated(filterBean.getStart(), filterBean.getSize());
			return profileList;
		}
		profileList =profileService.getProfiles();
		//Agrega links a cada perfil
		for(Profile profile:profileList) {
			this.addLinks(uriInfo, profile);
		}
		//TODO RETURN RESPONSE
		return profileList;
	}

	// Get specific profile GET
	@GET
	@Path("/{profileName}")
	public Response getProfile(@PathParam("profileName") String profileName, @Context UriInfo uriInfo) {
		Profile profile = profileService.getProfile(profileName);
		addLinks(uriInfo, profile);
		//TODO all messages for profile in link
		return Response.ok().entity(profile).build();
	}

	

	// Add profile POST
	@POST
	public Response addProfile(Profile profile, @Context UriInfo uriInfo) {
		Profile newProfile =profileService.addProfile(profile);
		URI uri = uriInfo.getAbsolutePathBuilder().path(newProfile.getProfileName()).build();
		return Response.created(uri).entity(newProfile).build();
	}

	// Update profile PUT
	@PUT
	@Path("/{profileName}")
	public Response updateProfile(@PathParam("profileName") String profileName, Profile profile) {
		profile.setProfileName(profileName);
		Profile updatedProfile = profileService.updateProfile(profile);
		return Response.status(Status.OK).entity(updatedProfile).build();
	}

	// Delete profile DELETE
	@DELETE
	@Path("/{profileName}")
	public Response deleteProfile(@PathParam("profileName") String profileName) {
		Profile removeProfile = profileService.removeProfile(profileName);
		//TODO check if it is better to return property as null or empty
		removeProfile.setLinks(null);
		return Response.status(Status.OK).entity(removeProfile).build();
	}
	
	//METODOS QUE AGREGAN LINKS DE HATEOAS
	//METODO PARA SELF
	private String getGenericLink(UriInfo uriInfo, Profile profile) {
		String link = uriInfo.getBaseUriBuilder()
				.path(ProfileResource.class)
				.path(profile.getProfileName())
				.build().toString();
		return link;
	}
	//METODO PARA ALL MESSAGES
	private String getLinkForAllMessages(UriInfo uriInfo, Profile profile) {
		String link = uriInfo.getBaseUriBuilder()
				.path(MessageResource.class)
				.path(MessageResource.class, "getMessagesByAuthor")
				.resolveTemplate("author", profile.getProfileName())
				.build()
				.toString();
		return link;
	}
	
	private void addLinks(UriInfo uriInfo, Profile profile) {
		profile.setLinks(new ArrayList<Link>());
		profile.addLink(this.getGenericLink(uriInfo, profile), "self", "GET");
		profile.addLink(this.getGenericLink(uriInfo, profile), "update","UPDATE" );
		profile.addLink(this.getGenericLink(uriInfo, profile), "delete","DELETE" );
		profile.addLink(this.getLinkForAllMessages(uriInfo, profile), "all messages", "GET");
	}

}
