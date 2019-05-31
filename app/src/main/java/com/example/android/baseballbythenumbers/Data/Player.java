package com.example.android.baseballbythenumbers.Data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Comparator;

import static com.example.android.baseballbythenumbers.Data.Constants.BatterBaseStats.BATTING_DOUBLE_PCT_MEAN;
import static com.example.android.baseballbythenumbers.Data.Constants.BatterBaseStats.BATTING_HOME_RUN_PCT_MEAN;
import static com.example.android.baseballbythenumbers.Data.Constants.BatterBaseStats.BATTING_TRIPLE_PCT_MEAN;
import static com.example.android.baseballbythenumbers.Data.Constants.BatterBaseStats.BATTING_Z_SWING_PCT_MEAN;
import static com.example.android.baseballbythenumbers.Data.Positions.getPositionName;

public class Player implements Parcelable
{

    @SerializedName("firstName")
    @Expose
    private String firstName;
    @SerializedName("middleName")
    @Expose
    private String middleName;
    @SerializedName("lastName")
    @Expose
    private String lastName;
    @SerializedName("primaryPosition")
    @Expose
    private int primaryPosition;
    @SerializedName("alternatePositions")
    @Expose
    private int alternatePositions;
    @SerializedName("age")
    @Expose
    private int age;
    @SerializedName("dateOfBirth")
    @Expose
    private String dateOfBirth;
    @SerializedName("Hits")
    @Expose
    private String hits;
    @SerializedName("Throws")
    @Expose
    private String _throws;
    @SerializedName("currentSeasonStats")
    @Expose
    private String currentSeasonStats;
    @SerializedName("careerStats")
    @Expose
    private String careerStats;
    @SerializedName("hittingPercentages")
    @Expose
    private HittingPercentages hittingPercentages;
    @SerializedName("pitchingPercentages")
    @Expose
    private PitchingPercentages pitchingPercentages;
    public final static Parcelable.Creator<Player> CREATOR = new Creator<Player>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Player createFromParcel(Parcel in) {
            return new Player(in);
        }

