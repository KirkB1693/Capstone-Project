package com.example.android.baseballbythenumbers.Generators;

import android.content.Context;

import com.example.android.baseballbythenumbers.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.TreeMap;

import timber.log.Timber;

import static com.example.android.baseballbythenumbers.Data.Constants.Countries.ALL_COUNTRIES;
import static com.example.android.baseballbythenumbers.Data.Constants.Countries.CANADA;
import static com.example.android.baseballbythenumbers.Data.Constants.Countries.JAPAN;
import static com.example.android.baseballbythenumbers.Data.Constants.Countries.MEXICO;
import static com.example.android.baseballbythenumbers.Data.Constants.Countries.USA;
import static com.example.android.baseballbythenumbers.Data.Constants.DivisionNames.CENTRAL;
import static com.example.android.baseballbythenumbers.Data.Constants.DivisionNames.EAST;
import static com.example.android.baseballbythenumbers.Data.Constants.DivisionNames.NORTH;
import static com.example.android.baseballbythenumbers.Data.Constants.DivisionNames.NO_DIVISONS;
import static com.example.android.baseballbythenumbers.Data.Constants.DivisionNames.SOUTH;
import static com.example.android.baseballbythenumbers.Data.Constants.DivisionNames.WEST;

public class CityGenerator {
    private final Random random;

    private int numberOfDivisions = 0;

    private int countriesIncluded;

    private List<Integer> listOfCountriesIncluded;

    private TreeMap<Float, String> allCityNames;

    private TreeMap<Float, String> westCityNames;

    private TreeMap<Float, String> eastCityNames;

    private TreeMap<Float, String> centralCityNames;

    private TreeMap<Float, String> northCityNames;

    private TreeMap<Float, String> southCityNames;

    private Context context;

    public CityGenerator(Context context) {
        this(context, 0, ALL_COUNTRIES);
    }

    public CityGenerator(Context context, int numberOfDivisions, int citiesFromCountries) {
        this.context = context;
        this.numberOfDivisions = numberOfDivisions;

        this.random = new Random();

        countriesIncluded = citiesFromCountries;

        listOfCountriesIncluded = getListOfCountries(citiesFromCountries);

        try {
            switch (numberOfDivisions) {
                case 0:
                    allCityNames = loadTeamNames(R.raw.cities_metroarea, NO_DIVISONS);
                    break;
                case 1:
                    allCityNames = loadTeamNames(R.raw.cities_metroarea, NO_DIVISONS);
                    break;
                case 2:
                    westCityNames = loadTeamNames(R.raw.cities_metroarea, WEST);
                    eastCityNames = loadTeamNames(R.raw.cities_metroarea, EAST);
                    break;
                case 3:
                    westCityNames = loadTeamNames(R.raw.cities_metroarea, WEST);
                    centralCityNames = loadTeamNames(R.raw.cities_metroarea, CENTRAL);
                    eastCityNames = loadTeamNames(R.raw.cities_metroarea, EAST);
                    break;
                case 4:
                    westCityNames = loadTeamNames(R.raw.cities_metroarea, WEST);
                    northCityNames = loadTeamNames(R.raw.cities_metroarea, NORTH);
                    southCityNames = loadTeamNames(R.raw.cities_metroarea, SOUTH);
                    eastCityNames = loadTeamNames(R.raw.cities_metroarea, EAST);
                    break;
                default:
                    allCityNames = loadTeamNames(R.raw.cities_metroarea, NO_DIVISONS);
            }

        } catch (IOException e) {
            Timber.e("IOException while loading names files.  You may not get any results!");
        }
    }

    private List<Integer> getListOfCountries(int countriesIncluded) {
        List<Integer> countriesList = new ArrayList<>();
        if (countriesIncluded == ALL_COUNTRIES) {
            return countriesList;
        } else {
            if (countriesIncluded >= CANADA) {
                countriesList.add(CANADA);
                countriesIncluded -= CANADA;
            }
            if (countriesIncluded >= MEXICO) {
                countriesList.add(MEXICO);
                countriesIncluded -= MEXICO;
            }
            if (countriesIncluded >= JAPAN) {
                countriesList.add(JAPAN);
                countriesIncluded -= JAPAN;
            }
            if (countriesIncluded >= USA) {
                countriesList.add(USA);
            }
        }
        return countriesList;
    }

    /**
     * Generate a single name
     *
     * @return a randomly generated city name.
     */
    public String generateCity(String divisionName) {
        switch (numberOfDivisions) {
            case 0:
                return (pickName(allCityNames));  // City Name
            case 1:
                return (pickName(allCityNames));  // City Name
            case 2:
                if (divisionName.equals(WEST)) {
                    return (pickName(westCityNames));  // City Name
                } else {
                    return (pickName(eastCityNames));  // City Name
                }
            case 3:
                if (divisionName.equals(WEST)) {
                    return (pickName(westCityNames));  // City Name
                } else if (divisionName.equals(CENTRAL)) {
                    return (pickName(centralCityNames));  // City Name
                } else {
                    return (pickName(eastCityNames));  // City Name
                }

            case 4:
                if (divisionName.equals(WEST)) {
                    return (pickName(westCityNames));  // City Name
                } else if (divisionName.equals(NORTH)) {
                    return (pickName(northCityNames));  // City Name
                } else if (divisionName.equals(SOUTH)) {
                    return (pickName(southCityNames));  // City Name
                } else {
                    return (pickName(eastCityNames));  // City Name
                }
            default:
                return (pickName(allCityNames));  // City Name
        }


    }


