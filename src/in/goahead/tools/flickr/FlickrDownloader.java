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

package in.goahead.tools.flickr;

import static in.goahead.tools.flickr.utils.AppConstants.*;
import in.goahead.tools.flickr.utils.AppLogger;
import in.goahead.tools.flickr.utils.Backup;
import in.goahead.tools.flickr.utils.FlickrAuth;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.aetrion.flickr.auth.Permission;
import com.aetrion.flickr.util.IOUtilities;

public class FlickrDownloader {

	private static AppLogger logger = AppLogger.getLogger(FlickrDownloader.class);
	
	public static void main(String[] args) throws Exception {
		FlickrDownloader flickrDownloader = new FlickrDownloader();
		Map<String, String> appDetails  = flickrDownloader.getAppDetails("app.properties");
		FlickrAuth flickrAuthObj = new FlickrAuth();
		
		//Forming auth URL.
		logger.info("Press return after you granted access at this URL:");
		logger.info(flickrAuthObj.getForbAuthURL(appDetails.get(API_KEY), appDetails.get(APP_SECRET), Permission.WRITE));
		BufferedReader infile = new BufferedReader ( new InputStreamReader (System.in) );
		String line = infile.readLine();
		flickrAuthObj.getFlickrUserAuthObj();
		
		logger.info("File download will begin shortly...");
		String outputDir = appDetails.containsKey(OUTPUT_DIR) && appDetails.get(OUTPUT_DIR) != null?appDetails.get(OUTPUT_DIR):DEFAULT_OUTPUT_DIR;
		Backup backup = new Backup(flickrAuthObj.getFlickrObj(), flickrAuthObj.getUser());
		backup.doBackup(new File(outputDir));
		logger.info("Files have been downloaded.");
	}


	public Map<String, String> getAppDetails(String propertyFile) {
		InputStream in = null;
		Map<String, String> propertyMap = new HashMap<String, String>();
		Properties properties = null;
		try {
			in = new FileInputStream(propertyFile);
			properties = new Properties();
			properties.load(in);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOUtilities.close(in);
		}

		if(properties != null) {
			propertyMap.put(API_KEY, properties.getProperty("api.key"));
			propertyMap.put(APP_SECRET, properties.getProperty("app.secret"));
			propertyMap.put(OUTPUT_DIR, properties.getProperty("outputdir"));
		}
		return propertyMap;
	}
}
