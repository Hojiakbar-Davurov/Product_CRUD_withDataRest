package com.example.appauthemailauditing.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "users")                                   // there is a already user table in postgres
public class User implements UserDetails {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Email
    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @CreationTimestamp                                  // auto set created time
    @Column(nullable = false, updatable = false)        //  cannot update because of creation
    private Timestamp createdAt;

    @UpdateTimestamp
    private Timestamp updateAt;

    @ManyToMany(fetch = FetchType.EAGER)                // DB dan Rollarni olib kelish
    private Set<Role> roles;                            //to be unique

    private String emailCode;


    // Dinamik qilish uchun qayta yozilyadi va DB ga bog'lanyapdi

    private boolean accountNonExpired = true;          //  userning muddati o'tgan yoki yo'qligini belgilaydi
    private boolean accountNonLocked = true;           //  userning bloklangan yoki yo'qligini belgilaydi
    private boolean credentialsNonExpired = true;      //  userning ishonchli yoki yo'qligini belgilaydi
    private boolean enabled = false;                   //  userning acconutni yoniq yoki yo'qligini belgilaydi


    /**
     * these are UserDetails methods
     * Role(Lavozim) and Permission()
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
