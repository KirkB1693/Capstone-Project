package com.example.android.baseballbythenumbers.data;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import static androidx.room.ForeignKey.CASCADE;
import static com.example.android.baseballbythenumbers.constants.Constants.BatterBaseStats.BATTING_DOUBLE_PCT_MEAN;
import static com.example.android.baseballbythenumbers.constants.Constants.BatterBaseStats.BATTING_HOME_RUN_PCT_MEAN;
import static com.example.android.baseballbythenumbers.constants.Constants.BatterBaseStats.BATTING_HOME_RUN_PCT_MIN;
import static com.example.android.baseballbythenumbers.constants.Constants.BatterBaseStats.BATTING_HOME_RUN_RANGE;
import static com.example.android.baseballbythenumbers.constants.Constants.BatterBaseStats.BATTING_O_CONTACT_PCT_MIN;
import static com.example.android.baseballbythenumbers.constants.Constants.BatterBaseStats.BATTING_O_CONTACT_RANGE;
import static com.example.android.baseballbythenumbers.constants.Constants.BatterBaseStats.BATTING_O_SWING_PCT_MEAN;
import static com.example.android.baseballbythenumbers.constants.Constants.BatterBaseStats.BATTING_O_SWING_PCT_MIN;
import static com.example.android.baseballbythenumbers.constants.Constants.BatterBaseStats.BATTING_O_SWING_RANGE;
import static com.example.android.baseballbythenumbers.constants.Constants.BatterBaseStats.BATTING_SPEED_MIN;
import static com.example.android.baseballbythenumbers.constants.Constants.BatterBaseStats.BATTING_SPEED_RANGE;
import static com.example.android.baseballbythenumbers.constants.Constants.BatterBaseStats.BATTING_TRIPLE_PCT_MEAN;
import static com.example.android.baseballbythenumbers.constants.Constants.BatterBaseStats.BATTING_Z_CONTACT_PCT_MIN;
import static com.example.android.baseballbythenumbers.constants.Constants.BatterBaseStats.BATTING_Z_CONTACT_RANGE;
import static com.example.android.baseballbythenumbers.constants.Constants.BatterBaseStats.BATTING_Z_SWING_PCT_MIN;
import static com.example.android.baseballbythenumbers.constants.Constants.BatterBaseStats.BATTING_Z_SWING_RANGE;
import static com.example.android.baseballbythenumbers.constants.Constants.PitcherBaseStats.PITCHER_HOME_RUN_PCT_MEAN;
import static com.example.android.baseballbythenumbers.constants.Constants.PitcherBaseStats.PITCHER_O_CONTACT_PCT_MEAN;
import static com.example.android.baseballbythenumbers.constants.Constants.PitcherBaseStats.PITCHER_O_CONTACT_PCT_MIN;
import static com.example.android.baseballbythenumbers.constants.Constants.PitcherBaseStats.PITCHER_O_CONTACT_RANGE;
import static com.example.android.baseballbythenumbers.constants.Constants.PitcherBaseStats.PITCHER_O_SWING_PCT_MEAN;
import static com.example.android.baseballbythenumbers.constants.Constants.PitcherBaseStats.PITCHER_O_SWING_PCT_MIN;
import static com.example.android.baseballbythenumbers.constants.Constants.PitcherBaseStats.PITCHER_O_SWING_RANGE;
import static com.example.android.baseballbythenumbers.constants.Constants.PitcherBaseStats.PITCHER_ZONE_PCT_MEAN;
import static com.example.android.baseballbythenumbers.constants.Constants.PitcherBaseStats.PITCHER_ZONE_PCT_MIN;
import static com.example.android.baseballbythenumbers.constants.Constants.PitcherBaseStats.PITCHER_ZONE_RANGE;
import static com.example.android.baseballbythenumbers.constants.Constants.PitcherBaseStats.PITCHER_Z_CONTACT_PCT_MEAN;
import static com.example.android.baseballbythenumbers.constants.Constants.PitcherBaseStats.PITCHER_Z_CONTACT_PCT_MIN;
import static com.example.android.baseballbythenumbers.constants.Constants.PitcherBaseStats.PITCHER_Z_CONTACT_RANGE;
import static com.example.android.baseballbythenumbers.constants.Constants.PitcherBaseStats.PITCHER_Z_SWING_PCT_MEAN;
import static com.example.android.baseballbythenumbers.constants.Constants.PitcherBaseStats.PITCHER_Z_SWING_PCT_MIN;
import static com.example.android.baseballbythenumbers.constants.Constants.PitcherBaseStats.PITCHER_Z_SWING_RANGE;
import static com.example.android.baseballbythenumbers.constants.Constants.PitcherBaseStats.STARTER_STAMINA_MAX;
import static com.example.android.baseballbythenumbers.constants.Positions.getPositionNameFromPrimaryPosition;
import static com.example.android.baseballbythenumbers.generators.PitcherGenerator.ONE_HUNDRED_PERCENT;

