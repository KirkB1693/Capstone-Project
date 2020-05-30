package com.example.android.baseballbythenumbers.generators;

import android.content.Context;

import com.example.android.baseballbythenumbers.data.Name;
import com.example.android.baseballbythenumbers.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;
import java.util.TreeMap;

import timber.log.Timber;

public class NameGenerator
{
    private final Random random;

    private final NameGeneratorOptions options;

    private TreeMap<Float, String> surnames;

    private TreeMap<Float, String> firstNames;

    private Context context;

    public NameGenerator(Context context) {
        this(new NameGeneratorOptions(), context);
    }

    public NameGenerator(NameGeneratorOptions options, Context context) {
        this.context = context;
        this.options = options;
        if (options.getRandomSeed() == null) {
            this.random = new Random();
        } else {
            this.random = new Random( options.getRandomSeed() );
        }
        try {
            surnames = loadNames(R.raw.lastnames);
            firstNames = loadNames(R.raw.firstnames);
        }
        catch (IOException e) {
            Timber.e("IOException while loading names files.  You may not get any results!");
        }
    }

    /**
     * Generate a single name
     *
     * @return a randomly generated name.
     */
    public Name generateName() {
        return new Name(
                pickName(firstNames),  // First Name
                pickName(firstNames),  // Middle Name
                pickName(surnames));  // Last Name
    }


    /**
     * Pick a name from the specified TreeMap based on a random number.
     *
     * @return the picked name.
     */
    private String pickName(final TreeMap<Float, String> map) {
        if (map.isEmpty()) return null;

        Float key = null;
        while (key == null) {
            try {
                key = map.ceilingKey(random.nextFloat());
            }
            catch (NullPointerException ignored) {
                //Do nothing.
            }
        }

        return map.get(key);
    }

    /**
     * Loads all of the names in the given file into a TreeMap keyed by the name's cumulative frequency.
     *
     * @param fileResource the resource path to the text file containing the names to load.
     * @return a TreeMap of all names in the file.
     */
    private TreeMap<Float, String> loadNames(final int fileResource) throws IOException {
        TreeMap<Float, String> names = new TreeMap<>();
        InputStream is = context.getResources().openRawResource(fileResource);

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line = reader.readLine();
            while (line != null) {
                String[] fields = line.split(",");
                names.put(Float.parseFloat(fields[1]), fields[0]);
                line = reader.readLine();
            }


        return names;
    }
}
