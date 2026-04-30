package infrastructure.adapters.persistence.entity;

import com.matchpuff.profileservice.infrastructure.adapters.persistence.entity.UserDocument;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@NoArgsConstructor
public class StudentProfileDocument extends UserDocument {
    @Field("carreer")
    private CarreerEnum carreer;

    @Field("semester")
    private Integer semester;

    @Field("biography")
    private String biography;

    @Field("privacy_level")
    private PrivacyLevelEnum privacyLevel;

    @Field("interests")
    private List<TagDocument> interests;

    @Field("schedule")
    private List<ScheduleDocument> schedule;
}
