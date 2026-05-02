package com.matchpuff.profileservice.infrastructure.adapters.persistence.mapper;

import java.util.Collections;
import java.util.List;

import com.matchpuff.profileservice.domain.model.Schedule;
import com.matchpuff.profileservice.domain.model.StudentProfile;
import com.matchpuff.profileservice.domain.model.Tag;
import com.matchpuff.profileservice.infrastructure.adapters.persistence.entity.ScheduleDocument;
import com.matchpuff.profileservice.infrastructure.adapters.persistence.entity.StudentProfileDocument;
import com.matchpuff.profileservice.infrastructure.adapters.persistence.entity.TagDocument;
import com.matchpuff.profileservice.infrastructure.adapters.persistence.entity.UserType;

public class UserMapper {

    private UserMapper() {
        // Private constructor to prevent instantiation
    }

	public static StudentProfileDocument toDocument(StudentProfile profile) {
		if (profile == null) {
			return null;
		}

		StudentProfileDocument doc = new StudentProfileDocument();
		doc.setId(profile.getId());
		doc.setUserType(UserType.STUDENT);
		doc.setName(profile.getName());
		doc.setEmail(profile.getEmail()); 
		doc.setGender(profile.getGender());
		doc.setBirthdate(profile.getDateOfBirth() == null ? null : profile.getDateOfBirth().atStartOfDay());
		doc.setCreatedAt(profile.getCreatedAt());
		doc.setPhoto(profile.getPhotoUrl());
		doc.setCareer(profile.getCareer());
		doc.setSemester(profile.getSemester());
		doc.setBiography(profile.getBiography());
		doc.setPrivacyLevel(profile.getPrivacyLevel());
		doc.setSchedule(toScheduleDocumentList(profile.getSchedules()));
		doc.setInterests(toTagDocumentList(profile.getTags()));

		return doc;
	}

	public static StudentProfile toDomain(StudentProfileDocument doc) {
		if (doc == null) {
			return null;
		}

		StudentProfile profile = new StudentProfile();
		profile.setId(doc.getId());
		profile.setName(doc.getName());
		profile.setEmail(doc.getEmail());
		profile.setGender(doc.getGender());
		profile.setDateOfBirth(doc.getBirthdate() == null ? null : doc.getBirthdate().toLocalDate());
		profile.setCreatedAt(doc.getCreatedAt());
		profile.setPhotoUrl(doc.getPhoto());
		profile.setCareer(doc.getCareer());
		profile.setSemester(doc.getSemester() == null ? 0 : doc.getSemester());
		profile.setBiography(doc.getBiography());
		profile.setPrivacyLevel(doc.getPrivacyLevel());
		profile.setSchedules(toScheduleList(doc.getSchedule()));
		profile.setTags(toTagList(doc.getInterests()));

		return profile;
	}

	public static List<StudentProfile> toDomainList(List<StudentProfileDocument> docs) {
		if (docs == null) {
			return Collections.emptyList();
		}
		return docs.stream().map(UserMapper::toDomain).toList();
	}

	public static List<Schedule> toScheduleList(List<ScheduleDocument> scheduleDocs) {
		if (scheduleDocs == null) {
			return Collections.emptyList();
		}

		return scheduleDocs.stream().map(doc -> {
			Schedule schedule = new Schedule();
			schedule.setDayOfWeek(doc.getDayOfWeek());
			schedule.setName(doc.getName());
			schedule.setStartTime(doc.getStartHour());
			schedule.setEndTime(doc.getFinishHour());
			return schedule;
		}).toList();
	}

	public static List<Tag> toTagList(List<TagDocument> tagDocs) {
		if (tagDocs == null) {
			return Collections.emptyList();
		}

		return tagDocs.stream().map(doc -> {
			Tag tag = new Tag();
			tag.setName(doc.getName());
			tag.setCategory(doc.getCategory());
			return tag;
		}).toList();
	}

	private static List<ScheduleDocument> toScheduleDocumentList(List<Schedule> schedules) {
		if (schedules == null) {
			return Collections.emptyList();
		}

		return schedules.stream().map(schedule -> {
			ScheduleDocument doc = new ScheduleDocument();
			doc.setDayOfWeek(schedule.getDayOfWeek());
			doc.setName(schedule.getName());
			doc.setStartHour(schedule.getStartTime());
			doc.setFinishHour(schedule.getEndTime());
			return doc;
		}).toList();
	}

	private static List<TagDocument> toTagDocumentList(List<Tag> tags) {
		if (tags == null) {
			return Collections.emptyList();
		}

		return tags.stream().map(tag -> {
			TagDocument doc = new TagDocument();
			doc.setName(tag.getName());
			doc.setCategory(tag.getCategory());
			return doc;
		}).toList();
	}
}
