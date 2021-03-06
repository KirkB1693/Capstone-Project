/*
 * Copyright 2017, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.baseballbythenumbers;

import android.app.Application;

import com.example.android.baseballbythenumbers.database.AppDatabase;
import com.example.android.baseballbythenumbers.repository.Repository;

/**
 * Android Application class. Used for accessing singletons.
 */
public class BaseballByTheNumbersApp extends Application {

    // Resource Provider
    private ResourceProvider mResourceProvider;
    public ResourceProvider getResourceProvider() {
        if (mResourceProvider == null)
            mResourceProvider = new ResourceProvider(this);

        return mResourceProvider;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mResourceProvider = getResourceProvider();
    }

    public AppDatabase getDatabase() {
        return AppDatabase.getInstance(this);
    }

    public Repository getRepository() {
        return Repository.getInstance(getDatabase());
    }


}
