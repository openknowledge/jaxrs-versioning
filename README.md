Why interface versioning?
=========================
When you have a server with an interface and clients that use that interface
you either have to update every client on every server-release
or you have to take care of backward-compatibility and interface versioning,
otherwise older clients will not work after the release.

Backward-compatible interface development
=========================================
When you try to implement a backward-compatible interface by yourself,
you have to write much boilerplate code, i.e. instead of renaming an attribute,
you have to duplicate it and map between the both.
With this framework you are able to replace most of such boilerplate code
with some simple annotations.

Example:
--------------------------------

    "address": {
       "street": {
           "name": "Samplestreet",
           "number": "1"
       },
       "city": "12345 Samplecity"
    }

When you want to rename "name" of "street", you have to copy it to be backward-compatible like

    "address": {
       "street": {
           "name": "Samplestreet",
           "streetName": "Samplestreet",
           "number": "1"
       },
       "city": "12345 Samplecity"
    }

Of course your server has to deal with both kind of data, one that contains a "name"
(from older clients) and one that contains "streetName" (from newer clients).
When answering on such requests, both fields have to be filled.
This framework automatically supports such backward-compatibility mapping by Java Annotations.
Simply annotate the "streetName" field in Java with @MovedFrom

    public class Street {
        @Deprecated
        private String ~ ~name~ ~;
        @MovedFrom("name")
        private String streetName;
        private String number;
    }
    
With the Annotations @MovedFrom, @Added and @Removed the framework supports most of the
use cases of backward-compatible interface development. For the more sophisticated cases
an interface named Provider exists, that can be implemented to do the logic in java code.

How to implement breaking changes?
==================================
This framework has the concept to handle backward-compatible changes within one Java Class
(see sample above) or a set of them.
When you want to implement a breaking change (i.e. really remove an attribute),
you have to switch to a different class
and specify the version and the corresponding class of the previous version via annotation.

    @SupportedVersion(version = "v2", previous = StreetV1.class)
    public class Street {
        ...
    }


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
