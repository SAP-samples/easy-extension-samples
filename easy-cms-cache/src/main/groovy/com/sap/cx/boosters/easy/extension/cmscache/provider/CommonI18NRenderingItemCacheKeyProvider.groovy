package com.sap.cx.boosters.easy.extension.cmscache.provider

import de.hybris.platform.cmsfacades.rendering.cache.keyprovider.RenderingItemCacheKeyProvider
import de.hybris.platform.commerceservices.i18n.CommerceCommonI18NService
import de.hybris.platform.core.model.ItemModel

import javax.annotation.Resource

class CommonI18NRenderingItemCacheKeyProvider implements RenderingItemCacheKeyProvider<ItemModel> {

    @Resource
    CommerceCommonI18NService commerceCommonI18NService

    @Override
    String getKey(ItemModel item) {
        StringBuilder key = new StringBuilder();
        //PK
        key.append(item.getPk().getLongValueAsString());
        //Modified Time
        key.append(item.getModifiedtime());
        //Currency
        key.append(commerceCommonI18NService.getCurrentCurrency().getIsocode());
        //Language
        key.append(commerceCommonI18NService.getCurrentLanguage().getIsocode());
        //Key
        return key.toString();
    }




}