package com.sap.cx.boosters.easy.extension.cmscache.provider

import de.hybris.platform.core.model.ItemModel
import de.hybris.platform.personalizationservices.segment.CxSegmentService
import de.hybris.platform.servicelayer.user.UserService

import javax.annotation.Resource
import java.util.stream.Collectors;

class SegmentRenderingItemCacheKeyProvider extends UserGroupRenderingItemCacheKeyProvider {

    @Resource
    UserService userService;

    @Resource
    CxSegmentService cxSegmentService;

    @Override
    String getKey(ItemModel item) {
        final StringBuilder key = new StringBuilder(super.getKey(item));
        //Segments
        key.append(
                cxSegmentService.getUserToSegmentForCalculation(userService.getCurrentUser())
                .stream()
                .sorted((o1,o2) -> {
                    o1.getPk().getLong() <=> o2.getPk().getLong()
                })
                .map { it.getPk().getLongValueAsString() }
                .collect(Collectors.joining("-"))
        )
        //Key
        return key.toString();
    }




}