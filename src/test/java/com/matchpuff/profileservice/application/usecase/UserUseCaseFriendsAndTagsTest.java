package com.matchpuff.profileservice.application.usecase;

import com.matchpuff.profileservice.application.dto.event.FriendshipCreatedEventDto;
import com.matchpuff.profileservice.application.service.PasswordHashingService;
import com.matchpuff.profileservice.domain.exceptions.InvalidInputException;
import com.matchpuff.profileservice.domain.exceptions.ProfileServiceException;
import com.matchpuff.profileservice.domain.model.Admin;
import com.matchpuff.profileservice.domain.model.StudentProfile;
import com.matchpuff.profileservice.domain.model.User;
import com.matchpuff.profileservice.domain.ports.out.FriendshipEventPublisherPort;
import com.matchpuff.profileservice.domain.ports.out.ImageStoragePort;
import com.matchpuff.profileservice.domain.ports.out.TagCatalogPort;
import com.matchpuff.profileservice.domain.ports.out.UserRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserUseCaseFriendsAndTagsTest {

    UserRepositoryPort userRepository;
    ImageStoragePort imageStoragePort;
    PasswordHashingService passwordHashingService;
    TagCatalogPort tagCatalogPort;
    FriendshipEventPublisherPort friendshipEventPublisher;
    UserUseCase useCase;

    StudentProfile student;

    @BeforeEach
    void setUp(){
        userRepository = mock(UserRepositoryPort.class);
        imageStoragePort = mock(ImageStoragePort.class);
        passwordHashingService = mock(PasswordHashingService.class);
        tagCatalogPort = mock(TagCatalogPort.class);
        friendshipEventPublisher = mock(FriendshipEventPublisherPort.class);

        useCase = new UserUseCase(userRepository, imageStoragePort, passwordHashingService, tagCatalogPort, friendshipEventPublisher);

        student = new StudentProfile();
        student.setId(UUID.randomUUID());
        student.setTagsId(new ArrayList<>());
        student.setFriendsId(new ArrayList<>());
    }

    @Test
    void getUserTags_returnsList_forStudent() {
        student.setTagsId(new ArrayList<>(List.of(UUID.randomUUID())));
        when(userRepository.findById(student.getId())).thenReturn(Optional.of(student));

        List<UUID> tags = useCase.getUserTags(student.getId());

        assertEquals(1, tags.size());
    }

    @Test
    void getUserTags_throws_forNonStudent() {
        Admin admin = new Admin();
        admin.setId(UUID.randomUUID());
        when(userRepository.findById(admin.getId())).thenReturn(Optional.of(admin));

        assertThrows(ProfileServiceException.class, () -> useCase.getUserTags(admin.getId()));
    }

    @Test
    void getUserTags_returnsEmpty_whenNull() {
        student.setTagsId(null);
        when(userRepository.findById(student.getId())).thenReturn(Optional.of(student));

        List<UUID> tags = useCase.getUserTags(student.getId());

        assertNotNull(tags);
        assertTrue(tags.isEmpty());
    }

    @Test
    void getUserTagsNames_filtersNulls() {
        UUID t1 = UUID.randomUUID();
        UUID t2 = UUID.randomUUID();
        student.setTagsId(new ArrayList<>(List.of(t1, t2)));
        when(userRepository.findById(student.getId())).thenReturn(Optional.of(student));
        when(tagCatalogPort.getTagNameById(t1)).thenReturn("TagOne");
        when(tagCatalogPort.getTagNameById(t2)).thenReturn(null);

        List<String> names = useCase.getUserTagsNames(student.getId());

        assertEquals(1, names.size());
        assertEquals("TagOne", names.get(0));
    }

    @Test
    void getUserFriends_returnsList_forStudent() {
        UUID f = UUID.randomUUID();
        student.setFriendsId(new ArrayList<>(List.of(f)));
        when(userRepository.findById(student.getId())).thenReturn(Optional.of(student));

        List<UUID> friends = useCase.getUserFriends(student.getId());
        assertEquals(1, friends.size());
    }

    @Test
    void getUserFriends_throws_forNonStudent() {
        Admin admin = new Admin();
        admin.setId(UUID.randomUUID());
        when(userRepository.findById(admin.getId())).thenReturn(Optional.of(admin));

        assertThrows(ProfileServiceException.class, () -> useCase.getUserFriends(admin.getId()));
    }

    @Test
    void addFriendToStudent_variousValidationErrors() {
        UUID a = UUID.randomUUID();
        // self add
        assertThrows(InvalidInputException.class, () -> useCase.addFriendToStudent(a, a));

        // friend not found
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.empty());
        UUID b = UUID.randomUUID();
        assertThrows(ProfileServiceException.class, () -> useCase.addFriendToStudent(a, b));

        // friend exists but not student
        Admin admin = new Admin();
        admin.setId(UUID.randomUUID());
        when(userRepository.findById(b)).thenReturn(Optional.of(admin));
        assertThrows(InvalidInputException.class, () -> useCase.addFriendToStudent(a, b));
    }

    @Test
    void addFriendToStudent_throws_whenCallerNotStudent() {
        UUID a = UUID.randomUUID();
        UUID b = UUID.randomUUID();

        Admin caller = new Admin();
        caller.setId(a);
        when(userRepository.findById(b)).thenReturn(Optional.of(new StudentProfile()));
        when(userRepository.findById(a)).thenReturn(Optional.of(caller));

        assertThrows(ProfileServiceException.class, () -> useCase.addFriendToStudent(a, b));
    }

    @Test
    void addFriendToStudent_throws_whenAlreadyFriend() {
        UUID a = UUID.randomUUID();
        UUID b = UUID.randomUUID();
        student.setId(a);
        student.setFriendsId(new ArrayList<>(List.of(b)));

        when(userRepository.findById(b)).thenReturn(Optional.of(new StudentProfile()));
        when(userRepository.findById(a)).thenReturn(Optional.of(student));

        assertThrows(InvalidInputException.class, () -> useCase.addFriendToStudent(a, b));
    }

    @Test
    void addFriendToStudent_success_addsBothSides() {
        UUID a = UUID.randomUUID();
        UUID b = UUID.randomUUID();
        student.setId(a);
        StudentProfile friend = new StudentProfile();
        friend.setId(b);
        friend.setFriendsId(new ArrayList<>());

        when(userRepository.findById(b)).thenReturn(Optional.of(friend));
        when(userRepository.findById(a)).thenReturn(Optional.of(student));
        when(userRepository.update(any(UUID.class), any())).thenReturn(null);

        User result = useCase.addFriendToStudent(a, b);

        assertTrue(student.getFriendsId().contains(b));
        assertTrue(friend.getFriendsId().contains(a));
        verify(friendshipEventPublisher).publishFriendshipCreated(any(FriendshipCreatedEventDto.class));
    }

    @Test
    void removeFriendFromStudent_errors_and_success() {
        UUID a = UUID.randomUUID();
        UUID b = UUID.randomUUID();
        student.setId(a);

        // no friends
        student.setFriendsId(null);
        when(userRepository.findById(a)).thenReturn(Optional.of(student));
        assertThrows(ProfileServiceException.class, () -> useCase.removeFriendFromStudent(a, b));

        // not found removal
        student.setFriendsId(new ArrayList<>());
        when(userRepository.findById(a)).thenReturn(Optional.of(student));
        assertThrows(ProfileServiceException.class, () -> useCase.removeFriendFromStudent(a, b));

        // success both sides
        student.setFriendsId(new ArrayList<>(List.of(b)));
        StudentProfile friend = new StudentProfile();
        friend.setId(b);
        friend.setFriendsId(new ArrayList<>(List.of(a)));
        when(userRepository.findById(a)).thenReturn(Optional.of(student));
        when(userRepository.findById(b)).thenReturn(Optional.of(friend));
        when(userRepository.update(any(UUID.class), any())).thenReturn(null);

        User result = useCase.removeFriendFromStudent(a, b);
        assertFalse(student.getFriendsId().contains(b));
    }
}
