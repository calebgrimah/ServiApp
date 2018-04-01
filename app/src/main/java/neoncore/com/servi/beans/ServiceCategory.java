package neoncore.com.servi.beans;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Musa on 3/9/2018.
 */

public class ServiceCategory {

    String category_id, category_name,cart_image_url, category_description;
    @ServerTimestamp
    Date timestamp;

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getCategory_description() {
        return category_description;
    }

    public void setCategory_description(String category_description) {
        this.category_description = category_description;
    }

    /*
        * @param category_id required id for each unique category which is tightly coupled to this class
        * @param category_name required name for the category
        * @param cart_image_url required string version for the category image URL.
        *
        *
        */
    public ServiceCategory(String category_name, String cart_image_url) {
        this.category_id = UUID.randomUUID().toString();
        this.category_name = category_name;
        this.cart_image_url = cart_image_url;
    }

    public ServiceCategory(String category_id, String category_name, String category_description) {
        this.category_id = category_id;
        this.category_name = category_name;
        this.category_description = category_description;
    }

    public ServiceCategory() {
    }

    public String getCategory_id() {
        return category_id;
    }


    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getCart_image_url() {
        return cart_image_url;
    }

    public void setCart_image_url(String cart_image_url) {
        this.cart_image_url = cart_image_url;
    }
}
