package com.xust.hotel.user.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import per.bhj.xust.hotel_manager.dbo.CustomerInfoPojo;

import java.util.List;

/**
 * @author bhj
 */
@Slf4j
public class UniversalUtil {

    public static boolean checkCustomerInfoList(List<CustomerInfoPojo> customerInfoPojoList) {
        if (CollectionUtils.isEmpty(customerInfoPojoList)) {
            log.error("checkCustomerInfoList, customerInfoPojoList is null.");
            return false;
        }
        for (CustomerInfoPojo temp : customerInfoPojoList) {
            if (StringUtils.isBlank(temp.getCustomerName()) || StringUtils.isBlank(temp.getCustomerIdenType())
                    || StringUtils.isBlank(temp.getCustomerPhone()) || StringUtils.isBlank(temp.getCustomerIdenNum())) {
                log.error("checkCustomerInfoList, list's temp is null.temp={}", temp.toString());
                return false;
            }
        }
        return true;
    }

}
