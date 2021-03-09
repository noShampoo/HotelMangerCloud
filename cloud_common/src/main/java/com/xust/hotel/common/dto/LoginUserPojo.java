package com.xust.hotel.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * login user info dto
 *
 * @author bhj
 */
@Data
@Builder
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class LoginUserPojo extends BasePojo {
    private static final long serialVersionUID = -1110936828440609618L;

    /**
     * login user
     */
    private String user;

    /**
     * login password
     */
    private String password;

    /**
     * login roles(user type)
     */
    private String roles;

    @Override
    public String toString() {
        return super.toString();
    }
}
