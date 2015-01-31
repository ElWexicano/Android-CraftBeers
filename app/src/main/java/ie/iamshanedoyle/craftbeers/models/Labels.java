package ie.iamshanedoyle.craftbeers.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Represents a Beer labels object.
 *
 * @author Shane Doyle <@ElWexicano>
 */
public class Labels implements Parcelable {
    private String icon;
    private String medium;
    private String large;

    public Labels() {}

    public Labels(Parcel parcel) {
        readFromParcel(parcel);
    }

    public String getIcon() {
        return icon;
    }

    public String getMedium() {
        return medium;
    }

    public String getLarge() {
        return large;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(icon);
        dest.writeString(medium);
        dest.writeString(large);
    }

    private void readFromParcel(Parcel parcel) {
        icon = parcel.readString();
        medium = parcel.readString();
        large = parcel.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<Labels> CREATOR
            = new Parcelable.Creator<Labels>() {
        public Labels createFromParcel(Parcel in) {
            return new Labels(in);
        }

        public Labels[] newArray(int size) {
            return new Labels[size];
        }
    };
}
