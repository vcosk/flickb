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
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.aetrion.flickr.Flickr;
import com.aetrion.flickr.FlickrException;
import com.aetrion.flickr.REST;
import com.aetrion.flickr.RequestContext;
import com.aetrion.flickr.auth.Auth;
import com.aetrion.flickr.auth.Permission;
import com.aetrion.flickr.people.User;
import com.aetrion.flickr.photos.Photo;
import com.aetrion.flickr.photos.PhotoList;
import com.aetrion.flickr.photos.PhotosInterface;
import com.aetrion.flickr.photos.Size;
import com.aetrion.flickr.photosets.Photoset;
import com.aetrion.flickr.photosets.PhotosetsInterface;
import com.aetrion.flickr.util.AuthStore;
import com.aetrion.flickr.util.FileAuthStore;

public class Backup {

	private static AppLogger logger = AppLogger.getLogger(Backup.class);
    private Flickr flickr = null;
    private AuthStore authStore = null;
    private User user = null;

    public Backup(Flickr flickr, User user) {
        this.flickr = flickr;
        this.user = user;
    }

	public void doBackup(File directory) throws Exception {
		if (!directory.exists()) directory.mkdir();
		
	
		PhotosetsInterface pi = flickr.getPhotosetsInterface();
		PhotosInterface photoInt = flickr.getPhotosInterface();
		Map<String, PhotoList> allPhotos = new HashMap<String, PhotoList>();
		
		for(Object setObj : pi.getList(this.user.getId()).getPhotosets()) {
			Photoset set = (Photoset)setObj;
			PhotoList photos = pi.getPhotos(set.getId(), 500, 1);
			allPhotos.put(set.getTitle(), photos);
		}
		
		logger.info("Reading available sets");
		logger.info("Is user a pro user? "+user.isPro());
		for(String setTitle : allPhotos.keySet()) {
			String setDirectoryName = makeSafeFilename(setTitle);
			File setDirectory = new File(directory, setDirectoryName);
			setDirectory.mkdir();
			
			for(Object photoObj : allPhotos.get(setTitle)) {
			
				Photo photo = (Photo) photoObj;
				String url = photo.getLargeUrl();
				URL u = new URL(url);
				String filename = u.getFile();
				filename = filename.substring(filename.lastIndexOf("/") + 1 , filename.length());				
				System.out.println("Now writing " + filename + " to " + setDirectory.getCanonicalPath());
				BufferedInputStream inStream = new BufferedInputStream(photoInt.getImageAsStream(photo, Size.ORIGINAL));
				File newFile = new File(setDirectory.getCanonicalPath()+"/"+filename);
				
				FileOutputStream fos = new FileOutputStream(newFile);
			
				int read;
			
				while ((read = inStream.read()) != -1) {
					fos.write(read);
				}
				fos.flush();
				fos.close();
				inStream.close();
			}
		}
		
	}

	private String makeSafeFilename(String input) {
		byte[] fname = input.getBytes();
		byte[] bad = new byte[] {'\\', '/', '"', ' '};
		byte replace = '_';
		for (int i = 0; i < fname.length; i++) {
			for (int j = 0; j < bad.length; j++) {
				if (fname[i] == bad[j]) fname[i] = replace;
			}
		}
		return new String(fname);
	}
}
