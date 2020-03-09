package mock.accounts;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter @Setter @AllArgsConstructor
@Builder @NoArgsConstructor @EqualsAndHashCode (of = "id")
public class Account {

    @Id @GeneratedValue
    private Integer id;

    private String email;

    private String password;

    @ElementCollection(fetch = FetchType.EAGER) // EAGER 즉시로딩, LAZY 는 지연로딩
    @Enumerated(EnumType.STRING)
    private Set<AccountRole> roles;

}
