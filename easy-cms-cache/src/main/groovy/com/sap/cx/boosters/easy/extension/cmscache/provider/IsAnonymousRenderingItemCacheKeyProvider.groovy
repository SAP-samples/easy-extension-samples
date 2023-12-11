package com.sap.cx.boosters.easy.extension.cmscache.provider

import de.hybris.platform.core.model.ItemModel
import de.hybris.platform.servicelayer.user.UserService

import javax.annotation.Resource

class IsAnonymousRenderingItemCacheKeyProvider extends CommonI18NRenderingItemCacheKeyProvider {

    @Resource
    UserService userService;

    @Override
    String getKey(ItemModel item) {
        final StringBuilder key = new StringBuilder(super.getKey(item));
        //Anon or Logged
        key.append(String.valueOf(userService.isAnonymousUser(userService.getCurrentUser())));
        //Key
        return key.toString();
    }




}