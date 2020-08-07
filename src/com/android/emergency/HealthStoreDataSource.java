/*
 * Copyright (C) 2020 The LineageOS project
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

package com.android.emergency;

import static com.android.emergency.PreferenceKeys.*;

import android.content.Context;

import androidx.preference.PreferenceDataStore;

import org.lineageos.mod.health.common.values.BloodType;
import org.lineageos.mod.health.common.values.OrganDonor;
import org.lineageos.mod.health.sdk.model.profile.MedicalProfile;
import org.lineageos.mod.health.sdk.repo.MedicalProfileRepo;

public final class HealthStoreDataSource extends PreferenceDataStore {

    private final MedicalProfileRepo repo;

    public HealthStoreDataSource(Context context) {
        repo = MedicalProfileRepo.getInstance(context.getContentResolver());
    }

    @Override
    public void putString(String key, String value) {
        final MedicalProfile profile = repo.get();
        switch (key) {
            case KEY_ALLERGIES:
                profile.setAllergies(value);
                break;
            case KEY_MEDICATIONS:
                profile.setMedications(value);
                break;
            case KEY_MEDICAL_CONDITIONS:
                profile.setNotes(value);
                break;
            case KEY_BLOOD_TYPE:
                profile.setBloodType(bloodTypeToInt(value));
                break;
            case KEY_ORGAN_DONOR:
                profile.setOrganDonor(organDonorToInt(value));
                break;
            default:
                return;
        }
        repo.set(profile);
    }

    @Override
    public String getString(String key, String defValue) {
        final MedicalProfile profile = repo.get();
        switch (key) {
            case KEY_ALLERGIES:
                return profile.getAllergies();
            case KEY_MEDICATIONS:
                return profile.getMedications();
            case KEY_MEDICAL_CONDITIONS:
                return profile.getNotes();
            case KEY_BLOOD_TYPE:
                return bloodTypeToString(profile.getBloodType());
            case KEY_ORGAN_DONOR:
                return organDonorToString(profile.getOrganDonor());
            default:
                return defValue;
        }
    }

    public boolean isSet() {
        return !new MedicalProfile().equals(repo.get());
    }

    /* Converters */

    private int bloodTypeToInt(String value) {
        // Match strings with R.strings.blood_type_values
        switch (value) {
            case "0+":
                return BloodType.O_POS;
            case "O-":
                return BloodType.O_NEG;
            case "A+":
                return BloodType.A_POS;
            case "A-":
                return BloodType.A_NEG;
            case "B+":
                return BloodType.B_POS;
            case "B-":
                return BloodType.B_NEG;
            case "AB+":
                return BloodType.AB_POS;
            case "AB-":
                return BloodType.AB_NEG;
            case "H/H":
                return BloodType.HH;
            default:
                return BloodType.UNKNOWN;
        }
    }

    private String bloodTypeToString(int value) {
        // Match strings with R.strings.blood_type_values
        switch (value) {
            case BloodType.O_POS:
                return "0+";
            case BloodType.O_NEG:
                return "O-";
            case BloodType.A_POS:
                return "A+";
            case BloodType.A_NEG:
                return "A-";
            case BloodType.B_POS:
                return "B+";
            case BloodType.B_NEG:
                return "B-";
            case BloodType.AB_POS:
                return "AB+";
            case BloodType.AB_NEG:
                return "AB-";
            case BloodType.HH:
                return "H/H";
            default:
                return "";
        }
    }

    private int organDonorToInt(String value) {
        // Match strings with R.strings.organ_donor_values
        switch (value) {
            case "Yes":
                return OrganDonor.YES;
            case "No":
                return OrganDonor.NO;
            default:
                return OrganDonor.UNKNOWN;
        }
    }

    private String organDonorToString(int value) {
        // Match strings with R.strings.organ_donor_values
        switch (value) {
            case OrganDonor.YES:
                return "Yes";
            case OrganDonor.NO:
                return "No";
            default:
                return "";
        }
    }
}
