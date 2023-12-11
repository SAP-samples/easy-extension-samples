package com.sap.cx.boosters.easy.extension.cmscache.provider

import de.hybris.platform.core.model.ItemModel
import de.hybris.platform.servicelayer.user.UserService

import javax.annotation.Resource
import java.util.stream.Collectors

class UserGroupRenderingItemCacheKeyProvider extends IsAnonymousRenderingItemCacheKeyProvider {

    @Resource
    UserService userService;

    @Override
    String getKey(ItemModel item) {
        final StringBuilder key = new StringBuilder(super.getKey(item));
        //Groups
        key.append(
                userService.getCurrentUser().getGroups()
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