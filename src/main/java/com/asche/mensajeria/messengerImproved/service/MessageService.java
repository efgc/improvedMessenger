package com.asche.mensajeria.messengerImproved.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;


import com.asche.mensajeria.messengerImproved.database.Database;
import com.asche.mensajeria.messengerImproved.exception.DataNotFoundException;
import com.asche.mensajeria.messengerImproved.exception.GenericException;
import com.asche.mensajeria.messengerImproved.model.Message;

public class MessageService {
	private Map<Long, Message> messages = Database.getMessages();

	public MessageService() {

	}

	// Get all messages
	public List<Message> getAllMessages() {
		List<Message> messageList = new ArrayList<Message>(messages.values());
		if (messageList.size() == 0) {
			throw new DataNotFoundException("No messages found.");
		}
		return messageList;
	}

	// Get all messages by year
	public List<Message> getAllMessagesForYear(int year) {
		List<Message> messageList = new ArrayList<Message>();
		Calendar cal = Calendar.getInstance();
		for (Message msg : messages.values()) {
			cal.setTime(msg.getCreated());
			if (year == cal.get(Calendar.YEAR)) {
				messageList.add(msg);
			}
		}
		if (messageList.size() == 0) {
			throw new DataNotFoundException("No messages found for year: " + year);
		}
		return messageList;

	}
	// Get all messages paginated
	public List<Message> getAllMessagesPaginated(int start, int size){
		ArrayList<Message> messageList = new ArrayList<Message> (messages.values());
		if(start+size > messageList.size()) {
			throw new GenericException("No messages fount for given parameters.", 422);
		}
		return messageList.subList(start, size);
	}
	//Get messages by author
	public List<Message> getMessagesByAuthor(String author){
		List<Message> list = new ArrayList<Message>();
		for(Message m:messages.values()) {
			if(m.getAuthor().equals(author)) {
				list.add(m);
			}
		}
		return list;
	}
	
	// Get single message
	public Message getMessage(long id) {
		Message message =messages.get(id);
		if(message == null) {
			throw new DataNotFoundException("Message with id "+id+"not found.");
		}
		return message;
	}
	// Add new message
	public Message addMessage(Message message) {
		message.setId(messages.size()+1);
		messages.put(message.getId(), message);
		return message;
	}
	// Update message
	public Message updateMessage(Message message) {
		/*TODO verificar que el id del mensaje sea el mismo que el id viejo
		 * que sea la misma fecha y el mismo autor
		 */
		//verificar que el id no sea 0, lanzar exception
		if(message.getId()<=0) {
			throw new GenericException("Unable to update, invalid id.", 422);
		}
		//verificar que el id se encuentre en los mensajes existentes
		else if(!messages.containsKey(message.getId())) {
			throw new DataNotFoundException("Unable to update, message with id: "+message.getId()+" not found.");
		}
		//verificaciones sobre los campos
		Message oldMessage = messages.get(message.getId());
		if(!message.getAuthor().equals(oldMessage.getAuthor())) {
			message.setAuthor(oldMessage.getAuthor());
		}
		if(!message.getCreated().equals(oldMessage.getCreated())) {
			message.setCreated(oldMessage.getCreated());
		}
		//agregar el mensaje al map
		messages.put(message.getId(), message);
		//retornar el mensaje
		return message;
	}
	// Remove message
	public Message removeMessage(long id) {
		if (!messages.containsKey(id)) {
			throw new DataNotFoundException(
					"Unable to delete, message with id: " + id + " not found.");
		}
		return messages.remove(id);
	}
}
