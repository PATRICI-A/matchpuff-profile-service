package com.matchpuff.profileservice.infrastructure.adapters.persistence.mapper;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.matchpuff.profileservice.domain.model.Admin;
import com.matchpuff.profileservice.domain.model.Organizer;
import com.matchpuff.profileservice.domain.model.Schedule;
import com.matchpuff.profileservice.domain.model.StudentProfile;
import com.matchpuff.profileservice.domain.model.Tag;
import com.matchpuff.profileservice.domain.model.User;
import com.matchpuff.profileservice.infrastructure.adapters.persistence.entity.AdminProfileDocument;
import com.matchpuff.profileservice.infrastructure.adapters.persistence.entity.OrganizerProfileDocument;
import com.matchpuff.profileservice.infrastructure.adapters.persistence.entity.ScheduleDocument;
import com.matchpuff.profileservice.infrastructure.adapters.persistence.entity.StudentProfileDocument;
import com.matchpuff.profileservice.infrastructure.adapters.persistence.entity.TagDocument;
import com.matchpuff.profileservice.infrastructure.adapters.persistence.entity.UserDocument;
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
		doc.setPasswordHash(profile.getPasswordHash());
		doc.setGender(profile.getGender());
		doc.setBirthdate(profile.getDateOfBirth() == null ? null : profile.getDateOfBirth().atStartOfDay());
		doc.setCreatedAt(profile.getCreatedAt());
		doc.setPhotourl(profile.getPhotoUrl());
		doc.setCareer(profile.getCareer());
		doc.setSemester(profile.getSemester());
		doc.setStudentCarnet(profile.getStudentCarnet());
		doc.setBiography(profile.getBiography());
		doc.setPrivacyLevel(profile.getPrivacyLevel());
		doc.setSchedule(toScheduleDocumentList(profile.getSchedules()));
		doc.setInterests(toTagDocumentList(profile.getTags()));

		return doc;
	}

	public static AdminProfileDocument toDocument(Admin admin) {
		if (admin == null) {
			return null;
		}

		AdminProfileDocument doc = new AdminProfileDocument();
		doc.setId(admin.getId());
		doc.setUserType(UserType.ADMIN);
		doc.setName(admin.getName());
		doc.setEmail(admin.getEmail());
		doc.setPasswordHash(admin.getPasswordHash());
		doc.setGender(admin.getGender());
		doc.setCreatedAt(admin.getCreatedAt());

		return doc;
	}

	public static OrganizerProfileDocument toDocument(Organizer organizer) {
		if (organizer == null) {
			return null;
		}

		OrganizerProfileDocument doc = new OrganizerProfileDocument();
		doc.setId(organizer.getId());
		doc.setUserType(UserType.ORGANIZER);
		doc.setName(organizer.getName());
		doc.setEmail(organizer.getEmail());
		doc.setPasswordHash(organizer.getPasswordHash());
		doc.setGender(organizer.getGender());
		doc.setCreatedAt(organizer.getCreatedAt());
		doc.setContact(organizer.getContactInfo());

		return doc;
	}

	public static StudentProfile toDomain(StudentProfileDocument doc) {
		if (doc == null) {
			return null;
		}

		StudentProfile profile = new StudentProfile();
		fillCommonUserFields(profile, doc);
		profile.setDateOfBirth(doc.getBirthdate() == null ? null : doc.getBirthdate().toLocalDate());
		profile.setPhotoUrl(doc.getPhotourl());
		profile.setCareer(doc.getCareer());
		profile.setSemester(doc.getSemester() == null ? 0 : doc.getSemester());
		profile.setStudentCarnet(doc.getStudentCarnet());
		profile.setBiography(doc.getBiography());
		profile.setPrivacyLevel(doc.getPrivacyLevel());
		profile.setSchedules(toScheduleList(doc.getSchedule()));
		profile.setTags(toTagList(doc.getInterests()));

		return profile;
	}

	public static Admin toDomain(AdminProfileDocument doc) {
		if (doc == null) {
			return null;
		}

		return buildAdmin(doc);
	}

	public static Organizer toDomain(OrganizerProfileDocument document) {
		if (document == null) {
			return null;
		}

		return buildOrganizer(document);
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

		return scheduleDocs.stream()
			.map(doc -> new Schedule(
				doc.getDayOfWeek(),
				doc.getName(),
				doc.getStartHour(),
				doc.getFinishHour()
			))
			.toList();
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


	public static Optional<User> toDomainByType(UserDocument doc) {
		if (doc == null || doc.getUserType() == null) {
			return Optional.empty();
		}

		return switch (doc.getUserType()) {
			case STUDENT -> Optional.of((User) toDomain((StudentProfileDocument) doc));
			case ADMIN -> Optional.of((User) toDomainAdmin((AdminProfileDocument) doc));
			case ORGANIZER -> Optional.of((User) toDomainOrganizer((OrganizerProfileDocument) doc));
		};
	}


	public static Admin toDomainAdmin(AdminProfileDocument document) {
		if (document == null) {
			return null;
		}

		return buildAdmin(document);
	}

	public static Organizer toDomainOrganizer(OrganizerProfileDocument doc) {
		if (doc == null) {
			return null;
		}

		return buildOrganizer(doc);
	}

	private static Admin buildAdmin(AdminProfileDocument doc) {
		Admin admin = new Admin();
		fillCommonUserFields(admin, doc);
		return admin;
	}

	private static Organizer buildOrganizer(OrganizerProfileDocument doc) {
		Organizer organizer = new Organizer();
		fillCommonUserFields(organizer, doc);
		organizer.setContactInfo(doc.getContact());
		return organizer;
	}

	private static void fillCommonUserFields(User user, UserDocument doc) {
		user.setId(doc.getId());
		user.setName(doc.getName());
		user.setEmail(doc.getEmail());
		user.setPasswordHash(doc.getPasswordHash());
		user.setGender(doc.getGender());
		user.setCreatedAt(doc.getCreatedAt());
	}
}
