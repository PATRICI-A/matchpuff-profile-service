package com.matchpuff.profileservice.entrypoints.rest.controller;

import com.matchpuff.profileservice.application.dto.request.BatchProfileRequest;
import com.matchpuff.profileservice.application.dto.response.CategoryWithTagsResponse;
import com.matchpuff.profileservice.application.dto.response.BatchProfileResponse;
import com.matchpuff.profileservice.application.dto.response.UserResponse;
import com.matchpuff.profileservice.application.service.UserServicePort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import com.matchpuff.profileservice.entrypoints.advice.ErrorResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserQueryController {
    private final UserServicePort userService;

    @GetMapping("/{userId}")
    @Tag(name = "Users - Reading", description = "Obtain information about users")
    @Operation(summary = "Obtain user by ID",
            description = "Retrieves a full public representation of a user by UUID. Useful for client displays or other services that require detailed profile fields such as name, email, role-specific data and public attributes.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User retrieved successfully. Returns a UserResponse with public profile data.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class), examples = @ExampleObject(value = "{\"id\": \"00000000-0000-0000-0000-000000000000\", \"name\": \"John Doe\", \"email\": \"john@example.com\"}"))),
            @ApiResponse(responseCode = "400", description = "Bad request: invalid userId format.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "{\"message\": \"Invalid UUID format.\", \"status\": 400, \"timestamp\": \"2026-05-22T12:00:00\"}"))),
            @ApiResponse(responseCode = "401", description = "Unauthorized: authentication required.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "{\"message\": \"Unauthorized: token missing.\", \"status\": 401, \"timestamp\": \"2026-05-22T12:00:00\"}"))),
            @ApiResponse(responseCode = "404", description = "Not found: no user exists with the provided id.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "{\"message\": \"User not found.\", \"status\": 404, \"timestamp\": \"2026-05-22T12:00:00\"}"))),
            @ApiResponse(responseCode = "500", description = "Internal server error: unexpected failure while fetching the user.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "{\"message\": \"Server error retrieving user.\", \"status\": 500, \"timestamp\": \"2026-05-22T12:00:00\"}")))
    })
    public ResponseEntity<UserResponse> getUser(@PathVariable UUID userId) {
        return ResponseEntity.ok(userService.getUser(userId));
    }

    @GetMapping("/mail/{email}")
    @Tag(name = "Users - Reading", description = "Obtain information about users")
    @Operation(summary = "Obtain user by email",
            description = "Retrieve public user data by email address. Useful for login checks and client searches that start from an email identifier.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User retrieved successfully. Returns a UserResponse.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class), examples = @ExampleObject(value = "{\"id\": \"00000000-0000-0000-0000-000000000000\", \"name\": \"John Doe\", \"email\": \"john@example.com\"}"))),
            @ApiResponse(responseCode = "400", description = "Bad request: malformed email.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "{\"message\": \"Malformed email provided.\", \"status\": 400, \"timestamp\": \"2026-05-22T12:00:00\"}"))),
            @ApiResponse(responseCode = "404", description = "Not found: user with given email does not exist.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "{\"message\": \"No user found with email.\", \"status\": 404, \"timestamp\": \"2026-05-22T12:00:00\"}"))),
            @ApiResponse(responseCode = "500", description = "Internal server error: error while retrieving user by email.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "{\"message\": \"Server error fetching user by email.\", \"status\": 500, \"timestamp\": \"2026-05-22T12:00:00\"}")))
    })
    public ResponseEntity<UserResponse> getUserByEmail(@PathVariable String email) {
        return ResponseEntity.ok(userService.getUserByEmail(email));
    }

    @GetMapping("/student-profiles")
    @Tag(name = "Users - Reading", description = "Obtain information about users")
    @Operation(summary = "Obtain all student profiles",
            description = "Returns a list of student profiles available in the system. Use to populate directories, lists or admin interfaces. Supports pagination/filters in service layer if implemented.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Student profiles retrieved successfully. Returns a list of UserResponse objects.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class), examples = @ExampleObject(value = "[{\"id\": \"00000000-0000-0000-0000-000000000000\", \"name\": \"Alice\", \"email\": \"alice@example.com\"}]"))),
            @ApiResponse(responseCode = "401", description = "Unauthorized: authentication required to access student profiles if protected.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "{\"message\": \"Unauthorized: authentication required.\", \"status\": 401, \"timestamp\": \"2026-05-22T12:00:00\"}"))),
            @ApiResponse(responseCode = "500", description = "Internal server error: unexpected error fetching profiles.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "{\"message\": \"Server error retrieving student profiles.\", \"status\": 500, \"timestamp\": \"2026-05-22T12:00:00\"}")))
    })
    public ResponseEntity<List<UserResponse>> getAllStudentProfiles() {
        return ResponseEntity.ok(userService.getAllStudentProfiles());
    }

    @GetMapping("/{userId}/tags")
    @Tag(name = "Users - Reading", description = "Obtain information about users")
    @Operation(summary = "Obtain user tags",
            description = "Returns the list of tag identifiers associated with a user. Useful to understand a user's interests and to power recommendation/matching features.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User tags retrieved successfully. Returns a list of UUIDs representing tags.",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "[\"00000000-0000-0000-0000-000000000000\"]"))),
            @ApiResponse(responseCode = "400", description = "Bad request: invalid userId.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "{\"message\": \"Invalid user id.\", \"status\": 400, \"timestamp\": \"2026-05-22T12:00:00\"}"))),
            @ApiResponse(responseCode = "404", description = "Not found: user does not exist.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "{\"message\": \"User not found.\", \"status\": 404, \"timestamp\": \"2026-05-22T12:00:00\"}"))),
            @ApiResponse(responseCode = "500", description = "Internal server error: failed to retrieve tags.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "{\"message\": \"Server error retrieving tags.\", \"status\": 500, \"timestamp\": \"2026-05-22T12:00:00\"}")))
    })
    public ResponseEntity<List<UUID>> getUserTags(@PathVariable UUID userId) {
        return ResponseEntity.ok(userService.getUserTags(userId));
    }

    @GetMapping("/{userId}/tags/names")
    @Tag(name = "Users - Reading", description = "Obtain information about users")
    @Operation(summary = "Obtain user tags with names",
            description = "Returns the human-readable names of tags associated with the user. Useful for UI displays and human-friendly matching interfaces.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User tags retrieved successfully. Returns a list of tag names.",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "[\"java\", \"spring\"]"))),
            @ApiResponse(responseCode = "400", description = "Bad request: invalid userId.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "{\"message\": \"Invalid user id.\", \"status\": 400, \"timestamp\": \"2026-05-22T12:00:00\"}"))),
            @ApiResponse(responseCode = "404", description = "Not found: user or tags not found.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "{\"message\": \"Tags not found for user.\", \"status\": 404, \"timestamp\": \"2026-05-22T12:00:00\"}"))),
            @ApiResponse(responseCode = "500", description = "Internal server error: error while mapping tag ids to names.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "{\"message\": \"Server error mapping tags.\", \"status\": 500, \"timestamp\": \"2026-05-22T12:00:00\"}")))
    })
    public ResponseEntity<List<String>> getUserTagNames(@PathVariable UUID userId) {
        return ResponseEntity.ok(userService.getUserTagsNames(userId));
    }

    @GetMapping("/{userId}/friends")
    @Tag(name = "Users - Reading", description = "Obtain information about users")
    @Operation(summary = "Obtain user friends",
            description = "Retrieves the list of friend user IDs for a given user. Useful for social features and building friend graphs in clients.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User friends retrieved successfully. Returns a list of UUIDs.",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "[\"00000000-0000-0000-0000-000000000000\"]"))),
            @ApiResponse(responseCode = "400", description = "Bad request: invalid userId.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "{\"message\": \"Invalid user id.\", \"status\": 400, \"timestamp\": \"2026-05-22T12:00:00\"}"))),
            @ApiResponse(responseCode = "404", description = "Not found: user not found.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "{\"message\": \"User not found.\", \"status\": 404, \"timestamp\": \"2026-05-22T12:00:00\"}"))),
            @ApiResponse(responseCode = "500", description = "Internal server error: failed to retrieve friends list.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "{\"message\": \"Server error retrieving friends.\", \"status\": 500, \"timestamp\": \"2026-05-22T12:00:00\"}")))
    })
    public ResponseEntity<List<UUID>> getUserFriends(@PathVariable UUID userId) {
        return ResponseEntity.ok(userService.getUserFriends(userId));
    }

    @GetMapping
    @Tag(name = "Users - Reading", description = "Obtain information about users")
    @Operation(summary = "Obtain all users",
            description = "Returns a list with all registered users. Intended for administrative or batch-processing clients; consider adding server-side pagination if the dataset grows.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Users retrieved successfully. Returns a list of UserResponse objects.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class), examples = @ExampleObject(value = "[{\"id\": \"00000000-0000-0000-0000-000000000000\", \"name\": \"User A\", \"email\": \"a@example.com\"}]"))),
            @ApiResponse(responseCode = "401", description = "Unauthorized: authentication required for this endpoint if protected.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "{\"message\": \"Unauthorized.\", \"status\": 401, \"timestamp\": \"2026-05-22T12:00:00\"}"))),
            @ApiResponse(responseCode = "500", description = "Internal server error: error while fetching users.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "{\"message\": \"Server error fetching users.\", \"status\": 500, \"timestamp\": \"2026-05-22T12:00:00\"}")))
    })
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PostMapping("/batch")
    @Tag(name = "Users - Reading", description = "Obtain information about users")
    @Operation(summary = "Obtain multiple user profiles by IDs",
            description = "Accepts a BatchProfileRequest containing a list of user UUIDs and returns their profiles. Useful for batch clients that want to fetch many user profiles in a single call.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User profiles retrieved successfully. Returns a list of BatchProfileResponse objects.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = BatchProfileResponse.class), examples = @ExampleObject(value = "[{\"id\": \"00000000-0000-0000-0000-000000000000\", \"name\": \"Batch User\", \"email\": \"batch@example.com\"}]"))),
            @ApiResponse(responseCode = "400", description = "Bad request: invalid or empty id list.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "{\"message\": \"Request must include a non-empty list of ids.\", \"status\": 400, \"timestamp\": \"2026-05-22T12:00:00\"}"))),
            @ApiResponse(responseCode = "500", description = "Internal server error: failure while processing batch retrieval.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "{\"message\": \"Server error processing batch.\", \"status\": 500, \"timestamp\": \"2026-05-22T12:00:00\"}")))
    })
    public ResponseEntity<List<BatchProfileResponse>> getUsersByIds(
            @Valid @RequestBody BatchProfileRequest request) {
        return ResponseEntity.ok(userService.getUsersByIds(request.getIds()));
    }

    @GetMapping("/tags/catalog")
    @Tag(name = "User Profiles", description = "Manage user profiles and content")
    @Operation(summary = "Get all available tags grouped by category",
            description = "Returns the available tags organized by category. Useful for tag selection UIs and for clients that need the full taxonomy of tags and categories.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tags retrieved successfully. Returns a list of CategoryWithTagsResponse objects.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CategoryWithTagsResponse.class), examples = @ExampleObject(value = "[{\"category\": \"Programming\", \"tags\": [{\"id\": \"00000000-0000-0000-0000-000000000000\", \"name\": \"Java\"}]}]"))),
            @ApiResponse(responseCode = "500", description = "Internal server error: error retrieving tag catalog.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "{\"message\": \"Server error retrieving tag catalog.\", \"status\": 500, \"timestamp\": \"2026-05-22T12:00:00\"}")))
    })
    public ResponseEntity<List<CategoryWithTagsResponse>> getTagCatalog() {
        return ResponseEntity.ok(userService.getTagCatalog());
    }

    @GetMapping("/internal/{userId}/friends-count")
    @Tag(name = "Users - Internal", description = "Internal endpoints for inter-service communication")
    @Operation(summary = "Get total friend count for a user (inter-service)",
            description = "Returns the total number of friends for a user. Designed for internal service calls that need quick metrics like friend counts for ranking or display.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Friend count retrieved. Returns an integer with the number of friends.",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "3"))),
            @ApiResponse(responseCode = "400", description = "Bad request: invalid userId.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "{\"message\": \"Invalid user id.\", \"status\": 400, \"timestamp\": \"2026-05-22T12:00:00\"}"))),
            @ApiResponse(responseCode = "500", description = "Internal server error: failed to compute friend count.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "{\"message\": \"Server error computing friend count.\", \"status\": 500, \"timestamp\": \"2026-05-22T12:00:00\"}")))
    })
    public ResponseEntity<Integer> getConnectionsCount(@PathVariable UUID userId) {
        return ResponseEntity.ok(userService.getUserFriends(userId).size());
    }

}
