package com.asche.mensajeria.messengerImproved.resources;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.asche.mensajeria.messengerImproved.model.Comment;
import com.asche.mensajeria.messengerImproved.service.CommentService;

import io.swagger.annotations.Api;

@Path("/")
@Api(tags= {"Comments"})
@Produces(value = { MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
@Consumes(value = { MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
public class CommentResource {
	private CommentService commentService = new CommentService();

	@GET
	public List<Comment> getAllComments(@PathParam("messageID") long messageID){
		return commentService.getAllComments(messageID);
	}
	@GET
	@Path("/{commentID}")
	public Comment getComment(@PathParam("messageID") long messageID, 
			@PathParam("commentID") long commentID) {
		return commentService.getComment(messageID, commentID);
	}
	@POST
	public Comment addComment(@PathParam("messageID") long messageID,Comment comment) {
		return commentService.addComment(messageID, comment);
	}
	@PUT
	@Path("/{commentID}")
	public Comment updateComment(@PathParam("messageID") long messageID,
			@PathParam("commentID") long commentID, Comment comment) {
		comment.setId(commentID);
		return commentService.updateComment(messageID, comment);
	}
	@DELETE
	@Path("/{commentID}")
	public Comment removeComment(@PathParam("messageID") long messageID,
			@PathParam("commentID") long commentID){
				return commentService.removeComment(messageID, commentID);
			}
}
