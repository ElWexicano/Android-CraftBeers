package ie.iamshanedoyle.craftbeers.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Represents a Glass object.
 *
 * @author Shane Doyle <@ElWexicano>
 */
public class Glass implements Parcelable {
    public static final Parcelable.Creator<Glass> CREATOR
            = new Parcelable.Creator<Glass>() {
        public Glass createFromParcel(Parcel in) {
            return new Glass(in);
        }

        public Glass[] newArray(int size) {
            return new Glass[size];
        }
    };
    private String name;

    public Glass() {
    }

    public Glass(Parcel parcel) {
        readFromParcel(parcel);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private void readFromParcel(Parcel parcel) {
        name = parcel.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
