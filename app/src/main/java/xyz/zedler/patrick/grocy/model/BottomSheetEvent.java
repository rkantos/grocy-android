package xyz.zedler.patrick.grocy.model;

/*
    This file is part of Grocy Android.

    Grocy Android is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Grocy Android is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Grocy Android.  If not, see <http://www.gnu.org/licenses/>.

    Copyright 2020 by Patrick Zedler & Dominic Zedler
*/

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import xyz.zedler.patrick.grocy.fragment.bottomSheetDialog.CustomBottomSheet;

public class BottomSheetEvent extends Event {

    private final CustomBottomSheet bottomSheet;
    private final Bundle bundle;

    public BottomSheetEvent(@NonNull CustomBottomSheet bottomSheet, @Nullable Bundle bundle) {
        this.bottomSheet = bottomSheet;
        this.bundle = bundle;
    }

    public BottomSheetEvent(@NonNull CustomBottomSheet bottomSheet) {
        this(bottomSheet, null);
    }

    public CustomBottomSheet getBottomSheet() {
        return bottomSheet;
    }

    @Override
    public Bundle getBundle() {
        return bundle;
    }

    @Override
    public int getType() {
        return Event.BOTTOM_SHEET;
    }

}