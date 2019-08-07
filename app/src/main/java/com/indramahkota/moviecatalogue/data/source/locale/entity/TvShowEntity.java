package com.indramahkota.moviecatalogue.data.source.locale.entity;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.google.gson.annotations.SerializedName;
import com.indramahkota.moviecatalogue.data.source.locale.converter.CreditsTypeConverter;
import com.indramahkota.moviecatalogue.data.source.locale.converter.GenreListTypeConverter;
import com.indramahkota.moviecatalogue.data.source.remote.response.others.Credits;
import com.indramahkota.moviecatalogue.data.source.remote.response.others.Genres;

import java.util.List;

@Entity(tableName = TvShowEntity.TABLE_NAME)
public class TvShowEntity implements Parcelable {
    static final String TABLE_NAME = "tv_show";

    private static final String ID = "itemId";
    private static final String NAME = "name";
    private static final String FIRST_AIR_DATE = "firstAirDate";
    private static final String VOTE_AVERAGE = "voteAverage";
    private static final String OVERVIEW = "overview";
    private static final String POSTER_PATH = "posterPath";
    private static final String BACKDROP_PATH = "backdropPath";
    private static final String CREDITS = "credits";
    private static final String GENRES = "genres";
    private static final String ORIGINAL_LANGUAGE = "original_language";

    @PrimaryKey()
    @ColumnInfo(name = ID)
    @SerializedName("id")
    private Long id;

    @ColumnInfo(name = NAME)
    @SerializedName("name")
    private String name;

    @ColumnInfo(name = FIRST_AIR_DATE)
    @SerializedName("first_air_date")
    private String firstAirDate;

    @ColumnInfo(name = VOTE_AVERAGE)
    @SerializedName("vote_average")
    private Double voteAverage;

    @ColumnInfo(name = OVERVIEW)
    @SerializedName("overview")
    private String overview;

    @ColumnInfo(name = POSTER_PATH)
    @SerializedName("poster_path")
    private String posterPath;

    @ColumnInfo(name = BACKDROP_PATH)
    @SerializedName("backdrop_path")
    private String backdropPath;

    @ColumnInfo(name = CREDITS)
    @TypeConverters(CreditsTypeConverter.class)
    @SerializedName("credits")
    private Credits credits;

    @ColumnInfo(name = GENRES)
    @TypeConverters(GenreListTypeConverter.class)
    @SerializedName("genres")
    private List<Genres> genres;

    @ColumnInfo(name = ORIGINAL_LANGUAGE)
    @SerializedName("original_language")
    private String originalLanguage;

    public TvShowEntity() {}

    private TvShowEntity(@NonNull Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        name = in.readString();
        firstAirDate = in.readString();
        if (in.readByte() == 0) {
            voteAverage = null;
        } else {
            voteAverage = in.readDouble();
        }
        overview = in.readString();
        posterPath = in.readString();
        backdropPath = in.readString();
        credits = in.readParcelable(Credits.class.getClassLoader());
        genres = in.createTypedArrayList(Genres.CREATOR);
        originalLanguage = in.readString();
    }

    public static final Creator<TvShowEntity> CREATOR = new Creator<TvShowEntity>() {
        @Override
        public TvShowEntity createFromParcel(Parcel in) {
            return new TvShowEntity(in);
        }

        @Override
        public TvShowEntity[] newArray(int size) {
            return new TvShowEntity[size];
        }
    };

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getFirstAirDate() {
        return firstAirDate;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public String getOverview() {
        return overview;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public Credits getCredits() {
        return credits;
    }

    public List<Genres> getGenres() {
        return genres;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        if (id == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeLong(id);
        }
        parcel.writeString(name);
        parcel.writeString(firstAirDate);
        if (voteAverage == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeDouble(voteAverage);
        }
        parcel.writeString(overview);
        parcel.writeString(posterPath);
        parcel.writeString(backdropPath);
        parcel.writeParcelable(credits, i);
        parcel.writeTypedList(genres);
        parcel.writeString(originalLanguage);
    }
}