    /**
     * Pick a name from the specified TreeMap based on a random number.
     *
     * @return the picked name.
     */
    private String pickName(final TreeMap<Float, String> map) {
        assert !map.isEmpty();

        Float key = null;
        while (key == null) {
            try {
                key = map.ceilingKey(random.nextFloat());
            } catch (NullPointerException ignored) {
                //Do nothing.
            }
        }

        String city = map.get(key);
        if (!key.equals(map.ceilingKey(0f))) {
            map.remove(key);                       // Remove this city from the list unless it is the largest city
        }
        return city;
    }

    /**
     * Loads all of the names in the given file into a TreeMap keyed by the city's cumulative frequency, which is a product of it's size. (i.e. bigger cities are picked more often)
     *
     * @param fileResource the resource path to the text file containing the names to load.
     * @return a TreeMap of all names in the file.
     */
    private TreeMap<Float, String> loadTeamNames(final int fileResource, String divisionName) throws IOException {
        TreeMap<Float, String> names = new TreeMap<>();
        InputStream is = context.getResources().openRawResource(fileResource);

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        String line = reader.readLine();
        float runningFloatTotal = 0;
        while (line != null) {

            String[] fields = line.split(",");
            switch (numberOfDivisions) {
                case 0:
                    if (countryIncluded(Integer.parseInt(fields[3]))) {
                        runningFloatTotal += Float.parseFloat(fields[1]);
                        names.put(runningFloatTotal, fields[0]);
                    }
                    break;
                case 1:
                    if (countryIncluded(Integer.parseInt(fields[3]))) {
                        runningFloatTotal += Float.parseFloat(fields[1]);
                        names.put(runningFloatTotal, fields[0]);
                    }
                    break;
                case 2:
                    if (countryIncluded(Integer.parseInt(fields[3]))) {
                        if (divisionName.equals(WEST)) {
                            if (Float.parseFloat(fields[6]) <= -100 || Float.parseFloat(fields[6]) > 0) {
                                runningFloatTotal += Float.parseFloat(fields[1]);
                                names.put(runningFloatTotal, fields[0]);
                            }
                        } else {
                            if (Float.parseFloat(fields[6]) > -100 && Float.parseFloat(fields[6]) < 0) {
                                runningFloatTotal += Float.parseFloat(fields[1]);
                                names.put(runningFloatTotal, fields[0]);
                            }
                        }
                    }
                    break;
                case 3:
                    if (countryIncluded(Integer.parseInt(fields[3]))) {
                        if (divisionName.equals(WEST)) {
                            if (Float.parseFloat(fields[6]) <= -110 || Float.parseFloat(fields[6]) > 0) {
                                runningFloatTotal += Float.parseFloat(fields[1]);
                                names.put(runningFloatTotal, fields[0]);
                            }
                        } else if (divisionName.equals(CENTRAL)) {
                            if (Float.parseFloat(fields[6]) > -110 && Float.parseFloat(fields[6]) <= -84.5) {
                                runningFloatTotal += Float.parseFloat(fields[1]);
                                names.put(runningFloatTotal, fields[0]);
                            }
                        } else {
                            if (Float.parseFloat(fields[6]) > -84.5 && Float.parseFloat(fields[6]) < 0) {
                                runningFloatTotal += Float.parseFloat(fields[1]);
                                names.put(runningFloatTotal, fields[0]);
                            }
                        }
                    }
                    break;
                case 4:
                    if (countryIncluded(Integer.parseInt(fields[3]))) {
                        switch (divisionName) {
                            case WEST:
                                if (Float.parseFloat(fields[6]) <= -110 || Float.parseFloat(fields[6]) > 0) {
                                    runningFloatTotal += Float.parseFloat(fields[1]);
                                    names.put(runningFloatTotal, fields[0]);
                                }
                                break;
                            case NORTH:
                                if (Float.parseFloat(fields[6]) > -110 && Float.parseFloat(fields[6]) <= -84.5 && Float.parseFloat(fields[5]) > 36.15) {
                                    runningFloatTotal += Float.parseFloat(fields[1]);
                                    names.put(runningFloatTotal, fields[0]);
                                }
                                break;
                            case SOUTH:
                                if (Float.parseFloat(fields[6]) > -110 && Float.parseFloat(fields[6]) <= -84.5 && Float.parseFloat(fields[5]) <= 36.15) {
                                    runningFloatTotal += Float.parseFloat(fields[1]);
                                    names.put(runningFloatTotal, fields[0]);
                                }
                                break;
                            default:
                                if (Float.parseFloat(fields[6]) > -84.5 && Float.parseFloat(fields[6]) < 0) {
                                    runningFloatTotal += Float.parseFloat(fields[1]);
                                    names.put(runningFloatTotal, fields[0]);
                                }
                                break;
                        }
                    }
                    break;
                default:
                    if (countryIncluded(Integer.parseInt(fields[3]))) {
                        runningFloatTotal += Float.parseFloat(fields[1]);
                        names.put(runningFloatTotal, fields[0]);
                    }
            }
            line = reader.readLine();
        }


        return names;
    }

    private boolean countryIncluded(int countryToCheck) {
        if (countriesIncluded == ALL_COUNTRIES) {
            return true;
        } else {
            if (listOfCountriesIncluded.contains(countryToCheck)) {
                return true;
            } else {
                return false;
            }
        }
    }
}
