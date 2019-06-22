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
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import com.asche.mensajeria.messengerImproved.beans.FilterBean;
import com.asche.mensajeria.messengerImproved.model.Link;
import com.asche.mensajeria.messengerImproved.model.Message;
import com.asche.mensajeria.messengerImproved.service.MessageService;

import io.swagger.annotations.Api;

@Path("/messages")
@Api(tags= {"Messages"})
@Consumes(value = { MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
@Produces(value = { MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
public class MessageResource {
	MessageService messageService = new MessageService();

	// get all messages
	@GET
	public List<Message> getMessages(@BeanParam FilterBean filterBean,@Context UriInfo uriInfo) {
		filterBean.setStart(filterBean.getStart() - 1);
		//adding hateoas to messages
		for(Message m:messageService.getAllMessages()) {
			addLinks(uriInfo, m);
		}
		if (filterBean.getYear() > 0) {
			return messageService.getAllMessagesForYear(filterBean.getYear());
		}
		if (filterBean.getStart() >= 0 && filterBean.getSize() > 0) {
			return messageService.getAllMessagesPaginated(filterBean.getStart(), filterBean.getSize());
		}

		
		return messageService.getAllMessages();
	}

	// get single message
	@GET
	@Path("/{messageID}")
	public Response getMessage(@PathParam("messageID") Long messageID, @Context UriInfo uriInfo) {
		Message message = messageService.getMessage(messageID);
		
		addLinks(uriInfo, message);
		return Response.status(Status.OK).entity(message).build();

	}
	//TODO revisar el devolver todos los mensajes de author
	@GET
	@Path("/{author}/messages")
	public List<Message> getMessagesByAuthor(@PathParam("author") String author, @Context UriInfo uriInfo) {
		List<Message> messages = messageService.getMessagesByAuthor(author);
		
		//addLinks(uriInfo, message);
		//TODO serialize list for response
		return messages;

	}

	// add new message
	@POST
	public Response addMessage(@Context UriInfo uriInfo, Message message) {
		Message newMessage = messageService.addMessage(message);
		URI uri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(newMessage.getId())).build();
		return Response.created(uri).entity(newMessage).build();
	}

	// update message
	@PUT
	@Path("/{messageID}")
	public Response updateMessage(@PathParam("messageID") long messageID, Message message) {
		message.setId(messageID);
		Message updatedMessage = messageService.updateMessage(message);
		return Response.status(Status.OK).entity(updatedMessage).build();
	}

	// delete message
	@DELETE
	@Path("/{messageID}")
	public Response removeMessage(@PathParam("messageID") long messageID) {
		Message message = messageService.removeMessage(messageID);
		return Response.status(Status.OK).entity(message).build();
	}
	
	@Path("/{messageID}/comments")
	public CommentResource getCommentResource() {
		return new CommentResource();
	}
	
	/*HATEOAS
	 * self, get, update, delete, comments, author profile
	 */
	//link for self
	private String getGenericLink(UriInfo uriInfo, Message message) {
		String link =uriInfo.getBaseUriBuilder()
				.path(MessageResource.class)
				.path(Long.toString(message.getId()))
				.build()
				.toString();
		return link;
	}
	
	//link for author profile
	private String getLinkForAuthorProfile(UriInfo uriInfo, Message message) {
		String link =uriInfo.getBaseUriBuilder()
				.path(ProfileResource.class)
				.path(message.getAuthor())
				.build()
				.toString();
		return link;
	}
	//TODO implement get comments
	
	private void addLinks(UriInfo uriInfo, Message message) {
		message.setLinks(new ArrayList<Link>());
		message.addLink(getGenericLink(uriInfo, message), "self", "GET");
		message.addLink(getGenericLink(uriInfo, message), "update", "UPDATE");
		message.addLink(getGenericLink(uriInfo, message), "delete", "DELETE");
		message.addLink(getLinkForAuthorProfile(uriInfo, message), "author profile", "GET");
	}
	
}
