package ie.iamshanedoyle.craftbeers.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Beer object.
 *
 * @author Shane Doyle <@ElWexicano>
 */
public class Beer implements Parcelable {

    public static final Parcelable.Creator<Beer> CREATOR
            = new Parcelable.Creator<Beer>() {
        public Beer createFromParcel(Parcel in) {
            return new Beer(in);
        }

        public Beer[] newArray(int size) {
            return new Beer[size];
        }
    };
    private String id;
    private String name;
    private String description;
    private String ibu;
    private String abv;
    private int year;
    private List<Brewery> breweries = new ArrayList<>();
    private Glass glass;
    private Images labels;
    private Style style;

    public Beer() {
    }

    public Beer(Parcel parcel) {
        readFromParcel(parcel);
    }

    public String getIbu() {
        return ibu;
    }

    public void setIbu(String ibu) {
        this.ibu = ibu;
    }

    public String getAbv() {
        return abv;
    }

    public void setAbv(String abv) {
        this.abv = abv;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
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

    public Images getLabels() {
        return labels;
    }

    public void setLabels(Images labels) {
        this.labels = labels;
    }

    public boolean hasLabel() {
        return labels != null && labels.getIcon() != null;
    }

    public boolean hasBrewery() {
        return breweries != null && breweries.size() >= 1;
    }

    public List<Brewery> getBreweries() {
        return breweries;
    }

    public void setBreweries(List<Brewery> breweries) {
        this.breweries = breweries;
    }

    public Brewery getBrewery() {
        return breweries.get(0);
    }

    public Glass getGlass() {
        return glass;
    }

    public void setGlass(Glass glass) {
        this.glass = glass;
    }

    public Style getStyle() {
        return style;
    }

    public void setStyle(Style style) {
        this.style = style;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(abv);
        dest.writeString(ibu);
        dest.writeInt(year);
        dest.writeList(breweries);
        dest.writeParcelable(glass, flags);
        dest.writeParcelable(labels, flags);
        dest.writeParcelable(style, flags);
    }

    private void readFromParcel(Parcel parcel) {
        id = parcel.readString();
        name = parcel.readString();
        description = parcel.readString();
        abv = parcel.readString();
        ibu = parcel.readString();
        year = parcel.readInt();
        parcel.readList(breweries, Brewery.class.getClassLoader());
        glass = parcel.readParcelable(Images.class.getClassLoader());
        labels = parcel.readParcelable(Images.class.getClassLoader());
        style = parcel.readParcelable(Images.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

}
