package com.example.android.baseballbythenumbers.Data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "batting_stats", foreignKeys = @ForeignKey(entity = Player.class, parentColumns = "playerId", childColumns = "playerId", onDelete = CASCADE), indices = @Index(value = "playerId", unique = true))
public class BattingStats implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private long battingStatsId;

    private long playerId;

    @SerializedName("Year")
    @Expose
    private int year;
    @SerializedName("Games")
    @Expose
    private int games;
    @SerializedName("PlateAppearances")
    @Expose
    private int plateAppearances;
    @SerializedName("Hits")
    @Expose
    private int hits;
    @SerializedName("Singles")
    @Expose
    private int singles;
    @SerializedName("Doubles")
    @Expose
    private int doubles;
    @SerializedName("Triples")
    @Expose
    private int triples;
    @SerializedName("HomeRuns")
    @Expose
    private int homeRuns;
    @SerializedName("Runs")
    @Expose
    private int runs;
    @SerializedName("RunsBattedIn")
    @Expose
    private int runsBattedIn;
    @SerializedName("Walks")
    @Expose
    private int walks;
    @SerializedName("StrikeOuts")
    @Expose
    private int strikeOuts;
    @SerializedName("HitByPitch")
    @Expose
    private int hitByPitch;
    @SerializedName("StolenBases")
    @Expose
    private int stolenBases;
    @SerializedName("CaughtStealing")
    @Expose
    private int caughtStealing;
    @SerializedName("GroundBalls")
    @Expose
    private int groundBalls;
    @SerializedName("LineDrives")
    @Expose
    private int lineDrives;
    @SerializedName("FlyBalls")
    @Expose
    private int flyBalls;
    @SerializedName("Errors")
    @Expose
    private int errors;
    public final static Parcelable.Creator<BattingStats> CREATOR = new Creator<BattingStats>() {


        @SuppressWarnings({
                "unchecked"
        })
        public BattingStats createFromParcel(Parcel in) {
            return new BattingStats(in);
        }

        public BattingStats[] newArray(int size) {
            return (new BattingStats[size]);
        }

    };

    protected BattingStats(Parcel in) {
        this.battingStatsId = ((long) in.readValue((long.class.getClassLoader())));
        this.playerId = ((long) in.readValue((long.class.getClassLoader())));
        this.year = ((int) in.readValue((int.class.getClassLoader())));
        this.games = ((int) in.readValue((int.class.getClassLoader())));
        this.plateAppearances = ((int) in.readValue((int.class.getClassLoader())));
        this.hits = ((int) in.readValue((int.class.getClassLoader())));
        this.singles = ((int) in.readValue((int.class.getClassLoader())));
        this.doubles = ((int) in.readValue((int.class.getClassLoader())));
        this.triples = ((int) in.readValue((int.class.getClassLoader())));
        this.homeRuns = ((int) in.readValue((int.class.getClassLoader())));
        this.runs = ((int) in.readValue((int.class.getClassLoader())));
        this.runsBattedIn = ((int) in.readValue((int.class.getClassLoader())));
        this.walks = ((int) in.readValue((int.class.getClassLoader())));
        this.strikeOuts = ((int) in.readValue((int.class.getClassLoader())));
        this.hitByPitch = ((int) in.readValue((int.class.getClassLoader())));
        this.stolenBases = ((int) in.readValue((int.class.getClassLoader())));
        this.caughtStealing = ((int) in.readValue((int.class.getClassLoader())));
        this.groundBalls = ((int) in.readValue((int.class.getClassLoader())));
        this.lineDrives = ((int) in.readValue((int.class.getClassLoader())));
        this.flyBalls = ((int) in.readValue((int.class.getClassLoader())));
        this.errors = ((int) in.readValue((int.class.getClassLoader())));
    }

    /**
     * No args constructor for use in serialization
     */
    public BattingStats() {
    }

    /**
     * @param games
     * @param stolenBases
     * @param singles
     * @param runsBattedIn
     * @param lineDrives
     * @param flyBalls
     * @param hits
     * @param errors
     * @param walks
     * @param caughtStealing
     * @param homeRuns
     * @param plateAppearances
     * @param triples
     * @param strikeOuts
     * @param year
     * @param doubles
     * @param hitByPitch
     * @param runs
     * @param groundBalls
     */
    @Ignore
    public BattingStats(int year, int games, int plateAppearances, int hits, int singles, int doubles, int triples, int homeRuns, int runs, int runsBattedIn, int walks, int strikeOuts, int hitByPitch, int stolenBases, int caughtStealing, int groundBalls, int lineDrives, int flyBalls, int errors, long playerId) {
        super();
        this.year = year;
        this.games = games;
        this.plateAppearances = plateAppearances;
        this.hits = hits;
        this.singles = singles;
        this.doubles = doubles;
        this.triples = triples;
        this.homeRuns = homeRuns;
        this.runs = runs;
        this.runsBattedIn = runsBattedIn;
        this.walks = walks;
        this.strikeOuts = strikeOuts;
        this.hitByPitch = hitByPitch;
        this.stolenBases = stolenBases;
        this.caughtStealing = caughtStealing;
        this.groundBalls = groundBalls;
        this.lineDrives = lineDrives;
        this.flyBalls = flyBalls;
        this.errors = errors;
        this.playerId = playerId;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getGames() {
        return games;
    }

    public void setGames(int games) {
        this.games = games;
    }

    public int getPlateAppearances() {
        return plateAppearances;
    }

    public void setPlateAppearances(int plateAppearances) {
        this.plateAppearances = plateAppearances;
    }

    public int getHits() {
        return hits;
    }

    public void setHits(int hits) {
        this.hits = hits;
    }

    public int getSingles() {
        return singles;
    }

    public void setSingles(int singles) {
        this.singles = singles;
    }

    public int getDoubles() {
        return doubles;
    }

    public void setDoubles(int doubles) {
        this.doubles = doubles;
    }

    public int getTriples() {
        return triples;
    }

    public void setTriples(int triples) {
        this.triples = triples;
    }

    public int getHomeRuns() {
        return homeRuns;
    }

    public void setHomeRuns(int homeRuns) {
        this.homeRuns = homeRuns;
    }

    public int getRuns() {
        return runs;
    }

    public void setRuns(int runs) {
        this.runs = runs;
    }

    public int getRunsBattedIn() {
        return runsBattedIn;
    }

    public void setRunsBattedIn(int runsBattedIn) {
        this.runsBattedIn = runsBattedIn;
    }

    public int getWalks() {
        return walks;
    }

    public void setWalks(int walks) {
        this.walks = walks;
    }

    public int getStrikeOuts() {
        return strikeOuts;
    }

    public void setStrikeOuts(int strikeOuts) {
        this.strikeOuts = strikeOuts;
    }

    public int getHitByPitch() {
        return hitByPitch;
    }

    public void setHitByPitch(int hitByPitch) {
        this.hitByPitch = hitByPitch;
    }

    public int getStolenBases() {
        return stolenBases;
    }

    public void setStolenBases(int stolenBases) {
        this.stolenBases = stolenBases;
    }

    public int getCaughtStealing() {
        return caughtStealing;
    }

    public void setCaughtStealing(int caughtStealing) {
        this.caughtStealing = caughtStealing;
    }

    public int getGroundBalls() {
        return groundBalls;
    }

    public void setGroundBalls(int groundBalls) {
        this.groundBalls = groundBalls;
    }

    public int getLineDrives() {
        return lineDrives;
    }

    public void setLineDrives(int lineDrives) {
        this.lineDrives = lineDrives;
    }

    public int getFlyBalls() {
        return flyBalls;
    }

    public void setFlyBalls(int flyBalls) {
        this.flyBalls = flyBalls;
    }

    public int getErrors() {
        return errors;
    }

    public void setErrors(int errors) {
        this.errors = errors;
    }

    public long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(long playerId) {
        this.playerId = playerId;
    }

    public long getBattingStatsId() {
        return battingStatsId;
    }

    public void setBattingStatsId(long battingStatsId) {
        this.battingStatsId = battingStatsId;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(battingStatsId);
        dest.writeValue(playerId);
        dest.writeValue(year);
        dest.writeValue(games);
        dest.writeValue(plateAppearances);
        dest.writeValue(hits);
        dest.writeValue(singles);
        dest.writeValue(doubles);
        dest.writeValue(triples);
        dest.writeValue(homeRuns);
        dest.writeValue(runs);
        dest.writeValue(runsBattedIn);
        dest.writeValue(walks);
        dest.writeValue(strikeOuts);
        dest.writeValue(hitByPitch);
        dest.writeValue(stolenBases);
        dest.writeValue(caughtStealing);
        dest.writeValue(groundBalls);
        dest.writeValue(lineDrives);
        dest.writeValue(flyBalls);
        dest.writeValue(errors);
    }

    @Override
    public String toString() {
        return new StringBuilder().append("\nYear : ").append(year).append(", Games : ").append(games).append(", Plate Appearances : ").append(plateAppearances)
                .append("\nHits : ").append(hits).append(", Singles : ").append(singles).append(", Doubles : ").append(doubles).append(", Triples : ").append(triples).append(" Home Runs : ").append(homeRuns)
                .append("\nRuns : ").append(runs).append(", RBI : ").append(runsBattedIn).append(", BB : ").append(walks).append(", SO : ").append(strikeOuts).append(", HBP : ").append(hitByPitch)
                .append(", SB : ").append(stolenBases).append(", CS : ").append(caughtStealing).append("\nGround Balls : ").append(groundBalls).append(", Line Drives : ").append(lineDrives)
                .append(", Fly Balls : ").append(flyBalls).append("\nErrors : ").append(errors).append(", Avg : ").append(getAvg()).append(", OBP : ").append(getOBP()).toString();
    }

    private int getOBP() {
        if ((plateAppearances) != 0) {
            return (hits + walks + hitByPitch) * 1000 / (plateAppearances);
        } else {
            return 0;
        }
    }

    private int getAvg() {
        if ((plateAppearances - walks) != 0) {
            return hits * 1000 / (plateAppearances - walks);
        } else {
            return 0;
        }

    }

    public int describeContents() {
        return 0;
    }
}