@SuppressWarnings("NotNullFieldNotInitialized")
@Entity(tableName = "players", foreignKeys = @ForeignKey(entity = Team.class, parentColumns = "teamId", childColumns = "teamId", onDelete = CASCADE), indices = @Index(value = "teamId"))
public class Player implements Parcelable
{
    @PrimaryKey
    @NonNull
    private String playerId;

    private String teamId;

    private String firstName;

    private String middleName;

    private String lastName;

    private int primaryPosition;

    private int alternatePositions;

    private int age;

    private String dateOfBirth;

    private String hits;

    private String throwingHand;

    @Ignore
    private List<BattingStats> battingStats = null;

    @Ignore
    private List<PitchingStats> pitchingStats = null;

    @Embedded
    private HittingPercentages hittingPercentages;

    @Embedded
    private PitchingPercentages pitchingPercentages;


    /**
     * No args constructor for use in serialization
     *
     */

    public Player() {
    }

    /**
     *
     * @param middleName players middle name
     * @param dateOfBirth players birth date
     * @param lastName players last name
     * @param primaryPosition players primary position
     * @param battingStats list of the players batting stats
     * @param pitchingStats list of the players pitching stats
     * @param hittingPercentages players hitting percentages
     * @param pitchingPercentages players pitching percentages
     * @param hits what side the player hits from (Right, Left or Switch)
     * @param age how old the player is
     * @param alternatePositions other positions the player can play
     * @param firstName the players first name
     * @param throwingHand what hand the player throws with
     */
    @Ignore
    public Player(String firstName, String middleName, String lastName, int primaryPosition, int alternatePositions, int age, String dateOfBirth, String hits,
                  String throwingHand, List<BattingStats> battingStats, List<PitchingStats> pitchingStats, HittingPercentages hittingPercentages, PitchingPercentages pitchingPercentages,
                  String teamId) {
        super();
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.primaryPosition = primaryPosition;
        this.alternatePositions = alternatePositions;
        this.age = age;
        this.dateOfBirth = dateOfBirth;
        this.hits = hits;
        this.throwingHand = throwingHand;
        this.battingStats = battingStats;
        this.pitchingStats = pitchingStats;
        this.hittingPercentages = hittingPercentages;
        this.pitchingPercentages = pitchingPercentages;
        this.teamId = teamId;
        this.playerId = UUID.randomUUID().toString();
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

    public String getThrowingHand() {
        return throwingHand;
    }

    public void setThrowingHand(String throwingHand) {
        this.throwingHand = throwingHand;
    }

    public List<BattingStats> getBattingStats() {
        return battingStats;
    }

    public void setBattingStats(List<BattingStats> battingStats) {
        this.battingStats = battingStats;
    }

    public List<PitchingStats> getPitchingStats() {
        return pitchingStats;
    }

    public void setPitchingStats(List<PitchingStats> pitchingStats) {
        this.pitchingStats = pitchingStats;
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

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    @NotNull
    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(@NotNull String playerId) {
        this.playerId = playerId;
    }

    public BattingStats getBattingStatsForYear(int year) {
        if (battingStats != null) {
            for (BattingStats battingStats : battingStats) {
                if (battingStats.getYear() == year) {
                    return battingStats;
                }
            }
        }
        return null;
    }

    public PitchingStats getPitchingStatsForYear(int year) {
        if (pitchingStats != null) {
            for (PitchingStats pitchingStats : pitchingStats) {
                if (pitchingStats.getYear() == year) {
                    return pitchingStats;
                }
            }
        }
        return null;
    }

    public String getName() {return firstName + " " + lastName;}

    public int getBattingContactRating() {
        if (getHittingPercentages() != null) {
            return ((int) (((double) (getHittingPercentages().getOContactPct() - BATTING_O_CONTACT_PCT_MIN) / BATTING_O_CONTACT_RANGE) * 50) +
                    (int) (((double) (getHittingPercentages().getZContactPct() - BATTING_Z_CONTACT_PCT_MIN) / BATTING_Z_CONTACT_RANGE) * 50));
        } else {
            return -1;
        }
    }

    public int getBattingEyeRating() {
        if (getHittingPercentages() != null) {
            return (int) ((50.0 - ((double) (getHittingPercentages().getOSwingPct()- BATTING_O_SWING_PCT_MIN) / BATTING_O_SWING_RANGE) * 50)) +
                    (int) (((double) (getHittingPercentages().getZSwingPct()- BATTING_Z_SWING_PCT_MIN) / BATTING_Z_SWING_RANGE) * 50);
        } else {
            return -1;
        }
    }

    public int getBattingPowerRating() {
        if (getHittingPercentages() != null) {
            return (int) (((double) (getHittingPercentages().getHomeRunPct()- BATTING_HOME_RUN_PCT_MIN) / BATTING_HOME_RUN_RANGE) * 100);
        } else {
            return -1;
        }
    }

    public int getBattingSpeedRating() {
        if (getHittingPercentages() != null) {
            return (int) (((double) (getHittingPercentages().getSpeed()- BATTING_SPEED_MIN) / BATTING_SPEED_RANGE) * 100);
        } else {
            return -1;
        }
    }

    public int getMovementRating() {
        if (getPitchingPercentages() != null) {
            return (int) (50.0 - ((double) (getPitchingPercentages().getOContactPct() - PITCHER_O_CONTACT_PCT_MIN) / PITCHER_O_CONTACT_RANGE) * 50) +
                    (int) (50.00 - ((double) (getPitchingPercentages().getZContactPct()- PITCHER_Z_CONTACT_PCT_MIN) / PITCHER_Z_CONTACT_RANGE) * 50);
        } else {
            return -1;
        }
    }

    public int getDeceptionRating() {
        if (getPitchingPercentages() != null) {
            return (int) ((((double) (getPitchingPercentages().getOSwingPct()- PITCHER_O_SWING_PCT_MIN) / PITCHER_O_SWING_RANGE) * 50)) +
                    (int) (50.0 - ((double) (getPitchingPercentages().getZSwingPct()- PITCHER_Z_SWING_PCT_MIN) / PITCHER_Z_SWING_RANGE) * 50);
        } else {
            return -1;
        }
    }

    public int getAccuracyRating() {
        if (getPitchingPercentages() != null) {
            return (int) (((double) (getPitchingPercentages().getZonePct()- PITCHER_ZONE_PCT_MIN) / PITCHER_ZONE_RANGE) * 100);
        } else {
            return -1;
        }
    }

    public int getPitchingStaminaRating() {
        if (getPitchingPercentages() != null) {
            return (int)  (((double) getPitchingPercentages().getPitchingStamina() / STARTER_STAMINA_MAX) * 100);
        } else {
            return -1;
        }
    }



    @NotNull
    @Override
    public String toString() {
        return "Name : " + firstName + " " + middleName + " " + lastName + "\n"+
                "Position : " + getPositionNameFromPrimaryPosition(primaryPosition) + "\n"+
                "Age : " + age + "\n"+
                "Date of Birth : " + dateOfBirth + "\n"+
                "Batting Stats : " + battingStats + "\n"+
                "Pitching Stats : " + pitchingStats + "\n"+
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
            int ballsPctToAdd = ((ONE_HUNDRED_PERCENT - player1.hittingPercentages.getOSwingPct())*(ONE_HUNDRED_PERCENT - BATTING_O_SWING_PCT_MEAN))/(ONE_HUNDRED_PERCENT);
            int adjustedBallsPctToAdd = getIntToPower(ballsPctToAdd, 5);
            int onBasePlayer1 = (player1.hittingPercentages.getBattingAverageBallsInPlay() + adjustedBallsPctToAdd);
            ballsPctToAdd = ((ONE_HUNDRED_PERCENT - player2.hittingPercentages.getOSwingPct())*(ONE_HUNDRED_PERCENT - BATTING_O_SWING_PCT_MEAN))/(ONE_HUNDRED_PERCENT);
            adjustedBallsPctToAdd = getIntToPower(ballsPctToAdd, 5);
            int onBasePlayer2 = player2.hittingPercentages.getBattingAverageBallsInPlay() + adjustedBallsPctToAdd;
            //descending order so player2-player1, want highest on base rate (best on base pct) first
            return onBasePlayer2-onBasePlayer1;
        }

    };

    public static Comparator<Player> BestCombinedOnBaseAndPowerComparator = new Comparator<Player>() {
        @Override
        public int compare(Player player1, Player player2) {
            int ballsPctToAdd = ((ONE_HUNDRED_PERCENT - player1.hittingPercentages.getOSwingPct())*(ONE_HUNDRED_PERCENT - BATTING_O_SWING_PCT_MEAN))/(ONE_HUNDRED_PERCENT);
            int adjustedBallsPctToAdd = getIntToPower(ballsPctToAdd, 5);
            int combinedPlayer1 = (player1.hittingPercentages.getBattingAverageBallsInPlay() + adjustedBallsPctToAdd) +
                    player1.hittingPercentages.getHomeRunPct() - BATTING_HOME_RUN_PCT_MEAN +
                    player1.hittingPercentages.getTriplePct() - BATTING_TRIPLE_PCT_MEAN  +
                    player1.hittingPercentages.getDoublePct() - BATTING_DOUBLE_PCT_MEAN;
            ballsPctToAdd = ((ONE_HUNDRED_PERCENT - player2.hittingPercentages.getOSwingPct())*(ONE_HUNDRED_PERCENT - BATTING_O_SWING_PCT_MEAN))/(ONE_HUNDRED_PERCENT);
            adjustedBallsPctToAdd = getIntToPower(ballsPctToAdd, 5);
            int combinedPlayer2 = player2.hittingPercentages.getBattingAverageBallsInPlay() + adjustedBallsPctToAdd +
                    player2.hittingPercentages.getHomeRunPct() - BATTING_HOME_RUN_PCT_MEAN + player2.hittingPercentages.getTriplePct() - BATTING_TRIPLE_PCT_MEAN  +
                    player2.hittingPercentages.getDoublePct() - BATTING_DOUBLE_PCT_MEAN;

            //descending order so player2-player1, want highest combined rate (best on base pct + power) first
            return combinedPlayer2-combinedPlayer1;
        }

    };

    public static Comparator<Player> BestPitcherComparator = new Comparator<Player>() {
        @Override
        public int compare(Player player1, Player player2) {
            int pitchingPlayer1 = (((PITCHER_O_SWING_PCT_MEAN - player1.pitchingPercentages.getOSwingPct())*4) +
                    ( player1.pitchingPercentages.getZSwingPct() - PITCHER_Z_SWING_PCT_MEAN) * 2 +
                    (player1.pitchingPercentages.getZContactPct() - PITCHER_Z_CONTACT_PCT_MEAN) * 2  +
                    (PITCHER_ZONE_PCT_MEAN - player1.pitchingPercentages.getZonePct())  +
                    (player1.pitchingPercentages.getOContactPct() - PITCHER_O_CONTACT_PCT_MEAN)) * 4  +
                    (player1.pitchingPercentages.getHomeRunPct() - PITCHER_HOME_RUN_PCT_MEAN) * 13;
            int pitchingPlayer2 = (((PITCHER_O_SWING_PCT_MEAN - player2.pitchingPercentages.getOSwingPct())*4) +
                    ( player2.pitchingPercentages.getZSwingPct() - PITCHER_Z_SWING_PCT_MEAN) * 2 +
                    (player2.pitchingPercentages.getZContactPct() - PITCHER_Z_CONTACT_PCT_MEAN) * 2  +
                    (PITCHER_ZONE_PCT_MEAN - player2.pitchingPercentages.getZonePct())  +
                    (player2.pitchingPercentages.getOContactPct() - PITCHER_O_CONTACT_PCT_MEAN)) * 4  +
                    (player2.pitchingPercentages.getHomeRunPct() - PITCHER_HOME_RUN_PCT_MEAN) * 13;

            //ascending order so player1-player2, want lowest pitching rate (best fip = lowest runs allowed projection) first
            return pitchingPlayer1-pitchingPlayer2;
        }

    };

    private static int getIntToPower(int number, int power) {
        int product = number;
        for (int i = 0; i < power-1; i++) {
            product = (product * number)/(ONE_HUNDRED_PERCENT);
        }
        return product;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.playerId);
        dest.writeString(this.teamId);
        dest.writeString(this.firstName);
        dest.writeString(this.middleName);
        dest.writeString(this.lastName);
        dest.writeInt(this.primaryPosition);
        dest.writeInt(this.alternatePositions);
        dest.writeInt(this.age);
        dest.writeString(this.dateOfBirth);
        dest.writeString(this.hits);
        dest.writeString(this.throwingHand);
        dest.writeTypedList(this.battingStats);
        dest.writeTypedList(this.pitchingStats);
        dest.writeParcelable(this.hittingPercentages, flags);
        dest.writeParcelable(this.pitchingPercentages, flags);
    }

    @SuppressWarnings("ConstantConditions")
    protected Player(Parcel in) {
        this.playerId = in.readString();
        this.teamId = in.readString();
        this.firstName = in.readString();
        this.middleName = in.readString();
        this.lastName = in.readString();
        this.primaryPosition = in.readInt();
        this.alternatePositions = in.readInt();
        this.age = in.readInt();
        this.dateOfBirth = in.readString();
        this.hits = in.readString();
        this.throwingHand = in.readString();
        this.battingStats = in.createTypedArrayList(BattingStats.CREATOR);
        this.pitchingStats = in.createTypedArrayList(PitchingStats.CREATOR);
        this.hittingPercentages = in.readParcelable(HittingPercentages.class.getClassLoader());
        this.pitchingPercentages = in.readParcelable(PitchingPercentages.class.getClassLoader());
    }

    public static final Creator<Player> CREATOR = new Creator<Player>() {
        @Override
        public Player createFromParcel(Parcel source) {
            return new Player(source);
        }

        @Override
        public Player[] newArray(int size) {
            return new Player[size];
        }
    };
}