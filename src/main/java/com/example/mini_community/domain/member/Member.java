package com.example.mini_community.domain.member;

import com.example.mini_community.common.entity.BaseTimeEntity;
import com.example.mini_community.domain.refreshToken.RefreshToken;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Table(name="member")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Member extends BaseTimeEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String profile_img;

    @OneToOne(mappedBy = "member")
    @PrimaryKeyJoinColumn
    private RefreshToken refreshToken;

    @Builder
    public Member(String email, String nickname, String profile_img, String password) {
        this.email = email;
        this.nickname = nickname;
        this.profile_img = profile_img;
        this.password = password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("member"));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        // 만료됐는지 확인하는 로직
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // 계정이 잠금되었는지 확인하는 로직
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // 패스워드 만료 여부 확인 로직
        return true;
    }

    @Override
    public boolean isEnabled() {
        // 계정 사용 가능 여부 확인 로직
        return true;
    }
}
