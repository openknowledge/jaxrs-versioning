/*
 * Copyright (C) open knowledge GmbH
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package de.openknowledge.jaxrs.versioning.conversion;

import de.openknowledge.jaxrs.versioning.MovedFrom;

/**
 * @author Arne Limburg - open knowledge GmbH
 * @author Philipp Geers - open knowledge GmbH
 */
public class CompatibilityMapper {

    private VersionTypeFactory versionTypeFactory;

    CompatibilityMapper (VersionTypeFactory factory) {
        versionTypeFactory = factory;
    }
    public void map(Object object) {
        VersionType versionType = versionTypeFactory.get(object.getClass());

        for (VersionProperty versionProperty : versionType.getProperties()) {
            MovedFrom movedFrom = versionProperty.getAnnotation(MovedFrom.class);

            if(getPrevious(versionType,versionProperty, movedFrom, object) == null) {
                getNext(versionType,versionProperty, movedFrom, object);

            }

//            if(versionProperty.get(object) == null) {
//                //parent
//               versionProperty.set(object, versionType.getProperty(movedFrom.value()).get(object));
//
//                // falls parent null, value vom child
//            } else {
//                versionType.getProperty(movedFrom.value()).set(object, versionProperty.get(object));
//            }
        }

    }

    private Object getNext(VersionType versionType, VersionProperty property, MovedFrom movedFrom, Object object) {
        return null;
    }

    private Object getPrevious(VersionType versionType, VersionProperty property, MovedFrom movedFrom, Object object) {
        if(movedFrom == null && property.get(object) == null) {
            //root
            return versionType.getProperty(movedFrom.value()).get(object);
        }
        if(property.get(object) == null) {


            return getPrevious(versionType,versionType.getProperty(movedFrom.value()),movedFrom,object);
        }
        return property.get(object);

    }

    private VersionProperty getNext(VersionType type, MovedFrom movedFrom ) {
        return type.getProperty(movedFrom.value());
    }



}
