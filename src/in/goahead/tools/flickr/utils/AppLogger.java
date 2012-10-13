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
import java.util.HashMap;
import java.util.Map;

public class AppLogger {

	private static Map<String, AppLogger> LoggerMap = new HashMap<String, AppLogger>();
	
	@SuppressWarnings("rawtypes")
	public static AppLogger getLogger(Class className) {
		
		AppLogger retLogger = null;
		
		if(LoggerMap.containsKey(className.toString())) {
			retLogger = LoggerMap.get(className.toString());
		}
		else {
			retLogger = new AppLogger();
			LoggerMap.put(className.toString(), retLogger);
		}
		return retLogger;
	}
	
	
	public void fatal(Object obj) {
		this.log(obj);
	}
	
	public void error(Object obj) {
		this.log(obj);
	}
	
	public void warn(Object obj) {
		this.log(obj);
	}
	
	public void info(Object obj) {
		this.log(obj);
	}
	
	public void debug(Object obj) {
		this.log(obj);
	}
	
	public void trace(Object obj) {
		this.log(obj);
	}
	
	private void log(Object obj) {
		System.out.println(obj);
	}
	
}
