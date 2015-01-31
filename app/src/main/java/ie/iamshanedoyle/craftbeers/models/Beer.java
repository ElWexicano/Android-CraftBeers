package ie.iamshanedoyle.craftbeers.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Represents a Beer object.
 *
 * @author Shane Doyle <@ElWexicano>
 */
public class Beer implements Parcelable {

    private String id;
    private String name;
    private String description;
    private Labels labels;

    public Beer() {}

    public Beer(Parcel parcel) {
        readFromParcel(parcel);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Labels getLabels() {
        return labels;
    }

    public void setLabels(Labels labels) {
        this.labels = labels;
    }

    public boolean hasLabel() {
        return labels != null && labels.getIcon() != null;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeParcelable(labels, flags);
    }

    private void readFromParcel(Parcel parcel) {
        id = parcel.readString();
        name = parcel.readString();
        description = parcel.readString();
        labels = parcel.readParcelable(Labels.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<Beer> CREATOR
            = new Parcelable.Creator<Beer>() {
        public Beer createFromParcel(Parcel in) {
            return new Beer(in);
        }

        public Beer[] newArray(int size) {
            return new Beer[size];
        }
    };
}
