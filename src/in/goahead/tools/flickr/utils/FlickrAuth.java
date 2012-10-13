/*
 * This file is part of flickb.

    flickb 
    is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    flickb
     is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with flickb.
    If not, see <http://www.gnu.org/licenses/>.
 */
package in.goahead.tools.flickr.utils;
import java.io.IOException;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.aetrion.flickr.Flickr;
import com.aetrion.flickr.FlickrException;
import com.aetrion.flickr.REST;
import com.aetrion.flickr.auth.Auth;
import com.aetrion.flickr.auth.AuthInterface;
import com.aetrion.flickr.auth.Permission;
import com.aetrion.flickr.people.User;

public class FlickrAuth {
	
	private AuthInterface authInterface;
	private String frob;
	private Auth authObj;
	private Flickr flickr;
	
	public String getForbAuthURL(String apiKey, String secret, Permission permission) throws ParserConfigurationException, IOException, SAXException, FlickrException {
		flickr = new Flickr(apiKey, secret, new REST());
		authInterface = flickr.getAuthInterface();
		frob = authInterface.getFrob();
		
		URL url = authInterface.buildAuthenticationUrl(permission, frob);
		
		return url.toExternalForm();
	}
	
	public Auth getFlickrUserAuthObj() throws IOException, SAXException, FlickrException {
		authObj = authInterface.getToken(frob);
		return authObj;
	}
	
	public AuthInterface getAuthInterface() {
		return authInterface;
	}
	
	public void setFrob(String frob) {
		this.frob = frob;
	}
	
	public String getFrob() {
		return frob;
	}
	
	public Auth getAuthObj() {
		return authObj;
	}
	
	public Flickr getFlickrObj() {
		return flickr;
	}

	public User getUser() {
		return authObj.getUser();
	}
}