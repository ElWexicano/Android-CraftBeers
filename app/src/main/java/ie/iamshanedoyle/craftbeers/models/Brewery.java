package ie.iamshanedoyle.craftbeers.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Represents a Brewery object.
 *
 * @author Shane Doyle <@ElWexicano>
 */
public class Brewery implements Parcelable {

    private String name;
    private String description;
    private String website;
    private String established;
    private Images images;

    public Brewery() {}

    public Brewery(Parcel parcel) {
        readParcel(parcel);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getEstablished() {
        return established;
    }

    public void setEstablished(String established) {
        this.established = established;
    }

    public Images getImages() {
        return images;
    }

    public void setImages(Images images) {
        this.images = images;
    }

    public boolean hasImage() {
        return images != null && images.getIcon() != null;
    }

    public String getImage() {
        return images.getIcon();
    }

    private void readParcel(Parcel parcel) {
        name = parcel.readString();
        description = parcel.readString();
        website = parcel.readString();
        established = parcel.readString();
        images = parcel.readParcelable(Images.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(website);
        dest.writeString(established);
        dest.writeParcelable(images, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<Brewery> CREATOR
            = new Parcelable.Creator<Brewery>() {
        public Brewery createFromParcel(Parcel in) {
            return new Brewery(in);
        }

        public Brewery[] newArray(int size) {
            return new Brewery[size];
        }
    };
}
