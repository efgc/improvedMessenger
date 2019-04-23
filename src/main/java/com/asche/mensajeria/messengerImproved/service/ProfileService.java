package com.asche.mensajeria.messengerImproved.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import com.asche.mensajeria.messengerImproved.database.Database;
import com.asche.mensajeria.messengerImproved.exception.DataNotFoundException;
import com.asche.mensajeria.messengerImproved.exception.GenericException;
import com.asche.mensajeria.messengerImproved.model.Profile;

//Esta clase contiene los metodos necesarios para hacer el CRUD con los datos
//de la base de datos
public class ProfileService {

	private Map<String, Profile> profiles = Database.getProfiles();

	// get all profiles
	public List<Profile> getProfiles() {
		List<Profile> profileList = new ArrayList<Profile>(profiles.values());
		if (profileList.size() == 0) {
			throw new DataNotFoundException("No profiles found.");
		}
		return profileList;

	}

	// get profiles for a given year
	public List<Profile> getAllProfilesForYear(int year) {
		List<Profile> profilesForYear = new ArrayList<>();
		Calendar cal = Calendar.getInstance();
		for (Profile pro : profiles.values()) {
			cal.setTime(pro.getCreationDate());
			if (cal.get(Calendar.YEAR) == year) {
				profilesForYear.add(pro);
			}
		}
		if (profilesForYear.size() == 0) {
			throw new DataNotFoundException("No profiles found for year " + year);
		}
		return profilesForYear;
	}

	// get profiles paginated
	public List<Profile> getAllProfilesPaginated(int start, int size) {
		List<Profile> list = new ArrayList<Profile>(profiles.values());
		if (start + size > list.size()) {
			throw new GenericException("Unable to get profiles, invalid parameters.", 422);
		}
		return list.subList(start, start + size);
	}

	// get individual profile
	public Profile getProfile(String profileName) {
		Profile profile = profiles.get(profileName);
		if (profile == null) {
			throw new DataNotFoundException("Profile with name: " + profileName + " not found.");
		}
		return profile;
	}

	// add profile
	public Profile addProfile(Profile profile) {
		profile.setId(profiles.size() + 1);
		profiles.put(profile.getProfileName(), profile);
		return profile;

	}

	// update profile
	public Profile updateProfile(Profile profileNew) {
		// verifying if profileName is given
		if (profileNew.getProfileName() == "") {
			throw new GenericException("Unable to update, invalid profile name.",422);

		}
		// verifying if profile is in profile list
		else if (!profiles.containsKey(profileNew.getProfileName())) {
			throw new DataNotFoundException(
					"Unable to update, profile with name: " + profileNew.getProfileName() + " not found.");

		}
		Profile profileOld = profiles.get(profileNew.getProfileName());
		// verifying if new profileId is the same as old
		if (profileNew.getId() != profileOld.getId()) {
			profileNew.setId(profileOld.getId());
		}
		profiles.put(profileNew.getProfileName(), profileNew);
		return profileNew;
	}

	// remove profile
	public Profile removeProfile(String profileName) {
		if (!profiles.containsKey(profileName)) {
			throw new DataNotFoundException("Unable to delete, profile with name: " + profileName + " not found.");
		}
		return profiles.remove(profileName);
	}

}
