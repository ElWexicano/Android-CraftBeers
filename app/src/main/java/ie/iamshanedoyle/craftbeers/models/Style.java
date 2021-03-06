package ie.iamshanedoyle.craftbeers.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Represents a Style object.
 *
 * @author Shane Doyle <@ElWexicano>
 */
public class Style implements Parcelable {
    public static final Parcelable.Creator<Style> CREATOR
            = new Parcelable.Creator<Style>() {
        public Style createFromParcel(Parcel in) {
            return new Style(in);
        }

        public Style[] newArray(int size) {
            return new Style[size];
        }
    };
    private String name;
    private String description;

    public Style() {
    }

    public Style(Parcel parcel) {
        readFromParcel(parcel);
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

    private void readFromParcel(Parcel parcel) {
        name = parcel.readString();
        description = parcel.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(description);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
