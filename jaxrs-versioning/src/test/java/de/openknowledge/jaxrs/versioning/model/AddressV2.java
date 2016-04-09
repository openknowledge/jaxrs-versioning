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
package de.openknowledge.jaxrs.versioning.model;

/**
 * @author Arne Limburg - open knowledge GmbH
 * @author Philipp Geers - open knowledge GmbH
 */
public class AddressV2 {

    private String adressLine1;

    private String adressLine2;

    private LocationV2 location;

    public AddressV2(String adressLine1, String adressLine2, LocationV2 location) {
        this.adressLine1 = adressLine1;
        this.adressLine2 = adressLine2;
        this.location = location;
    }

    public String getAdressLine1() {
        return adressLine1;
    }

    public String getAdressLine2() {
        return adressLine2;
    }

}
