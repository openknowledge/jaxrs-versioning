[![Build Status](https://travis-ci.org/openknowledge/jaxrs-versioning.svg?branch=master)](https://travis-ci.org/openknowledge/jaxrs-versioning) [![sonarcloud](https://sonarcloud.io/api/project_badges/measure?project=de.openknowledge.jaxrs%3Ajaxrs-versioning&metric=security_rating)](https://sonarcloud.io/dashboard?id=de.openknowledge.jaxrs%3Ajaxrs-versioning) [![sonarcloud](https://sonarcloud.io/api/project_badges/measure?project=de.openknowledge.jaxrs%3Ajaxrs-versioning&metric=vulnerabilities)](https://sonarcloud.io/dashboard?id=de.openknowledge.jaxrs%3Ajaxrs-versioning) [![sonarcloud](https://sonarcloud.io/api/project_badges/measure?project=de.openknowledge.jaxrs%3Ajaxrs-versioning&metric=bugs)](https://sonarcloud.io/dashboard?id=de.openknowledge.jaxrs%3Ajaxrs-versioning) [![sonarcloud](https://sonarcloud.io/api/project_badges/measure?project=de.openknowledge.jaxrs%3Ajaxrs-versioning&metric=coverage)](https://sonarcloud.io/dashboard?id=de.openknowledge.jaxrs%3Ajaxrs-versioning)

Why interface versioning?
=========================
When you have a server with an interface and clients that use that interface
you either have to update every client on every server-release
or you have to take care of backward-compatibility and interface versioning,
otherwise older clients will not work after the release.

Backward-compatible interface development
=========================================
Backward-compatible interface development means that clients, that use an old version of the interface still
contiue to work when your interface evolves. To archive this, you have to be very careful
when you add, remove, move or rename attributes. I.e. when you add an attribute you only may add it as optional,
since old clients still will not send the attribute. Instead of removing an attribute,
you have to make it optional, too, since old clients will continue to send it and even may expect it being returned
by your interface. When you rename an attribute, you have to leave the old attribute in place
and just add the attribute with the new name. To support both the old and the new version of the interface,
you have to fill both attributes. This frameworks helps you developing a backward-compatible interface
by doing the appropriate mapping.

Renaming an attribute
--------------------------------
As already written, instead of simply renaming the attribute,
you have to duplicate it with the old and the new name.

Example:
--------------------------------
 
    "address": {
        "street": {
            "name": "Samplestreet",
            "number": "1"
        },
        "city": "12345 Samplecity"
    }

When you want to rename "name" and "number" of "street", you have to copy it to be backward-compatible like

    "address": {
        "street": {
            "name": "Samplestreet",
            "streetName": "Samplestreet",
            "number": "1",
            "houseNumber": "1"
        },
        "city": "12345 Samplecity"
    }

Of course your server has to deal with both kind of data, one that contains a "name"
(from older clients) and one that contains "streetName" (from newer clients).
When answering on such requests, both fields have to be filled.
This framework automatically supports such backward-compatibility mapping by Java Annotations.
Simply annotate the "streetName" and "houseNumber" fields in Java with @MovedFrom

    public class Street {
        @Deprecated
        private String name;
        @MovedFrom("name")
        private String streetName;
        @Deprecated
        private String number;
        @MovedFrom("number")
        private String houseNumber;
    }

Adding an attribute
--------------------------------
When you add an attribute, you have to remember that old clients will not send the attribute.
To ensure that the attribute nevertheless is filled, you may specify a default value with this framework:

Example:
--------------------------------

    public class Address {
        private Street street;
        private String city;
        
        @Added(defaultValue = "DE")
        private String countryCode;
    }

Removing an attribute
--------------------------------
Removing an attribute is a similar operation to adding it. Here it's the other way round:
old clients will expect the attribute to be there. To satisfy old clients,
you may specify a default value for those attributes, too.

Example:
--------------------------------

    public class Address {
        private Street street;
        private String city;

        @Removed(defaultValue = "DE")
        private String countryCode;
    }

Moving an attribute
-------------------------------
@MovedFrom not only supports renaming, but also moving to another level as you can see in the following sample.
On the one hand "streetName" and "houseNumber" are moved out of the Street object into the Address object.
On the other hand a City object is added and "zipCode" and "cityName" are moved into it.

Example:
--------------------------------

    public class Address {
        private Street street;
        @MovedFrom("street/streetName")
        private String streetName;
        @MovedFrom("street/houseNumber")
        private String houseNumber;
        private String zipCode;
        private String cityName;
        @Added
        private City city;
    }

    public class City {
        @MovedFrom("../zipCode")
        private String zipCode;
        @MovedFrom("../cityName")
        private String name;
    }

With the Annotations @MovedFrom, @Added and @Removed the framework supports most of the
use cases of backward-compatible interface development. For the more sophisticated cases
an interface named Provider exists, that can be implemented to do the logic in java code.

Splitting and Merging attributes
--------------------------------
To merge two ore more attributes, you have to implement the Provider-Interface and to the appropriate logic there.
You can specify your implementation of the Provider interface with the @Added or @Removed annotations.
When merging attributes you have to ensure, that the source attributes are already filled by the framework.
You can tell the framework, which attributes you need with the "dependsOn" attribute of the @Added annotation
and the "isDependencyOf" attribute of the @Removed annotation.
Remember: To be backward-compatible, you have to provide both operations (splitting and merging).
When older clients send data to your server, the framework has to do the merge operation.
But, when you operate on the merged attribute and want to send it back to the client,
the framework has to split it, because older clients still expect the splitted version of the attribute.

Example:
--------------------------------

    public class Address {
        private String streetName;
        private String houseNumber;
        @Removed(provider = CityAggregationProvider.class, isDependencyOf = {"zipCode", "cityName"})
        private String city;
        @Added(provider = ZipCodeProvider.class, dependsOn = "city")
        private String zipCode;
        @Added(provider = CityNameProvider.class, dependsOn = "city")
        private String cityName;
    }

    public class CityAggregationProvider implements Provider<String> {
        @Override
        public String get(VersionContext versionContext) {
            Address address = versionContext.getParent(Address.class);
            return address.getZipCode() + " " + address.getCityName();
        }
    }

    public class ZipCodeProvider implements Provider<String> {
        @Override
        public String get(VersionContext versionContext) {
            Address address = versionContext.getParent(Address.class);
            String[] parts = address.getCity().split(" ");
            return parts[0];
        }
    }

    public class CityNameProvider implements Provider<String> {
        @Override
        public String get(VersionContext versionContext) {
            Address address = versionContext.getParent(Address.class);
            String[] parts = address.getCity().split(" ");
            return parts[1];
        }
    }

How to implement breaking changes?
==================================
This framework has the concept to handle backward-compatible changes within one Java Class
(see samples above) or a set of them.
When you want to implement a breaking change (i.e. really remove an attribute),
you have to switch to a different class
and specify the version and the corresponding class of the previous version via annotation.

    @SupportedVersion(version = "v2", previous = StreetV1.class)
    public class Street {
        ...
    }

The framework then automatically will do the mapping between both versions of the class
by copying over attributes with the same name and matching types and doing the appropriate conversion.

How is type matching and conversion done?
-----------------------------------------
Simple types in the sense of the framework are all Java primitive types and their corresponding wrapper
and java.lang.String and java.util.Date. Conversion between these objects is done by using the constructor
that takes one single String-argument.
Complex Java classes match for automatic interversion mapping
when they have at least one attribute with same name and matching types.

How to integrate the framework
==============================
Simply put the jar in your classpath and specify a {version} path param in your REST resource.

    @Path("addresses/{version}")
    public class AddressResource {
    
        @Path("{id}")
        public Address getAddress(@PathParam("id") int id) {
            ...
        }
    }
    
The framework will detect the @SupportedVersion annotation at your model class
(Address in the example above) and do the appropriate mapping and conversion.
