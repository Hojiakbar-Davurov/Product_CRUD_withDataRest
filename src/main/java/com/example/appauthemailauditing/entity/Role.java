package com.example.appauthemailauditing.entity;

import com.example.appauthemailauditing.entity.enums.RoleName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity

//  User GrantedAuthority so'ragani uchun implement qildi
public class Role implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)    // to confirm as a ENUM  & type=String
    private RoleName roleName;

    @Override
    public String getAuthority() {
        return roleName.name(); //String qaytaradi shuning uchun .name() oqrali String ga o'girildi
    }
}
