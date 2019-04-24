package com.asche.mensajeria.messengerImproved.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.asche.mensajeria.messengerImproved.database.Database;
import com.asche.mensajeria.messengerImproved.exception.DataNotFoundException;
import com.asche.mensajeria.messengerImproved.model.Comment;
import com.asche.mensajeria.messengerImproved.model.Message;

public class CommentService {
	private Map<Long, Message> messages = Database.getMessages();

	// get all comments
	public List<Comment> getAllComments(long messageID) {
		try {
			Map<Long, Comment> comments = messages.get(messageID).getComments();
			return new ArrayList<Comment>(comments.values());
		} catch (NullPointerException e) {
			throw new DataNotFoundException("Comments not found.");
		}
	}

	// get single comment
	public Comment getComment(long messageID, long commentID) {
		try {
			Map<Long, Comment> comments = messages.get(messageID).getComments();
			Comment comment = comments.get(commentID);
			if (comment == null) {
				throw new DataNotFoundException("Unable to find comment.");
			}
			return comment;
		} catch (NullPointerException e) {
			throw new DataNotFoundException("Unable to get comment, message not found");
		}
	}

	// add comment
	public Comment addComment(long messageID, Comment comment) {
		try {
			Map<Long, Comment> comments = messages.get(messageID).getComments();
			comment.setId(comments.size() + 1);
			comment.setCreated(new Date());
			comments.put(comment.getId(), comment);
			return comment;

		} catch (NullPointerException e) {
			throw new DataNotFoundException("Unable to add comment, message not found.");
		}
	}

	// update comment
	public Comment updateComment(long messageID, Comment comment) {
		try {
			Map<Long, Comment> comments = messages.get(messageID).getComments();
			if (!comments.containsKey(comment.getId())) {
				throw new DataNotFoundException("Unable to update, comment not found.");
			}
			comments.put(comment.getId(), comment);
			return comment;
		} catch (NullPointerException e) {
			throw new DataNotFoundException("Unable to update, message not found");
		}
	}

	// delete comment
	public Comment removeComment(long messageID, long commentID) {
		try {
			Map<Long, Comment> comments = messages.get(messageID).getComments();
			if (!comments.containsKey(commentID)) {
				throw new DataNotFoundException("Unable to delete, comment with id: "+commentID+" not found");
			}
			return comments.remove(commentID);
		} catch (NullPointerException e) {
			throw new DataNotFoundException("Unable to delete, message with id: "+messageID+" not found");
		}
	}
}
