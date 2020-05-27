/*
 * Copyright 2018-2020 KunMinX
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.kunminx.puremusic;

import android.os.Bundle;
import android.view.View;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.kunminx.puremusic.ui.base.BaseActivity;
import com.kunminx.puremusic.ui.base.DataBindingConfig;
import com.kunminx.puremusic.vm.state.MainActivityViewModel;

/**
 * Create by KunMinX at 19/10/16
 */

public class MainActivity extends BaseActivity {

    private MainActivityViewModel mMainActivityViewModel;
    private boolean mIsListened = false;

    @Override
    protected void initViewModel() {
        mMainActivityViewModel = getActivityViewModel(MainActivityViewModel.class);
    }

    @Override
    protected DataBindingConfig getDataBindingConfig() {
        return new DataBindingConfig(R.layout.activity_main, mMainActivityViewModel)
                .addBindingParam(BR.event, new EventHandler());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSharedViewModel().activityCanBeClosedDirectly.observe(this, aBoolean -> {
            NavController nav = Navigation.findNavController(this, R.id.main_fragment_host);
            if (nav.getCurrentDestination() != null && nav.getCurrentDestination().getId() != R.id.mainFragment) {
                nav.navigateUp();
            } else if (getSharedViewModel().isDrawerOpened.get()) {
                getSharedViewModel().openOrCloseDrawer.setValue(false);
            } else {
                super.onBackPressed();
            }
        });

        getSharedViewModel().openOrCloseDrawer.observe(this, aBoolean -> {
            mMainActivityViewModel.openDrawer.setValue(aBoolean);
        });

        getSharedViewModel().enableSwipeDrawer.observe(this, aBoolean -> {
            mMainActivityViewModel.allowDrawerOpen.setValue(aBoolean);
        });
    }

    public class EventHandler extends DrawerLayout.SimpleDrawerListener {
        @Override
        public void onDrawerOpened(View drawerView) {
            super.onDrawerOpened(drawerView);
            getSharedViewModel().isDrawerOpened.set(true);
        }

        @Override
        public void onDrawerClosed(View drawerView) {
            super.onDrawerClosed(drawerView);
            getSharedViewModel().isDrawerOpened.set(false);
            mMainActivityViewModel.openDrawer.setValue(false);
        }
    }
}
