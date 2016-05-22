package com.abstractcoders.mostafa.foodies.Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mostafa on 03/02/2016.
 */
public class GooglePlaceDetails {

    /**
     *
     */
    private static final long serialVersionUID = -4041502421563593320L;
    //@Key
    private String name;
    //@Key
    private String vicinity;
    //@Key
    private String formatted_address;
    //@Key
    private String formatted_phone_number;

    private String photoReference;

    //@Key
    private boolean isOpen;

    //@Key
    private List<Photo> photos;

    //@Key
    private List<String> types;

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }

    public static class Photo implements Serializable {


        private int width;
        private int height;
        private String photo_reference;


        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public String getPhoto_reference() {
            return photo_reference;
        }

        public void setPhoto_reference(String photo_reference) {
            this.photo_reference = photo_reference;
        }


    }



    public static class Geometry implements Serializable {
        /**
         *
         */
        private static final long serialVersionUID = 2946649576104623502L;

        public static class Location implements Serializable {
            /**
             *
             */
            private static final long serialVersionUID = -1861462299276634548L;
            //@Key
            private double lat;
            //@Key
            private double lng;

            /**
             * @return the lat
             */
            public double getLat() {
                return lat;
            }

            /**
             * @param lat the lat to set
             */
            public void setLat(double lat) {
                this.lat = lat;
            }

            /**
             * @return the lng
             */
            public double getLng() {
                return lng;
            }

            /**
             * @param lng the lng to set
             */
            public void setLng(double lng) {
                this.lng = lng;
            }
        }

        //@Key
        private Location location;

        /**
         * @param location the location to set
         */
        public void setLocation(Location location) {
            this.location = location;
        }

        /**
         * @return the location
         */
        public Location getLocation() {
            return location;
        }
    }

    //@Key
    private Geometry geometry;

    //@Key
    private String icon;

    private List<String> openingTimes = new ArrayList<String>();


    //@Key
    private String id;

    //@Key
    private String reference;

    //@Key
    private float rating;

    //@Key
    private String url;

    private ArrayList<Review> reviews;

    public static class Review implements Serializable {

        public static class Aspect implements Serializable {
            private int rating;
            private String type;

            public void setRating(int r) {
                rating = r;
            }

            public int getRating() {
                return rating;
            }

            public void setType(String t) {
                type = t;
            }

            public String getType() {
                return type;
            }
        }

        private ArrayList<Aspect> aspects;
        private String author_name;
        private int rating;
        private String text;
                /*
                 * For example, the JSON data looks like this...
                 *
                "author_name" : "Little Al",
                "author_url" : "https://plus.google.com/103209428135026695692",
                "language" : "en",
                "rating" : 5,
                "text" : "fabulous experience ..really enjoyed",
                "time" : 1422356740
                *
                */

        public int getRating() { return rating; }

        public void setRating(int rating) {
            this.rating = rating;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getAuthor_name() {
            return author_name;
        }

        public void setAuthor_name(String author_name) {
            this.author_name = author_name;
        }

        public void setAspects(ArrayList<Aspect> aspects) {
            this.aspects = aspects;
        }

        public List<Aspect> getAspects() {
            return aspects;
        }
    }

    //@Key
    private String website;

    public GooglePlaceDetails() {
        types = new ArrayList<String>();
    }

    public GooglePlaceDetails(String input, GooglePlace place) throws JSONException {
        types = new ArrayList<String>();
        this.photos = new ArrayList<Photo>();
        JSONObject array = new JSONObject(input);
        JSONObject result = new JSONObject(array.getJSONObject("result").toString());
        if(result.has("photos")) {
            JSONArray photos = (JSONArray) result.get("photos");
            for(int i = 0; i < photos.length();i++)
            {
                Photo photo = new Photo();
                photo.setPhoto_reference((String) photos.getJSONObject(i).get("photo_reference"));
                photo.setWidth((int) photos.getJSONObject(i).get("width"));
                photo.setHeight((int) photos.getJSONObject(i).get("height"));
                getPhotos().add(photo);

            }
            JSONObject photoDetails = photos.getJSONObject(0);
            if(photoDetails.has("photo_reference"))
            {
                String photoReference = (String) photoDetails.get("photo_reference");
                this.photoReference = photoReference;
            }
        }
        if(result.has("types")) {
            JSONArray typesArray = (JSONArray) result.get("types");
            for(int i = 0; i < typesArray.length(); i++)
            {
                String s = (String) typesArray.get(i);
                types.add(s);
            }
        }
        if(result.has("formatted_address"))
        {
            setFormatted_address((String) result.get("formatted_address"));
        }
        if(result.has("website"))
        {
            setWebsite((String) result.get("website"));
        }
        if(result.has("opening_hours"))
        {
            JSONObject opening_hours = (JSONObject) result.get("opening_hours");
            if(opening_hours.has("open_now")) {
                boolean isOpen = (boolean) opening_hours.get("open_now");
                setIsOpen(isOpen);
            }
            if(opening_hours.has("weekday_text"))
            {
                JSONArray openingsArray = (JSONArray) opening_hours.get("weekday_text");
                for(int i = 0; i < openingsArray.length(); i++) {
                    String openingTime = (String) openingsArray.get(i);
                    openingTimes.add(openingTime);
                }
            }
        }
    }

    @Override
    public String toString() {
        String typeList = "";
        for (String type : types) {
            typeList = typeList + type + " ";
        }
        return name + "\n" + vicinity + "\n" + typeList + "\n" +
                this.getGeometry().getLocation().getLat() + ", " +
                this.getGeometry().getLocation().getLng();
    }

    /**
     * @param geometry the geometry to set
     */
    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    /**
     * @return the geometry
     */
    public Geometry getGeometry() {
        return geometry;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(ArrayList<Review> reviews) {
        this.reviews = reviews;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVicinity() {
        return vicinity;
    }

    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }

    public List<String> getTypes() {
        return types;
    }

    /**
     * @param types the types to set
     */
    public void setTypes(List<String> types) {
        this.types = types;
    }

    public void addType(String type) {
        types.add(type);
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getFormatted_address() {
        return formatted_address;
    }

    public void setFormatted_address(String formatted_address) {
        this.formatted_address = formatted_address;
    }

    public String getFormatted_phone_number() {
        return formatted_phone_number;
    }

    public void setFormatted_phone_number(String formatted_phone_number) {
        this.formatted_phone_number = formatted_phone_number;
    }

    public String getPhotoReference() {
        return photoReference;
    }

    public void setPhotoReference(String photoReference) {
        this.photoReference = photoReference;
    }

    public boolean getIsOpen() {
        return isOpen;
    }

    public void setIsOpen(boolean isOpen) {
        this.isOpen = isOpen;
    }

    public List<String> getOpeningTimes() {
        return openingTimes;
    }

    public void setOpeningTimes(List<String> openingTimes) {
        this.openingTimes = openingTimes;
    }
}
