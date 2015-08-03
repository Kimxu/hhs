/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kimxu.hhs.z_api.network;


import java.net.URI;
import java.util.Set;

import kimxu.hhs.z_volley.base.GenericHelper;

/**
 * 
 * @author ss
 */
public interface NetworkInfoHelper extends GenericHelper {
	// ConnectivityManager conM;

	public boolean requestRouteToHost(int networkType, int hostAddress);

	public Set<Integer> getAllActiveNetworkType();

	public boolean checkConnectivity(int networkType);
	
	public byte[] checkFinalIp(URI uri) ;

	public abstract boolean isWifi();
}
