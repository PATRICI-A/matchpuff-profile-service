package infrastructure.adapters.persistence.entity;

import com.matchpuff.profileservice.infrastructure.adapters.persistence.entity.UserDocument;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@NoArgsConstructor
public class OrganizerDocument extends UserDocument {
    @Field("contact")
    private String contact;
}
