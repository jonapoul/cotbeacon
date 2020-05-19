package com.jon.cotbeacon.service;

import android.content.SharedPreferences;
import android.util.Log;

import com.jon.cotbeacon.cot.CursorOnTarget;
import com.jon.cotbeacon.utils.Key;
import com.jon.cotbeacon.utils.PrefUtils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

class UdpCotThread extends CotThread {
    private static final String TAG = UdpCotThread.class.getSimpleName();
    private DatagramSocket socket;

    UdpCotThread(SharedPreferences sharedPreferences) {
        super(sharedPreferences);
    }
    UdpCotThread(SharedPreferences prefs, CotGenerator generator) {
        super(prefs, generator);
    }

    @Override
    void shutdown() {
        super.shutdown();
        if (socket != null) {
            socket.close();
            socket = null;
        }
    }

    @Override
    public void run() {
        super.run();
        initialiseDestAddress();
        openSocket();
        int bufferTimeMs = periodMilliseconds() / cotIcons.size();

        while (isRunning) {
            for (CursorOnTarget cot : cotIcons) {
                sendToDestination(cot);
                bufferSleep(bufferTimeMs);
            }
            cotIcons = cotGenerator.generate();
        }
        shutdown();
    }

    protected void initialiseDestAddress() {
        try {
            destIp = InetAddress.getByName(PrefUtils.getString(prefs, Key.DEST_ADDRESS));
        } catch (UnknownHostException e) {
            Log.e(TAG, "Error parsing destination address: " + prefs.getString(Key.DEST_ADDRESS, ""));
            shutdown();
        }
        destPort = PrefUtils.parseInt(prefs, Key.DEST_PORT);
    }

    protected void openSocket() {
        try {
            if (destIp.isMulticastAddress()) {
                socket = new MulticastSocket();
                ((MulticastSocket)socket).setLoopbackMode(false);
            } else {
                socket = new DatagramSocket();
            }
        } catch (IOException e) {
            Log.e(TAG, "Error when building transmit UDP socket");
            shutdown();
        }
    }

    @Override
    protected void sendToDestination(CursorOnTarget cot) {
        Log.i(TAG, "Sending cot: " + cot);
        try {
            final byte[] buf = cot.toBytes();
            socket.send(new DatagramPacket(buf, buf.length, destIp, destPort));
            Log.i(TAG, "Sent cot: " + cot.toString());
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
            shutdown();
        } catch (NullPointerException e) {
            shutdown();
        }
    }

}
