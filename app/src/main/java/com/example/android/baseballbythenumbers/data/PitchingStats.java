package com.example.android.baseballbythenumbers.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.UUID;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@SuppressWarnings("NotNullFieldNotInitialized")
@Entity(tableName = "pitching_stats", foreignKeys = @ForeignKey(entity = Player.class, parentColumns = "playerId", childColumns = "playerId", onDelete = CASCADE), indices = @Index(value = "playerId"))
public class PitchingStats implements Parcelable, Comparable<PitchingStats> {

    @PrimaryKey
    @NonNull
    private String pitchingStatsId;

    private String playerId;

    private int year;

    private int wins;

    private int losses;

    private int games;

    private int gamesStarted;

    private int completeGames;

    private int shutOuts;

    private int saves;

    private int holds;

    private int blownSaves;

    private float inningsPitched;

    private int totalBattersFaced;

    private int hits;

    private int runs;

    private int earnedRuns;

    private int homeRuns;

    private int walks;

    private int strikeOuts;

    private int hitByPitch;

    private int wildPitches;

    private int balks;

    private int groundBalls;

    private int lineDrives;

    private int flyBalls;

    /**
     * No args constructor for use in serialization
     */
    public PitchingStats() {
    }

    /**
     * @param blownSaves number of blown saves
     * @param games number of games played
     * @param gamesStarted number of games started by pitcher
     * @param shutOuts number of shutouts (complete game pitched with no runs allowed)
     * @param totalBattersFaced number of batters faced
     * @param wildPitches number of wild pitches
     * @param completeGames number of complete games
     * @param lineDrives number of line drives allowed
     * @param flyBalls number of fly balls allowed
     * @param hits number of hits allowed
     * @param walks number of walks allowed
     * @param earnedRuns number of earned runs allowed
     * @param balks number of balks committed
     * @param homeRuns number of home runs allowed
     * @param saves number of saves
     * @param strikeOuts number of strike outs
     * @param losses number of losses
     * @param year the year for these stats
     * @param hitByPitch number of batters hit by pitch
     * @param runs number of runs allowed
     * @param holds number of holds
     * @param inningsPitched number of innings pitched
     * @param groundBalls number of ground balls allowed
     * @param wins number of wins
     */
    @Ignore
    public PitchingStats(int year, int wins, int losses, int games, int gamesStarted, int completeGames, int shutOuts, int saves, int holds, int blownSaves, float inningsPitched,
                         int totalBattersFaced, int hits, int runs, int earnedRuns, int homeRuns, int walks, int strikeOuts, int hitByPitch, int wildPitches, int balks, int groundBalls,
                         int lineDrives, int flyBalls, String playerId) {
        super();
        this.year = year;
        this.wins = wins;
        this.losses = losses;
        this.games = games;
        this.gamesStarted = gamesStarted;
        this.completeGames = completeGames;
        this.shutOuts = shutOuts;
        this.saves = saves;
        this.holds = holds;
        this.blownSaves = blownSaves;
        this.inningsPitched = inningsPitched;
        this.totalBattersFaced = totalBattersFaced;
        this.hits = hits;
        this.runs = runs;
        this.earnedRuns = earnedRuns;
        this.homeRuns = homeRuns;
        this.walks = walks;
        this.strikeOuts = strikeOuts;
        this.hitByPitch = hitByPitch;
        this.wildPitches = wildPitches;
        this.balks = balks;
        this.groundBalls = groundBalls;
        this.lineDrives = lineDrives;
        this.flyBalls = flyBalls;
        this.playerId = playerId;
        this.pitchingStatsId = UUID.randomUUID().toString();
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public void incrementWins() {
        wins++;
    }

    public int getLosses() {
        return losses;
    }

    public void setLosses(int losses) {
        this.losses = losses;
    }

    public void incrementLosses() {
        losses++;
    }

    public int getGames() {
        return games;
    }

    public void setGames(int games) {
        this.games = games;
    }

    public void incrementGames() {
        games++;
    }

    public int getGamesStarted() {
        return gamesStarted;
    }

    public void setGamesStarted(int gamesStarted) {
        this.gamesStarted = gamesStarted;
    }

    public void incrementGamesStarted() {
        gamesStarted++;
    }

    public int getCompleteGames() {
        return completeGames;
    }

    public void setCompleteGames(int completeGames) {
        this.completeGames = completeGames;
    }

    public void incrementCompleteGames() {
        completeGames++;
    }

    public int getShutOuts() {
        return shutOuts;
    }

    public void setShutOuts(int shutOuts) {
        this.shutOuts = shutOuts;
    }

    public void incrementShutOuts() {
        shutOuts ++;
    }

    public int getSaves() {
        return saves;
    }

    public void setSaves(int saves) {
        this.saves = saves;
    }

    public void incrementSaves() {
        saves ++;
    }

    public int getHolds() {
        return holds;
    }

    public void setHolds(int holds) {
        this.holds = holds;
    }

    public void incrementHolds() {
        holds ++;
    }

    public int getBlownSaves() {
        return blownSaves;
    }

    public void setBlownSaves(int blownSaves) {
        this.blownSaves = blownSaves;
    }

    public void incrementBlownSaves() {
        blownSaves ++;
    }

    public float getInningsPitched() {
        return inningsPitched;
    }

    public void setInningsPitched(float inningsPitched) {
        this.inningsPitched = inningsPitched;
    }

    public void addToInningsPitched(int outs) {
        for (int i = 0; i < outs; i++) {
            inningsPitched += .1;
            if (Math.round(inningsPitched * 10) >= (((Math.round(inningsPitched) * 10) + 3))) {
                inningsPitched = inningsPitched + 1.0f - 0.3f;
                inningsPitched = Math.round(inningsPitched * 10) / 10.0f;
            }
        }

    }

    public int getTotalBattersFaced() {
        return totalBattersFaced;
    }

    public void setTotalBattersFaced(int totalBattersFaced) {
        this.totalBattersFaced = totalBattersFaced;
    }

    public void incrementTotalBattersFaced() {
        totalBattersFaced ++;
    }

    public int getHits() {
        return hits;
    }

    public void setHits(int hits) {
        this.hits = hits;
    }

    public void incrementHits() {
        hits ++;
    }

    public int getRuns() {
        return runs;
    }

    public void setRuns(int runs) {
        this.runs = runs;
    }

    public int getEarnedRuns() {
        return earnedRuns;
    }

    public void setEarnedRuns(int earnedRuns) {
        this.earnedRuns = earnedRuns;
    }

    public int getHomeRuns() {
        return homeRuns;
    }

    public void setHomeRuns(int homeRuns) {
        this.homeRuns = homeRuns;
    }

    public void incrementHomeRuns() {
        homeRuns ++;
    }

    public int getWalks() {
        return walks;
    }

    public void setWalks(int walks) {
        this.walks = walks;
    }

    public void incrementWalks() {
        walks ++;
    }

    public int getStrikeOuts() {
        return strikeOuts;
    }

    public void setStrikeOuts(int strikeOuts) {
        this.strikeOuts = strikeOuts;
    }

    public void incrementStrikeOuts() {
        strikeOuts ++;
    }

    public int getHitByPitch() {
        return hitByPitch;
    }

    public void setHitByPitch(int hitByPitch) {
        this.hitByPitch = hitByPitch;
    }

    public void incrementHitByPitch() {
        hitByPitch ++;
    }

    public int getWildPitches() {
        return wildPitches;
    }

    public void setWildPitches(int wildPitches) {
        this.wildPitches = wildPitches;
    }

    public void incrementWildPitches() {
        wildPitches ++;
    }

    public int getBalks() {
        return balks;
    }

    public void setBalks(int balks) {
        this.balks = balks;
    }

    public void incrementBalks() {
        balks ++;
    }

    public int getGroundBalls() {
        return groundBalls;
    }

    public void setGroundBalls(int groundBalls) {
        this.groundBalls = groundBalls;
    }

    public void incrementGroundBalls() {
        groundBalls ++;
    }

    public int getLineDrives() {
        return lineDrives;
    }

    public void setLineDrives(int lineDrives) {
        this.lineDrives = lineDrives;
    }

    public void incrementLineDrives() {
        lineDrives ++;
    }

    public int getFlyBalls() {
        return flyBalls;
    }

    public void setFlyBalls(int flyBalls) {
        this.flyBalls = flyBalls;
    }

    public void incrementFlyBalls() {
        flyBalls ++;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    @NotNull
    public String getPitchingStatsId() {
        return pitchingStatsId;
    }

    public void setPitchingStatsId(@NotNull String pitchingStatsId) {
        this.pitchingStatsId = pitchingStatsId;
    }

    public String getERA() {
        if (inningsPitched > 0) {
            double ip = ((inningsPitched % 1) * 3.333333333) + Math.round(inningsPitched);
            DecimalFormat decimalFormat = new DecimalFormat("0.00");
            return decimalFormat.format(Math.round(earnedRuns / (ip / 9) * 100.0) / 100.0);
        } else {
            if (earnedRuns > 0) {
                return "infinite";
            }
            return "0.00";
        }
    }

    public String getWHIP() {
        if (inningsPitched > 0) {
            double ip = ((inningsPitched % 1) * 3.333333333) + Math.round(inningsPitched);
            DecimalFormat decimalFormat = new DecimalFormat("0.00");
            return decimalFormat.format(Math.round(((walks + hits) / (ip)) * 100.0) / 100.0);

        } else {
            if ((walks + hits) > 0) {
                return "infinite";
            }
            return "0.00";
        }
    }

    @Override
    public int compareTo(PitchingStats pitchingStats) {
        if (year > pitchingStats.year) {
            return 1;
        } else if (year < pitchingStats.year) {
            return -1;
        } else {
            return 0;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.pitchingStatsId);
        dest.writeString(this.playerId);
        dest.writeInt(this.year);
        dest.writeInt(this.wins);
        dest.writeInt(this.losses);
        dest.writeInt(this.games);
        dest.writeInt(this.gamesStarted);
        dest.writeInt(this.completeGames);
        dest.writeInt(this.shutOuts);
        dest.writeInt(this.saves);
        dest.writeInt(this.holds);
        dest.writeInt(this.blownSaves);
        dest.writeFloat(this.inningsPitched);
        dest.writeInt(this.totalBattersFaced);
        dest.writeInt(this.hits);
        dest.writeInt(this.runs);
        dest.writeInt(this.earnedRuns);
        dest.writeInt(this.homeRuns);
        dest.writeInt(this.walks);
        dest.writeInt(this.strikeOuts);
        dest.writeInt(this.hitByPitch);
        dest.writeInt(this.wildPitches);
        dest.writeInt(this.balks);
        dest.writeInt(this.groundBalls);
        dest.writeInt(this.lineDrives);
        dest.writeInt(this.flyBalls);
    }

    @SuppressWarnings("ConstantConditions")
    protected PitchingStats(Parcel in) {
        this.pitchingStatsId = in.readString();
        this.playerId = in.readString();
        this.year = in.readInt();
        this.wins = in.readInt();
        this.losses = in.readInt();
        this.games = in.readInt();
        this.gamesStarted = in.readInt();
        this.completeGames = in.readInt();
        this.shutOuts = in.readInt();
        this.saves = in.readInt();
        this.holds = in.readInt();
        this.blownSaves = in.readInt();
        this.inningsPitched = in.readFloat();
        this.totalBattersFaced = in.readInt();
        this.hits = in.readInt();
        this.runs = in.readInt();
        this.earnedRuns = in.readInt();
        this.homeRuns = in.readInt();
        this.walks = in.readInt();
        this.strikeOuts = in.readInt();
        this.hitByPitch = in.readInt();
        this.wildPitches = in.readInt();
        this.balks = in.readInt();
        this.groundBalls = in.readInt();
        this.lineDrives = in.readInt();
        this.flyBalls = in.readInt();
    }

    public static final Creator<PitchingStats> CREATOR = new Creator<PitchingStats>() {
        @Override
        public PitchingStats createFromParcel(Parcel source) {
            return new PitchingStats(source);
        }

        @Override
        public PitchingStats[] newArray(int size) {
            return new PitchingStats[size];
        }
    };
}
