package ie.iamshanedoyle.craftbeers.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import de.greenrobot.event.EventBus;
import ie.iamshanedoyle.craftbeers.events.NetworkStateChangedEvent;

/**
 * This receiver is used to detect changes in the network.
 *
 * @author Shane Doyle <@ElWexicano>
 */
public class NetworkStateReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        ConnectivityManager connectivityManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        NetworkStateChangedEvent networkStateChangedEvent =
                new NetworkStateChangedEvent(networkInfo == null);

        EventBus.getDefault().post(networkStateChangedEvent);
    }
}