        public Player[] newArray(int size) {
            return (new Player[size]);
        }

    }
            ;

    protected Player(Parcel in) {
        this.firstName = ((String) in.readValue((String.class.getClassLoader())));
        this.middleName = ((String) in.readValue((String.class.getClassLoader())));
        this.lastName = ((String) in.readValue((String.class.getClassLoader())));
        this.primaryPosition = ((int) in.readValue((int.class.getClassLoader())));
        this.alternatePositions = ((int) in.readValue((int.class.getClassLoader())));
        this.age = ((int) in.readValue((int.class.getClassLoader())));
        this.dateOfBirth = ((String) in.readValue((String.class.getClassLoader())));
        this.hits = ((String) in.readValue((String.class.getClassLoader())));
        this._throws = ((String) in.readValue((String.class.getClassLoader())));
        this.currentSeasonStats = ((String) in.readValue((String.class.getClassLoader())));
        this.careerStats = ((String) in.readValue((String.class.getClassLoader())));
        this.hittingPercentages = ((HittingPercentages) in.readValue((HittingPercentages.class.getClassLoader())));
        this.pitchingPercentages = ((PitchingPercentages) in.readValue((PitchingPercentages.class.getClassLoader())));
    }

    /**
     * No args constructor for use in serialization
     *
     */
    public Player() {
    }

    /**
     *
     * @param middleName
     * @param dateOfBirth
     * @param lastName
     * @param _throws
     * @param primaryPosition
     * @param currentSeasonStats
     * @param hittingPercentages
     * @param pitchingPercentages
     * @param hits
     * @param careerStats
     * @param age
     * @param alternatePositions
     * @param firstName
     */
    public Player(String firstName, String middleName, String lastName, int primaryPosition, int alternatePositions, int age, String dateOfBirth, String hits, String _throws, String currentSeasonStats, String careerStats, HittingPercentages hittingPercentages, PitchingPercentages pitchingPercentages) {
        super();
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.primaryPosition = primaryPosition;
        this.alternatePositions = alternatePositions;
        this.age = age;
        this.dateOfBirth = dateOfBirth;
        this.hits = hits;
        this._throws = _throws;
        this.currentSeasonStats = currentSeasonStats;
        this.careerStats = careerStats;
        this.hittingPercentages = hittingPercentages;
        this.pitchingPercentages = pitchingPercentages;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getPrimaryPosition() {
        return primaryPosition;
    }

    public void setPrimaryPosition(int primaryPosition) {
        this.primaryPosition = primaryPosition;
    }

    public int getAlternatePositions() {
        return alternatePositions;
    }

    public void setAlternatePositions(int alternatePositions) {
        this.alternatePositions = alternatePositions;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getHits() {
        return hits;
    }

    public void setHits(String hits) {
        this.hits = hits;
    }

    public String getThrows() {
        return _throws;
    }

    public void setThrows(String _throws) {
        this._throws = _throws;
    }

    public String getCurrentSeasonStats() {
        return currentSeasonStats;
    }

    public void setCurrentSeasonStats(String currentSeasonStats) {
        this.currentSeasonStats = currentSeasonStats;
    }

    public String getCareerStats() {
        return careerStats;
    }

    public void setCareerStats(String careerStats) {
        this.careerStats = careerStats;
    }

    public HittingPercentages getHittingPercentages() {
        return hittingPercentages;
    }

    public void setHittingPercentages(HittingPercentages hittingPercentages) {
        this.hittingPercentages = hittingPercentages;
    }

    public PitchingPercentages getPitchingPercentages() {
        return pitchingPercentages;
    }

    public void setPitchingPercentages(PitchingPercentages pitchingPercentages) {
        this.pitchingPercentages = pitchingPercentages;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(firstName);
        dest.writeValue(middleName);
        dest.writeValue(lastName);
        dest.writeValue(primaryPosition);
        dest.writeValue(alternatePositions);
        dest.writeValue(age);
        dest.writeValue(dateOfBirth);
        dest.writeValue(hits);
        dest.writeValue(_throws);
        dest.writeValue(currentSeasonStats);
        dest.writeValue(careerStats);
        dest.writeValue(hittingPercentages);
        dest.writeValue(pitchingPercentages);
    }

    public int describeContents() {
        return 0;
    }


    public String getName() {return firstName + " " + lastName;}

    @Override
    public String toString() {
        return "Name : " + firstName + " " + middleName + " " + lastName + "\n"+
                "Position : " + getPositionName(primaryPosition) + "\n"+
                "Age : " + age + "\n"+
                "Date of Birth : " + dateOfBirth + "\n"+
                "Current Season Stats : " + currentSeasonStats + "\n"+
                "Career Stats : " + careerStats + "\n"+
                "Hitting Percentages : " + hittingPercentages + "\n"+
                "Pitching Percentages : " + pitchingPercentages + "\n";
    }

    public static Comparator<Player> PrimaryPositionComparator = new Comparator<Player>() {
        @Override
        public int compare(Player player1, Player player2) {
            int positionOfPlayer1 = player1.primaryPosition;
            int positionOfPlayer2 = player2.primaryPosition;
            //ascending order so player1-player2 --> Gives output in order from least to highest position number
            return positionOfPlayer1-positionOfPlayer2;
        }
    };

    public static Comparator<Player> ErrorPctComparator = new Comparator<Player>() {
        @Override
        public int compare(Player player1, Player player2) {
            int errorPctPlayer1 = player1.hittingPercentages.getErrorPct();
            int errorPctPlayer2 = player2.hittingPercentages.getErrorPct();
            //ascending order so player1-player2, want lowest error rate (best fielder) first
            return errorPctPlayer1-errorPctPlayer2;
        }
    };

    public static Comparator<Player> BestPowerComparator = new Comparator<Player>() {
        @Override
        public int compare(Player player1, Player player2) {
            int powerPlayer1 = ((player1.hittingPercentages.getHomeRunPct() - BATTING_HOME_RUN_PCT_MEAN)) + player1.hittingPercentages.getTriplePct() - BATTING_TRIPLE_PCT_MEAN +
                    ((player1.hittingPercentages.getDoublePct() - BATTING_DOUBLE_PCT_MEAN));
            int powerPlayer2 = ((player2.hittingPercentages.getHomeRunPct() - BATTING_HOME_RUN_PCT_MEAN)) + player2.hittingPercentages.getTriplePct() - BATTING_TRIPLE_PCT_MEAN +
                    ((player2.hittingPercentages.getDoublePct() - BATTING_DOUBLE_PCT_MEAN));
            //descending order so player2-player1, want highest power rate (best power) first
            return powerPlayer2-powerPlayer1;
        }
    };

    public static Comparator<Player> BestOnBaseComparator = new Comparator<Player>() {
        @Override
        public int compare(Player player1, Player player2) {
            int onBasePlayer1 = BATTING_Z_SWING_PCT_MEAN - player1.hittingPercentages.getZSwingPct() + player1.hittingPercentages.getBattingAverageBallsInPlay();
            int onBaseplayer2 = BATTING_Z_SWING_PCT_MEAN - player2.hittingPercentages.getZSwingPct() + player2.hittingPercentages.getBattingAverageBallsInPlay();
            //descending order so player2-player1, want highest on base rate (best onbase pct) first
            return onBaseplayer2-onBasePlayer1;
        }
    };

    public static Comparator<Player> BestCombinedOnBaseAndPowerComparator = new Comparator<Player>() {
        @Override
        public int compare(Player player1, Player player2) {
            int powerPlayer1 = BATTING_Z_SWING_PCT_MEAN - player1.hittingPercentages.getZSwingPct() + player1.hittingPercentages.getBattingAverageBallsInPlay() +
                    ((player1.hittingPercentages.getHomeRunPct() - BATTING_HOME_RUN_PCT_MEAN)) + player1.hittingPercentages.getTriplePct() - BATTING_TRIPLE_PCT_MEAN  +
                    player1.hittingPercentages.getDoublePct() - BATTING_DOUBLE_PCT_MEAN;
            int powerplayer2 = BATTING_Z_SWING_PCT_MEAN - player2.hittingPercentages.getZSwingPct() + player2.hittingPercentages.getBattingAverageBallsInPlay() +
                    ((player2.hittingPercentages.getHomeRunPct() - BATTING_HOME_RUN_PCT_MEAN)) + player2.hittingPercentages.getTriplePct() - BATTING_TRIPLE_PCT_MEAN  +
                    player2.hittingPercentages.getDoublePct() - BATTING_DOUBLE_PCT_MEAN;
            //descending order so player2-player1, want highest combined rate (best onbase pct + power) first
            return powerplayer2-powerPlayer1;
        }
    };
}