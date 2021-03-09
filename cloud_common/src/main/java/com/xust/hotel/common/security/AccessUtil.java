package com.xust.hotel.common.security;

import com.xust.hotel.common.dto.LoginUserPojo;
import com.xust.hotel.common.exception.InnerErrorException;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;

/**
 * @author bhj
 */
@Slf4j
public class AccessUtil {

    /**
     * check
     * @param request
     * @param tarAccess
     * @return
     * @throws InnerErrorException
     */
    public static boolean checkAccess(HttpServletRequest request, String... tarAccess) throws InnerErrorException {
        try {

            if (request == null || request.getAttribute(JwtConstantConfig.REQ_ATTRIBUTE_KEY) == null) {
                log.error("checkAccess, param error.");
                return false;
            }
            if (tarAccess.length == 0) {
                log.error("checkAccess, param access is null.");
                return false;
            }
            LoginUserPojo loginUserPojo = (LoginUserPojo) request.getAttribute(JwtConstantConfig.REQ_ATTRIBUTE_KEY);
            if (loginUserPojo == null) {
                log.info("checkAccess, no access.");
                return false;
            }
            for (String access : tarAccess) {
                log.info("checkAccess, access={}, login={}", access, loginUserPojo.getRoles());
                if (access.equals(loginUserPojo.getRoles())) {
                    return true;
                }
            }
            log.info("checkAccess, no access.");
            return false;
        } catch (Exception e) {
            log.error("checkAccess occur exception.");
            throw new InnerErrorException("checkAccess occur exception.");
        }
    }

}
