package com.jon.cotbeacon.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.core.content.ContextCompat;

import com.jon.cotbeacon.R;
import com.jon.cotbeacon.service.CotService;
import com.jon.cotbeacon.utils.Notify;
import com.leinardi.android.speeddial.SpeedDialActionItem;
import com.leinardi.android.speeddial.SpeedDialView;

class SpeedDialCreator {
    private SpeedDialCreator() {
    }

    static SpeedDialView getSpeedDial(Activity activity) {
        SpeedDialView speedDial = activity.findViewById(R.id.speedDial);
        speedDial.addActionItem(buildSpeedDialViewItem(activity, R.id.startEmergency, R.drawable.start, R.string.startEmergency));
        speedDial.addActionItem(buildSpeedDialViewItem(activity, R.id.cancelEmergency, R.drawable.stop, R.string.cancelEmergency));
        speedDial.setOnActionSelectedListener(actionItem -> {
            Intent intent = new Intent(activity, CotService.class);
            intent.setAction(actionItem.getId() == R.id.startEmergency ? CotService.START_EMERGENCY : CotService.CANCEL_EMERGENCY);
            activity.startService(intent);
            return false;
        });
        return speedDial;
    }

    static SpeedDialView getDisabledSpeedDial(Activity activity) {
        SpeedDialView speedDial = activity.findViewById(R.id.speedDialDisabled);
        speedDial.setOnChangeListener(new SpeedDialView.OnChangeListener() {
            @Override
            public boolean onMainActionSelected() {
                Notify.orange(activity.findViewById(android.R.id.content), "Press the start button first!");
                return false;
            }

            @Override
            public void onToggleChanged(boolean isOpen) {
            }
        });
        return speedDial;
    }

    private static SpeedDialActionItem buildSpeedDialViewItem(Context context, int itemId, int iconId, int textId) {
        int textColour = ContextCompat.getColor(context, R.color.colorAccentDark);
        int backgroundColour = ContextCompat.getColor(context, R.color.white);
        return new SpeedDialActionItem.Builder(itemId, iconId)
                .setFabBackgroundColor(backgroundColour)
                .setFabImageTintColor(textColour)
                .setLabel(context.getString(textId))
                .setLabelColor(textColour)
                .setLabelBackgroundColor(backgroundColour)
                .setLabelClickable(true)
                .create();
    }
}
