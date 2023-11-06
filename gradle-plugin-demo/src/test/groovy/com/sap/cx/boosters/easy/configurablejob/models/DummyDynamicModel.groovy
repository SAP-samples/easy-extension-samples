package com.sap.cx.boosters.easy.configurablejob.models

import com.sap.cx.boosters.easytype.ItemModelAttribute
import de.hybris.platform.core.model.ItemModel
import org.springframework.util.ReflectionUtils

class DummyDynamicModel extends ItemModel {

    @ItemModelAttribute(read = true, write = true)
    String code

    def getProperty(String name) {
        if (this.getMetaClass().hasProperty(this,name) && checkAttribute(name)) {
            this.getPersistenceContext().getPropertyValue(name)
        } else {
            super.getProperty(name)
            // throw undefinedPropertyException(name)
        }
    }

    void setProperty(String name, Object value) {
        if (this.getMetaClass().hasProperty(this,name) && checkAttribute(name)) {
            this.getPersistenceContext().setPropertyValue(name, value)
        } else {
            super.setProperty(name,value)
            // throw undefinedPropertyException(name)
        }
    }

    private boolean checkAttribute(String name) {
        ReflectionUtils.getDeclaredFields(this.getClass()).findAll{it.isAnnotationPresent(ItemModelAttribute)}.any {it.name == name}
    }

    // private RuntimeException undefinedPropertyException(String name) {
    //     new RuntimeException("Undefined attribute ${name} for ${this.getClass().name}")
    // }

}
