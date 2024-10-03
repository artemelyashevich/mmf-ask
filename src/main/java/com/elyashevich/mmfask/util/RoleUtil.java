package com.elyashevich.mmfask.util;

import com.elyashevich.mmfask.entity.Role;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Set;

@UtilityClass
public class RoleUtil {

    public static List<SimpleGrantedAuthority> mapToSimpleGrantedAuthority(final Set<Role> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.name()))
                .toList();
    }
}
