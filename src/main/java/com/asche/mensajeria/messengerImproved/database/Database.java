package com.asche.mensajeria.messengerImproved.database;

import java.util.HashMap;
import java.util.Map;

import com.asche.mensajeria.messengerImproved.model.Comment;
import com.asche.mensajeria.messengerImproved.model.Message;
import com.asche.mensajeria.messengerImproved.model.Profile;

public class Database {

	private static Map<String, Profile> profiles = new HashMap<>();
	private static Map<Long, Message> messages = new HashMap<>();

	public static Map<String, Profile> getProfiles() {
		if (profiles.size() == 0) {
			initProfiles();
		}
		return profiles;
	}
	public static Map<Long, Message> getMessages() {
		if (messages.size() == 0) {
			initMessages();
		}
		return messages;
	}

	private static void initProfiles() {
		profiles.put("MadHunter", new Profile(1, "MadHunter", "Rodrigo", "Lopez"));
		profiles.put("Camarografa", new Profile(2, "Camarografa", "Rocio", "Garcia"));

	}

	private static void initMessages() {
		messages.put(1L, new Message(1L, "Saludos Banda!!", "MadHunter"));
		messages.put(2L, new Message(2L, "Grabando con este calor", "Camarografa"));
		for (Message m : messages.values()) {
			initComments(m);
		}
	}

	private static void initComments(Message message) {
		Map<Long, Comment> comments = new HashMap<>();
		comments.put(1L, new Comment(1L, "Saludos Rodrigo", "Camarografa"));
		comments.put(2L, new Comment(2L, "Un buen tepache xD", "MadHunter"));

	}
}
